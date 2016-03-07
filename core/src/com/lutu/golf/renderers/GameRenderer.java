
package com.lutu.golf.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.ecrane.GameScreen;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.models.obstacle.AbstractObstacleModel;
import com.lutu.golf.models.obstacle.BallDestroyingObstacleModel;
import com.lutu.golf.models.obstacle.ForceApplyingObstacleModel;
import com.lutu.golf.models.obstacle.SlowdownObstacleModel;
import com.lutu.golf.renderers.obstacle.AbstractObstacleRenderer;
import com.lutu.golf.renderers.obstacle.BallDestroyingObstacleRenderer;
import com.lutu.golf.renderers.obstacle.ForceApplyingObstacleRenderer;
import com.lutu.golf.renderers.obstacle.SlowdownObstacleRenderer;

/**
 * This renderer is the main game renderer.
 * <p>
 * It contains all renderers. The model is passed as a parameter.
 * </p>
 */
public class GameRenderer {
	/**
	 * Object which contains all models which are used in game.
	 */
	private final GameModel mGameModel;

	private final BallRenderer mBallRenderer;
	private final HoleRenderer mHoleRenderer;
	private final GameMapRenderer mGameMapRenderer;

	private final ClubRenderer mClubRenderer;
	private final ArrowRenderer mArrowRenderer;

	private final Camera mGameCamera;
	private final SpriteBatch mSpriteBatch;

	private final AbstractObstacleRenderer<?>[] mObstaclesRenderer;

	/**
	 * Constructs the {@link GameRenderer} responsible for rendering the whole scene.
	 * 
	 * @param gameModel
	 *            {@link GameModel} instance containing data used in rendering
	 * @param camera
	 *            {@link Camera} instance used for transformations
	 */
	public GameRenderer(GameModel gameModel, Camera camera) {
		mGameModel = gameModel;
		mGameCamera = camera;
		mSpriteBatch = new SpriteBatch();

		// There should be created all renderers.
		mBallRenderer = new BallRenderer(mGameModel.getBallModel());

		mClubRenderer = new ClubRenderer(mGameModel.getClubModel());
		mArrowRenderer = new ArrowRenderer(mGameModel.getArrowModel());

		mHoleRenderer = new HoleRenderer(mGameModel.getHoleModel());
		mGameMapRenderer = new GameMapRenderer(mGameModel.getGameMapModel(), (OrthographicCamera) camera);

		final int obstaclesCount = mGameModel.getObstraclesContenerModel().getObstaclesCount();
		mObstaclesRenderer = new AbstractObstacleRenderer<?>[obstaclesCount];
		for (int i = 0; i < obstaclesCount; i++) {
			final AbstractObstacleModel obstacleModel = mGameModel.getObstraclesContenerModel().getObstacleModel(i);
			switch (obstacleModel.getTypeObstacle()) {
			case BALL_DESTROYING:
				mObstaclesRenderer[i] = new BallDestroyingObstacleRenderer((BallDestroyingObstacleModel) obstacleModel);
				break;
			case FORCE_APPLYING:
				mObstaclesRenderer[i] = new ForceApplyingObstacleRenderer((ForceApplyingObstacleModel) obstacleModel);
				break;
			case SLOW_DOWN:
				mObstaclesRenderer[i] = new SlowdownObstacleRenderer((SlowdownObstacleModel) obstacleModel);
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * Declares assets to load by the {@link com.lutu.golf.utils.Assets} class. Those assets remain constant for the
	 * whole lifetime of the screen.
	 */
	public void create() {
		mArrowRenderer.create();
		mBallRenderer.create();
		mClubRenderer.create();
	}

	/**
	 * Called whenever the {@link GameRenderer} should load its level or chapter-specific assets.
	 */
	public void createSpecific() {
		mHoleRenderer.create();

		for (int i = 0; i < mObstaclesRenderer.length; i++) {
			mObstaclesRenderer[i].create();
		}
	}

	/**
	 * Called whenever the {@link GameRenderer} wants to dispose its level or chapter-specific assets. Rolls back the
	 * changes made by {@link #createSpecific()}.
	 */
	public void disposeSpecific() {
		mHoleRenderer.dispose();

		for (int i = 0; i < mObstaclesRenderer.length; i++) {
			mObstaclesRenderer[i].dispose();
		}
	}

	/**
	 * Rolls back the changes made by {@link #create()}. Unload the constant assets in this method.
	 */
	public void dispose() {
		mArrowRenderer.dispose();
		mBallRenderer.dispose();
		mClubRenderer.dispose();
	}

	/**
	 * Called when the {@link GameScreen} hosting this {@link GameRenderer} is shown.
	 */
	public void show() {
		mArrowRenderer.show();
		mBallRenderer.show();
		mClubRenderer.show();
	}

	/**
	 * Called by the {@link GameScreen#render(float)} method to perform the actual rendering.
	 */
	public void render() {
		// Sub-renderers render methods should be called here.

		mGameMapRenderer.render();

		mBallRenderer.render(mSpriteBatch, mGameCamera);
		mHoleRenderer.render(mSpriteBatch, mGameCamera);
		for (int i = 0; i < mObstaclesRenderer.length; i++) {
			mObstaclesRenderer[i].render(mSpriteBatch, mGameCamera);
		}
		mClubRenderer.render(mSpriteBatch, mGameCamera);
		mArrowRenderer.render(mSpriteBatch, mGameCamera);

	}

	/**
	 * Called when renderer needs to be informed about golf club type change.
	 */
	public void onClubTypeChanged() {
		mClubRenderer.invalidateClubType();
	}

}
