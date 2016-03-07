
package com.lutu.golf.utils;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Class to manage audio.
 */
public final class GolfAudioManager {

	private static final float SOUND_VOLUME = 0.5f;
	private static final float MUSIC_VOLUME = 0.4f;
	private static boolean sIsMusicsOn;
	private static boolean sIsSoundsOn;

	private static final Set<String> SOUNDS_PATHS = new HashSet<String>();
	private static final Set<String> MUSICS_PATHS = new HashSet<String>();

	/**
	 * Hide constructor.
	 */
	private GolfAudioManager() {

	}

	/**
	 * Loads and adds sounds to collection. See
	 * {@link com.badlogic.gdx.Audio#newSound(com.badlogic.gdx.files.FileHandle)}
	 * 
	 * @param path
	 *            path to file
	 */
	public static void addSound(String path) {
		SOUNDS_PATHS.add(path);
		Assets.get().load(path, Sound.class);
	}

	/**
	 * Loads and adds musics to collection. See
	 * {@link com.badlogic.gdx.Audio#newMusic(com.badlogic.gdx.files.FileHandle)}
	 * 
	 * @param path
	 *            path to file
	 */
	public static void addMusic(String path) {
		MUSICS_PATHS.add(path);
		Assets.get().load(path, Music.class);
	}

	/**
	 * Plays sound. See {@link Sound#play()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void playSound(String path) {
		if (sIsSoundsOn) {
			final Sound s = Assets.get().getSound(path);
			if (s != null) {
				s.play(SOUND_VOLUME);
			}
		}
	}

	/**
	 * Plays music. See {@link Music#play()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void playMusic(String path) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			if (!m.isPlaying()) {
				final float volume = sIsMusicsOn ? MUSIC_VOLUME : 0.0f;
				m.setVolume(volume);
				m.play();
			}
		}
	}

	/**
	 * Pauses music. See {@link Music#pause()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void pauseMusic(String path) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			m.pause();
		}
	}

	/**
	 * Stops sound. See {@link Sound#stop()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void stopSound(String path) {
		if (sIsSoundsOn) {
			final Sound s = Assets.get().getSound(path);
			if (s != null) {
				s.stop();
			}
		}
	}

	/**
	 * Stops music. See {@link Music#stop()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void stopMusic(String path) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			m.stop();
		}
	}

	/**
	 * Sets looping music. See {@link Music#setLooping(boolean)}.
	 * 
	 * @param path
	 *            path to file
	 * @param looping
	 *            if true music will be played in looping otherwise music will be played once
	 */
	public static void setLoopingMusic(String path, boolean looping) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			m.setLooping(looping);
		}
	}

	/**
	 * Returns information that music is looping.. See {@link Music#isLooping()}.
	 * 
	 * @param path
	 *            path to file
	 * @return true if music is looping otherwise false
	 */
	public static boolean isLoopingMusic(String path) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			return m.isLooping();
		}
		return false;
	}

	/**
	 * Sets music volume. See {@link Music#setVolume(float)}.
	 * 
	 * @param path
	 *            path to file
	 * @param volume
	 *            value of volume - should be between 0 and 1
	 */
	public void setVolumeMusic(String path, float volume) {
		final Music m = Assets.get().getMusic(path);
		if (m != null) {
			m.setVolume(volume);
		}
	}

	/**
	 * Disposes and removes music resource. See {@link Music#dispose()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void disposeMusic(String path) {
		MUSICS_PATHS.remove(path);
		Assets.get().unload(path);
	}

	/**
	 * Disposes and removes sound resource. See {@link Sound#dispose()}.
	 * 
	 * @param path
	 *            path to file
	 */
	public static void disposeSound(String path) {
		SOUNDS_PATHS.remove(path);
		Assets.get().unload(path);
	}

	/**
	 * Removes all musics resource. See {@link Set#clear()}.
	 */
	public static void clearMusicsRes() {
		MUSICS_PATHS.clear();
	}

	/**
	 * Removes all sounds resource paths. See {@link Set#clear()}.
	 */
	public static void clearSoundsRes() {
		SOUNDS_PATHS.clear();
	}

	/**
	 * Disposes and removes all sounds and musics resource. See {@link GolfAudioManager#clearMusicsRes()} and
	 * {@link GolfAudioManager#clearSoundsRes()}.
	 */
	public static void clearAudioRes() {
		clearMusicsRes();
		clearSoundsRes();
	}

	/**
	 * Sets and switches on/of musics.
	 * 
	 * @param isMusicsOn
	 *            if true music will be played, otherwise music will not be played
	 */
	public static void setAndSwitchMusicsOn(boolean isMusicsOn) {
		sIsMusicsOn = isMusicsOn;
		final float volume = isMusicsOn ? MUSIC_VOLUME : 0.0f;
		for (String path : MUSICS_PATHS) {
			final Music m = Assets.get().getMusic(path);
			m.setVolume(volume);
		}
	}

	/**
	 * Switches on/of sounds.
	 * 
	 * @param isSoundsOn
	 *            if true sounds will be played, otherwise sounds will not be played
	 */
	public static void setSoundsOn(boolean isSoundsOn) {
		sIsSoundsOn = isSoundsOn;
	}

	/**
	 * Returns information than music will be played.
	 * 
	 * @return true if music will be played, otherwise false
	 */
	public static boolean isMusicsOn() {
		return sIsMusicsOn;
	}

	/**
	 * Returns information than sounds will be played.
	 * 
	 * @return true if music will be played, otherwise false
	 */
	public static boolean isSoundsOn() {
		return sIsSoundsOn;
	}

}
