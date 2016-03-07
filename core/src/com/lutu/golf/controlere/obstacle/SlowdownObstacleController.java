
package com.lutu.golf.controlere.obstacle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.controlere.BallController;
import com.lutu.golf.controlere.IContactItem;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.obstacle.SlowdownObstacleModel;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Object which supports contact obstacle with ball.
 */
public class SlowdownObstacleController implements IContactItem {

	private final SlowdownObstacleModel mSlowdownObstacleModel;
	private final World mWorld;
	private final BallController mBallController;
	private final BallModel mBallModel;

	/**
	 * Constructs {@link SlowdownObstacleController}.
	 * 
	 * @param world
	 *            instance of {@link World}
	 * @param ballController
	 *            instance of {@link BallController}
	 * @param slowdownObstacleModel
	 *            instance of {@link SlowdownObstacleModel}
	 * @param ballModel
	 *            instance of {@link BallModel}
	 * 
	 */
	public SlowdownObstacleController(World world, BallController ballController,
			SlowdownObstacleModel slowdownObstacleModel, BallModel ballModel) {
		mSlowdownObstacleModel = slowdownObstacleModel;
		mWorld = world;
		mBallController = ballController;
		mBallModel = ballModel;
		createSlowdownObstacleBody();
	}

	/**
	 * Calls constructor and returns instance of {@link SlowdownObstacleController}.
	 * 
	 * @param world
	 *            instance of {@link World}
	 * @param ballController
	 *            instance of {@link BallController}
	 * @param slowdownObstacleModel
	 *            instance of {@link SlowdownObstacleModel}
	 * @param ballModel
	 *            instance of {@link BallModel}
	 * @return instance of {@link SlowdownObstacleController}
	 */
	public static SlowdownObstacleController registerSlowdownObstacleInWorld(World world,
			BallController ballController, SlowdownObstacleModel slowdownObstacleModel, BallModel ballModel) {
		return new SlowdownObstacleController(world, ballController, slowdownObstacleModel, ballModel);
	}

	private void createSlowdownObstacleBody() {
		final PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(mSlowdownObstacleModel.getWidth() / 2, mSlowdownObstacleModel.getHeight() / 2);
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(
				mSlowdownObstacleModel.getPositionX() + mSlowdownObstacleModel.getWidth() / 2,
				mSlowdownObstacleModel.getPositionY()
						+ (mSlowdownObstacleModel.getHeight() - mSlowdownObstacleModel.getDepth()) / 2);
		bodyDef.type = BodyType.StaticBody;
		final Body result = mWorld.createBody(bodyDef);
		result.createFixture(polygonShape, 0.0f);
		result.setUserData(this);
		polygonShape.dispose();
	}

	@Override
	public void onBeginContact() {
		mBallController.setStrength(Vector2.Zero.x, Vector2.Zero.y);
		if (!mBallModel.isOnSlowdownObstacle()) {
			mBallModel.setIsOnSlowdownObstacle(true);
		}
		final String pathToSound = mSlowdownObstacleModel.getCollisionSoundPath();
		if (pathToSound != null) {
			GolfAudioManager.playSound(pathToSound);
		}
	}

	@Override
	public void onEndContact() {
		if (mBallController.getStrengthAppliedByShootY() < 0) {
			mBallController.setStrength(Vector2.Zero.x, Vector2.Zero.y);
			mBallController.stopBallRotation();
		}
	}
}
