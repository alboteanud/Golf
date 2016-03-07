
package com.lutu.golf.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Model which represents golf club.
 * <p>
 * This model contains information about club position, size, rotate angle and rotation center.
 * </p>
 */
public class ClubModel {
	private static final String TAG = ClubModel.class.getSimpleName();

	/**
	 * Enum representing club type with their basic parameters.
	 */
	public enum ClubType {
		/**
		 * Wooden club - stronger shot power, but with diminished precision.
		 */
		WOODEN(0.2f, 1.5f),
		/**
		 * Iron club - standard shot power, no precision penalty.
		 */
		IRON(0.0f, 1.0f),
		/**
		 * Putter club - weaker shot power, can only shoot horizontally.
		 */
		PUTTER(0.0f, 0.7f);

		private final float mMaxRandomCoefficient;
		private final float mMinRandomCoefficient;
		private final float mForceMultiplier;

		ClubType(float pRandomCoefficient, float pForceMultiplier) {
			mMaxRandomCoefficient = 1.0f + pRandomCoefficient;
			mMinRandomCoefficient = 1.0f - pRandomCoefficient;
			mForceMultiplier = pForceMultiplier;
		}

		public float getMaxRandomCoefficient() {
			return mMaxRandomCoefficient;
		}

		public float getMinRandomCoefficient() {
			return mMinRandomCoefficient;
		}

		public float getForceMultiplier() {
			return mForceMultiplier;
		}
	}

	/**
	 * Rotation center offset parameter when club is on the left.
	 */
	public static final float LEFT_ROTATION_OFFSET = 0.25f;

	/**
	 * Rotation center offset parameter when club is on the right.
	 */
	public static final float RIGHT_ROTATION_OFFSET = 0.75f;

	private static final float DEFAULT_HEIGHT = 1.5f;
	private static final float DEFAULT_WIDTH = 0.5f;
	private static final float DEFAULT_ROTATION_X = 0.75f * DEFAULT_WIDTH;
	private static final float DEFAULT_ROTATION_Y = 1.5f;
	private final Vector2 mPosition;
	private final Vector2 mRotationCenter;
	private final Vector2 mSize;
	private float mAngle;
	private boolean mDirectionRight = true;
	private boolean mShowClub;
	private ClubType mClubType;

	/**
	 * Constructor.
	 */
	public ClubModel() {
		mPosition = new Vector2();
		mRotationCenter = new Vector2(DEFAULT_ROTATION_X, DEFAULT_ROTATION_Y);
		mSize = new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		mAngle = 0f;
		mClubType = ClubType.IRON;
	}

	/**
	 * Returns if club should be shown or not.
	 * 
	 * @return true if club visible, false otherwise
	 */
	public boolean isShowClub() {
		return mShowClub;
	}

	/**
	 * Sets if club should be shown or not.
	 * 
	 * @param showIndicator
	 *            true if club visible, false otherwise
	 */
	public void setShowClub(boolean showIndicator) {
		mShowClub = showIndicator;
	}

	/**
	 * Returns club rotation angle.
	 * 
	 * @return club rotation angle in degrees
	 */
	public float getAngle() {
		return mAngle;
	}

	/**
	 * Sets rotation angle.
	 * 
	 * @param angle
	 *            value of rotation angle to set
	 */
	public void setAngle(float angle) {
		mAngle = angle;
	}

	/**
	 * Returns x coordinate which defines x position of club.
	 * 
	 * @return x coordinate value
	 */
	public float getPositionX() {
		return mPosition.x;
	}

	/**
	 * Returns y coordinate which defines y position of club.
	 * 
	 * @return y coordinate value
	 */
	public float getPositionY() {
		return mPosition.y;
	}

	/**
	 * Sets club position coordinates.
	 * 
	 * @param x
	 *            value which will be set as x coordinate
	 * @param y
	 *            value which will be set as y coordinate
	 */
	public void setPosition(float x, float y) {
		mPosition.x = x;
		mPosition.y = y;
	}

	/**
	 * Returns x coordinate which defines x position of club rotation center.
	 * 
	 * @return x coordinate value
	 */
	public float getRotationCenterX() {
		return mRotationCenter.x;
	}

	/**
	 * Returns y coordinate which defines y position of club rotation center.
	 * 
	 * @return y coordinate value
	 */
	public float getRotationCenterY() {
		return mRotationCenter.y;
	}

	/**
	 * Sets x coordinate which defines x position of club rotation center.
	 * 
	 * @param rotationCenterX
	 *            rotation center x coordinate
	 */
	public void setRotationCenterX(float rotationCenterX) {
		mRotationCenter.x = rotationCenterX;
	}

	/**
	 * Sets y coordinate which defines y position of club rotation center.
	 * 
	 * @param rotationCenterY
	 *            rotation center y coordinate
	 */
	public void setRotationCenterY(float rotationCenterY) {
		mRotationCenter.y = rotationCenterY;
	}

	/**
	 * Returns club width.
	 * 
	 * @return club width
	 */
	public float getWidth() {
		return mSize.x;
	}

	/**
	 * Returns club height.
	 * 
	 * @return club height
	 */
	public float getHeight() {
		return mSize.y;
	}

	/**
	 * Sets club size.
	 * 
	 * @param width
	 *            club width
	 * @param height
	 *            club height
	 */
	public void setSize(float width, float height) {
		mSize.x = width;
		mSize.y = height;
	}

	/**
	 * Sets club direction.
	 * 
	 * @param isDirectionRight
	 *            true if club oriented right, false otherwise
	 */
	public void setDirectionRight(boolean isDirectionRight) {
		mDirectionRight = isDirectionRight;
	}

	/**
	 * Returns club orientation.
	 * 
	 * @return true if club oriented right, false otherwise
	 */
	public boolean getDirectionRight() {
		return mDirectionRight;
	}

	/**
	 * Returns club type.
	 * 
	 * @return the type of the club
	 */
	public ClubType getClubType() {
		return mClubType;
	}

	/**
	 * Sets club type.
	 * 
	 * @param pClubType
	 *            new club type
	 */
	public void setClubType(ClubType pClubType) {
		Gdx.app.log(TAG, "setClubType start, was: " + mClubType + " will be: " + pClubType);
		mClubType = pClubType;
	}
}
