
package com.lutu.golf.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.utils.Assets;

/**
 * Class which is used to draw game ball.
 * 
 */
public class BallRenderer {

	private static final String GOLF_BALL_PATH = "golf_ball.png";

	private final BallModel mBallModel;

	private Texture mBallTexture;

	/**
	 * Constructor.
	 * 
	 * @param ballModel
	 *            object which contains information about ball
	 */
	public BallRenderer(BallModel ballModel) {
		mBallModel = ballModel;
	}

	void create() {
		Assets.get().load(GOLF_BALL_PATH, Texture.class);
	}

	void dispose() {
		Assets.get().unload(GOLF_BALL_PATH);
	}

	void show() {
		mBallTexture = Assets.get().get(GOLF_BALL_PATH);
	}

	/**
	 * Renders the {@link BallModel}
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

		batch.draw(mBallTexture, mBallModel.getPositionX() - mBallModel.getRadius(), mBallModel.getPositionY()
				- mBallModel.getRadius(), 2.0f * mBallModel.getRadius(), 2.0f * mBallModel.getRadius());
		batch.end();
	}
}
