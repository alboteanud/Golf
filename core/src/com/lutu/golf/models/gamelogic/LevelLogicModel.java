
package com.lutu.golf.models.gamelogic;

import com.lutu.golf.ecrane.GameScreen;

/**
 * Class which contains information about level.
 */
public final class LevelLogicModel {
	private final ChapterLogicModel mChapterLogicModel;
	private final String mFileName;
	private final String mIconImgPath;
	private final int mLevelNumber;

	private float mStarsHighScore;
	private int mMinHits;
	private boolean mEnabled;

	/**
	 * Constructs {@link LevelLogicModel}.
	 */
	private LevelLogicModel(Builder builder) {
		mChapterLogicModel = builder.mChapterLogicModel;
		mIconImgPath = builder.mIconImgName;
		mFileName = builder.mFileName;
		mLevelNumber = builder.mLevelNumber;
	}

	/**
	 * Returns parent.
	 * 
	 * @return instance of {@link ChapterLogicModel}
	 */
	public ChapterLogicModel getChapterLogicModel() {
		return mChapterLogicModel;
	}

	/**
	 * Returns path to .tmx file. This file contains information about game map.
	 * 
	 * @return path to .tmx file
	 */
	public String getPathToTmxFile() {
		return mChapterLogicModel.getDirPath() + mChapterLogicModel.getLevelsDirName() + mFileName;
	}

	/**
	 * Returns path to packer folder. This folder contains TileSet files.
	 * 
	 * @return path to packer folder
	 */
	public String getPackerDirPath() {
		return mChapterLogicModel.getDirPath() + mChapterLogicModel.getPackerDirName();
	}

	/**
	 * Returns path to file which contains information about tiles bodies.
	 * 
	 * @return path to file with tiles bodies definitions
	 */
	public String getBodiesDataFilePath() {
		return mChapterLogicModel.getDirPath() + mChapterLogicModel.getBodiesDataFileName();
	}

	/**
	 * Returns path to file which contains music which will be played on {@link GameScreen}.
	 * 
	 * @return path to game music file
	 */
	public String getGameMusicPath() {
		return mChapterLogicModel.getAudioDirPath() + mChapterLogicModel.getGameMusic();
	}

	/**
	 * Returns path to icon background image.
	 * 
	 * @return path to icon background image
	 */
	public String getPathToIconImg() {
		return mChapterLogicModel.getAccessoriesDirPath() + mIconImgPath;
	}

	/**
	 * Returns level index.
	 * 
	 * @return level index
	 */
	public int getLevelNumber() {
		return mLevelNumber;
	}

	/**
	 * Returns stars high score.
	 * 
	 * @return stars high score
	 */
	public float getStarsHighScore() {
		return mStarsHighScore;
	}

	/**
	 * Sets stars high score.
	 * 
	 * @param starsHighScore
	 *            stars high score
	 */
	public void setStarsHighScore(float starsHighScore) {
		mStarsHighScore = starsHighScore;
	}

	/**
	 * Returns value of minimum hits.
	 * 
	 * @return value of minimum hits
	 */
	public int getMinHits() {
		return mMinHits;
	}

	/**
	 * Sets value of minimum hits.
	 * 
	 * @param minHits
	 *            value of minimum hits
	 */
	public void setMinHits(int minHits) {
		mMinHits = minHits;
	}

	/**
	 * Returns information that level is enabled.
	 * 
	 * @return true if level is enabled otherwise false
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	/**
	 * Sets level as enable or disable.
	 * 
	 * @param enabled
	 *            if true level will be enabled otherwise level will be disabled
	 */
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	/**
	 * Builder which constructs {@link LevelLogicModel} object.
	 * 
	 */
	public static class Builder {
		private ChapterLogicModel mChapterLogicModel;
		private String mFileName;
		private String mIconImgName;
		private int mLevelNumber;

		/**
		 * Sets parent.
		 * 
		 * @param chapterLogicModel
		 *            instance of {@link ChapterLogicModel}
		 * @return instance of {@link Builder}
		 */
		public Builder setChapterLogicModel(ChapterLogicModel chapterLogicModel) {
			mChapterLogicModel = chapterLogicModel;
			return this;
		}

		/**
		 * Sets *tmx file name.
		 * 
		 * @param tmxFileName
		 *            *tmx file name
		 * @return instance of {@link Builder}
		 */
		public Builder setFileName(String tmxFileName) {
			mFileName = tmxFileName;
			return this;
		}

		/**
		 * Sets level index.
		 * 
		 * @param levelNumber
		 *            level index
		 * @return instance of {@link Builder}
		 */
		public Builder setLevelNumber(int levelNumber) {
			mLevelNumber = levelNumber;
			return this;
		}

		/**
		 * Sets icon background image path.
		 * 
		 * @param iconImgName
		 *            icon background image path
		 * @return instance of {@link Builder}
		 */
		public Builder setIconImgName(String iconImgName) {
			mIconImgName = iconImgName;
			return this;
		}

		/**
		 * Builds {@link LevelLogicModel} object.
		 * 
		 * @return new instance of {@link LevelLogicModel} object
		 */
		public LevelLogicModel build() {
			return new LevelLogicModel(this);
		}
	}

}
