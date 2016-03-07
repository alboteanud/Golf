
package com.lutu.golf.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Singleton wrapper for the {@link AssetManager} class.
 */
public final class Assets extends AssetManager {

	private static Assets sInstance;

	private Assets() {

	}

	/**
	 * {@link AssetManager} singleton access method.
	 * 
	 * @return {@link AssetManager} singleton instance
	 */
	public static Assets get() {
		if (sInstance == null) {
			sInstance = new Assets();
			Texture.setAssetManager(sInstance);
		}
		return sInstance;
	}

	/**
	 * Disposes the {@link AssetManager} instance.
	 */
	public static void destroy() {
		if (sInstance != null) {
			sInstance.dispose();
			sInstance = null;
		}
	}

	/**
	 * Creates {@link TextureRegionDrawable} from the specified path.
	 * 
	 * @param path
	 *            path to the resource
	 * @return new {@link TextureRegionDrawable} holding a texture created from path
	 */
	public TextureRegionDrawable getTextureRegionDrawable(String path) {
		final Texture texture = get(path, Texture.class);
		return new TextureRegionDrawable(new TextureRegion(texture));
	}

	/**
	 * Returns {@link Music} from the specified path.
	 * 
	 * @param path
	 *            path to the resource
	 * @return instance of {@link Music}
	 */
	Music getMusic(String path) {
		return get(path);
	}

	/**
	 * Returns {@link Sound} from the specified path.
	 * 
	 * @param path
	 *            path to the resource
	 * @return instance of {@link Sound}
	 */
	Sound getSound(String path) {
		return get(path);
	}
}
