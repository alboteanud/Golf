
package com.lutu.golf.controlere;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.models.BallModel;

/**
 * Class which is responsible for golf ball control.
 */
public class BallController {

	private static final float DEFAULT_BALL_DENSITY = 20.0f;
	private static final float DEFAULT_BALL_FRICTION = 0.9f;
	private static final float DEFAULT_BALL_RESTITUTION = 0.6f;

	private static final float TEMP_ANGULAR_DAMPING = 1.0f;
	private static final float DEFAULT_ANGULAR_DAMPING = 20.0f;

	private static final float MIN_BALL_VELOCITY = 0.035f;

	private final BallModel mBallModel;

	private final World mPhysicsWorld;

	private final Body mBallBody;
	private final Set<BallStoppedListener> mBallStoppedListeners;

	private final Vector2 mStrengthAppliedByShoot = new Vector2();

	// Ball type parameters

	private float mTorqueMultiplier = 0.0f;
	private float mRestitution = DEFAULT_BALL_RESTITUTION;
	private final float mFriction = DEFAULT_BALL_FRICTION;
	private boolean mBallDestroyed;

	/**
	 * Constructor.
	 * 
	 * @param ballModel
	 *            object which contains information about ball
	 * @param physicsWorld
	 *            physics world
	 */
	public BallController(BallModel ballModel, World physicsWorld) {
		mBallDestroyed = false;
		mBallModel = ballModel;
		mPhysicsWorld = physicsWorld;
		final BodyDef ballBodyDef = createBallBodyDef();
		mBallBody = mPhysicsWorld.createBody(ballBodyDef);
		mBallBody.setAngularDamping(TEMP_ANGULAR_DAMPING);
		// XXX: Set test values for ball parameters here if you want them changed from the start
		// begin
		// setRestitution(0.7f);
		// setTorqueMultiplier(-10.0f);
		// mFriction = 0.1f;
		// end
		final FixtureDef ballFixtureDef = createBallFixtureDef();
		mBallBody.createFixture(ballFixtureDef);
		ballFixtureDef.shape.dispose();
		mBallModel.setPosition(mBallBody.getPosition().x, mBallBody.getPosition().y);
		mBallModel.setCurrentPostionAsLastStaticPosition();

		mBallStoppedListeners = new HashSet<BallStoppedListener>(1);
	}

	/**
	 * Creates and returns object which contains definition for fixture ball body.
	 * 
	 * @return fixture ball body definition
	 */
	private FixtureDef createBallFixtureDef() {
		final CircleShape circleShape = new CircleShape();
		circleShape.setRadius(mBallModel.getRadius());
		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = DEFAULT_BALL_DENSITY;
		fixtureDef.friction = mFriction;
		fixtureDef.restitution = mRestitution;
		return fixtureDef;
	}

	/**
	 * Creates and returns object which contains definition for ball body.
	 * 
	 * @return ball body definition
	 */
	private BodyDef createBallBodyDef() {
		final BodyDef bodyDef = new BodyDef();
		// ball has to be dynamic - it has to move.
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(mBallModel.getPositionX(), mBallModel.getPositionY());
		// ball is a body which is fast moving so I think that should be prevented from tunneling through other moving
		// bodies.
		// TODO try set false to optimization
		bodyDef.bullet = true;
		return bodyDef;
	}

	/**
	 * Method that is invoked every step in box2d. <b> Invoked by the GameController#update(float) to update the
	 * BallModel based on the Box2D step output. Do not allocate objects in this method.</b>
	 * 
	 * @param delta
	 *            the time in seconds since the last render
	 */
	public void update(float delta) {
		mBallModel.setPosition(mBallBody.getPosition().x, mBallBody.getPosition().y);
		if (mBallBody.getLinearVelocity().len() < MIN_BALL_VELOCITY) {
			mBallBody.setLinearVelocity(Vector2.Zero);
			stopBallRotation();
		}
		if (mBallModel.isBallMoving()) {
			dampAngularVelocityIfNeeded();
			if (!mBallBody.isAwake()) {
				notifyBallStoppedListeners();
				mBallModel.setBallMoving(false);
			}
		} else {
			if (mBallBody.isAwake()) {
				mBallModel.setBallMoving(true);
			}
		}
	}

	private void dampAngularVelocityIfNeeded() {
		final float nearZeroVelocity = 0.7f;
		if (Math.abs(mBallBody.getAngularVelocity()) < nearZeroVelocity) {
			mBallBody.setAngularDamping(DEFAULT_ANGULAR_DAMPING);
		}
	}

	/**
	 * Sets linear velocity. {@link Body#setLinearVelocity(float, float)}
	 * 
	 * @param x
	 *            coordinate (this value should not exceed abs(20) maybe less)
	 *            <p>
	 *            when x is less than 0 , the ball will fly in the left otherwise in the right
	 *            </p>
	 * @param y
	 *            coordinate coordinate (this value should not exceed 20 but more than 0)
	 */
	public void setStrength(float x, float y) {
		mStrengthAppliedByShoot.x = x;
		mStrengthAppliedByShoot.y = y;
		mBallBody.setLinearVelocity(mStrengthAppliedByShoot);
	}

	/**
	 * Stops rotation ball.
	 */
	public void stopBallRotation() {
		mBallBody.setAngularVelocity(0);
	}

	/**
	 * Returns value y of strength applied by shoot.
	 * 
	 * @return y value of strength applied by shoot vector
	 */
	public float getStrengthAppliedByShootY() {
		return mStrengthAppliedByShoot.y;
	}

	/**
	 * Applies forces to ball center. {@link Body#applyForceToCenter(float, float)}.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void applyForce(float x, float y) {
		mBallBody.applyForceToCenter(x, y);
	}

	/**
	 * Sets ball position. <b> This method uses {@link Body#setTransform(float,float, float)} so it can not be used when
	 * world is locked (method {@link World#isLocked()} returns true).</b>
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setBallPosition(float x, float y) {
		mBallBody.setTransform(x, y, 0f);
	}

	/**
	 * Notifies the {@link BallController} that the ball has been destroyed.
	 */
	public void onBallDestroyed() {
		setBallPosition(mBallModel.getLastStaticPostionX(), mBallModel.getLastStaticPostionY());
		mBallDestroyed = true;
	}

	// Code for changing ball parameters - start

	/**
	 * Returns current restitution.
	 * 
	 * @return current restitution
	 */
	public float getRestitution() {
		return mRestitution;
	}

	/**
	 * Sets new restitution and applies it to the body. It is recommended for the value to be between 0.0f and 0.8f.
	 * 
	 * @param pRestitution
	 *            new restitution
	 */
	public void setRestitution(float pRestitution) {
		mRestitution = pRestitution;

		for (Fixture fixture : mBallBody.getFixtureList()) {
			fixture.setRestitution(pRestitution);
		}
	}

	/**
	 * Returns current torque multiplier.
	 * 
	 * @return current torque multiplier
	 */
	public float getTorqueMultiplier() {
		return mTorqueMultiplier;
	}

	/**
	 * Sets the torque multiplier, which determines ball's angular velocity.
	 * 
	 * @param pTorqueMultiplier
	 *            torque multiplier to be used in torque calculations - use negative value for the opposite rotation
	 */
	public void setTorqueMultiplier(float pTorqueMultiplier) {
		final int internalTorqueMultiplier = 1000000;
		mTorqueMultiplier = pTorqueMultiplier * internalTorqueMultiplier;
	}

	/**
	 * Applies torque to the ball body.
	 * 
	 * @param pTorque
	 *            torque to be applied to ball body
	 */
	public void applyTorqueToBody(float pTorque) {
		mBallBody.applyTorque(pTorque);
		mBallBody.setAngularDamping(TEMP_ANGULAR_DAMPING);
	}

	// Code for changing ball parameters - end

	private void notifyBallStoppedListeners() {
		for (BallStoppedListener listener : mBallStoppedListeners) {
			listener.onBallStopped(mBallModel.getPositionX(), mBallModel.getPositionY(),
					mBallModel.getLastStaticPostionX(), mBallModel.getLastStaticPostionY());
		}
	}

	/**
	 * Adds the {@link BallStoppedListener} to the listener list.
	 * 
	 * @param listener
	 *            {@link BallStoppedListener} instance
	 * @return true if the listener was not already in the list, false otherwise
	 */
	public boolean addBallStoppedListener(BallStoppedListener listener) {
		return mBallStoppedListeners.add(listener);
	}

	/**
	 * Removes the {@link BallStoppedListener} from the listener list.
	 * 
	 * @param listener
	 *            {@link BallStoppedListener} instance
	 * @return true if the listener was successfully removed, false otherwise
	 */
	public boolean removeBallStoppedListener(BallStoppedListener listener) {
		return mBallStoppedListeners.remove(listener);
	}

	/**
	 * Returns the information about whether the ball is destroyed.
	 * 
	 * @return true, if the ball is destroyed, false otherwise
	 */
	public boolean isBallDestroyed() {
		return mBallDestroyed;
	}

	/**
	 * Sets the information about whether the ball is destroyed.
	 * 
	 * @param pBallDestroyed
	 *            true, if the ball is destroyed, false otherwise
	 */
	public void setBallDestroyed(boolean pBallDestroyed) {
		mBallDestroyed = pBallDestroyed;
	}

	/**
	 * Implementations will be notified when the ball stops.
	 */
	public interface BallStoppedListener {
		/**
		 * Called when the ball stops.
		 * 
		 * @param x
		 *            x coordinate of the current ball position
		 * @param y
		 *            y coordinate of the current ball position
		 * @param lastX
		 *            x coordinate of the previous ball position
		 * @param lastY
		 *            y coordinate of the previous ball position
		 */
		void onBallStopped(float x, float y, float lastX, float lastY);
	}

}
