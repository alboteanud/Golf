
package com.lutu.golf.renderers.obstacle;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lutu.golf.models.obstacle.ForceApplyingObstacleModel;

/**
 * Class used to draw the obstacle which applies force.
 */
public class ForceApplyingObstacleRenderer extends AbstractObstacleRenderer<ForceApplyingObstacleModel> {

	private static final int SEGMENTS = 20;

	/**
	 * Constructs {@link ForceApplyingObstacleRenderer}.
	 * 
	 * @param forceApplyingObstacleModel
	 *            object contains information about obstacle which applies force {@link ForceApplyingObstacleModel}
	 */
	public ForceApplyingObstacleRenderer(ForceApplyingObstacleModel forceApplyingObstacleModel) {
		super(forceApplyingObstacleModel);
		if (DEBUG_RENDERING) {
			mShapeRenderer = new ShapeRenderer();
		}
	}

	private ShapeRenderer mShapeRenderer;

	@Override
	void debugRender(Camera camera) {
		mShapeRenderer.setProjectionMatrix(camera.projection);
		mShapeRenderer.setTransformMatrix(camera.view);

		mShapeRenderer.begin(ShapeType.Circle);
		mShapeRenderer.setColor(Color.YELLOW);
		mShapeRenderer.circle(getObstacleModel().getForceBeginningPointX(), getObstacleModel()
				.getForceBeginningPointY(), getObstacleModel().getForceRange(), SEGMENTS);
		mShapeRenderer.end();

		mShapeRenderer.begin(ShapeType.Rectangle);
		mShapeRenderer.setColor(Color.YELLOW);
		mShapeRenderer.rect(getObstacleModel().getPositionX(), getObstacleModel().getPositionY(), getObstacleModel()
				.getWidth(), getObstacleModel().getHeight());
		mShapeRenderer.end();

		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(Color.RED);
		mShapeRenderer.line(getObstacleModel().getForceBeginningPointX(), getObstacleModel().getForceBeginningPointY(),
				getObstacleModel().getFirstArmAngleForceFieldX(), getObstacleModel().getFirstArmAngleForceFieldY());
		mShapeRenderer.end();

		mShapeRenderer.begin(ShapeType.Point);
		mShapeRenderer.setColor(Color.RED);
		mShapeRenderer.point(getObstacleModel().getForceBeginningPointX(),
				getObstacleModel().getForceBeginningPointY(), 0f);
		mShapeRenderer.end();

		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(Color.PINK);
		mShapeRenderer.line(getObstacleModel().getForceBeginningPointX(), getObstacleModel().getForceBeginningPointY(),
				getObstacleModel().getSecondArmAngleForceFieldX(), getObstacleModel().getSecondArmAngleForceFieldY());
		mShapeRenderer.end();

		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(Color.LIGHT_GRAY);
		mShapeRenderer.line(getObstacleModel().getForceBeginningPointX(), getObstacleModel().getForceBeginningPointY(),
				getObstacleModel().getDebugCircle().x, getObstacleModel().getDebugCircle().y);
		mShapeRenderer.end();

		// coordinates
		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(Color.WHITE);
		mShapeRenderer.line(getObstacleModel().getForceBeginningPointX() - getObstacleModel().getForceRange(),
				getObstacleModel().getForceBeginningPointY(), getObstacleModel().getForceBeginningPointX()
						+ getObstacleModel().getForceRange(), getObstacleModel().getForceBeginningPointY());
		mShapeRenderer.end();
		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.line(getObstacleModel().getForceBeginningPointX(), getObstacleModel().getForceBeginningPointY()
				- getObstacleModel().getForceRange(), getObstacleModel().getForceBeginningPointX(), getObstacleModel()
				.getForceBeginningPointY() + getObstacleModel().getForceRange());
		mShapeRenderer.end();
		// end coordinates

		mShapeRenderer.begin(ShapeType.Point);
		mShapeRenderer.setColor(Color.GREEN);
		mShapeRenderer.point(getObstacleModel().getFirstArmAngleForceFieldX(), getObstacleModel()
				.getFirstArmAngleForceFieldY(), 0f);
		mShapeRenderer.end();
	}
}
