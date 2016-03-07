
package com.lutu.golf.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.lutu.golf.models.GameMapModel;

/**
 * Class which is used to draw a game map.
 */
public class GameMapRenderer {

	private static final int[] LAYER_LIST = { 0 };

	// private static final String PACKER_DIR = "data/packer";

	private static final int TILE_SIZE = 32;

	private TileAtlas mTileAtlas;
	private TileMapRenderer mTileMapRenderer;
	private OrthographicCamera mCamera;

	/**
	 * Constructor.
	 * 
	 * @param gameMapModel
	 *            object which represents map {@link GameMapRenderer}
	 * 
	 * @param camera
	 *            instance of {@link OrthographicCamera}
	 */
	public GameMapRenderer(GameMapModel gameMapModel, OrthographicCamera camera) {
		final FileHandle packFileDirectory = Gdx.files.internal(gameMapModel.getPackerDirPath());
		prepareTileds(packFileDirectory, gameMapModel.getMap());
		mCamera = camera;

	}

	private void prepareTileds(FileHandle packFileDirectory, TiledMap map) {
		mTileAtlas = new TileAtlas(map, packFileDirectory);
		mTileMapRenderer = new TileMapRenderer(map, mTileAtlas, TILE_SIZE, TILE_SIZE, 1, 1);
	}

	/**
	 * Renders the {@link TiledMap}.
	 */
	public void render() {
		mTileMapRenderer.render(mCamera, LAYER_LIST);
	}
}
