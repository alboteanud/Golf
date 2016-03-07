
package com.lutu.golf.models.obstacle;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class which contains common information for all obstacles.
 */
public abstract class AbstractObstacleModel {

	static final int TILE_SIZE = 32;

	private static final String EMPTY_FILE_VALUE = "None";

	private static final String BALL_DESTROYING_INDEXES = "ball_destroying_indexes";
	private static final String FORCE_APPLYING_INDEXES = "force_applying_indexes";
	private static final String SLOW_DOWN_INDEXES = "slow_down_indexes";
	private static final String SPLIT_MARK = ",";
	private static final String OBSTACLE_FILE = "obstacle_file";
	private static final String COLISION_SOUND_FILE = "colision_sound";

	/**
	 * Enum which defines obstacle type.
	 */
	public enum ObstacleType {
		/** Obstacle which applies force the ball. */
		FORCE_APPLYING,
		/** Obstacle which slows down the ball. */
		SLOW_DOWN,
		/** Obstacle which destroys the ball. */
		BALL_DESTROYING
	}

	private static final float OBSTACLE_SIZE = 1f;
	private final Rectangle mBounds;

	private String mObstacleFilePath;

	private String mCollisionSoundPath;

	/**
	 * Constructor is visible only in "obstacle" package - this class should be used as base class for "obstacle"
	 * classes. This constructor prepares basic informations which are common for all obstacles.
	 */
	AbstractObstacleModel() {
		mBounds = new Rectangle();
		mBounds.setHeight(OBSTACLE_SIZE);
		mBounds.setWidth(OBSTACLE_SIZE);
	}

	/**
	 * Sets obstacle position in world coordinates system.
	 * 
	 * @param x
	 *            x coordinate value
	 * @param y
	 *            y coordinate value
	 */
	public void setPosition(float x, float y) {
		mBounds.x = x;
		mBounds.y = y;
	}

	/**
	 * Returns x coordinate which defines x position of obstacle.
	 * 
	 * @return x coordinate value
	 */
	public float getPositionX() {
		return mBounds.x;
	}

	/**
	 * Returns y coordinate which defines y position of obstacle.
	 * 
	 * @return y coordinate value
	 */
	public float getPositionY() {
		return mBounds.y;
	}

	/**
	 * Sets obstacle size.
	 * 
	 * @param width
	 *            obstacle width {@link Rectangle#width}
	 * @param height
	 *            obstacle height {@link Rectangle#height}
	 */
	public void setSize(float width, float height) {
		mBounds.width = width;
		mBounds.height = height;
	}

	/**
	 * Returns obstacle width.
	 * 
	 * @return {@link Rectangle#width}
	 */
	public float getWidth() {
		return mBounds.width;
	}

	/**
	 * Returns obstacle height.
	 * 
	 * @return {@link Rectangle#height}
	 */
	public float getHeight() {
		return mBounds.height;
	}

	/**
	 * Returns object which defines obstacle type.
	 * 
	 * @return obstacle type {@link ObstacleType}
	 */
	public abstract ObstacleType getTypeObstacle();

	/**
	 * Sets obstacle data form tiled map.
	 * 
	 * @param map
	 *            instance of {@link TiledMap}
	 * @param obstacleIndex
	 *            index of obstacle
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setObstacleDataFromTiledMap(TiledMap map, int obstacleIndex, int x, int y) {
		setPosition(x, y);
	}

	/**
	 * Returns indexes of tiles which represent obstacles.
	 * 
	 * @param obstacleType
	 *            instance of {@link ObstacleType}
	 * @param map
	 *            instance of {@link TiledMap}
	 * @return obstacles indexes array
	 */
	public static int[] getObstacleTilesIndexes(ObstacleType obstacleType, TiledMap map) {
		final String value;
		switch (obstacleType) {
		case BALL_DESTROYING:
			value = map.properties.get(BALL_DESTROYING_INDEXES);
			break;
		case FORCE_APPLYING:
			value = map.properties.get(FORCE_APPLYING_INDEXES);
			break;
		case SLOW_DOWN:
			value = map.properties.get(SLOW_DOWN_INDEXES);
			break;
		default:
			throw new IllegalArgumentException();
		}
		if (value != null) {
			final String[] array = value.split(SPLIT_MARK);
			final int size = array.length;
			final int[] obstacleIndexes = new int[size];
			for (int index = 0; index < size; index++) {
				obstacleIndexes[index] = Integer.valueOf(array[index]);
			}
			return obstacleIndexes;
		} else {
			return new int[0];
		}
	}

	/**
	 * Returns path to file which contains image which covers obstacle.
	 * 
	 * @return path to file
	 */
	public String getObstacleFilePath() {
		return mObstacleFilePath;
	}

	/**
	 * Sets path to file which contains image which covers obstacle. If it does not exist, this method returns null.
	 * 
	 * @param pathToDir
	 *            path to folder
	 * @param map
	 *            instance of {@link TiledMap}
	 * @param index
	 *            index of tile
	 */
	public void setObstacleFilePath(String pathToDir, TiledMap map, int index) {
		final String obstacleFileName = map.getTileProperty(index, OBSTACLE_FILE);
		if (EMPTY_FILE_VALUE.equals(obstacleFileName) || obstacleFileName == null) {
			mObstacleFilePath = null;
		} else {
			mObstacleFilePath = pathToDir + obstacleFileName;
		}

	}

	/**
	 * Sets path to sound of collision.
	 * 
	 * @param pathToDir
	 *            path to folder
	 * @param map
	 *            instance of {@link TiledMap}
	 * @param index
	 *            index of tile
	 */
	public void setCollisionSoundPath(String pathToDir, TiledMap map, int index) {
		final String obstacleFileName = map.getTileProperty(index, COLISION_SOUND_FILE);
		if (EMPTY_FILE_VALUE.equals(obstacleFileName) || obstacleFileName == null) {
			mCollisionSoundPath = null;
		} else {
			mCollisionSoundPath = pathToDir + obstacleFileName;
		}

	}

	public String getCollisionSoundPath() {
		return mCollisionSoundPath;
	}
}
