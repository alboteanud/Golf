
package com.lutu.golf.renderers.obstacle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lutu.golf.models.obstacle.BallDestroyingObstacleModel;

/**
 * Class used to draw the obstacle which destroys the ball.
 */
public class BallDestroyingObstacleRenderer extends AbstractObstacleRenderer<BallDestroyingObstacleModel> {

	private ShapeRenderer mShapeRenderer;

	/**
	 * Constructs {@link BallDestroyingObstacleRenderer}.
	 * 
	 * @param ballDestroyingObstacleModel
	 *            object contains information about obstacle which destroys the ball {@link BallDestroyingObstacleModel}
	 */
	public BallDestroyingObstacleRenderer(BallDestroyingObstacleModel ballDestroyingObstacleModel) {
		super(ballDestroyingObstacleModel);
		if (DEBUG_RENDERING) {
			mShapeRenderer = new ShapeRenderer();
		}
	}

	@Override
	void debugRender(Camera camera) {
		mShapeRenderer.setProjectionMatrix(camera.projection);
		mShapeRenderer.setTransformMatrix(camera.view);

		mShapeRenderer.begin(ShapeType.Rectangle);
		mShapeRenderer.setColor(Color.YELLOW);
		mShapeRenderer.rect(getObstacleModel().getPositionX(), getObstacleModel().getPositionY(), getObstacleModel()
				.getWidth(), getObstacleModel().getHeight());
		mShapeRenderer.end();
	}
}
