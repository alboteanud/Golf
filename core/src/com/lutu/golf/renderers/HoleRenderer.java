
package com.lutu.golf.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.models.HoleModel;
import com.lutu.golf.utils.Assets;

/**
 * Class which is used to render hole on screen.
 */
public class HoleRenderer {
	private final HoleModel mHoleModel;

	private Texture mHoleTexture;
	private String mHoleFilePath;

	/**
	 * Constructor.
	 * 
	 * @param holeModel
	 *            object which contains information about hole
	 */
	public HoleRenderer(HoleModel holeModel) {
		mHoleModel = holeModel;
	}

	void create() {
		mHoleFilePath = mHoleModel.getHoleFilePath();
		Assets.get().load(mHoleFilePath, Texture.class);

		Assets.get().finishLoading();
		mHoleTexture = Assets.get().get(mHoleFilePath);
	}

	void dispose() {
		if (mHoleFilePath != null) {
			Assets.get().unload(mHoleFilePath);
		}
	}

	/**
	 * Renders the {@link HoleModel}
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
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();
		batch.draw(mHoleTexture, mHoleModel.getPositionX(), mHoleModel.getPositionY(), mHoleModel.getWidth(),
				mHoleModel.getHeight());
		batch.end();
	}

}
