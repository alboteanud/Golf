
package com.lutu.golf.models.gamelogic;

import com.lutu.golf.ecrane.LevelSelectionScreen;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains information about chapter.
 */
public final class ChapterLogicModel {

	private List<LevelLogicModel> mLevelLogicModels;

	private final GameLogicModel mGameLogicModel;
	private final String mBodiesDataFileName;
	private final String mLevelsDirName;
	private final String mPackerDirName;
	private final String mAccessoriesDirName;
	private final String mAudioDir;
	private final String mGameMusic;
	private final String mLevelSelectionMusic;
	private final String mDirPath;
	private final String mTitleDrawablePath;
	private final String mBackgroundDrawablePath;
	private final int mChapterNumber;
	private String mLockLevelIconImgName;

	private ChapterLogicModel(Builder builder) {
		mLevelLogicModels = new LinkedList<LevelLogicModel>();
		mGameLogicModel = builder.mGameLogicModel;
		mBodiesDataFileName = builder.mBodiesDataFileName;
		mLevelsDirName = builder.mLevelsDirPath;
		mPackerDirName = builder.mPackerDirName;
		mAccessoriesDirName = builder.mAccesoriesDirName;
		mAudioDir = builder.mAudioDir;
		mGameMusic = builder.mGameMusic;
		mLevelSelectionMusic = builder.mLevelSelectionMusic;
		mDirPath = builder.mDirPath;
		mTitleDrawablePath = builder.mTitleDrawablePath;
		mBackgroundDrawablePath = builder.mBackgroundDrawablePath;
		mChapterNumber = builder.mChapterNumber;
		mLockLevelIconImgName = builder.mLockLevelIconImgName;
	}

	/**
	 * Returns {@link LevelLogicModel} object at index. Throws IndexOutOfBoundsException exception if index >=
	 * {@link ChapterLogicModel#getLevelCount() }. See {@link List#get(int)}.
	 * 
	 * 
	 * @param index
	 *            index of chapter which will be returned
	 * @return instance of {@link LevelLogicModel}
	 */
	public LevelLogicModel getLevelAt(int index) {
		return mLevelLogicModels.get(index);
	}

	/**
	 * Adds {@link LevelLogicModel} object.
	 * 
	 * @param levelLogicModel
	 *            instance of {@link LevelLogicModel}
	 */
	public void addLevel(LevelLogicModel levelLogicModel) {
		mLevelLogicModels.add(levelLogicModel);
	}

	/**
	 * Returns number of levels. {@link List#size()}.
	 * 
	 * @return number of chapter
	 */
	public int getLevelCount() {
		return mLevelLogicModels.size();
	}

	/**
	 * Returns parent.
	 * 
	 * @return instance of {@link GameLogicModel}
	 */
	public GameLogicModel getGameLogicModel() {
		return mGameLogicModel;
	}

	/**
	 * Returns file name which contains information about tiles bodies.
	 * 
	 * @return file name
	 */
	public String getBodiesDataFileName() {
		return mBodiesDataFileName;
	}

	/**
	 * Returns path to levels.
	 * 
	 * @return path to levels
	 */
	public String getLevelsDirName() {
		return mLevelsDirName;
	}

	/**
	 * Returns path to packer folder.
	 * 
	 * @return path to packer folder
	 */
	public String getPackerDirName() {
		return mPackerDirName;
	}

	/**
	 * Returns path to main chapter folder.
	 * 
	 * @return path to main chapter folder
	 */
	public String getDirPath() {
		return mDirPath;
	}

	/**
	 * Returns chapter title image path.
	 * 
	 * @return chapter title image path
	 */
	public String getTitleDrawablePath() {
		return getAccessoriesDirPath() + mTitleDrawablePath;
	}

	/**
	 * Returns chapter background image path.
	 * 
	 * @return chapter background image path
	 */
	public String getBackgroundDrawablePath() {
		return getAccessoriesDirPath() + mBackgroundDrawablePath;
	}

	/**
	 * Returns accessories folder path.
	 * 
	 * @return accessories folder path
	 */
	public String getAccessoriesDirPath() {
		return getDirPath() + mAccessoriesDirName;
	}

	/**
	 * Returns audio folder path.
	 * 
	 * @return audio folder path
	 */
	public String getAudioDirPath() {
		return getDirPath() + mAudioDir;
	}

	/**
	 * Returns path to file which contains music which will be played on
	 * {@link LevelSelectionScreen}.
	 * 
	 * @return path to level selection music file
	 */
	public String getLvlSelectionMusicPath() {
		return getAudioDirPath() + mLevelSelectionMusic;
	}

	/**
	 * Returns name of file which contains level selection music file.
	 * 
	 * @return path to level selection music file
	 */
	public String getGameMusic() {
		return mGameMusic;
	}

	/**
	 * Returns chapter number.
	 * 
	 * @return chapter number
	 */
	public int getChapterNumber() {
		return mChapterNumber;
	}

	/**
	 * Returns path to file which contains lock level icon image.
	 * 
	 * @return path to file which contains lock level icon image
	 */
	public String getLockLevelIconImgPath() {
		return getAccessoriesDirPath() + mLockLevelIconImgName;
	}

	/**
	 * Builder which constructs {@link ChapterLogicModel} object.
	 * 
	 */
	public static class Builder {

		private String mLevelsDirPath;
		private GameLogicModel mGameLogicModel;
		private String mBodiesDataFileName;
		private String mPackerDirName;
		private String mAccesoriesDirName;
		private String mDirPath;
		private String mTitleDrawablePath;
		private String mBackgroundDrawablePath;
		private int mChapterNumber;
		private String mAudioDir;
		private String mGameMusic;
		private String mLevelSelectionMusic;
		private String mLockLevelIconImgName;

		/**
		 * Sets parent.
		 * 
		 * @param gameLogicModel
		 *            parent
		 * @return instance of {@link Builder}
		 */
		public Builder setGameLogicModel(GameLogicModel gameLogicModel) {
			mGameLogicModel = gameLogicModel;
			return this;
		}

		/**
		 * Sets file name which contains information about tiles bodies.
		 * 
		 * @param bodiesDataFileName
		 *            name of file which contains information about tiles bodies
		 * @return instance of {@link Builder}
		 */
		public Builder setBodiesDataFileName(String bodiesDataFileName) {
			mBodiesDataFileName = bodiesDataFileName;
			return this;
		}

		/**
		 * Sets path to levels folder.
		 * 
		 * @param levelsDirPath
		 *            path to levels folder
		 * @return instance of {@link Builder}
		 */
		public Builder setLevelsDirPath(String levelsDirPath) {
			mLevelsDirPath = levelsDirPath;
			return this;
		}

		/**
		 * Sets path to packer folder.
		 * 
		 * @param packerDirName
		 *            path to packer folder
		 * @return instance of {@link Builder}
		 */
		public Builder setPackerDirName(String packerDirName) {
			mPackerDirName = packerDirName;
			return this;
		}

		/**
		 * Sets chapter title image path.
		 * 
		 * @param titleDrawablePath
		 *            chapter title image path
		 * @return instance of {@link Builder}
		 */
		public Builder setTitleDrawablePath(String titleDrawablePath) {
			mTitleDrawablePath = titleDrawablePath;
			return this;
		}

		/**
		 * Sets chapter background image path.
		 * 
		 * @param backgroundDrawablePath
		 *            chapter background image path
		 * @return instance of {@link Builder}
		 */
		public Builder setBackgroundDrawablePath(String backgroundDrawablePath) {
			mBackgroundDrawablePath = backgroundDrawablePath;
			return this;
		}

		/**
		 * Sets main chapter folder path.
		 * 
		 * @param dirPath
		 *            main chapter folder path
		 * @return instance of {@link Builder}
		 */
		public Builder setDirPath(String dirPath) {
			mDirPath = dirPath;
			return this;
		}

		/**
		 * Sets chapter number.
		 * 
		 * @param chapterNumber
		 *            chapter number
		 * @return instance of {@link Builder}
		 */
		public Builder setChapterNumber(int chapterNumber) {
			mChapterNumber = chapterNumber;
			return this;
		}

		/**
		 * Sets accessories folder name.
		 * 
		 * @param accessoriesDirName
		 *            accessories folder name
		 * @return instance of {@link Builder}
		 */
		public Builder setAccessoriesDirName(String accessoriesDirName) {
			mAccesoriesDirName = accessoriesDirName;
			return this;
		}

		/**
		 * Sets name of music file, which will be played in game.
		 * 
		 * @param gameMusic
		 *            game music file name
		 * @return instance of {@link Builder}
		 */
		public Builder setGameMusic(String gameMusic) {
			mGameMusic = gameMusic;
			return this;
		}

		/**
		 * Sets name of music file, which will be played in level selection menu.
		 * 
		 * @param levelSelectionMusic
		 *            level selection music file name
		 * @return instance of {@link Builder}
		 */
		public Builder setLevelSelectionMusic(String levelSelectionMusic) {
			mLevelSelectionMusic = levelSelectionMusic;
			return this;
		}

		/**
		 * Sets audio folder name.
		 * 
		 * @param audioDir
		 *            sounds folder name
		 * @return instance of {@link Builder}
		 */
		public Builder setAudioDirName(String audioDir) {
			mAudioDir = audioDir;
			return this;
		}

		/**
		 * Sets name of file which contains lock level icon image.
		 * 
		 * @param lockLevelIconImgName
		 *            name of file which contains lock level icon image
		 * @return instance of {@link Builder}
		 */
		public Builder setLockLevelIconImgName(String lockLevelIconImgName) {
			mLockLevelIconImgName = lockLevelIconImgName;
			return this;
		}

		/**
		 * Builds {@link ChapterLogicModel} object.
		 * 
		 * @return new instance of {@link ChapterLogicModel} object
		 */
		public ChapterLogicModel build() {
			return new ChapterLogicModel(this);
		}

	}

}
