
package com.lutu.golf.ecrane;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lutu.golf.ScreenType;
import com.lutu.golf.controlere.XmlGolfGameReader;
import com.lutu.golf.framework.AbstractFlipperViewAdapter;
import com.lutu.golf.framework.ChapterIcon;
import com.lutu.golf.framework.ChapterIcon.OnChapterIconClickListener;
import com.lutu.golf.framework.FlipperView.FlipperElementPosition;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;
import com.lutu.golf.models.gamelogic.GameLogicModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Class which represents chapter selection screen.
 */
public class ChapterSelectionScreen
		extends
		AbstractScreenWithFlipper<AbstractFlipperViewAdapter<ChapterIcon, ChapterLogicModel>, ChapterIcon, ChapterLogicModel> {

	private static final String CHAPTER_BG_NORMAL = "levelsmenu/chapter_bg_normal.png";
	private static final String CHAPTER_GB_PRESSED = "levelsmenu/chapter_bg_pressed.png";
	private static final String CHAPTER_BG_DISABLE = "levelsmenu/chapter_bg_disable.png";

	private static final String SELECT_CHAPTER_TITLE = "levelsmenu/select_chapter_title.png";

	private GameLogicModel mGameLogicModel;

	/**
	 * Constructs the {@link ChapterSelectionScreen} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public ChapterSelectionScreen(Game game, ScreenType type) {
		super(game, type);
	}

	public void setGameLogicModel(GameLogicModel gameLogicModel) {
		mGameLogicModel = gameLogicModel;
	}

	@Override
	public void create() {
		super.create();

		mGameLogicModel = new GameLogicModel();
		XmlGolfGameReader.fillGameModel(mGameLogicModel);
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;

		Assets.get().load(CHAPTER_BG_NORMAL, Texture.class, param);
		Assets.get().load(CHAPTER_GB_PRESSED, Texture.class, param);
		Assets.get().load(CHAPTER_BG_DISABLE, Texture.class, param);
		Assets.get().load(SELECT_CHAPTER_TITLE, Texture.class, param);

		final int size = mGameLogicModel.getChapterCount();
		for (int index = 0; index < size; index++) {
			final ChapterLogicModel chapter = mGameLogicModel.getChapterAt(index);
			Assets.get().load(chapter.getTitleDrawablePath(), Texture.class, param);
			Assets.get().load(chapter.getBackgroundDrawablePath(), Texture.class, param);
		}

		GolfAudioManager.addMusic(AudioPaths.MENU_MUSIC);
	}

	@Override
	public void dispose() {
		super.dispose();

		final int size = mGameLogicModel.getChapterCount();
		for (int index = 0; index < size; index++) {
			final ChapterLogicModel chapter = mGameLogicModel.getChapterAt(index);
			Assets.get().unload(chapter.getTitleDrawablePath());
			Assets.get().unload(chapter.getBackgroundDrawablePath());
		}

		Assets.get().unload(CHAPTER_BG_NORMAL);
		Assets.get().unload(CHAPTER_GB_PRESSED);
		Assets.get().unload(CHAPTER_BG_DISABLE);
		Assets.get().unload(SELECT_CHAPTER_TITLE);

		GolfAudioManager.disposeMusic(AudioPaths.MENU_MUSIC);
	}

	@Override
	protected void onShow() {
		super.onShow();
		GolfAudioManager.playMusic(AudioPaths.MENU_MUSIC);
		GolfAudioManager.setLoopingMusic(AudioPaths.MENU_MUSIC, true);
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			setScreen(ScreenType.SCREEN_MAIN_MENU);
			return true;
		default:
			return false;
		}
	}

	@Override
	AbstractFlipperViewAdapter<ChapterIcon, ChapterLogicModel> getFlipperAdapter() {
		final List<ChapterLogicModel> chapterLogicModels = new LinkedList<ChapterLogicModel>();
		final int size = mGameLogicModel.getChapterCount();
		for (int index = 0; index < size; index++) {

			chapterLogicModels.add(mGameLogicModel.getChapterAt(index));
		}
		final AbstractFlipperViewAdapter<ChapterIcon, ChapterLogicModel> result = new AbstractFlipperViewAdapter<ChapterIcon, ChapterLogicModel>(
				chapterLogicModels) {

			@Override
			public ChapterIcon getActor(final FlipperElementPosition position) {
				final ButtonStyle style = new ButtonStyle();
				style.down = Assets.get().getTextureRegionDrawable(CHAPTER_GB_PRESSED);
				style.up = Assets.get().getTextureRegionDrawable(CHAPTER_BG_NORMAL);
				style.disabled = Assets.get().getTextureRegionDrawable(CHAPTER_BG_DISABLE);
				final ChapterIcon result = new ChapterIcon(style);
				result.setOnChapterIconClickListener(new OnChapterIconClickListener() {

					@Override
					public void onClick(ChapterLogicModel chapterLogicModel) {
						if (FlipperElementPosition.CENTER.equals(position)) {
							GolfAudioManager.playSound(AudioPaths.CLICK);
							final LevelSelectionScreen screen = (LevelSelectionScreen) findScreen(ScreenType.SCREEN_LEVEL_SELECTION);
							screen.setChapterLogicModel(chapterLogicModel);
							setScreen(screen);
							GolfAudioManager.stopMusic(AudioPaths.MENU_MUSIC);
						}

					}
				});
				return result;
			}

			@Override
			public void fillValue(ChapterIcon actor, ChapterLogicModel value) {
				actor.setValue(value);
				actor.setTitleDrawable(Assets.get().getTextureRegionDrawable(value.getTitleDrawablePath()));
				actor.setBackground(Assets.get().getTextureRegionDrawable(value.getBackgroundDrawablePath()));
			}
		};
		return result;
	}

	@Override
	Actor prepareTitleView() {
		final Table result = new Table();
		result.add(new Image(Assets.get().getTextureRegionDrawable(SELECT_CHAPTER_TITLE))).center();
		return result;
	}
}
