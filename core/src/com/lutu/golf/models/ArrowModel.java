
package com.lutu.golf.models;

import com.badlogic.gdx.math.Vector2;

/**
 * Model which represents shot arrow with additional indicator to control shot direction and strength.
 */
public class ArrowModel {
	private static final String TAG = ArrowModel.class.getSimpleName();

	/**
	 * Maximum length of the shot arrow.
	 */
	public static final float MAX_ARROW_HEIGHT = 5.0f;

	/**
	 * Right angle - 90 degrees.
	 */
	public static final float RIGHT_ANGLE = 90.0f;

	/**
	 * Straight angle - 180 degrees.
	 */
	public static final float STRAIGHT_ANGLE = 2 * RIGHT_ANGLE;

	/** Max arrow width. */
	public static final float MAX_ARROW_WIDTH = 0.75f;

	/** Min arrow width. */
	public static final float MIN_ARROW_WIDTH = 0.15f;

	private static final float DEFAULT_HEIGHT = 0.0f;
	private static final float INDICATOR_RADIUS = 1.0f;
	private static final float DEFAULT_INDICATOR_ROTATION_X = 0.5f * INDICATOR_RADIUS;
	private static final float DEFAULT_INDICATOR_ROTATION_Y = 0.0f;
	private static final float DEFAULT_ROTATION_X_MULTIPLIER = 0.5f;
	private static final float DEFAULT_ROTATION_X = DEFAULT_ROTATION_X_MULTIPLIER * MIN_ARROW_WIDTH;
	private static final float DEFAULT_ROTATION_Y = 0.0f;
	private final Vector2 mArrowPosition;
	private final Vector2 mArrowRotationCenter;
	private final Vector2 mArrowSize;
	private final Vector2 mHorizontalArrowRotationCenter;
	private final Vector2 mHorizontalArrowSize;
	private float mHorizontalArrowAngle;
	private float mArrowAngle;
	private float mIndicatorAngle;
	private float mIndicatorLength;

	private final Vector2 mStrengthVector;

	private final Vector2 mIndicatorPosition;
	private final Vector2 mIndicatorSize;
	private final Vector2 mIndicatorRotationCenter;
	private final Vector2 mIndicatorPositionInCamera;
	private boolean mVisible;
	private boolean mHorizontalArrow;

	/**
	 * Constructor.
	 */
	public ArrowModel() {
		mArrowPosition = new Vector2();
		mArrowRotationCenter = new Vector2(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
		mArrowSize = new Vector2(MIN_ARROW_WIDTH, DEFAULT_HEIGHT);
		mHorizontalArrowRotationCenter = new Vector2(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
		mHorizontalArrowSize = new Vector2(MIN_ARROW_WIDTH, DEFAULT_HEIGHT);
		mHorizontalArrow = true;

		mStrengthVector = new Vector2();

		mIndicatorPosition = new Vector2();
		mIndicatorPositionInCamera = new Vector2();
		mIndicatorSize = new Vector2(INDICATOR_RADIUS, INDICATOR_RADIUS);
		mIndicatorRotationCenter = new Vector2(DEFAULT_INDICATOR_ROTATION_X, DEFAULT_INDICATOR_ROTATION_Y);
		resetModel();
	}

	/**
	 * Returns the indicator and arrow visibility.
	 * 
	 * @return true if indicator and arrow are visible, false otherwise
	 */
	public boolean isVisible() {
		return mVisible;
	}

	/**
	 * Sets the indicator and arrow visibility.
	 * 
	 * @param pVisible
	 *            true if arrow and indicator should be visible, false otherwise
	 */
	public void setVisible(boolean pVisible) {
		mVisible = pVisible;
	}

	/**
	 * Sets arrow position in general coordinates system.
	 * 
	 * @param x
	 *            x coordinate value
	 * @param y
	 *            y coordinate value
	 */
	public void setArrowPosition(float x, float y) {
		mArrowPosition.x = x;
		mArrowPosition.y = y;
	}

	/**
	 * Returns x coordinate which defines x position of arrow.
	 * 
	 * @return x coordinate value
	 */
	public float getArrowPositionX() {
		return mArrowPosition.x;
	}

	/**
	 * Returns y coordinate which defines y position of arrow.
	 * 
	 * @return y coordinate value
	 */
	public float getArrowPositionY() {
		return mArrowPosition.y;
	}

	/**
	 * Sets arrow width.
	 * 
	 * @param width
	 *            arrow width
	 */
	public void setArrowWidth(float width) {
		mArrowSize.x = width;
		mArrowRotationCenter.x = DEFAULT_ROTATION_X_MULTIPLIER * width;
	}

	/**
	 * Sets arrow height (which practically indicates arrow length).
	 * 
	 * @param height
	 *            arrow height
	 */
	public void setArrowHeight(float height) {
		mArrowSize.y = height;
	}

	/**
	 * Returns arrow width.
	 * 
	 * @return arrow width
	 */
	public float getArrowWidth() {
		if (mHorizontalArrow) {
			return mHorizontalArrowSize.x;
		} else {
			return mArrowSize.x;
		}
	}

	/**
	 * Returns arrow height.
	 * 
	 * @return arrow height
	 */
	public float getArrowHeight() {
		if (mHorizontalArrow) {
			return mHorizontalArrowSize.y;
		} else {
			return mArrowSize.y;
		}
	}

	/**
	 * Returns the indicator length.
	 * 
	 * @return the indicator length
	 */
	public float getIndicatorLength() {
		return mIndicatorLength;
	}

	/**
	 * Sets the indicator length.
	 * 
	 * @param length
	 *            new indicator length
	 */
	public void setIndicatorLength(float length) {
		mIndicatorLength = length;
	}

	/**
	 * Sets arrow horizontal length.
	 * 
	 * @param pLength
	 *            horizontal arrow length
	 */
	public void setHorizontalArrowLength(float pLength) {
		mHorizontalArrowSize.y = pLength;
	}

	/**
	 * Sets arrow horizontal width.
	 * 
	 * @param pWidth
	 *            horizontal arrow width
	 */
	public void setHorizontalArrowWidth(float pWidth) {
		mHorizontalArrowSize.x = pWidth;
		mHorizontalArrowRotationCenter.x = DEFAULT_ROTATION_X_MULTIPLIER * pWidth;
	}

	/**
	 * Sets arrow rotation angle.
	 * 
	 * @param angle
	 *            angle in degrees
	 */
	public void setArrowAngle(float angle) {
		mArrowAngle = angle;
	}

	/**
	 * Returns arrow rotation angle.
	 * 
	 * @return arrow rotation angle
	 */
	public float getArrowAngle() {
		if (mHorizontalArrow) {
			return mHorizontalArrowAngle;
		} else {
			return mArrowAngle;
		}
	}

	/**
	 * Sets horizontal arrow rotation angle.
	 * 
	 * @param angle
	 *            horizontal angle in degrees
	 */
	public void setHorizontalArrowAngle(float angle) {
		mHorizontalArrowAngle = angle;
	}

	/**
	 * Returns the indicator rotation angle.
	 * 
	 * @return indicator rotation angle
	 */
	public float getIndicatorAngle() {
		return mIndicatorAngle;
	}

	/**
	 * Sets the indicator rotation angle.
	 * 
	 * @param angle
	 *            angle in degrees
	 */
	public void setIndicatorAngle(float angle) {
		mIndicatorAngle = angle;
	}

	/**
	 * Returns x coordinate which defines x position of arrow rotation center.
	 * 
	 * @return x coordinate value
	 */
	public float getArrowRotationCenterX() {
		if (mHorizontalArrow) {
			return mHorizontalArrowRotationCenter.x;
		} else {
			return mArrowRotationCenter.x;
		}
	}

	/**
	 * Returns y coordinate which defines y position of arrow rotation center.
	 * 
	 * @return y coordinate value
	 */
	public float getArrowRotationCenterY() {
		if (mHorizontalArrow) {
			return mHorizontalArrowRotationCenter.y;
		} else {
			return mArrowRotationCenter.y;
		}
	}

	/**
	 * Sets shot vector parameters.
	 * 
	 * @param x
	 *            value of horizontal parameter
	 * @param y
	 *            value of vertical parameter
	 */
	public void setShotVector(float x, float y) {
		mStrengthVector.x = x;
		mStrengthVector.y = y;
	}

	/**
	 * Returns horizontal parameter of shot vector.
	 * 
	 * @return shot vector horizontal parameter
	 */
	public float getShotVectorX() {
		return mStrengthVector.x;
	}

	/**
	 * Returns vertical parameter of shot vector.
	 * 
	 * @return shot vector vertical parameter
	 */
	public float getShotVectorY() {
		if (mHorizontalArrow) {
			return 0.0f;
		} else {
			return mStrengthVector.y;
		}
	}

	/**
	 * Returns indicator width.
	 * 
	 * @return indicator width
	 */
	public float getIndicatorWidth() {
		return mIndicatorSize.x;
	}

	/**
	 * Returns indicator height.
	 * 
	 * @return indicator height
	 */
	public float getIndicatorHeight() {
		return mIndicatorSize.y;
	}

	/**
	 * Sets indicator position in general coordinates system.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setIndicatorPosition(float x, float y) {
		mIndicatorPosition.x = x;
		mIndicatorPosition.y = y;
	}

	/**
	 * Returns x coordinate which defines x position of indicator.
	 * 
	 * @return x coordinate
	 */
	public float getIndicatorPositionX() {
		return mIndicatorPosition.x;
	}

	/**
	 * Returns y coordinate which defines y position of indicator.
	 * 
	 * @return y coordinate
	 */
	public float getIndicatorPositionY() {
		return mIndicatorPosition.y;
	}

	/**
	 * Sets x coordinate defining horizontal indicator rotation center.
	 * 
	 * @param x
	 *            x coordinate
	 */
	public void setIndicatorRotationCenterX(float x) {
		mIndicatorRotationCenter.x = x;
	}

	/**
	 * Sets y coordinate defining vertical indicator rotation center.
	 * 
	 * @param y
	 *            y coordinate
	 */
	public void setIndicatorRotationCenterY(float y) {
		mIndicatorRotationCenter.y = y;
	}

	/**
	 * Returns x coordinate of indicator rotation center.
	 * 
	 * @return x coordinate
	 */
	public float getIndicatorRotationCenterX() {
		return mIndicatorRotationCenter.x;
	}

	/**
	 * Returns y coordinate of indicator rotation center.
	 * 
	 * @return y coordinate
	 */
	public float getIndicatorRotationCenterY() {
		return mIndicatorRotationCenter.y;
	}

	/**
	 * Sets indicator position for touch detection purpose.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setTouchIndicatorPosition(float x, float y) {
		mIndicatorPositionInCamera.x = x;
		mIndicatorPositionInCamera.y = y;
	}

	/**
	 * Returns x coordinate which defines x position of indicator for touch detection purpose.
	 * 
	 * @return x coordinate
	 */
	public float getTouchIndicatorPositionX() {
		return mIndicatorPositionInCamera.x;
	}

	/**
	 * Returns y coordinate which defines y position of indicator for touch detection purpose.
	 * 
	 * @return y coordinate
	 */
	public float getTouchIndicatorPositionY() {
		return mIndicatorPositionInCamera.y;
	}

	/**
	 * Returns true, if arrow will be displayed as horizontal only, false otherwise.
	 * 
	 * @return true, if arrow will be displayed as horizontal only, false otherwise
	 */
	public boolean isHorizontalArrow() {
		return mHorizontalArrow;
	}

	/**
	 * Makes the shot arrow horizontal, if club type is PUTTER.
	 * 
	 * @param pClubType
	 *            new club type
	 */
	public void onClubTypeChanged(ClubModel.ClubType pClubType) {
		if (ClubModel.ClubType.PUTTER.equals(pClubType)) {
			mHorizontalArrow = true;
		} else {
			mHorizontalArrow = false;
		}
	}

	/**
	 * Resets the basic model parameters to the default values.
	 */
	public void resetModel() {
		mArrowAngle = 0.0f;
		mArrowSize.set(MIN_ARROW_WIDTH, DEFAULT_HEIGHT);
		mHorizontalArrowSize.set(MIN_ARROW_WIDTH, DEFAULT_HEIGHT);
		mArrowRotationCenter.set(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
		mHorizontalArrowRotationCenter.set(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
		setArrowHeight(0.0f);
		mHorizontalArrowAngle = 0.0f;
		mIndicatorLength = 0.0f;
		mIndicatorAngle = 0.0f;
		setShotVector(0.0f, 0.0f);
	}
}
