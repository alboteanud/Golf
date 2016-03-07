
package com.lutu.golf.controlere;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.controlere.BallController.BallStoppedListener;
import com.lutu.golf.controlere.CameraController.CameraAnimation.OnAnimationStoppedListener;
import com.lutu.golf.controlere.animation.PanAndZoomAnimation;
import com.lutu.golf.controlere.obstacle.ForceApplyingObstacleController;
import com.lutu.golf.ecrane.GameScreen;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.models.obstacle.AbstractObstacleModel;
import com.lutu.golf.models.obstacle.BallDestroyingObstacleModel;
import com.lutu.golf.models.obstacle.ForceApplyingObstacleModel;
import com.lutu.golf.models.obstacle.SlowdownObstacleModel;

/**
 * This controller is the main game controller. It contains all controllers. The model is passed as a parameter.
 */
public class GameController {

	private static final String PAR = "par";
	private static final boolean BOX2D_DO_SLEEP = true;
	static final float BOX2D_STEP = 1.0f / 60.0f;
	static final int BOX2D_VELOCITY_ITERATIONS = 6;
	static final int BOX2D_POSITION_ITERATIONS = 2;
	private static final float VIEWPORT_HEIGHT_TO = 10.0f;
	private static final float PERCENT_OF_STRENGTH = 0.3f;

	/**
	 * Object which contains all models which are used in game.
	 */
	private final GameModel mGameModel;

	private final World mWorld;

	private final com.lutu.golf.controlere.BallController mBallController;

	private final com.lutu.golf.controlere.ClubController mClubController;

	private final com.lutu.golf.controlere.ArrowController mArrowController;

	private float mAccumulator;

	private final Camera mCamera;

	private final HoleController mHoleController;

	private final GameMapController mGameMapController;

	private final com.lutu.golf.controlere.CameraController mCameraController;

	private final List<ForceApplyingObstacleController> mForceApplyingObstacleControllers;

	private final List<com.lutu.golf.controlere.obstacle.BallDestroyingObstacleController> mBallDestroyingObstacleControllers;

	private OnBallStoppedListener mBallStoppedListener;

	/**
	 * Constructs the main game-logic controlling {@link GameController} instance.
	 * 
	 * @param gameModel
	 *            controlled {@link GameModel} instance
	 * @param camera
	 *            {@link Camera} instance
	 */
	public GameController(GameModel gameModel, Camera camera) {
		mGameModel = gameModel;
		mWorld = new World(Vector2.Zero, BOX2D_DO_SLEEP);
		mCamera = camera;

		mGameMapController = new GameMapController(mGameModel.getGameMapModel(), mGameModel.getHoleModel(),
				mGameModel.getObstraclesContenerModel(), mGameModel.getBallModel(), mWorld);

		mHoleController = new HoleController(mGameModel.getHoleModel(), mGameModel, mWorld);
		mBallController = new com.lutu.golf.controlere.BallController(mGameModel.getBallModel(), mWorld);

		mClubController = new com.lutu.golf.controlere.ClubController(mGameModel.getClubModel(), mGameModel.getBallModel(),
				mGameModel.getArrowModel());
		mArrowController = new com.lutu.golf.controlere.ArrowController(mGameModel.getArrowModel(), mGameModel.getBallModel(), mCamera, mGameModel);

		mCameraController = new com.lutu.golf.controlere.CameraController(mGameModel.getGameMapModel(), mCamera);

		initializeCameraAnimations();

		final BallStoppedListener resetArrowBallListener = new BallStoppedListener() {

			@Override
			public void onBallStopped(float x, float y, float lastX, float lastY) {
//				if (!mBallController.isBallDestroyed())
					mArrowController.resetArrowSettings();

				mBallController.setBallDestroyed(false);
				mArrowController.setVisible(true);
				mGameModel.setLastShot(mGameModel.getHits() == (mGameModel.getPar() * 2));
				if (mBallStoppedListener != null) {
					mBallStoppedListener.onBallStopped();
				}
			}
		};
		mBallController.addBallStoppedListener(resetArrowBallListener);

		mForceApplyingObstacleControllers = new ArrayList<ForceApplyingObstacleController>();
		mBallDestroyingObstacleControllers = new ArrayList<com.lutu.golf.controlere.obstacle.BallDestroyingObstacleController>();
		fillObstacleLists();
		setParFromMap();
	}


	private void setParFromMap() {
		final String parAsStr = mGameModel.getGameMapModel().getMap().properties.get(PAR);
		final int par = Integer.valueOf(parAsStr);
		mGameModel.setPar(par);
	}

	private void initializeCameraAnimations() {
		final PanAndZoomAnimation pazAnimation = new PanAndZoomAnimation(mGameModel.getHoleModel().getPositionX(),
				mGameModel.getHoleModel().getPositionY(), mGameModel.getGameMapModel().getMapHeight(), mGameModel
						.getBallModel().getPositionX(), mGameModel.getBallModel().getPositionY(), VIEWPORT_HEIGHT_TO);

		pazAnimation.setOnAnimationStoppedListener(new OnAnimationStoppedListener() {

			private final com.lutu.golf.controlere.animation.FollowAnimation mFollowAnimation = new com.lutu.golf.controlere.animation.FollowAnimation(mGameModel.getBallModel());
			private final BallStoppedListener mBallStoppedListener = new BallStoppedListener() {

				@Override
				public void onBallStopped(float x, float y, float lastX, float lastY) {
					mCameraController.setAnimation(mFollowAnimation);
					mCameraController.startAnimation();
				}
			};

			@Override
			public void onAnimationStopped() {
				mBallController.addBallStoppedListener(mBallStoppedListener);
			}
		});

		mCameraController.setAnimation(pazAnimation);
		mCameraController.startAnimation();
	}

	private void fillObstacleLists() {
		final int obstacleCount = mGameModel.getObstraclesContenerModel().getObstaclesCount();
		for (int i = 0; i < obstacleCount; i++) {
			final AbstractObstacleModel abstractObstacleModel = mGameModel.getObstraclesContenerModel()
					.getObstacleModel(i);
			switch (abstractObstacleModel.getTypeObstacle()) {
			case BALL_DESTROYING:
				mBallDestroyingObstacleControllers.add(new com.lutu.golf.controlere.obstacle.BallDestroyingObstacleController(
						(BallDestroyingObstacleModel) abstractObstacleModel, mBallController,
						mGameModel.getBallModel(), mWorld));
				break;
			case FORCE_APPLYING:
				mForceApplyingObstacleControllers.add(new ForceApplyingObstacleController(mBallController,
						(ForceApplyingObstacleModel) abstractObstacleModel, mGameModel.getBallModel()));
				break;
			case SLOW_DOWN:
				com.lutu.golf.controlere.obstacle.SlowdownObstacleController.registerSlowdownObstacleInWorld(mWorld, mBallController,
						(SlowdownObstacleModel) abstractObstacleModel, mGameModel.getBallModel());
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * Tells all the sub-controllers to update their state.
	 * 
	 * @param delta
	 *            time since last frame (in seconds)
	 */
	public void update(float delta) {
		mBallController.update(delta);
		mGameMapController.update();
		mHoleController.update();
		mArrowController.update();
		if (mGameModel.isShoot()) {
			if (mClubController.shotAnimation()) {
				mGameModel.onShootButtonClicked(false);
				shoot();
			}
		} else {
			mClubController.update();
		}
		final int countForceApplyingObstacles = mForceApplyingObstacleControllers.size();
		for (int index = 0; index < countForceApplyingObstacles; index++) {
			mForceApplyingObstacleControllers.get(index).update();
		}
		final int countBallDesrtoyingObstacles = mBallDestroyingObstacleControllers.size();
		for (int index = 0; index < countBallDesrtoyingObstacles; index++) {
			mBallDestroyingObstacleControllers.get(index).update();
		}
		mAccumulator += delta;
		while (mAccumulator > BOX2D_STEP) {
			mWorld.step(BOX2D_STEP, BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
			mAccumulator -= BOX2D_STEP;
		}
		mClassToRemove.debugRenderer();
		mCameraController.update(delta);

	}

	/**
	 * Game screen got resized in {@link GameScreen#resize(int, int)}.
	 * 
	 * This method keeps the aspect ratio of the camera with a constant height.
	 * 
	 * @param width
	 *            new viewport width
	 * @param height
	 *            new viewport height
	 */
	public void resize(int width, int height) {
		mCameraController.resize(width, height);
	}

	/**
	 * Initiates the shot with current power and angle settings.
	 */
	public void shoot() {

		final float randomizedMultiplier = calculateRandomShotMultiplier();

		float xStrength = mGameModel.getArrowModel().getShotVectorX()
				* mGameModel.getClubModel().getClubType().getForceMultiplier() * randomizedMultiplier;
		float yStrength = mGameModel.getArrowModel().getShotVectorY()
				* mGameModel.getClubModel().getClubType().getForceMultiplier() * randomizedMultiplier;

		mGameModel.getBallModel().setCurrentPostionAsLastStaticPosition();
		mGameModel.addHit();

		if (mGameModel.getBallModel().isOnSlowdownObstacle()) {
			if (Float.compare(xStrength, 0.0f) != 0) {
				xStrength = xStrength - ((xStrength * PERCENT_OF_STRENGTH) * (xStrength / Math.abs(xStrength)));
			}
			if (Float.compare(yStrength, 0.0f) != 0) {
				yStrength = yStrength - ((yStrength * PERCENT_OF_STRENGTH) * (yStrength / Math.abs(yStrength)));
			}
			if (Float.compare(xStrength, 0.0f) != 0 || Float.compare(yStrength, 0.0f) != 0) {
				mGameModel.getBallModel().setIsOnSlowdownObstacle(false);
			}
		}
		mBallController.setStrength(xStrength, yStrength);
		mBallController.applyTorqueToBody((xStrength + yStrength) * mBallController.getTorqueMultiplier());
		mArrowController.setVisible(false);

		mCameraController.setAnimation(new com.lutu.golf.controlere.animation.FollowAnimation(mGameModel.getBallModel()));
		mCameraController.startAnimation();
	}

	private float calculateRandomShotMultiplier() {
		// calculate a random multiplier within given limits
		final SecureRandom rand = new SecureRandom();
		final float minRandom = mGameModel.getClubModel().getClubType().getMinRandomCoefficient();
		final float maxRandom = mGameModel.getClubModel().getClubType().getMaxRandomCoefficient();

		return (rand.nextFloat() * (maxRandom - minRandom) + minRandom);
	}

	public com.lutu.golf.controlere.ArrowController getArrowController() {
		return mArrowController;
	}

	public com.lutu.golf.controlere.CameraController getCameraController() {
		return mCameraController;
	}

	private final ClassToDebug mClassToRemove = new ClassToDebug();

	/**
	 * Sets new {@link OnBallStoppedListener}.
	 * 
	 * @param pBallStopListener
	 *            new {@link OnBallStoppedListener}
	 */
	public void setOnBallStoppedListener(OnBallStoppedListener pBallStopListener) {
		mBallStoppedListener = pBallStopListener;
	}

	/**
	 * This class is used to debug. It draws shape from physic word.
	 */
	private class ClassToDebug {

		public ClassToDebug() {
			mDebugRenderer = new Box2DDebugRenderer();
		}

		private final Box2DDebugRenderer mDebugRenderer;

		private void debugRenderer() {
			mDebugRenderer.render(mWorld, mCamera.combined);
		}
	}


	/**
	 * Interface for informing when the ball has stopped.
	 */
	public interface OnBallStoppedListener {

		/** Should be called when the ball has stopped. */

		void onBallStopped();
	}
}
