
package com.lutu.golf.controlere;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.lutu.golf.models.GameMapModel;

/**
 * Controls the {@link Camera} transformation by handling gesture events and applying {@link CameraAnimation
 * CameraAnimations}.
 */
public class CameraController implements GestureListener {

	private static final float CAMERA_MIN_HEIGHT = 8.0f;
	private static final float CAMERA_MAX_HEIGHT = 50.0f;

	private final Camera mCamera;
	private final GameMapModel mGameMapModel;

	private float mCameraRatio;

	private boolean mUpdatePinchStartPoints = true;
	private final Vector3 mPrev1 = new Vector3();
	private final Vector3 mPrev2 = new Vector3();
	private final Vector3 mCurr1 = new Vector3();
	private final Vector3 mCurr2 = new Vector3();

	private CameraAnimation mAnimation;

	/**
	 * Constructs the {@link CameraController} instance.
	 * 
	 * @param gameMapModel
	 *            the {@link GameMapModel} instance
	 * @param camera
	 *            {@link Camera} instance
	 */
	public CameraController(GameMapModel gameMapModel, Camera camera) {
		mGameMapModel = gameMapModel;
		mCamera = camera;
		mCamera.viewportHeight = gameMapModel.getMapHeight();
	}

	/**
	 * Updates the controller state.
	 * 
	 * @param delta
	 *            time elapsed since last frame
	 */
	public void update(float delta) {
		if (mAnimation != null && mAnimation.isStarted()) {
			mAnimation.onApply(mCamera, delta);
		}

		fixCameraViewPort(mCamera);
		mCamera.update();
		fixCameraPosition(mCamera);
		mCamera.update();
	}

	/**
	 * Called when the viewport is resized.
	 * 
	 * @param width
	 *            new viewport width
	 * @param height
	 *            new viewport height
	 */
	public void resize(int width, int height) {
		mCameraRatio = (float) width / height;
		fixCameraViewPort(mCamera);

		if (mAnimation != null) {
			mAnimation.onCameraViewportChanged(this, mCamera);
		}
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	/**
	 * Sets the {@link CameraAnimation} instance to be used for {@link Camera} transformations.
	 * 
	 * @param animation
	 *            {@link CameraAnimation} instance
	 */
	public void setAnimation(CameraAnimation animation) {
		if (mAnimation != null) {
			mAnimation.stopAnimation(mCamera);
		}

		mAnimation = animation;
	}

	/**
	 * Starts the {@link CameraAnimation} if set.
	 */
	public void startAnimation() {
		fixCameraViewPort(mCamera);
		fixCameraPosition(mCamera);
		if (mAnimation != null) {
			mAnimation.startAnimation(mCamera);
		}
	}

	/**
	 * Stops the {@link CameraAnimation} if set.
	 */
	public void stopAnimation() {
		if (mAnimation != null) {
			mAnimation.stopAnimation(mCamera);
		}
	}

	/**
	 * Handles a single touch.
	 * 
	 * @param x
	 *            current screen-space x coordinate
	 * @param y
	 *            current screen-space y coordinate
	 * @param pointer
	 *            pointer index
	 * @param button
	 *            button index
	 * @return true if the event was handled, false otherwise
	 */
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) {
			// Pinch started

			mUpdatePinchStartPoints = true;
		}

		stopAnimation();

		return true;
	}

	/**
	 * Handles panning gesture.
	 * 
	 * @param x
	 *            current screen-space x coordinate
	 * @param y
	 *            current screen-space y coordinate
	 * @param dx
	 *            screen-space x coordinate difference
	 * @param dy
	 *            screen-space y coordinate difference
	 * @return true if the event was handled, false otherwise
	 */
	@Override
	public boolean pan(float x, float y, float dx, float dy) {
		final Vector3 p = Vector3.tmp2.set(x - dx, y - dy, 0.0f); // previous
		final Vector3 s = Vector3.tmp.set(x, y, 0.0f); // current

		// To world-space

		mCamera.unproject(p);
		mCamera.unproject(s);

		// Update the position
		mCamera.translate(p.sub(s));

		return true;
	}

	/**
	 * Handles pinching gesture.
	 * 
	 * @param s1
	 *            initial first pointer position
	 * @param s2
	 *            initial second pointer position
	 * @param p1
	 *            current first pointer position
	 * @param p2
	 *            current second pointer position
	 * @return true if the event was handled, false otherwise
	 */
	@Override
	public boolean pinch(Vector2 s1, Vector2 s2, Vector2 p1, Vector2 p2) {
		if (mUpdatePinchStartPoints) {
			mUpdatePinchStartPoints = false;

			// Calculate the previous value of the pinch (from s1 and s2 vectors)

			mPrev1.set(s1.x, s1.y, 0.0f);
			mPrev2.set(s2.x, s2.y, 0.0f);
			mCamera.unproject(mPrev1);
			mCamera.unproject(mPrev2);
		}

		// Calculate current pointer positions in world coordinates

		mCurr1.set(p1.x, p1.y, 0.0f);
		mCurr2.set(p2.x, p2.y, 0.0f);
		mCamera.unproject(mCurr1);
		mCamera.unproject(mCurr2);

		// Update zoom level

		final float prevLen = Vector3.tmp.set(mPrev1).sub(mPrev2).len();
		final float currLen = Vector3.tmp.set(mCurr1).sub(mCurr2).len();

		mCamera.viewportHeight += prevLen - currLen;
		fixCameraViewPort(mCamera);

		mCamera.update();
		mCurr1.set(p1.x, p1.y, 0.0f);
		mCurr2.set(p2.x, p2.y, 0.0f);
		mCamera.unproject(mCurr1);
		mCamera.unproject(mCurr2);

		if (fixCameraPosition(mCamera)) {
			mCamera.update();
			mCurr1.set(p1.x, p1.y, 0.0f);
			mCurr2.set(p2.x, p2.y, 0.0f);
			mCamera.unproject(mCurr1);
			mCamera.unproject(mCurr2);
		}

		mPrev1.set(p1.x, p1.y, 0.0f);
		mPrev2.set(p2.x, p2.y, 0.0f);
		mCamera.unproject(mPrev1);
		mCamera.unproject(mPrev2);

		return true;
	}

	/**
	 * Modifies the camera view port based on mouse scroll input.
	 * 
	 * @param amount
	 *            mouse scroll value
	 */
	public void scrolled(int amount) {
		mCamera.viewportHeight += amount;

		fixCameraViewPort(mCamera);
		mCamera.update();

		if (amount > 0) {
			fixCameraPosition(mCamera);
		}
	}

	/**
	 * Modifies the camera position if it went out of map bounds.
	 * 
	 * @param camera
	 *            {@link Camera} instance
	 * 
	 * @return true if the position was modified, false otherwise
	 */
	public boolean fixCameraPosition(Camera camera) {
		final Rectangle cameraRect = Rectangle.tmp;
		cameraRect.height = camera.viewportHeight;
		cameraRect.width = camera.viewportWidth;
		cameraRect.x = camera.position.x - camera.viewportWidth * 0.5f;
		cameraRect.y = camera.position.y - camera.viewportHeight * 0.5f;

		final Rectangle mapRect = Rectangle.tmp2;
		mapRect.height = mGameMapModel.getMapHeight();
		mapRect.width = mGameMapModel.getMapWidth();

		if (mapRect.contains(cameraRect)) {
			// Camera within bounds, no need to fix

			return false;
		}

		if (cameraRect.x < mapRect.x) {
			camera.position.x = mapRect.x + camera.viewportWidth * 0.5f;
		} else if (cameraRect.x + cameraRect.width > mapRect.x + mapRect.width) {
			camera.position.x = mapRect.x + mapRect.width - camera.viewportWidth * 0.5f;
		}

		if (cameraRect.y < mapRect.y) {
			camera.position.y = mapRect.y + camera.viewportHeight * 0.5f;
		} else if (cameraRect.y + cameraRect.height > mapRect.y + mapRect.height) {
			camera.position.y = mapRect.y + mapRect.height - camera.viewportHeight * 0.5f;
		}

		return true;
	}

	/**
	 * Makes sure the camera stays within reasonable size and keeps its aspect ratio.
	 * 
	 * @param camera
	 *            {@link Camera} instance
	 */
	public void fixCameraViewPort(Camera camera) {

		if (camera.viewportHeight < CAMERA_MIN_HEIGHT) {
			camera.viewportHeight = CAMERA_MIN_HEIGHT;
		} else if (camera.viewportHeight > mGameMapModel.getMapHeight()) {
			// Map is shorter than the camera
			camera.viewportHeight = mGameMapModel.getMapHeight();
		} else if (camera.viewportHeight > CAMERA_MAX_HEIGHT) {
			camera.viewportHeight = CAMERA_MAX_HEIGHT;
		}

		camera.viewportWidth = camera.viewportHeight * mCameraRatio;

		if (camera.viewportWidth > mGameMapModel.getMapWidth()) {
			// Camera is wider than the map
			camera.viewportWidth = mGameMapModel.getMapWidth();
			camera.viewportHeight = camera.viewportWidth / mCameraRatio;
		}
	}

	/**
	 * Implementations should change the {@link Camera}'s properties based on the elapsed time to perform an animation.
	 */
	public abstract static class CameraAnimation {

		private boolean mStarted;

		private OnAnimationStartedListener mOnAnimationStartedListener;
		private OnAnimationStoppedListener mOnAnimationStoppedListener;

		public boolean isStarted() {
			return mStarted;
		}

		/**
		 * Starts the animation.
		 * 
		 * This method is meant to be called from the {@link CameraController} and the animation itself.
		 * 
		 * @param camera
		 *            {@link Camera} instance
		 */
		protected void startAnimation(Camera camera) {
			mStarted = true;
			onStartAnimation(camera);

			if (mOnAnimationStartedListener != null) {
				mOnAnimationStartedListener.onAnimationStarted();
			}
		}

		/**
		 * Stops the animation.
		 * 
		 * This method is meant to be called from the {@link CameraController} and the animation itself.
		 * 
		 * @param camera
		 *            {@link Camera} instance
		 */
		protected void stopAnimation(Camera camera) {
			mStarted = false;
			onStopAnimation(camera);

			if (mOnAnimationStoppedListener != null) {
				mOnAnimationStoppedListener.onAnimationStopped();
			}
		}

		/**
		 * Sets the {@link OnAnimationStartedListener} instance.
		 * 
		 * @param listener
		 *            {@link OnAnimationStartedListener} instance
		 */
		public void setOnAnimationStartedListener(OnAnimationStartedListener listener) {
			mOnAnimationStartedListener = listener;
		}

		/**
		 * Sets the {@link OnAnimationStoppedListener} instance.
		 * 
		 * @param listener
		 *            {@link OnAnimationStoppedListener} instance
		 */
		public void setOnAnimationStoppedListener(OnAnimationStoppedListener listener) {
			mOnAnimationStoppedListener = listener;
		}

		/**
		 * Applies the animation to the {@link Camera}.
		 * 
		 * @param delta
		 *            time elapsed since last frame
		 * @param camera
		 *            {@link Camera} instance
		 */
		public abstract void onApply(Camera camera, float delta);

		/**
		 * Called when the animation is started.
		 * 
		 * @param camera
		 *            {@link Camera} instance
		 */
		public abstract void onStartAnimation(Camera camera);

		/**
		 * Called when the animation is stopped.
		 * 
		 * @param camera
		 *            {@link Camera} instance
		 */
		public abstract void onStopAnimation(Camera camera);

		/**
		 * Called when the camera viewport was changed.
		 * 
		 * @param controller
		 *            {@link CameraController} that uses this animation
		 * @param camera
		 *            {@link Camera} instance
		 */
		public abstract void onCameraViewportChanged(CameraController controller, Camera camera);

		/**
		 * Implementations will be notified when the animation starts.
		 */
		public interface OnAnimationStartedListener {
			/**
			 * Called when the animation starts.
			 */
			void onAnimationStarted();
		}

		/**
		 * Implementations will be notified when the animation stops.
		 */
		public interface OnAnimationStoppedListener {
			/**
			 * Called when the animation stops.
			 */
			void onAnimationStopped();
		}
	}
}
