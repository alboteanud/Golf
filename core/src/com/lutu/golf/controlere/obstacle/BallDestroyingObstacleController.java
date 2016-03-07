
package com.lutu.golf.controlere.obstacle;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.controlere.BallController;
import com.lutu.golf.controlere.IContactItem;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.obstacle.BallDestroyingObstacleModel;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Object to control obstacle which destroys ball.
 */
public class BallDestroyingObstacleController implements IContactItem {

	private final BallDestroyingObstacleModel mBallDestroyingObstacleModel;
	private final BallController mBallController;
	private final BallModel mBallModel;

	private final World mWorld;
	private boolean mBallDestroy;

	/**
	 * Constructs object to control obstacle which destroys ball.
	 * 
	 * @param ballDestroyingObstacleModel
	 *            instance of {@link BallDestroyingObstacleModel}
	 * @param ballController
	 *            instance of {@link BallController}
	 * @param ballModel
	 *            instance of {@link BallModel}
	 * @param world
	 *            instance of {@link World}
	 */
	public BallDestroyingObstacleController(BallDestroyingObstacleModel ballDestroyingObstacleModel,
			BallController ballController, BallModel ballModel, World world) {
		mBallDestroyingObstacleModel = ballDestroyingObstacleModel;
		mBallController = ballController;
		mBallModel = ballModel;
		mWorld = world;
		createBallDestroyingObstacle();
	}

	private void createBallDestroyingObstacle() {
		final PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(mBallDestroyingObstacleModel.getWidth() / 2,
				mBallDestroyingObstacleModel.getDestroyPositionOffset() / 2);
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(mBallDestroyingObstacleModel.getPositionX() + mBallDestroyingObstacleModel.getWidth() / 2,
				mBallDestroyingObstacleModel.getPositionY() + mBallDestroyingObstacleModel.getDestroyPositionOffset()
						/ 2);
		bodyDef.type = BodyType.StaticBody;
		final Body result = mWorld.createBody(bodyDef);
		result.createFixture(polygonShape, 0.0f);
		result.setUserData(this);
		polygonShape.dispose();
	}

	/**
	 * Method that is invoked every step in box2d. <b> Invoked by the GameController#update(float) to update the
	 * BallModel based on the Box2D step output. Do not allocate objects in this method.</b>
	 */
	public void update() {
		if (isBallInFallingForceField()) {
			if (mBallModel.isOutsideDestroyingObstacle()) {
				final String pathToSound = mBallDestroyingObstacleModel.getCollisionSoundPath();
				if (pathToSound != null) {
					GolfAudioManager.playSound(pathToSound);
				}
				mBallModel.setIsOutsideDestroyingObstacle(false);
			}
			mBallController.setStrength(0, mBallDestroyingObstacleModel.getFallVelocity());
		}
		if (!mWorld.isLocked() && mBallDestroy) {
			mBallDestroy = false;
			mBallController.onBallDestroyed();
			mBallModel.setIsOutsideDestroyingObstacle(true);
		}
	}

	private boolean isBallInFallingForceField() {
		final float ballXPos = mBallModel.getPositionX();
		final float ballYPos = mBallModel.getPositionY();
		final float obstacleXFrom = mBallDestroyingObstacleModel.getPositionX();
		final float obstacleYFrom = mBallDestroyingObstacleModel.getPositionY();
		final float obstacleXTo = obstacleXFrom + mBallDestroyingObstacleModel.getWidth();
		final float obstacleYTo = obstacleYFrom + mBallDestroyingObstacleModel.getHeight();
		return (obstacleXFrom < ballXPos) && (obstacleXTo > ballXPos) && (obstacleYFrom < ballYPos)
				&& (obstacleYTo > ballYPos);
	}

	@Override
	public void onBeginContact() {
		mBallDestroy = true;
	}

	@Override
	public void onEndContact() {
		// do nothing
	}
}
