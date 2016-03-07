
package com.lutu.golf.controlere;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;
import com.lutu.golf.models.gamelogic.GameLogicModel;
import com.lutu.golf.models.gamelogic.LevelLogicModel;
import com.lutu.golf.utils.GolfPreferences;

/**
 * Utility class used to parse XML file. This file contains information about game structure.
 */
public final class XmlGolfGameReader {

	private static final String PATH_TO_CHAPTER_INFO_XML = "chapters/chapters.xml";

	private XmlGolfGameReader() {

	}

	/**
	 * Fills {@link GameLogicModel} object via data from XML.
	 * 
	 * @param mGameModel
	 *            instance of {@link GameLogicModel}
	 */
	public static void fillGameModel(GameLogicModel mGameModel) {

		final XmlReader xmlReader = new XmlReader();
		final FileHandle fileHandle = Gdx.files.internal(PATH_TO_CHAPTER_INFO_XML);
		final Element rootTag;
		try {
			rootTag = xmlReader.parse(fileHandle);
			final int countRootChilds = rootTag.getChildCount();
			for (int index = 0; index < countRootChilds; index++) {
				final Element rootChild = rootTag.getChild(index);
				if (rootChild.getName().equals(Tags.CHAPTER)) {
					final ChapterLogicModel chapterModel = getChapterFromXml(rootChild, mGameModel, index);
					mGameModel.addChapter(chapterModel);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static ChapterLogicModel getChapterFromXml(Element chapterTag, GameLogicModel mGameModel, int chapterIndex) {
		final String dirPath = chapterTag.get(Attr.DIR_PATH);
		final String levelsDirName = chapterTag.get(Attr.LEVELS_DIR_NAME);
		final String bodiesDataFile = chapterTag.get(Attr.BODIES_DATA_FILE);
		final String packerDirName = chapterTag.get(Attr.PACKER_DIR_NAME);
		final String accessoriesDir = chapterTag.get(Attr.ACCESSORIES_DIR);
		final String audioDir = chapterTag.get(Attr.AUDIO_DIR);
		final String gameMusic = chapterTag.get(Attr.GAME_MUSIC);
		final String levelSelectMusic = chapterTag.get(Attr.LEVEL_SELECT_MUSIC);
		final String titleImage = chapterTag.get(Attr.TITLE_IMAGE);
		final String bgImage = chapterTag.get(Attr.BG_IMAGE);
		final String levelLockImg = chapterTag.get(Attr.LEVEL_LOCK_IMG);
		//@formatter:off
		final ChapterLogicModel result = new ChapterLogicModel.Builder()
			.setGameLogicModel(mGameModel)
			.setDirPath(dirPath)
			.setLevelsDirPath(levelsDirName)
			.setBodiesDataFileName(bodiesDataFile)
			.setPackerDirName(packerDirName)
			.setAccessoriesDirName(accessoriesDir)
			.setGameMusic(gameMusic)
			.setLevelSelectionMusic(levelSelectMusic)
			.setAudioDirName(audioDir)
			.setTitleDrawablePath(titleImage)
			.setBackgroundDrawablePath(bgImage)
			.setLockLevelIconImgName(levelLockImg)
			.setChapterNumber(chapterIndex)
			.build();
		//@formatter:on
		final int countRootChilds = chapterTag.getChildCount();
		for (int index = 0; index < countRootChilds; index++) {
			final Element chapterChild = chapterTag.getChild(index);
			if (chapterChild.getName().equals(Tags.LEVEL)) {
				final LevelLogicModel levelLogicModel = getLevelFromXml(chapterChild, result, index);
				result.addLevel(levelLogicModel);
			}
		}
		return result;
	}

	private static LevelLogicModel getLevelFromXml(Element levelTag, ChapterLogicModel chapter, int index) {

		final String fileName = levelTag.get(Attr.FILE_NAME);
		final String levelIcon = levelTag.get(Attr.LEVEL_ICON);
		//@formatter:off
		final LevelLogicModel result = new LevelLogicModel.Builder()
				.setChapterLogicModel(chapter)
				.setFileName(fileName)
				.setIconImgName(levelIcon)
				.setLevelNumber(index)
				.build();
		//@formatter:on
		if (index == 0) {
			GolfPreferences.INSTANCE.setLevelUnblocked(chapter.getChapterNumber(), index, true);
			result.setEnabled(true);
		}
		result.setEnabled(GolfPreferences.INSTANCE.isLevelUnblocked(chapter.getChapterNumber(), index));
		result.setStarsHighScore(GolfPreferences.INSTANCE.getStarsCount(chapter.getChapterNumber(), index));
		result.setMinHits(GolfPreferences.INSTANCE.getBestScore(chapter.getChapterNumber(), index));
		return result;
	}

	/**
	 * Contains constants which define tags names.
	 */
	private static final class Tags {

		/**
		 * Hide constructor.
		 */
		private Tags() {

		}

		private static final String CHAPTER = "CHAPTER";
		private static final String LEVEL = "LEVEL";
	}

	/**
	 * Contains constants which define tags attributes.
	 */
	private static final class Attr {

		/**
		 * Hide constructor.
		 */
		private Attr() {

		}

		// CHAPTER - tag attributes
		public static final String BG_IMAGE = "bg_image";
		private static final String DIR_PATH = "dir_path";
		private static final String LEVELS_DIR_NAME = "levels_dir_name";
		private static final String BODIES_DATA_FILE = "bodies_data_file";
		private static final String PACKER_DIR_NAME = "packer_dir_name";
		private static final String TITLE_IMAGE = "title_image";
		private static final String ACCESSORIES_DIR = "accessories_dir_name";
		private static final String AUDIO_DIR = "audio_dir_name";
		private static final String GAME_MUSIC = "game_music";
		private static final String LEVEL_SELECT_MUSIC = "level_select_music";
		private static final String LEVEL_LOCK_IMG = "level_lock_img";
		// LEVEL - tag attributes
		private static final String FILE_NAME = "file_name";
		private static final String LEVEL_ICON = "level_icon";
	}
}
