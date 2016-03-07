
package com.lutu.golf.models.gamelogic;

import java.util.LinkedList;
import java.util.List;

/**
 * Class contains information about game structure.
 */
public class GameLogicModel {
	private List<ChapterLogicModel> mChapterModels;

	/**
	 * Constructs {@link GameLogicModel}.
	 */
	public GameLogicModel() {
		mChapterModels = new LinkedList<ChapterLogicModel>();
	}

	/**
	 * Adds {@link ChapterLogicModel} object. See {@link List#add(Object)}.
	 * 
	 * @param chapterModel
	 *            instance of {@link ChapterLogicModel}
	 */
	public void addChapter(ChapterLogicModel chapterModel) {
		mChapterModels.add(chapterModel);
	}

	/**
	 * Returns number of chapter. {@link List#size()}.
	 * 
	 * @return number of chapter
	 */
	public int getChapterCount() {
		return mChapterModels.size();
	}

	/**
	 * Returns chapter at index. Throws IndexOutOfBoundsException exception if index >=
	 * {@link GameLogicModel#getChapterCount() }. See {@link List#get(int)}.
	 * 
	 * 
	 * @param index
	 *            index of chapter which will be returned
	 * @return instance of {@link ChapterLogicModel}
	 */
	public ChapterLogicModel getChapterAt(int index) {
		return mChapterModels.get(index);
	}

}
