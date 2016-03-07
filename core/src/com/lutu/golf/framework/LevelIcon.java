
package com.lutu.golf.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.lutu.golf.models.gamelogic.LevelLogicModel;

/**
 * Class which represents Level icon. Extends {@link Table}.
 * 
 */
public class LevelIcon extends Table {

	private static final String EMPTY_SCORE = "X";

	private static final float CIRCLE_HORIZONTAL_OFFSET = 19.4f;
	private static final float CIRCLE_VERTICAL_OFFSET = 7.5f;

	private static final float BUTTON_SIZE = 80f;
	private static final float LOCK_IMAGE_PADDING_BOTTOM = 5f;
	private static final float LOCK_IMAGE_PADDING_RIGHT = 6f;
	private static final float STARS_PADDING_BOTTOM = 0.5f;

	private static final int FULL_STAR_DRAWABLE_INDEX = 0;
	private static final int HALF_STAR_DRAWABLE_INDEX = 1;
	private static final int EMPTY_STAR_DRAWABLE_INDEX = 2;
	private static final int DISABLE_STAR_DRAWABLE_INDEX = 3;

	private static final float BITMAP_SCALE = 0.7f;

	private final RatingBar mStars;
	private final Label mScoreLabel;
	private final Label mLevelNumber;
	private final Image mLockImage;

	private final Button mMainButton;

	private LevelLogicModel mValue;

	private OnLevelIconClick mOnLevelIconClick;

	private boolean mDisabled;

	private TextureRegionDrawable mLockBackgroundImage;

	/**
	 * Construct instance of {@link TextureRegionDrawable}.
	 * 
	 * @param lockBackgroundImage
	 *            lock background image
	 * @param starsTexture
	 *            textures array which will be used by {@link RatingBar} object
	 * @param buttonLevelStyle
	 *            button style
	 * @param lockTexture
	 *            texture for lock mark
	 * @param bitmapFont
	 *            bitmap font
	 */
	public LevelIcon(TextureRegionDrawable lockBackgroundImage, TextureRegionDrawable[] starsTexture,
			ButtonStyle buttonLevelStyle, TextureRegionDrawable lockTexture, BitmapFont bitmapFont) {
		this(lockBackgroundImage, starsTexture, buttonLevelStyle, lockTexture, bitmapFont, null);
	}

	/**
	 * Construct instance of {@link TextureRegionDrawable}.
	 * 
	 * @param backgroundImage
	 *            lock background image
	 * @param starsTexture
	 *            textures array which will be used by {@link RatingBar} object
	 * @param buttonLevelStyle
	 *            button style
	 * @param lockTexture
	 *            texture for lock mark
	 * @param bitmapFont
	 *            bitmap font
	 * @param score
	 *            score as string
	 */
	public LevelIcon(TextureRegionDrawable backgroundImage, TextureRegionDrawable[] starsTexture,
			ButtonStyle buttonLevelStyle, TextureRegionDrawable lockTexture, BitmapFont bitmapFont, String score) {
		mMainButton = new Button(buttonLevelStyle);
		mStars = new RatingBar(starsTexture[FULL_STAR_DRAWABLE_INDEX], starsTexture[HALF_STAR_DRAWABLE_INDEX],
				starsTexture[EMPTY_STAR_DRAWABLE_INDEX], starsTexture[DISABLE_STAR_DRAWABLE_INDEX]);
		mScoreLabel = prepareScoreLabel(bitmapFont, score);
		final LabelStyle ls = new LabelStyle(mScoreLabel.getStyle());
		ls.fontColor = Color.DARK_GRAY;
		mLevelNumber = new Label(EMPTY_SCORE, ls);
		mLockImage = new Image(lockTexture);
		mLockImage.setVisible(false);
		mMainButton.add(mScoreLabel).top().center().padTop(2f).colspan(2);
		mMainButton.row();

		final float halfWidth = mLevelNumber.getWidth() * 0.5f;
		final float halfHeight = mLevelNumber.getHeight() * 0.5f;
		mMainButton.add(mLevelNumber).left().padTop(CIRCLE_VERTICAL_OFFSET - halfHeight)
				.padLeft(CIRCLE_HORIZONTAL_OFFSET - halfWidth);

		mMainButton.add(mLockImage).right().padBottom(LOCK_IMAGE_PADDING_BOTTOM).padRight(LOCK_IMAGE_PADDING_RIGHT);
		mMainButton.row();
		mMainButton.add(mStars).bottom().padBottom(STARS_PADDING_BOTTOM).colspan(2);
		mMainButton.row();
		mLockBackgroundImage = backgroundImage;
		if (mDisabled) {
			setBackground(mLockBackgroundImage);
		}
		final ClickListener clickListener = new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Actor actor = event.getListenerActor();
				if (actor.equals(mMainButton) && !mMainButton.isDisabled()) {
					if (mOnLevelIconClick != null) {
						mOnLevelIconClick.onLevelIconClick(mValue);
					}
				}
			}
		};
		mMainButton.addListener(clickListener);
		add(mMainButton).size(BUTTON_SIZE, BUTTON_SIZE);
	}

	/**
	 * Set on level icon click listener.
	 * 
	 * @param onLevelIconClick
	 *            instance of {@link OnLevelIconClick}
	 */
	public void setOnLevelIconClick(OnLevelIconClick onLevelIconClick) {
		mOnLevelIconClick = onLevelIconClick;
	}

	@Override
	public void size(float width, float height) {
		super.size(width, height);
		mMainButton.size(width, height);
	}

	/**
	 * Sets disable.
	 * 
	 * @param isDisabled
	 *            if true control will be disable otherwise it will be enable
	 */
	public void setDisable(boolean isDisabled) {
		mMainButton.setDisabled(isDisabled);
		mStars.setEnabled(!isDisabled);
		mLockImage.setVisible(isDisabled);
		mDisabled = isDisabled;
		if (isDisabled) {
			mScoreLabel.getStyle().fontColor = Color.WHITE;
			setBackground(mLockBackgroundImage);
		} else {
			mScoreLabel.getStyle().fontColor = Color.ORANGE;
		}
	}

	/**
	 * Sets background icon.
	 * 
	 * @param mBackground
	 *            background image icon.
	 */
	public void setBackgroundIcon(Drawable mBackground) {
		setBackground(mBackground);
	}

	private Label prepareScoreLabel(BitmapFont bitmapFont, String score) {
		final LabelStyle labelStyle = new LabelStyle(new BitmapFont(bitmapFont.getData().getFontFile(), false),
				Color.ORANGE);
		labelStyle.font.setScale(BITMAP_SCALE);
		final Label result;
		if (score != null) {
			result = new Label(score, labelStyle);
		} else {
			result = new Label(EMPTY_SCORE, labelStyle);
		}
		return result;
	}

	/**
	 * Sets value for which will be represented by this control.
	 * 
	 * @param value
	 *            instance of {@link LevelLogicModel}
	 */
	public void setValue(LevelLogicModel value) {
		mValue = value;
		mLevelNumber.setText(String.valueOf(1 + value.getLevelNumber()));
		mStars.setValue(value.getStarsHighScore());
		setScore(value.getMinHits());
	}

	/**
	 * Sets score label text.
	 * 
	 * @param score
	 *            number of hits
	 */
	private void setScore(int score) {
		if (score == -1) {
			mScoreLabel.setText(EMPTY_SCORE);
		} else {
			mScoreLabel.setText(String.valueOf(score));
		}
	}

	/**
	 * On level clicked listener.
	 */
	public interface OnLevelIconClick {
		/**
		 * Calls when level icon is clicked.
		 * 
		 * @param value
		 *            instance of {@link LevelLogicModel}
		 */
		void onLevelIconClick(LevelLogicModel value);
	}
}
