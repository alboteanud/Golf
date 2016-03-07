
package com.lutu.golf.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

/**
 * Class which represents a map.
 */
public class GameMapModel {

	private final TiledMap mMap;

	private final String mBodiesDataFilePath;

	private final String mPackerDirPath;
	private final String mAccessoriesDirPath;
	private final String mSoundsDirPath;

	/**
	 * Constructs {@link GameMapModel} instance.
	 * 
	 * @param pathToTmxFile
	 *            path to tmx file
	 * @param packerDirPath
	 *            path to packer folder
	 * @param bodyDataFileName
	 *            path to file which contains information about tile body
	 * @param accessoriesDirPath
	 *            path to accessories folder
	 * @param soundsDirPath
	 *            path to folder with sounds
	 */
	public GameMapModel(String pathToTmxFile, String packerDirPath, String bodyDataFileName, String accessoriesDirPath,
			String soundsDirPath) {
		mMap = TiledLoader.createMap(Gdx.files.internal(pathToTmxFile));
		mBodiesDataFilePath = bodyDataFileName;
		mPackerDirPath = packerDirPath;
		mAccessoriesDirPath = accessoriesDirPath;
		mSoundsDirPath = soundsDirPath;
	}

	/**
	 * Returns map width {@link TiledMap#width}.
	 * 
	 * @return map width
	 */
	public int getMapWidth() {
		return mMap.width;
	}

	/**
	 * Returns map height {@link TiledMap#height}.
	 * 
	 * @return map height
	 */
	public int getMapHeight() {
		return mMap.height;
	}

	/**
	 * Returns map tiled layer at index {@link TiledMap#layers}.
	 * 
	 * @param index
	 *            index of layer which will be return
	 * @return tiled layer at index
	 */
	public TiledLayer getMapTiledLayerAt(int index) {
		final TiledLayer result = mMap.layers.get(index);
		if (result == null) {
			throw new IndexOutOfBoundsException();
		}
		return result;
	}

	/**
	 * Returns map tile height {@link TiledMap#tileHeight}.
	 * 
	 * @return map tile height
	 */
	public int getTileHeight() {
		return mMap.tileHeight;
	}

	/**
	 * Returns map tile width {@link TiledMap#tileWidth}.
	 * 
	 * @return map tile width
	 */
	public int getTileWidth() {
		return mMap.tileWidth;
	}

	/**
	 * Returns tile size - tile should has the same height and width.
	 * 
	 * @return tile size as float
	 */
	public float getTileSize() {
		return mMap.tileHeight;
	}

	/**
	 * Returns map object {@link TiledMap}.
	 * 
	 * @return map
	 */
	public TiledMap getMap() {
		return mMap;
	}

	/**
	 * Returns path to body data file.
	 * 
	 * @return path to body data file
	 */
	public String getBodiesDataFilePath() {
		return mBodiesDataFilePath;
	}

	/**
	 * Returns path to packer folder.
	 * 
	 * @return path to packer folder
	 */
	public String getPackerDirPath() {
		return mPackerDirPath;
	}

	/**
	 * Returns path to accessories folder.
	 * 
	 * @return path to accessories folder
	 */
	public String getAccessoriesDirPath() {
		return mAccessoriesDirPath;
	}

	/**
	 * Returns path to sounds folder.
	 * 
	 * @return path to accessories folder
	 */
	public String getSoundsDirPath() {
		return mSoundsDirPath;
	}
}
