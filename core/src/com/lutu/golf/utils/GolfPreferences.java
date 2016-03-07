
package com.lutu.golf.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * An enum singleton class for saving and loading data about levels.
 */
public enum GolfPreferences {
	/**
	 * The singleton instance of this enum.
	 */
	INSTANCE;

	private static final String PREF_STARS_COUNT = "pref_stars_count";
	private static final String PREF_BEST_SCORE = "pref_high_score";
	private static final String PREF_IS_LEVEL_UNBLOCKED = "pref_is_level_unblocked";

	private static final String AUDIO_PREF = "audio_pref";
	private static final String PREFS_IS_SOUND_ON = "is_sound_on";
	private static final String PREFS_IS_MUSIC_ON = "is_music_on";

	private Preferences getPrefs(int pChapterNumber, int pLevelNumber) {
		return Gdx.app.getPreferences("Golf_" + pChapterNumber + "_" + pLevelNumber);
	}

	/**
	 * Checks if the preferences contain information about best score for the given chapter and level.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * 
	 * @return true, if there is a information about best score, false otherwise
	 */
	public boolean containsBestScoreData(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).contains(PREF_BEST_SCORE);
	}

	/**
	 * Checks if the preferences contains information about stars count for the given chapter and level.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * 
	 * @return true, if there is information about stars count, false otherwise
	 */
	public boolean containsStarsData(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).contains(PREF_STARS_COUNT);
	}

	/**
	 * Checks if the preferences contain information about the 'unblocked' status for the given chapter and level.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * 
	 * @return true, if there is information about the 'unblocked' status, false otherwise
	 */
	public boolean containsUnblockedData(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).contains(PREF_IS_LEVEL_UNBLOCKED);
	}

	/**
	 * Writes the stars count to this level's preferences object.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @param pStarsCount
	 *            number of stars earned on the level
	 */
	public void setStarsCount(int pChapterNumber, int pLevelNumber, float pStarsCount) {
		final Preferences p = getPrefs(pChapterNumber, pLevelNumber);
		p.putFloat(PREF_STARS_COUNT, pStarsCount);
		p.flush();
	}

	/**
	 * Writes the new best score to this level's preferences object.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @param pBestScore
	 *            new hit best (lowest) score for the level
	 */
	public void setBestScore(int pChapterNumber, int pLevelNumber, int pBestScore) {
		final Preferences p = getPrefs(pChapterNumber, pLevelNumber);
		p.putInteger(PREF_BEST_SCORE, pBestScore);
		p.flush();
	}

	/**
	 * Writes the information about the level's 'unblocked' status to this level's preferences object.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @param pLevelUnblocked
	 *            should the level be unblocked
	 */
	public void setLevelUnblocked(int pChapterNumber, int pLevelNumber, boolean pLevelUnblocked) {
		final Preferences p = getPrefs(pChapterNumber, pLevelNumber);
		p.putBoolean(PREF_IS_LEVEL_UNBLOCKED, pLevelUnblocked);
		p.flush();
	}

	/**
	 * Returns the information about the level's 'unblocked' status, false by default.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @return level's 'unblocked' status, false by default
	 */
	public boolean isLevelUnblocked(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).getBoolean(PREF_IS_LEVEL_UNBLOCKED, false);
	}

	/**
	 * Returns the information about the number of stars for the level, 0.0f by default.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @return number of stars for the level, 0.0f by default
	 */
	public float getStarsCount(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).getFloat(PREF_STARS_COUNT, 0.0f);
	}

	/**
	 * Returns the information about the best score for the level, -1 by default.
	 * 
	 * @param pChapterNumber
	 *            chapter number
	 * @param pLevelNumber
	 *            level number within the chapter
	 * @return current best (lowest) score for the level, -1 by default
	 */
	public int getBestScore(int pChapterNumber, int pLevelNumber) {
		return getPrefs(pChapterNumber, pLevelNumber).getInteger(PREF_BEST_SCORE, -1);
	}

	/**
	 * Returns information that music is on or off.
	 * 
	 * @return true if music on otherwise false
	 */
	public boolean isMusicOn() {
		return Gdx.app.getPreferences(AUDIO_PREF).getBoolean(PREFS_IS_MUSIC_ON, true);
	}

	/**
	 * Returns information that sounds is on or off.
	 * 
	 * @return true if sounds on otherwise false
	 */
	public boolean isSoundOn() {
		return Gdx.app.getPreferences(AUDIO_PREF).getBoolean(PREFS_IS_SOUND_ON, true);
	}

	/**
	 * Sets music on or of.
	 * 
	 * @param isMusicOn
	 *            if true music is on otherwise music is off
	 */
	public void setMusicOn(boolean isMusicOn) {
		final Preferences p = Gdx.app.getPreferences(AUDIO_PREF);
		p.putBoolean(PREFS_IS_MUSIC_ON, isMusicOn);
		p.flush();
	}

	/**
	 * Sets sounds on or of.
	 * 
	 * @param isSoundOn
	 *            if true sounds is on otherwise music is off
	 */
	public void setSoundOn(boolean isSoundOn) {
		final Preferences p = Gdx.app.getPreferences(AUDIO_PREF);
		p.putBoolean(PREFS_IS_SOUND_ON, isSoundOn);
		p.flush();
	}

}