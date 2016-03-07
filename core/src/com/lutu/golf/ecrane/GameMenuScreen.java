
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lutu.golf.ScreenType;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Game menu screen.
 */
public class GameMenuScreen extends AbstractTransparentScreen {

	private static final String BUTTON_RESET_PRESSED_PATH = "gamemenu/button_reset_pressed2.png";
	private static final String BUTTON_RESET_PATH = "gamemenu/button_reset2.png";
	private static final String BUTTON_RESUME_PRESSED_PATH = "gamemenu/button_resume_pressed2.png";
	private static final String BUTTON_RESUME_PATH = "gamemenu/button_resume2.png";
	private static final String BUTTON_LEVEL_PRESSED_PATH = "gamemenu/button_levels_pressed2.png";
	private static final String BUTTON_LEVEL_PATH = "gamemenu/button_levels2.png";
	private static final String OVERLAY_PATH = "gamemenu/overlay.png";

	private static final float LAYOUT_BUTTON_PADDING = 25.0f;

	private static final Vector2 BUTTON_SIZE = new Vector2(100.0f, 100.0f);

	private GameModel mGameModel;

	/**
	 * Constructs the {@link GameMenuScreen} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public GameMenuScreen(Game game, ScreenType type) {
		super(game, type);
	}

	void setGameModel(GameModel model) {
		mGameModel = model;
	}

	@Override
	public void pause() {
		// Do nothing
	}

	@Override
	public void resume() {
		// Do nothing
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.get().unload(BUTTON_RESET_PRESSED_PATH);
		Assets.get().unload(BUTTON_RESET_PATH);
		Assets.get().unload(BUTTON_RESUME_PRESSED_PATH);
		Assets.get().unload(BUTTON_RESUME_PATH);
		Assets.get().unload(BUTTON_LEVEL_PRESSED_PATH);
		Assets.get().unload(BUTTON_LEVEL_PATH);
		Assets.get().unload(OVERLAY_PATH);
	}

	@Override
	boolean onKeyUpClick(int keycode) { // Do nothing
		switch (keycode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			showParent();
			return true;
		default:
			return false;
		}
	}

	@Override
	boolean onKeyDownClick(int keycode) {
		switch (keycode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void create() {
		super.create();
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;

		Assets.get().load(BUTTON_RESET_PRESSED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_RESET_PATH, Texture.class, param);
		Assets.get().load(BUTTON_RESUME_PRESSED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_RESUME_PATH, Texture.class, param);
		Assets.get().load(BUTTON_LEVEL_PRESSED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_LEVEL_PATH, Texture.class, param);
		Assets.get().load(OVERLAY_PATH, Texture.class, param);
	}

	@Override
	protected Actor onCreateLayout() {
		final Table resultTable = new Table();
		resultTable.setFillParent(true);

		// Background

		resultTable.setBackground(Assets.get().getTextureRegionDrawable(OVERLAY_PATH));

		final Button levelMenuButton = new Button(Assets.get().getTextureRegionDrawable(BUTTON_LEVEL_PATH), Assets
				.get().getTextureRegionDrawable(BUTTON_LEVEL_PRESSED_PATH));
		levelMenuButton.pad(LAYOUT_BUTTON_PADDING);
		resultTable.add(levelMenuButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y).center();

		// Resume button

		final Button resumeButton = new Button(Assets.get().getTextureRegionDrawable(BUTTON_RESUME_PATH), Assets.get()
				.getTextureRegionDrawable(BUTTON_RESUME_PRESSED_PATH));
		resumeButton.pad(LAYOUT_BUTTON_PADDING);
		resultTable.add(resumeButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y).center();//.row();

		// Reset button

		final Button resetButton = new Button(Assets.get().getTextureRegionDrawable(BUTTON_RESET_PATH), Assets.get()
				.getTextureRegionDrawable(BUTTON_RESET_PRESSED_PATH));
		resetButton.pad(LAYOUT_BUTTON_PADDING);

		resultTable.add(resetButton).size(BUTTON_SIZE.x, BUTTON_SIZE.y).center(); //.row();

		// Return to main menu button

//		resultTable.row();



		final ClickListener clickListener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GolfAudioManager.playSound(AudioPaths.CLICK);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Actor button = event.getListenerActor();
				final String pathToMusicFile = mGameModel.getLevelLogicModel().getGameMusicPath();
				if (button.equals(levelMenuButton)) {
					GolfAudioManager.stopMusic(pathToMusicFile);
					final LevelSelectionScreen screen = (LevelSelectionScreen) findScreen(ScreenType.SCREEN_LEVEL_SELECTION);
					screen.setChapterLogicModel(mGameModel.getLevelLogicModel().getChapterLogicModel());
					screen.setIndexOfSelectedPage(mGameModel.getLevelLogicModel().getLevelNumber()
							/ LevelSelectionScreen.NUMBER_OF_LEVELS_ON_PAGE);
					setScreen(screen);
				} else if (button.equals(resetButton)) {
					GolfAudioManager.stopMusic(pathToMusicFile);
					final com.lutu.golf.ecrane.GameScreen screen = (com.lutu.golf.ecrane.GameScreen) findScreen(ScreenType.SCREEN_GAME);
					screen.setLevelLogicModel(mGameModel.getLevelLogicModel());
					screen.reset();
					setScreen(screen);
				} else if (button.equals(resumeButton)) {
					showParent();
				}
			};
		};

		levelMenuButton.addListener(clickListener);
		resetButton.addListener(clickListener);
		resumeButton.addListener(clickListener);

		return resultTable;
	}
}
