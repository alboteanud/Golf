
package com.lutu.golf.controlere.animation;

import java.lang.ref.WeakReference;

import com.badlogic.gdx.graphics.Camera;
import com.lutu.golf.controlere.CameraController;
import com.lutu.golf.controlere.CameraController.CameraAnimation;
import com.lutu.golf.models.BallModel;

/**
 * {@link CameraAnimation} implementation that forces the {@link Camera} to follow the ball.
 */
public class FollowAnimation extends CameraAnimation {

	private static final float MULTIPLIER = 20.0f;

	private final WeakReference<BallModel> mBallModel;

	/**
	 * Constructs the {@link FollowAnimation} instance.
	 * 
	 * @param ballModel
	 *            the {@link BallModel} instance
	 */
	public FollowAnimation(BallModel ballModel) {
		mBallModel = new WeakReference<BallModel>(ballModel);
	}

	@Override
	public void onApply(Camera camera, float delta) {
		camera.position.x += (mBallModel.get().getPositionX() - camera.position.x) * delta * MULTIPLIER
				/ camera.viewportHeight;
		camera.position.y += (mBallModel.get().getPositionY() - camera.position.y) * delta * MULTIPLIER
				/ camera.viewportHeight;
	}

	@Override
	public void onStartAnimation(Camera camera) {
	}

	@Override
	public void onStopAnimation(Camera camera) {
	}

	@Override
	public void onCameraViewportChanged(CameraController controller, Camera camera) {
	}

}
