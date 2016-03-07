
package com.lutu.golf.models;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lutu.golf.models.HoleModel.HoleElement.HoleElementOrientation;

/**
 * Class which contains information about hole model. This class is used to communication controller with renderer.
 */
public class HoleModel {

	/**
	 * Enum which defines type of hole elements.
	 */
	public enum HoleElementType {
		/** Left wall. */
		LEFT_WALL,
		/** Right wall. */
		RIGHT_WALL,
		/** Bottom. */
		BOTTOM,
	}

	private static final float HOLE_SIZE = 1.0f;
	private static final String HOLE_FILE = "hole_file";
	private static final String HOLE_INDEX = "hole_index";

	private final EnumMap<HoleElementType, HoleElement> mHoleElements;

	private Rectangle mBound;

	private String mHoleFilePath;

	/**
	 * Constructor.
	 */
	public HoleModel() {
		mHoleElements = new EnumMap<HoleElementType, HoleElement>(HoleElementType.class);
		mHoleElements.put(HoleElementType.LEFT_WALL, new HoleElement(HoleElement.HoleElementOrientation.VERTICAL));
		mHoleElements.put(HoleElementType.RIGHT_WALL, new HoleElement(HoleElement.HoleElementOrientation.VERTICAL));
		mHoleElements.put(HoleElementType.BOTTOM, new HoleElement(HoleElement.HoleElementOrientation.HORIZONTAL));
		mBound = new Rectangle();
		mBound.setWidth(HOLE_SIZE);
		mBound.setHeight(HOLE_SIZE);
	}

	/**
	 * Set hole position.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setPosition(float x, float y) {
		mBound.setX(x);
		mBound.setY(y);

		final HoleElement leftWallElement = mHoleElements.get(HoleElementType.LEFT_WALL);
		if (leftWallElement == null) {
			throw new IllegalArgumentException("Left wall element not found!");
		}
		leftWallElement.setPosition(x, y);

		final HoleElement bottomElement = mHoleElements.get(HoleElementType.BOTTOM);
		if (bottomElement == null) {
			throw new IllegalArgumentException("Bottom element not found!");
		}
		bottomElement.setPosition(x, y);

		final HoleElement rightWallElement = mHoleElements.get(HoleElementType.RIGHT_WALL);
		if (rightWallElement == null) {
			throw new IllegalArgumentException("Left wall element not found!");
		}
		rightWallElement.setPosition(x + HOLE_SIZE - mHoleElements.get(HoleElementType.RIGHT_WALL).getWidth(), y);
	}

	/**
	 * Returns hole element x position.
	 * <p>
	 * This method should be used to create hole body.
	 * <p>
	 * 
	 * @param key
	 *            key which defines hole element
	 * @return hole position
	 */
	public float getHoleElementPositionX(HoleElementType key) {
		final HoleElement holeElement = mHoleElements.get(key);
		if (holeElement == null) {
			throw new IllegalArgumentException("Element not found!");
		}
		return holeElement.getPositionX();
	}

	/**
	 * Returns hole element x position.
	 * <p>
	 * This method should be used to create hole body.
	 * <p>
	 * 
	 * @param key
	 *            key which defines hole element
	 * @return hole position
	 */
	public float getHoleElementPositionY(HoleElementType key) {
		final HoleElement holeElement = mHoleElements.get(key);
		if (holeElement == null) {
			throw new IllegalArgumentException("Element not found!");
		}
		return holeElement.getPositionY();
	}

	/**
	 * Returns hole element width.
	 * <p>
	 * This method should be used to create hole body.
	 * <p>
	 * 
	 * @param key
	 *            key which defines hole element
	 * @return hole element width
	 */
	public float getElementWidth(HoleElementType key) {
		final HoleElement holeElement = mHoleElements.get(key);
		if (holeElement == null) {
			throw new IllegalArgumentException("Element not found!");
		}
		return holeElement.getWidth();
	}

	/**
	 * Returns hole element height.
	 * <p>
	 * This method should be used to create hole body.
	 * <p>
	 * 
	 * @param key
	 *            key which defines hole element
	 * @return hole element height
	 */
	public float getHoleElementHeight(HoleElementType key) {
		final HoleElement holeElement = mHoleElements.get(key);
		if (holeElement == null) {
			throw new IllegalArgumentException("Element not found!");
		}
		return holeElement.getHeight();
	}

	/**
	 * Returns hole x position.
	 * 
	 * @return hole x coordinate
	 */
	public float getPositionX() {
		return mBound.x;
	}

	/**
	 * Returns hole y position.
	 * 
	 * @return hole y coordinate
	 */
	public float getPositionY() {
		return mBound.y;
	}

	/**
	 * Returns hole width.
	 * 
	 * @return hole width
	 */
	public float getWidth() {
		return mBound.width;
	}

	/**
	 * Returns hole height.
	 * 
	 * @return hole height
	 */
	public float getHeight() {
		return mBound.height;
	}

	/**
	 * Returns hole index of tile.
	 * 
	 * @param map
	 *            tiles map
	 * 
	 * @return value of hole index
	 */
	public int getHoleIndex(TiledMap map) {
		final String holeIndex = map.properties.get(HOLE_INDEX);
		return Integer.valueOf(holeIndex);
	}

	/**
	 * Sets hole file path.
	 * 
	 * @param pathToDir
	 *            path to hole file
	 * @param map
	 *            tiles map
	 * @param index
	 *            hole tile index
	 */
	public void setHoleFilePathFromMap(String pathToDir, TiledMap map, int index) {
		mHoleFilePath = pathToDir + map.getTileProperty(index, HOLE_FILE);
	}

	/**
	 * Returns hole file path.
	 * 
	 * @return hole file path
	 */
	public String getHoleFilePath() {
		return mHoleFilePath;
	}

	/**
	 * Inner class which represents one hole elements.
	 */
	static class HoleElement {

		private static final float SHORTER_SIZE = 0.125f;
		private static final float MORE_SIZE = HOLE_SIZE;

		private final Vector2 mPosition;
		private final Vector2 mSize;

		/**
		 * Enum which defines hole element orientation.
		 */
		enum HoleElementOrientation {
			/** Should be used for walls. */
			HORIZONTAL,
			/** Should be used for bottom. */
			VERTICAL;
		}

		/**
		 * Constructor.
		 * 
		 * @param holeElementOrientation
		 *            element orientation - vertical or horizontal
		 */
		HoleElement(HoleElementOrientation holeElementOrientation) {
			mPosition = new Vector2();
			if (HoleElementOrientation.VERTICAL.compareTo(holeElementOrientation) == 0) {
				mSize = new Vector2(SHORTER_SIZE, MORE_SIZE);
			} else {
				mSize = new Vector2(MORE_SIZE, SHORTER_SIZE);
			}
		}

		/**
		 * Returns x position.
		 * 
		 * @return position of element
		 */
		float getPositionX() {
			return mPosition.x;
		}

		/**
		 * Returns y position.
		 * 
		 * @return position of element
		 */
		float getPositionY() {
			return mPosition.y;
		}

		/**
		 * Set element position.
		 * 
		 * @param x
		 *            x coordinates
		 * @param y
		 *            y coordinates
		 */
		void setPosition(float x, float y) {
			mPosition.x = x;
			mPosition.y = y;
		}

		/**
		 * Returns width.
		 * 
		 * @return width of element
		 */
		float getWidth() {
			return mSize.x;
		}

		/**
		 * Returns height.
		 * 
		 * @return width of element
		 */
		float getHeight() {
			return mSize.y;
		}
	}

}
