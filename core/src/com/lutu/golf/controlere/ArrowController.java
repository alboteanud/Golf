
package com.lutu.golf.controlere;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.lutu.golf.models.ArrowModel;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Class responsible for shot arrow control.
 */
public class ArrowController implements InputProcessor {

	private final BallModel mBallModel;
	private final ArrowModel mArrowModel;
	private final Camera mCamera;
	private static final float MAX_STRENGTH = 20.0f;
	private boolean mIsControlled = false;
	private boolean mIsFirstTouch = true;
	private final Vector3 mTouch;
	private final GameModel mGameModel;  		//eu

	/**
	 * Constructor.
	 *  @param arrowModel
	 *            object which contains information about shot arrow
	 * @param ballModel
	 *            object which contains information about ball
	 * @param camera
	 * @param mGameModel
	 */
	public ArrowController(ArrowModel arrowModel, BallModel ballModel, Camera camera, GameModel mGameModel) {
		mArrowModel = arrowModel;
		mBallModel = ballModel;
		mCamera = camera;
		mTouch = new Vector3();
		this.mGameModel=mGameModel;
	}

	/**
	 * Sets shot arrow position basing on position of the ball.
	 */
	public void setArrowPosition() {
		mArrowModel.setArrowPosition(mBallModel.getPositionX() - mArrowModel.getArrowWidth() / 2,
				mBallModel.getPositionY());
	}

	/**
	 * Sets shot arrow rotation angle.
	 * 
	 * @param angle
	 *            rotation angle in degrees
	 */
	public void setArrowAngle(float angle) {
		mArrowModel.setArrowAngle(angle);
	}

	/**
	 * Sets regular and horizontal length of the arrow.
	 * 
	 * @param pArrowLength
	 *            arrow length
	 * @param pHorizontalArrowLength
	 *            horizontal arrow length
	 * @param pArrowWidth
	 *            arrow width
	 * @param pArrowHorizontalWidth
	 *            horizontal arrow width
	 */
	public void setArrowSize(float pArrowLength, float pHorizontalArrowLength, float pArrowWidth,
			float pArrowHorizontalWidth) {
		if (pArrowLength < ArrowModel.MAX_ARROW_HEIGHT) {
			mArrowModel.setArrowHeight(pArrowLength);
		} else {
			mArrowModel.setArrowHeight(ArrowModel.MAX_ARROW_HEIGHT);
		}

		if (pHorizontalArrowLength < ArrowModel.MAX_ARROW_HEIGHT) {
			mArrowModel.setHorizontalArrowLength(pHorizontalArrowLength);
		} else {
			mArrowModel.setHorizontalArrowLength(ArrowModel.MAX_ARROW_HEIGHT);
		}

		if (pArrowWidth > ArrowModel.MAX_ARROW_WIDTH) {
			mArrowModel.setArrowWidth(ArrowModel.MAX_ARROW_WIDTH);
		} else {
			mArrowModel.setArrowWidth(pArrowWidth);
		}

		if (pArrowHorizontalWidth > ArrowModel.MAX_ARROW_WIDTH) {
			mArrowModel.setHorizontalArrowWidth(ArrowModel.MAX_ARROW_WIDTH);
		} else {
			mArrowModel.setHorizontalArrowWidth(pArrowHorizontalWidth);
		}
	}

	/**
	 * Sets the indicator rotation angle.
	 * 
	 * @param angle
	 *            rotation angle in degrees
	 */
	public void setIndicatorAngle(float angle) {
		mArrowModel.setIndicatorAngle(angle);
	}

	/**
	 * Sets the indicator (virtual) length, limited to a defined value.
	 * 
	 * @param length
	 *            indicator length
	 */
	public void setIndicatorLength(float length) {
		if (length < ArrowModel.MAX_ARROW_HEIGHT) {
			mArrowModel.setIndicatorLength(length);
		} else {
			mArrowModel.setIndicatorLength(ArrowModel.MAX_ARROW_HEIGHT);
		}
	}

	/**
	 * Sets shot vector parameters and adjust values to defined in {@link BallModel}.
	 * 
	 * @param x
	 *            horizontal parameter value
	 * @param y
	 *            vertical parameter value
	 */
	public void setShotVector(float x, float y) {
		mArrowModel.setShotVector(x / ArrowModel.MAX_ARROW_HEIGHT * MAX_STRENGTH, y / ArrowModel.MAX_ARROW_HEIGHT
				* MAX_STRENGTH);
	}

	/**
	 * Sets indicator position coordinates.
	 */
	public void setIndicatorPosition() {
		mArrowModel.setIndicatorPosition(mBallModel.getPositionX() - mArrowModel.getIndicatorWidth() / 2,
				mBallModel.getPositionY() + mArrowModel.getIndicatorLength() - mArrowModel.getIndicatorHeight() / 2);
	}

	/**
	 * Sets indicator vertical rotation center.
	 */
	public void setIndicatorRotationCenterY() {
		mArrowModel.setIndicatorRotationCenterY(-mArrowModel.getIndicatorLength() + mArrowModel.getIndicatorHeight()
				/ 2);
	}

	/**
	 * Sets indicator position for touch detection purpose. Position is counted basing on shot arrow coordinates.
	 * 
	 * @param x
	 *            arrow x coordinate
	 * @param y
	 *            arrow y coordinate
	 */
	public void setTouchIndicatorPosition(float x, float y) {
		mArrowModel.setTouchIndicatorPosition(mBallModel.getPositionX() - x, mBallModel.getPositionY() - y);
	}

	/**
	 * Sets arrow settings to initial values.
	 */
	public void resetArrowSettings() {
		mIsFirstTouch = true;
		mArrowModel.resetModel();
	}

	/**
	 * Updates state of shot arrow and its indicator.
	 */
	public void update() {
		setArrowPosition();
		setIndicatorPosition();
		setIndicatorRotationCenterY();
	}

	/**
	 * Sets if indicator and arrow should be shown or not.
	 * 
	 * @param pVisible
	 *            true if arrow and indicator should be visible, false otherwise
	 */
	public void setVisible(boolean pVisible) {
		mArrowModel.setVisible(pVisible);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// temporary solution for test: sets indicator position on first touch
		if (mIsFirstTouch && !mBallModel.isBallMoving()) {
			mArrowModel.setTouchIndicatorPosition(mBallModel.getPositionX(), mBallModel.getPositionY());
			mIsFirstTouch = false;
		}
		mTouch.set(screenX, screenY, 0.0f);
		mCamera.unproject(mTouch);
		final float x = mTouch.x;
		final float y = mTouch.y;
		if (x > (mArrowModel.getTouchIndicatorPositionX() - mArrowModel.getIndicatorWidth())
				&& x < (mArrowModel.getTouchIndicatorPositionX() + mArrowModel.getIndicatorWidth())
				&& y > (mArrowModel.getTouchIndicatorPositionY() - mArrowModel.getIndicatorHeight())
				&& y < (mArrowModel.getTouchIndicatorPositionY() + mArrowModel.getIndicatorHeight())) {
			mIsControlled = true;
			return true;
		}
		return false;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (mIsControlled) {
			mIsControlled = false;

            if (mGameModel.getBallModel().isOnSlowdownObstacle()) {   //eu
                GolfAudioManager.playSound(AudioPaths.SHOT_FROM_SAND);
            } else {
                GolfAudioManager.playSound(AudioPaths.SHOT);
            }
			mGameModel.onShootButtonClicked(true);   	 //eu

			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (mIsControlled) {
			mTouch.set(screenX, screenY, 0.0f);
			mCamera.unproject(mTouch);
			final float x = mTouch.x;
			final float y = mTouch.y;

			final float length = (float) (Math.sqrt(Math.pow((x - mBallModel.getPositionX()), 2)
					+ Math.pow((y - mBallModel.getPositionY()), 2)));
			final float angle = MathUtils.atan2(y - mBallModel.getPositionY(), x - mBallModel.getPositionX())
					* MathUtils.radiansToDegrees;
			setIndicatorLength(length);
			setIndicatorAngle(angle + ArrowModel.RIGHT_ANGLE);

			// It is necessary to add 180 degrees to each angle because arrow vector is mirror reflection of
			// indicator point
			final float arrowX = mArrowModel.getIndicatorLength() * MathUtils.cosDeg(angle + ArrowModel.STRAIGHT_ANGLE);
			final float arrowY = mArrowModel.getIndicatorLength() * MathUtils.sinDeg(angle + ArrowModel.STRAIGHT_ANGLE);

			setTouchIndicatorPosition(arrowX, arrowY);

			// It is necessary to add 90 degrees, because arrow shape has different rotation center comparing to
			// game
			// coordinate system (rotated 90 degrees)
			setArrowAngle(angle + ArrowModel.RIGHT_ANGLE);

			setShotVector(arrowX, arrowY);
			final float horizontalLength = Math.abs(x - mBallModel.getPositionX());
			float horizontalAngle;
			if (x >= mBallModel.getPositionX()) {
				horizontalAngle = ArrowModel.RIGHT_ANGLE;
			} else {
				horizontalAngle = -ArrowModel.RIGHT_ANGLE;
			}
			final float arrowWidth = ArrowModel.MIN_ARROW_WIDTH + length / ArrowModel.MAX_ARROW_HEIGHT
					* (ArrowModel.MAX_ARROW_WIDTH - ArrowModel.MIN_ARROW_WIDTH);
			final float arrowHorizontalWidth = ArrowModel.MIN_ARROW_WIDTH + horizontalLength
					/ ArrowModel.MAX_ARROW_HEIGHT * (ArrowModel.MAX_ARROW_WIDTH - ArrowModel.MIN_ARROW_WIDTH);
			setArrowSize(length, horizontalLength, arrowWidth, arrowHorizontalWidth);
			mArrowModel.setHorizontalArrowAngle(horizontalAngle);

			return true;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}
