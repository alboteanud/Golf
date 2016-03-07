
package com.lutu.golf.controlere.animation;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * {@link com.lutu.golf.controlere.CameraController.CameraAnimation} implementation that pans and zooms the camera from one point to another.
 */
public class PanAndZoomAnimation extends com.lutu.golf.controlere.CameraController.CameraAnimation {
	private static final float ANIMATION_TIME = 3.0f;
	private static final float ANIMATION_DELAY = -1.0f;

	private float mViewportFromHeight;
	private float mViewportToHeight;
	private float mElapsedTime;

	private final Vector2 mFromPosition;
	private final Vector2 mToPosition;

	/**
	 * Constructs the {@link PanAndZoomAnimation} instance.
	 * 
	 * @param fromX
	 *            x coordinate of the start point
	 * @param fromY
	 *            y coordinate of the start point
	 * @param fromHeight
	 *            viewport's height at the start point
	 * @param toX
	 *            x coordinate of the end point
	 * @param toY
	 *            y coordinate of the end point
	 * @param toHeight
	 *            viewport's height at the end point
	 */
	public PanAndZoomAnimation(float fromX, float fromY, int fromHeight, float toX, float toY, float toHeight) {
		mViewportFromHeight = fromHeight;
		mViewportToHeight = toHeight;

		mFromPosition = new Vector2(fromX, fromY);
		mToPosition = new Vector2(toX, toY);
	}

	@Override
	public void onApply(Camera camera, float delta) {
		mElapsedTime += delta;

		if (mElapsedTime > 0.0f) {
			final float normalizedDistance = (camera.position.x - mFromPosition.x) / (mToPosition.x - mFromPosition.x);

			camera.position.x = Interpolation.pow2.apply(mFromPosition.x, mToPosition.x, mElapsedTime / ANIMATION_TIME);
			camera.position.y = Interpolation.pow2.apply(mFromPosition.y, mToPosition.y, mElapsedTime / ANIMATION_TIME);
			if (normalizedDistance < 1.0f) {
				camera.viewportHeight = Interpolation.pow2.apply(mViewportFromHeight, mViewportToHeight,
						normalizedDistance);
			} else {
				camera.viewportHeight = mViewportFromHeight;
			}

			if (mElapsedTime > ANIMATION_TIME) {
				stopAnimation(camera);
			}
		}
	}

	@Override
	public void onStartAnimation(Camera camera) {
		mElapsedTime = ANIMATION_DELAY;
		camera.position.x = mFromPosition.x;
		camera.position.y = mFromPosition.y;
		camera.viewportHeight = mViewportFromHeight;
	}

	@Override
	public void onStopAnimation(Camera camera) {
	}

	@Override
	public void onCameraViewportChanged(com.lutu.golf.controlere.CameraController controller, Camera camera) {
		controller.fixCameraViewPort(camera);
		controller.fixCameraPosition(camera);
		mFromPosition.x = camera.position.x;
		mFromPosition.y = camera.position.y;
		mViewportFromHeight = camera.viewportHeight;

		final Camera tempCam = new OrthographicCamera();
		tempCam.viewportHeight = mViewportToHeight;
		tempCam.position.x = mToPosition.x;
		tempCam.position.y = mToPosition.y;

		controller.fixCameraViewPort(tempCam);
		controller.fixCameraPosition(tempCam);
		mToPosition.x = tempCam.position.x;
		mToPosition.y = tempCam.position.y;
		mViewportToHeight = tempCam.viewportHeight;
	}
}
