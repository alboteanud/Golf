
package com.lutu.golf.renderers.obstacle;

import com.badlogic.gdx.graphics.Camera;
import com.lutu.golf.models.obstacle.SlowdownObstacleModel;

/**
 * Class used to draw the obstacle which slows down the ball.
 */
public class SlowdownObstacleRenderer extends AbstractObstacleRenderer<SlowdownObstacleModel> {

	/**
	 * Constructs {@link SlowdownObstacleRenderer}.
	 * 
	 * @param slowdownObstacleModel
	 *            information about obstacle {@link SlowdownObstacleModel}
	 */
	public SlowdownObstacleRenderer(SlowdownObstacleModel slowdownObstacleModel) {
		super(slowdownObstacleModel);
	}

	@Override
	void debugRender(Camera camera) {
		// do nothing -
	}

}
