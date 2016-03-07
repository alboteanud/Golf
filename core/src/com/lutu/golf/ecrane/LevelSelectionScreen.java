
package com.lutu.golf.ecrane;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.Cell;
import com.lutu.golf.ScreenType;
import com.lutu.golf.framework.AbstractFlipperViewAdapter;
import com.lutu.golf.framework.FlipperView.FlipperElementPosition;
import com.lutu.golf.framework.LevelIcon;
import com.lutu.golf.framework.LevelIcon.OnLevelIconClick;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;
import com.lutu.golf.models.gamelogic.LevelLogicModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * A {@link com.badlogic.gdx.Screen} for selecting a levels from current chapter.
 */
public class LevelSelectionScreen
		extends
		AbstractScreenWithFlipper<AbstractFlipperViewAdapter<Table, List<LevelLogicModel>>, Table, List<LevelLogicModel>> {

	private static final String LEVEL_END_STAR_FULL = "level_end_star_full.png";
	private static final String LEVEL_END_STAR_HALF = "level_end_star_half.png";
	private static final String LEVEL_END_STAR_EMPTY = "level_end_star_empty.png";
	private static final String LEVEL_END_STAR_DISABLE = "level_end_star_disable.png";

	private static final String LEVEL_BG_NORMAL = "levelsmenu/level_bg_normal.png";
	private static final String LEVEL_BD_PRESSED = "levelsmenu/level_bg_pressed.png";
	private static final String LEVEL_BG_DISABLE = "levelsmenu/level_bg_disable.png";

	private static final String LOCKED = "levelsmenu/locked.png";
	private static final String GOLF_CLUB_ICON = "levelsmenu/golf_club_icon.png";

	private static final String PATH_TO_FONT = "levelsmenu/font.fnt";

	private static final String COLECTED_STARS = "levelsmenu/colected_stars_img.png";

	private static final String STARS_TO_UNLOCK = "levelsmenu/stars_to_unlock_img.png";

	private static final float STATISTIC_TABLE_ICON_SIZE = 30.0f;

	private static final int NUMBER_OF_LEVELS_IN_ROW = 4;
	private static final int NUMBER_OF_LEVELS_IN_COL = 3;
	/**
	 * Defines number of levels on page.
	 */
	public static final int NUMBER_OF_LEVELS_ON_PAGE = NUMBER_OF_LEVELS_IN_ROW * NUMBER_OF_LEVELS_IN_COL;

	private static final float LEVEL_ICON_SIZE = 80.0f;
	private static final float LEVEL_ICON_PAD = 10.0f;
	private static final float STAT_TABLE_LEFT_PAD = 10.0f;
	private static final float SCORE_LABEL_RIGHT_PAD = 10.0f;

	private ChapterLogicModel mChapterModel;

	/**
	 * Constructs the {@link LevelSelectionScreen}.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * 
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public LevelSelectionScreen(Game game, ScreenType type) {
		super(game, type);
	}

	public void setChapterLogicModel(ChapterLogicModel chapterModel) {
		mChapterModel = chapterModel;
	}

	@Override
	protected void onShow() {
		super.onShow();

		final String pathToMusicFile = mChapterModel.getLvlSelectionMusicPath();
		GolfAudioManager.playMusic(pathToMusicFile);
		GolfAudioManager.setLoopingMusic(pathToMusicFile, true);
	}

	@Override
	public void create() {
		super.create();

		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;

		Assets.get().load(LEVEL_END_STAR_FULL, Texture.class, param);
		Assets.get().load(LEVEL_END_STAR_HALF, Texture.class, param);
		Assets.get().load(LEVEL_END_STAR_EMPTY, Texture.class, param);
		Assets.get().load(LEVEL_END_STAR_DISABLE, Texture.class, param);

		Assets.get().load(LEVEL_BG_NORMAL, Texture.class, param);
		Assets.get().load(LEVEL_BD_PRESSED, Texture.class, param);
		Assets.get().load(LEVEL_BG_DISABLE, Texture.class, param);
		Assets.get().load(GOLF_CLUB_ICON, Texture.class, param);

		Assets.get().load(LOCKED, Texture.class, param);

		Assets.get().load(STARS_TO_UNLOCK, Texture.class, param);
		Assets.get().load(COLECTED_STARS, Texture.class, param);

		final BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.minFitler = TextureFilter.Linear;
		fontParam.maxFilter = TextureFilter.Linear;
		Assets.get().load(PATH_TO_FONT, BitmapFont.class, fontParam);

		Assets.get().load(mChapterModel.getTitleDrawablePath(), Texture.class, param);
		Assets.get().load(mChapterModel.getBackgroundDrawablePath(), Texture.class, param);

		final int levelCount = mChapterModel.getLevelCount();
		for (int index = 0; index < levelCount; index++) {
			Assets.get().load(mChapterModel.getLevelAt(index).getPathToIconImg(), Texture.class, param);
		}
		Assets.get().load(mChapterModel.getLockLevelIconImgPath(), Texture.class, param);
		final String pathToMusicFile = mChapterModel.getLvlSelectionMusicPath();
		GolfAudioManager.addMusic(pathToMusicFile);
	}

	@Override
	public void dispose() {
		super.dispose();

		Assets.get().unload(LEVEL_END_STAR_FULL);
		Assets.get().unload(LEVEL_END_STAR_HALF);
		Assets.get().unload(LEVEL_END_STAR_EMPTY);
		Assets.get().unload(LEVEL_END_STAR_DISABLE);

		Assets.get().unload(LEVEL_BG_NORMAL);
		Assets.get().unload(LEVEL_BD_PRESSED);
		Assets.get().unload(LEVEL_BG_DISABLE);
		Assets.get().unload(GOLF_CLUB_ICON);

		Assets.get().unload(LOCKED);

		Assets.get().unload(STARS_TO_UNLOCK);
		Assets.get().unload(COLECTED_STARS);

		Assets.get().unload(PATH_TO_FONT);

		Assets.get().unload(mChapterModel.getTitleDrawablePath());
		Assets.get().unload(mChapterModel.getBackgroundDrawablePath());

		final int levelCount = mChapterModel.getLevelCount();
		for (int index = 0; index < levelCount; index++) {
			Assets.get().unload(mChapterModel.getLevelAt(index).getPathToIconImg());
		}
		Assets.get().unload(mChapterModel.getLockLevelIconImgPath());
		final String pathToMusicFile = mChapterModel.getLvlSelectionMusicPath();
		GolfAudioManager.disposeMusic(pathToMusicFile);
	}

	private Table getFlipperActor(final FlipperElementPosition position) {
		// @formatter:off
		final TextureRegionDrawable[] starsTextureRegions = { Assets.get().getTextureRegionDrawable(LEVEL_END_STAR_FULL),
				Assets.get().getTextureRegionDrawable(LEVEL_END_STAR_HALF),
				Assets.get().getTextureRegionDrawable(LEVEL_END_STAR_EMPTY),
				Assets.get().getTextureRegionDrawable(LEVEL_END_STAR_DISABLE), };

		final ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.disabled = Assets.get().getTextureRegionDrawable(LEVEL_BG_DISABLE);
		buttonStyle.down = Assets.get().getTextureRegionDrawable(LEVEL_BD_PRESSED);
		buttonStyle.up = Assets.get().getTextureRegionDrawable(LEVEL_BG_NORMAL);
		// @formatter:on

		final Table result = new Table();
		final LevelIcon[][] levelIcons = new LevelIcon[NUMBER_OF_LEVELS_IN_ROW][NUMBER_OF_LEVELS_IN_COL];
		for (int y = 0; y < NUMBER_OF_LEVELS_IN_COL; y++) {
			for (int x = 0; x < NUMBER_OF_LEVELS_IN_ROW; x++) {
				final TextureRegionDrawable backgroundImage = Assets.get().getTextureRegionDrawable(
						mChapterModel.getLockLevelIconImgPath());

				levelIcons[x][y] = new LevelIcon(backgroundImage, starsTextureRegions, buttonStyle, Assets.get()
						.getTextureRegionDrawable(LOCKED), (BitmapFont) Assets.get().get(PATH_TO_FONT));
				result.add(levelIcons[x][y]).size(LEVEL_ICON_SIZE, LEVEL_ICON_SIZE).pad(LEVEL_ICON_PAD);
				levelIcons[x][y].setOnLevelIconClick(new OnLevelIconClick() {
					@Override
					public void onLevelIconClick(LevelLogicModel value) {
						if (FlipperElementPosition.CENTER.equals(position)) {
							GolfAudioManager.playSound(AudioPaths.CLICK);
							final String pathToMusicFile = mChapterModel.getLvlSelectionMusicPath();
							GolfAudioManager.stopMusic(pathToMusicFile);
							final GameScreen screen = (GameScreen) findScreen(ScreenType.SCREEN_GAME);
							screen.setLevelLogicModel(value);
							setScreen(screen);
						}
					}
				});
			}

			result.row();
		}
		return result;
	}


	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			/*final String pathToMusicFile = mChapterModel.getLvlSelectionMusicPath();
			GolfAudioManager.stopMusic(pathToMusicFile);
			final ChapterSelectionScreen screen = (ChapterSelectionScreen) findScreen(ScreenType.SCREEN_CHAPTER_SELECTION);
			screen.setGameLogicModel(mChapterModel.getGameLogicModel());
			screen.setIndexOfSelectedPage(mChapterModel.getChapterNumber());
			setScreen(screen);*/

			exit();  //eu
			return true;
		default:
			return false;
		}
	}

	//eu
	private void exit() {
		Assets.destroy();
		Gdx.app.exit();
	}



	@Override
	public AbstractFlipperViewAdapter<Table, List<LevelLogicModel>> getFlipperAdapter() {
		final List<List<LevelLogicModel>> values = new LinkedList<List<LevelLogicModel>>();
		final int levelsCount = mChapterModel.getLevelCount();
		List<LevelLogicModel> value = null;
		for (int index = 0; index < levelsCount; index++) {
			if (index % NUMBER_OF_LEVELS_ON_PAGE == 0) {
				if (value != null) {
					values.add(value);
				}
				value = new LinkedList<LevelLogicModel>();
			}
			if (value != null) {
				value.add(mChapterModel.getLevelAt(index));
			}
		}
		if (value != null) {
			values.add(value);
		}
		final AbstractFlipperViewAdapter<Table, List<LevelLogicModel>> result = new AbstractFlipperViewAdapter<Table, List<LevelLogicModel>>(
				values) {

			@Override
			public Table getActor(FlipperElementPosition position) {
				final Table result = getFlipperActor(position);
				return result;
			}

			@Override
			public void fillValue(Table actor, List<LevelLogicModel> values) {
				int valueIndicator = 0;
				final int valuesCount = values.size();
				for (Cell<?> cell : actor.getCells()) {
					final LevelIcon levelIcon = (LevelIcon) cell.getWidget();

					if (valuesCount > valueIndicator) {
						final LevelLogicModel value = values.get(valueIndicator);
						levelIcon.setVisible(true);
						levelIcon.setValue(value);
						levelIcon.setDisable(!value.isEnabled());
						if (value.isEnabled()) {
							levelIcon
									.setBackgroundIcon(Assets.get().getTextureRegionDrawable(value.getPathToIconImg()));
						}
					} else {
						levelIcon.setVisible(false);
					}
					valueIndicator++;
				}
			}
		};
		return result;
	}

	@Override
	Actor prepareTitleView() {
		final Table result = new Table();
		final Table statTable = createStatisticTable();
		final Image title = new Image(Assets.get().getTextureRegionDrawable(mChapterModel.getTitleDrawablePath()));
		final LabelStyle ls = new LabelStyle(((BitmapFont) Assets.get().get(PATH_TO_FONT)), Color.ORANGE);
		final int countHits = countHits();
		final Image golfClubIcon = new Image(Assets.get().getTextureRegionDrawable(GOLF_CLUB_ICON));
		final Label scoreLabel = new Label(String.valueOf(countHits), ls);
		result.row().fillX().expandX();
		result.add(statTable).left().padLeft(STAT_TABLE_LEFT_PAD).expandX().fillX();
		result.add(title).center().expandX();
		result.add(golfClubIcon).size(STATISTIC_TABLE_ICON_SIZE);
		result.add(scoreLabel).right().padRight(SCORE_LABEL_RIGHT_PAD);
		return result;
	}

	private int countHits() {
		int result = 0;
		final int lvlCount = mChapterModel.getLevelCount();
		for (int index = 0; index < lvlCount; index++) {
			final int levelHits = mChapterModel.getLevelAt(index).getMinHits();
			if (levelHits != -1) {
				result = result + levelHits;
			}
		}
		return result;
	}

	private Table createStatisticTable() {
		final Table result = new Table();
		final Image stars = new Image(Assets.get().getTextureRegionDrawable(COLECTED_STARS));
		result.add(stars).left().size(STATISTIC_TABLE_ICON_SIZE).expandX();

		final LabelStyle ls = new LabelStyle((BitmapFont) Assets.get().get(PATH_TO_FONT), Color.ORANGE);
		final float collectedStarsValue = countCollectedStars();
		final Label collectedStars = new Label(String.valueOf(collectedStarsValue), ls);
		result.add(collectedStars).left().expandX();
		result.row();
		final Image locked = new Image(Assets.get().getTextureRegionDrawable(STARS_TO_UNLOCK));
		result.add(locked).left().size(STATISTIC_TABLE_ICON_SIZE).expandX();
		final float unlockStarsValue = mChapterModel.getLevelCount() * 3 - collectedStarsValue;
		final Label lockedStars = new Label(String.valueOf(unlockStarsValue), ls);
		result.add(lockedStars).left().expandX();
		return result;
	}

	private float countCollectedStars() {
		float result = 0.0f;
		final int lvlCount = mChapterModel.getLevelCount();
		for (int index = 0; index < lvlCount; index++) {
			result = result + mChapterModel.getLevelAt(index).getStarsHighScore();
		}
		return result;

	}


}
