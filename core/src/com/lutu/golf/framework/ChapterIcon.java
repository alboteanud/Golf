
package com.lutu.golf.framework;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;

/**
 * Class which represents chapter icon.
 */
public class ChapterIcon extends Table {

	private static final float BUTTON_WIDTH = 350.0f;
	private static final float BUTTON_HEIGHT = 278.0f;
	private static final float TITLE_IMAGE_WIDTH = 400.0f;

	private ChapterLogicModel mChapterLogicModel;

	private OnChapterIconClickListener mOnChapterIconClickListener;

	private final Image mChapterTitleImage;
	private final Image mBackgroundImage;

	/**
	 * Constructs {@link ChapterIcon} object.
	 * 
	 * @param style
	 *            style of object see {@link Button#Button(ButtonStyle)}.
	 */
	public ChapterIcon(ButtonStyle style) {

		final Stack stack = new Stack();
		final Button mainButton = new Button(style);
		final ClickListener clickListener = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (mOnChapterIconClickListener != null) {
					mOnChapterIconClickListener.onClick(mChapterLogicModel);
				}
			};
		};
		mainButton.addListener(clickListener);

		mBackgroundImage = new Image();
		stack.add(mBackgroundImage);
		stack.add(mainButton);
		add(stack).center().size(BUTTON_WIDTH, BUTTON_HEIGHT);
		row();
		mChapterTitleImage = new Image();
		add(mChapterTitleImage).center().width(TITLE_IMAGE_WIDTH);

	}

	public void setOnChapterIconClickListener(OnChapterIconClickListener onChapterIconClickListener) {
		mOnChapterIconClickListener = onChapterIconClickListener;
	}

	/**
	 * Sets value.
	 * 
	 * @param chapterLogicModel
	 *            instance of {@link ChapterLogicModel}
	 */
	public void setValue(ChapterLogicModel chapterLogicModel) {
		mChapterLogicModel = chapterLogicModel;
	}

	@Override
	public void setBackground(Drawable background) {
		mBackgroundImage.setDrawable(background);
	}

	/**
	 * Sets title image. See {@link Image#setDrawable(Drawable)}.
	 * 
	 * @param chapterTitle
	 *            title of chapter image
	 */
	public void setTitleDrawable(Drawable chapterTitle) {
		mChapterTitleImage.setDrawable(chapterTitle);
	}

	/**
	 * On chapter icon click listener.
	 */
	public interface OnChapterIconClickListener {

		/**
		 * Calls when chapter icon is clicked.
		 * 
		 * @param chapterLogicModel
		 *            instance of {@link ChapterLogicModel}
		 */
		void onClick(ChapterLogicModel chapterLogicModel);
	}
}
