
package com.lutu.golf.models;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Model which represents golf ball.
 * <p>
 * This model contains information about ball position, radius and type.
 * </p>
 */
public class BallModel {

	private static final String BALL_START_POSITION_X = "ball_start_position_x";
	private static final String BALL_START_POSITION_Y = "ball_start_position_y";

	private static final float DEFAULT_RADIUS = 0.25f;

	private float mRadius;

	private final Vector2 mPosition;

	private final Vector2 mLastStaticPosition;

	private boolean mBallMoving;

	private boolean mIsOutsideDestroyingObstacle = true;
	private boolean mIsOnSlowdownObstacle = false;

	/**
	 * Constructor.
	 */
	public BallModel() {
		mPosition = new Vector2();
		mLastStaticPosition = new Vector2();
		mRadius = DEFAULT_RADIUS;
	}

	/**
	 * Returns ball radius.
	 * 
	 * @return ball radius
	 */
	public float getRadius() {
		return mRadius;
	}

	/**
	 * Sets ball radius.
	 * 
	 * @param radius
	 *            new ball radius
	 */
	public void setRadius(float radius) {
		mRadius = radius;
	}

	/**
	 * Returns x coordinate which defines x position.
	 * 
	 * @return x coordinate value.
	 */
	public float getPositionX() {
		return mPosition.x;
	}

	/**
	 * Returns y coordinate which defines y position.
	 * 
	 * @return y coordinate value.
	 */
	public float getPositionY() {
		return mPosition.y;
	}

	/**
	 * Sets ball position coordinates.
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
	 * Returns information if the ball is moving. See method {@link com.badlogic.gdx.physics.box2d.Body#isAwake()}.
	 * 
	 * @return true if ball is moving otherwise false
	 */
	public boolean isBallMoving() {
		return mBallMoving;
	}

	/**
	 * Sets information if the ball is moving.
	 * 
	 * @param ballMoving
	 *            true if ball is moving otherwise false
	 */
	public void setBallMoving(boolean ballMoving) {
		mBallMoving = ballMoving;
	}

	/**
	 * Returns x coordinate of last static position.
	 * 
	 * @return x coordinate
	 */
	public float getLastStaticPostionX() {
		return mLastStaticPosition.x;
	}

	/**
	 * Returns y coordinate of last static position.
	 * 
	 * @return y coordinate
	 */
	public float getLastStaticPostionY() {
		return mLastStaticPosition.y;
	}

	/**
	 * Sets information that ball is outside destroying obstacle.
	 * 
	 * @param isOutsideDestroyingObstacle
	 *            true if ball is outside destroying obstacle,false otherwise
	 */
	public void setIsOutsideDestroyingObstacle(boolean isOutsideDestroyingObstacle) {
		mIsOutsideDestroyingObstacle = isOutsideDestroyingObstacle;
	}

	/**
	 * Returns information that ball is outside destroying obstacle.
	 * 
	 * @return true if ball is outside destroying obstacle,false otherwise
	 */
	public boolean isOutsideDestroyingObstacle() {
		return mIsOutsideDestroyingObstacle;
	}

	/**
	 * Sets information that ball is on slow down obstacle.
	 * 
	 * @param isOnSlowdownObstacle
	 *            true if ball is on slow down obstacle,false otherwise
	 */
	public void setIsOnSlowdownObstacle(boolean isOnSlowdownObstacle) {
		mIsOnSlowdownObstacle = isOnSlowdownObstacle;
	}

	/**
	 * Returns information that ball is on slow down obstacle.
	 * 
	 * @return true if ball is on slow down obstacle otherwise false
	 */
	public boolean isOnSlowdownObstacle() {
		return mIsOnSlowdownObstacle;
	}

	/**
	 * Sets current position as last static position. The last static position will be used to recover position.
	 */
	public void setCurrentPostionAsLastStaticPosition() {
		mLastStaticPosition.x = mPosition.x;
		mLastStaticPosition.y = mPosition.y;
	}

	/**
	 * Loads and sets ball position from map.
	 * 
	 * @param map
	 *            instance of {@link TiledMap}
	 */
	public void loadPositionFromMap(TiledMap map) {
		final String x = map.properties.get(BALL_START_POSITION_X);
		final String y = map.properties.get(BALL_START_POSITION_Y);
		if (x == null || y == null) {
			throw new UnsupportedOperationException("Ball position have to be set.");
		}
		setPosition(Float.valueOf(x), Float.valueOf(y));
	}

}
