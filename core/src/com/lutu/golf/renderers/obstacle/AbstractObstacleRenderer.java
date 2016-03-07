
package com.lutu.golf.renderers.obstacle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.models.obstacle.AbstractObstacleModel;
import com.lutu.golf.utils.Assets;

/**
 * Class to draw obstacle - this class should be used to extend by all obstacle renderers.
 * 
 * @param <T>
 *            type of obstacle which will be drawn
 */
public abstract class AbstractObstacleRenderer<T extends AbstractObstacleModel> {

	static final boolean DEBUG_RENDERING = false;

	private final T mObstacle;

	private Texture mObstacleTexture;
	private String mObstacleTexturePath;

	/**
	 * Constructs {@link AbstractObstacleRenderer}.
	 * 
	 * @param obstacle
	 *            obstacle model of type which extends {@link AbstractObstacleModel}
	 */
	AbstractObstacleRenderer(T obstacle) {
		mObstacle = obstacle;
	}

	/**
	 * Called when the {@link AbstractObstacleRenderer} should create its assets with the {@link Assets}.
	 */
	public void create() {
		mObstacleTexturePath = mObstacle.getObstacleFilePath();
		if (mObstacleTexturePath != null) {
			Assets.get().load(mObstacleTexturePath, Texture.class);

			Assets.get().finishLoading();
			mObstacleTexture = Assets.get().get(mObstacleTexturePath);
		}
	}

	/**
	 * Called when the {@link AbstractObstacleRenderer} should dispose its assets created with {@link #create()}.
	 */
	public void dispose() {
		if (mObstacleTexturePath != null) {
			Assets.get().unload(mObstacleTexturePath);
		}
	}

	/**
	 * Renders the obstacle - one of:
	 * <ol>
	 * <li> {@link com.lutu.golf.models.obstacle.SlowdownObstacleModel}</li>
	 * <li> {@link com.lutu.golf.models.obstacle.ForceApplyingObstacleModel}</li>
	 * <li> {@link com.lutu.golf.models.obstacle.BallDestroyingObstacleModel}</li>
	 * </ol>
	 * <p>
	 * This method should be called on render() method in GameRenderer.
	 * </p>
	 * <b> Do not allocate objects in this method. </b>
	 * 
	 * @param batch
	 *            {@link SpriteBatch} used for drawing
	 * @param camera
	 *            {@link Camera} instance that defines the transformation used to render the map
	 */
	public void render(SpriteBatch batch, Camera camera) {
		if (mObstacleTexture != null) {
			batch.setProjectionMatrix(camera.projection);
			batch.setTransformMatrix(camera.view);
			batch.begin();
			batch.draw(mObstacleTexture, mObstacle.getPositionX(), mObstacle.getPositionY(), mObstacle.getWidth(),
					mObstacle.getHeight());
			batch.end();
		}
		if (DEBUG_RENDERING) {
			debugRender(camera);
		}
	}

	/**
	 * Returns information about obstacle which will be draw. It can be one of:
	 * <ol>
	 * <li> {@link com.lutu.golf.models.obstacle.SlowdownObstacleModel}</li>
	 * <li> {@link com.lutu.golf.models.obstacle.ForceApplyingObstacleModel}</li>
	 * <li> {@link com.lutu.golf.models.obstacle.BallDestroyingObstacleModel}</li>
	 * </ol>
	 * <br/>
	 * All above classes extend {@link AbstractObstacleModel}.
	 * 
	 * @return obstacle model
	 */
	public T getObstacleModel() {
		return mObstacle;
	}

	/**
	 * Method used to debug.
	 * 
	 * @param camera
	 *            {@link Camera} instance that defines the transformation used to render the map
	 */
	abstract void debugRender(Camera camera);
}
