
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.Cell;
import com.lutu.golf.ActionResolver;
import com.lutu.golf.ScreenType;
import com.lutu.golf.framework.RatingBar;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;
import com.lutu.golf.models.gamelogic.LevelLogicModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Dialog which is used to display game outcome such as score, collected stars.
 */
public class EndLevelDialog extends AbstractTransparentScreen {

	private static final String LEVEL_END_FULL = "level_end_star_full.png";
	private static final String LEVEL_END_HALF = "level_end_star_half.png";
	private static final String LEVEL_END_EMPTY = "level_end_star_empty.png";
	private static final String OVERLAY_PATH = "gamemenu/overlay.png";
	private static final String HITS_BG = "end_level/hits_bg.png";
	private static final String LEVEL_END_MENU_BUTTON_UP = "end_level/level_end_menu_button_up.png";
	private static final String LEVEL_END_MENU_BUTTON_DOWN = "end_level/level_end_menu_button_down.png";
	private static final String LEVEL_END_NEXT_BUTTON_UP = "end_level/level_end_next_button_up.png";
	private static final String LEVEL_END_NEXT_BUTTON_DOWN = "end_level/level_end_next_button_down.png";
	private static final String LEVEL_END_NEXT_BUTTON_DISABLE = "end_level/level_end_next_button_disable.png";
	private static final String LEVEL_END_REPLAY_BUTTON_UP = "end_level/level_end_replay_button_up.png";
	private static final String LEVEL_END_REPLAY_BUTTON_DOWN = "end_level/level_end_replay_button_down.png";
	private static final String LEVEL_END_STAR_BG = "end_level/level_end_star_bg.png";
	private static final String LEVEL_COMPLETED = "end_level/level_completed.png";
	private static final String LEVEL_LOSE = "end_level/level_lose.png";

	private static final String HOLE_IN_ONE = "end_level/hole_in_one.png";
	private static final String BALOONS_RIGHT = "end_level/baloons_right.png";
	private static final String BALOONS_LEFT = "end_level/baloons_left.png";

	private static final String PATH_TO_FONT = "levelsmenu/font.fnt";

	private static final String LABEL_FONT_COLOR = "2b2600";

	private static final float BUTTON_SIZE = 82.0f;
	private static final float BUTTON_STANDARD_PADDING = 5.0f;
	private static final float BUTTON_STANDARD_OFFSET = 2.0f;

	private static final float STARS_CONTAINER_WIDTH = 250.0f;
	private static final float STARS_CONTAINER_HEIGHT = 80.0f;

	private static final float DIALOG_TABLE_BOTTOM_PADDING = 20.0f;
	private static final float DIALOG_TABLE_WIDTH = 350.0f;
	private static final float DIALOG_TABLE_HEIGHT = 177.0f;
	private static final int DIALOG_COLSPAN = 3;

	private static final float TITLE_WIDTH = STARS_CONTAINER_WIDTH;
	private static final float TITLE_HEIGHT = 45.0f;

	private static final float LABEL_PADDING_RIGHT = -30.0f;
	private static final float LABEL_PADDING_BOTTOM = 3.0f;

	private static final float HITS_BG_WIDTH = 135.0f;
	private static final float HITS_BG_HEIGHT = 40.0f;

	private static final float BALLOON_SIZE_WIDTH = 80.0f;
	private static final float BALLOON_SIZE_HEIGHT = 120.0f;
	private static final float BALLOON_PAD = 100.0f;
	private static final float BALLOON_PAD_BOTTOM = 150.0f;
	private GameModel mGameModel;
	private RatingBar mStars;
	private Label mHits;
	private Image mTitleHoleInOne;
	private Image mTitleLevelCompleted;
	private Image mTitleLevelLose;
	private Button mNextLevelButton;
	private Cell<Image> mTitleCell;
	private Actor mHoleInOneDialog;

	/**
	 * Constructs the {@link EndLevelDialog}.
	 *  @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 * @param actionResolver
	 */
	private ActionResolver actionResolver;
	public EndLevelDialog(Game game, ScreenType type, ActionResolver actionResolver) {
		super(game, type);
		this.actionResolver = actionResolver;
	}

	void setGameModel(GameModel model) {
		mGameModel = model;
	}

	@Override
	protected void onShow() {
		super.onShow();
		GolfAudioManager.playMusic(AudioPaths.END_LEVEL);

		mStars.setValue(mGameModel.getStarsCount());
		mHits.setText(String.valueOf(mGameModel.getHits()));

		if (mGameModel.getHits() == 1) {
			mTitleCell.setWidget(mTitleHoleInOne);
			mHoleInOneDialog.setVisible(true);
		} else if (mGameModel.isLastShot() && !mGameModel.isLevelFinished()) {
			mTitleCell.setWidget(mTitleLevelLose);
			mHoleInOneDialog.setVisible(false);
		} else {
			mTitleCell.setWidget(mTitleLevelCompleted);
			mHoleInOneDialog.setVisible(false);
		}
		mNextLevelButton.setDisabled(mGameModel.isLastShot());
	}

	@Override
	protected void onHide() {
		super.onHide();
		GolfAudioManager.stopMusic(AudioPaths.END_LEVEL);
	}

	@Override
	public void create() {
		super.create();
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;
		Assets.get().load(LEVEL_END_FULL, Texture.class, param);
		Assets.get().load(LEVEL_END_HALF, Texture.class, param);
		Assets.get().load(LEVEL_END_EMPTY, Texture.class, param);
		Assets.get().load(OVERLAY_PATH, Texture.class);
		Assets.get().load(LEVEL_END_MENU_BUTTON_UP, Texture.class, param);
		Assets.get().load(LEVEL_END_MENU_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(LEVEL_END_NEXT_BUTTON_UP, Texture.class, param);
		Assets.get().load(LEVEL_END_NEXT_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(LEVEL_END_NEXT_BUTTON_DISABLE, Texture.class, param);
		Assets.get().load(LEVEL_END_REPLAY_BUTTON_UP, Texture.class, param);
		Assets.get().load(LEVEL_END_REPLAY_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(LEVEL_COMPLETED, Texture.class, param);
		Assets.get().load(LEVEL_LOSE, Texture.class, param);
		Assets.get().load(LEVEL_END_STAR_BG, Texture.class, param);
		Assets.get().load(HITS_BG, Texture.class, param);
		Assets.get().load(BALOONS_LEFT, Texture.class, param);
		Assets.get().load(BALOONS_RIGHT, Texture.class, param);
		Assets.get().load(HOLE_IN_ONE, Texture.class, param);

		final BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.minFitler = TextureFilter.Linear;
		fontParam.maxFilter = TextureFilter.Linear;
		Assets.get().load(PATH_TO_FONT, BitmapFont.class, fontParam);

		GolfAudioManager.addMusic(AudioPaths.END_LEVEL);
	}

	private Table prepareDialog() {
		final Table dialogTable = new Table();
		dialogTable.setBackground(Assets.get().getTextureRegionDrawable(LEVEL_END_STAR_BG));

		mStars = new RatingBar(Assets.get().getTextureRegionDrawable(LEVEL_END_FULL), Assets.get()
				.getTextureRegionDrawable(LEVEL_END_HALF), Assets.get().getTextureRegionDrawable(LEVEL_END_EMPTY));
		dialogTable.add(mStars).colspan(DIALOG_COLSPAN).height(STARS_CONTAINER_HEIGHT).width(STARS_CONTAINER_WIDTH)
				.padBottom(DIALOG_TABLE_BOTTOM_PADDING).center();

		mStars.setValue(mGameModel.getStarsCount());
		dialogTable.row();

		final Button menuButton = new Button(Assets.get().getTextureRegionDrawable(LEVEL_END_MENU_BUTTON_UP), Assets
				.get().getTextureRegionDrawable(LEVEL_END_MENU_BUTTON_DOWN));

		final ButtonStyle nextButtonStyle = new ButtonStyle();
		nextButtonStyle.up = Assets.get().getTextureRegionDrawable(LEVEL_END_NEXT_BUTTON_UP);
		nextButtonStyle.down = Assets.get().getTextureRegionDrawable(LEVEL_END_NEXT_BUTTON_DOWN);
		nextButtonStyle.disabled = Assets.get().getTextureRegionDrawable(LEVEL_END_NEXT_BUTTON_DISABLE);
		mNextLevelButton = new Button(nextButtonStyle);

		final Button replayButton = new Button(Assets.get().getTextureRegionDrawable(LEVEL_END_REPLAY_BUTTON_UP),
				Assets.get().getTextureRegionDrawable(LEVEL_END_REPLAY_BUTTON_DOWN));

		final ClickListener inputListener = new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				final Button buttonSelected = (Button) event.getListenerActor();
				if (!buttonSelected.isDisabled()) {
					GolfAudioManager.playSound(AudioPaths.CLICK);
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Actor buttonSelected = event.getListenerActor();

				if (buttonSelected.equals(mNextLevelButton) && !mNextLevelButton.isDisabled()) {
					// In future next level
					final LevelLogicModel levelInfo = mGameModel.getLevelLogicModel();
					final int lvlIndex = levelInfo.getLevelNumber();
					if (lvlIndex < levelInfo.getChapterLogicModel().getLevelCount() - 1) {
						final com.lutu.golf.ecrane.GameScreen screen = (com.lutu.golf.ecrane.GameScreen) findScreen(ScreenType.SCREEN_GAME);
						screen.setLevelLogicModel(levelInfo.getChapterLogicModel().getLevelAt(lvlIndex + 1));
						screen.reset();
						setScreen(screen);
					} else {
						final ChapterSelectionScreen screen = (ChapterSelectionScreen) findScreen(ScreenType.SCREEN_CHAPTER_SELECTION);
						final ChapterLogicModel chapterLogic = mGameModel.getLevelLogicModel().getChapterLogicModel();
						screen.setGameLogicModel(chapterLogic.getGameLogicModel());
						screen.setIndexOfSelectedPage(chapterLogic.getChapterNumber());
						setScreen(screen);
					}
                    actionResolver.showInterstital();                  //interstitial
				}
				if (buttonSelected.equals(menuButton)) {
					final com.lutu.golf.ecrane.LevelSelectionScreen screen = (com.lutu.golf.ecrane.LevelSelectionScreen) findScreen(ScreenType.SCREEN_LEVEL_SELECTION);
					screen.setChapterLogicModel(mGameModel.getLevelLogicModel().getChapterLogicModel());
					screen.setIndexOfSelectedPage(mGameModel.getLevelLogicModel().getLevelNumber()
							/ com.lutu.golf.ecrane.LevelSelectionScreen.NUMBER_OF_LEVELS_ON_PAGE);
					setScreen(screen);
				}
				if (buttonSelected.equals(replayButton)) {
					final com.lutu.golf.ecrane.GameScreen screen = (com.lutu.golf.ecrane.GameScreen) findScreen(ScreenType.SCREEN_GAME);
                    screen.reset();
					setScreen(screen);
                 //   actionResolver.showInterstital();                  //interstitial

				}
			}
		};
		mNextLevelButton.addListener(inputListener);
		menuButton.addListener(inputListener);
		replayButton.addListener(inputListener);

	/*	dialogTable.add(mNextLevelButton).right().padBottom(BUTTON_STANDARD_PADDING).padRight(BUTTON_STANDARD_PADDING)
				.size(BUTTON_SIZE);
		dialogTable.add(menuButton).center().padBottom(BUTTON_STANDARD_PADDING).padLeft(BUTTON_STANDARD_OFFSET)
				.size(BUTTON_SIZE);*/

        dialogTable.add(menuButton).right().padBottom(BUTTON_STANDARD_PADDING).padRight(BUTTON_STANDARD_PADDING)
                .size(BUTTON_SIZE);
        dialogTable.add(mNextLevelButton).center().padBottom(BUTTON_STANDARD_PADDING).padLeft(BUTTON_STANDARD_OFFSET)
                .size(BUTTON_SIZE);
		dialogTable.add(replayButton).left().padBottom(BUTTON_STANDARD_PADDING).padLeft(BUTTON_STANDARD_PADDING)
				.size(BUTTON_SIZE);
		dialogTable.row().bottom();

		return dialogTable;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Actor onCreateLayout() {
		final Stack result = new Stack();
		result.setFillParent(true);

		final Table backgroundTable = new Table();
		backgroundTable.setBackground(Assets.get().getTextureRegionDrawable(OVERLAY_PATH));
		result.add(backgroundTable);

		final Table mainTable = new Table();

		mHoleInOneDialog = createHitInOneDialog();
		result.add(mHoleInOneDialog);

		mainTable.setFillParent(true);

		mTitleHoleInOne = new Image(Assets.get().getTextureRegionDrawable(HOLE_IN_ONE));
		mTitleLevelCompleted = new Image(Assets.get().getTextureRegionDrawable(LEVEL_COMPLETED));
		mTitleLevelLose = new Image(Assets.get().getTextureRegionDrawable(LEVEL_LOSE));

		mTitleCell = mainTable.add().size(TITLE_WIDTH, TITLE_HEIGHT);
		mainTable.row();

		final Table hitsBg = new Table();
		hitsBg.setBackground(Assets.get().getTextureRegionDrawable(HITS_BG));
		final LabelStyle labelStyle = new LabelStyle((BitmapFont) Assets.get().get(PATH_TO_FONT), Color.GREEN);
		labelStyle.fontColor = Color.valueOf(LABEL_FONT_COLOR);
		mHits = new Label(String.valueOf(mGameModel.getHits()), labelStyle);
		hitsBg.add(mHits).right().padRight(LABEL_PADDING_RIGHT).padBottom(LABEL_PADDING_BOTTOM);
		mainTable.add(hitsBg).size(HITS_BG_WIDTH, HITS_BG_HEIGHT).center();

		mainTable.row();

		final Table dialogTable = prepareDialog();

		mainTable.add(dialogTable).center().size(DIALOG_TABLE_WIDTH, DIALOG_TABLE_HEIGHT);

		dialogTable.row();
		result.add(mainTable);
		return result;
	}

	private Actor createHitInOneDialog() {
		final Table result = new Table();
		final Image balloonsLeft = new Image(Assets.get().getTextureRegionDrawable(BALOONS_LEFT));
		final Image balloonsRight = new Image(Assets.get().getTextureRegionDrawable(BALOONS_RIGHT));
		result.add(balloonsLeft).size(BALLOON_SIZE_WIDTH, BALLOON_SIZE_HEIGHT).padRight(BALLOON_PAD)
				.padBottom(BALLOON_PAD_BOTTOM);
		result.add(balloonsRight).size(BALLOON_SIZE_WIDTH, BALLOON_SIZE_HEIGHT).padLeft(BALLOON_PAD)
				.padBottom(BALLOON_PAD_BOTTOM);

		return result;
	}

	@Override
	public void pause() {
		// Do nothing.
	}

	@Override
	public void resume() {
		// Do nothing.
	}

	@Override
	public void dispose() {
		super.dispose();

		Assets.get().unload(LEVEL_END_FULL);
		Assets.get().unload(LEVEL_END_HALF);
		Assets.get().unload(LEVEL_END_EMPTY);
		Assets.get().unload(OVERLAY_PATH);
		Assets.get().unload(LEVEL_END_MENU_BUTTON_UP);
		Assets.get().unload(LEVEL_END_MENU_BUTTON_DOWN);
		Assets.get().unload(LEVEL_END_NEXT_BUTTON_UP);
		Assets.get().unload(LEVEL_END_NEXT_BUTTON_DOWN);
		Assets.get().unload(LEVEL_END_NEXT_BUTTON_DISABLE);
		Assets.get().unload(LEVEL_END_REPLAY_BUTTON_UP);
		Assets.get().unload(LEVEL_END_REPLAY_BUTTON_DOWN);
		Assets.get().unload(LEVEL_COMPLETED);
		Assets.get().unload(LEVEL_LOSE);
		Assets.get().unload(LEVEL_END_STAR_BG);
		Assets.get().unload(PATH_TO_FONT);
		Assets.get().unload(HITS_BG);
		Assets.get().unload(BALOONS_LEFT);
		Assets.get().unload(BALOONS_RIGHT);
		Assets.get().unload(HOLE_IN_ONE);

		GolfAudioManager.disposeMusic(AudioPaths.END_LEVEL);
	}

	@Override
	boolean onKeyUpClick(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			final com.lutu.golf.ecrane.LevelSelectionScreen screen = (com.lutu.golf.ecrane.LevelSelectionScreen) findScreen(ScreenType.SCREEN_LEVEL_SELECTION);
			screen.setChapterLogicModel(mGameModel.getLevelLogicModel().getChapterLogicModel());
			screen.setIndexOfSelectedPage(mGameModel.getLevelLogicModel().getLevelNumber()
					/ com.lutu.golf.ecrane.LevelSelectionScreen.NUMBER_OF_LEVELS_ON_PAGE);
			setScreen(screen);
			return true;
		}
		return false;
	}

	@Override
	boolean onKeyDownClick(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			return true;
		}
		return false;
	}

}
