
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lutu.golf.ScreenType;
import com.lutu.golf.controlere.GameController;
import com.lutu.golf.models.ClubModel.ClubType;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.models.GameModel.GameState;
import com.lutu.golf.models.gamelogic.LevelLogicModel;
import com.lutu.golf.renderers.GameRenderer;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * This class represents game screen. The game is displayed on this screen.
 */
public class GameScreen extends GolfScreen {

	private static final boolean DEBUG_TABLE = false;

	private GameController mGameController;
	private GameModel mGameModel;
	private GameRenderer mGameRenderer;
	// private final GameScreenButtonManager mButtonManager;
	private InputMultiplexer mInputMultiplexer;

	private Camera mGameCamera;
	private GestureDetector mGestureDetector;

	private final HUDStage mHUDStage;

	private LevelLogicModel mLevelLogicModel;

	private boolean mNeedsRecreating;
	private String mPathToLevelMusic;
	private boolean mDisableShootButton;

	/**
	 * Constructs the actual {@link GameScreen} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 * 
	 */
	public GameScreen(Game game, ScreenType type) {
		super(game, type);
		mDisableShootButton = true;

		mHUDStage = new HUDStage(this);
	}

	public void setLevelLogicModel(LevelLogicModel model) {
		mLevelLogicModel = model;
	}

	/**
	 * Resets the Game so it uses the currently set {@link LevelLogicModel}. Reload your chapter and level-specific
	 * assets here.
	 */
	void reset() {
		setDisableShootButton(true);
		mNeedsRecreating = true;
		if (mGameRenderer != null) {
			disposeSpecific();
		}

		mInputMultiplexer = new InputMultiplexer();
		mGameCamera = new OrthographicCamera();
		mGameModel = new GameModel(mLevelLogicModel);
		mGameController = new GameController(mGameModel, mGameCamera);
		mGameRenderer = new GameRenderer(mGameModel, mGameCamera);
		mHUDStage.setGameModel(mGameModel);
		mHUDStage.setGameController(mGameController);

		mInputMultiplexer.addProcessor(mHUDStage);

		// XXX: only for test purpose (ArrowController)
		// start
		mInputMultiplexer.addProcessor(mGameController.getArrowController());
		// end

		mGestureDetector = new GestureDetector(mGameController.getCameraController()) {
			@Override
			public boolean scrolled(int amount) {
				// Handle mouse scrolling on desktop.
				mGameController.getCameraController().scrolled(amount);
				return true;
			}
		};
		mInputMultiplexer.addProcessor(mGestureDetector);
	}

	@Override
	public void render(float delta) {
		// If camera's position/viewport was modified - cam.update
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (GameState.RUNNING.equals(mGameModel.getGameState())) {
			mGameController.update(delta);
		}
		if (mGameModel.isLevelFinished()) {
			final String pathToMusicFile = mLevelLogicModel.getGameMusicPath();
			GolfAudioManager.stopMusic(pathToMusicFile);
			final EndLevelDialog screen = (EndLevelDialog) findScreen(ScreenType.SCREEN_END_LEVEL_DIALOG);
			screen.setParent(this);
			screen.setGameModel(mGameModel);
			setScreen(screen);
		} else if (mGameModel.isLastShot() && !mGameModel.isShoot()) {
			final EndLevelDialog screen = (EndLevelDialog) findScreen(ScreenType.SCREEN_END_LEVEL_DIALOG);
			screen.setParent(this);
			screen.setGameModel(mGameModel);
			setScreen(screen);
		}
		mGameRenderer.render();

		mHUDStage.act(delta);
		mHUDStage.draw();
		if (DEBUG_TABLE) {
			Table.drawDebug(mHUDStage);
		}
	}

	/**
	 * Renders only. See {@link GameRenderer#render()}
	 * 
	 * @param delta
	 *            time since last frame
	 */
	@Override
	public void onRender(float delta) {
		mGameRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// Our camera should be notified about the change in the screen size
		mGameController.resize(width, height);
		mHUDStage.onResize(width, height);
	}

	@Override
	public void create() {
		mHUDStage.create();
		reset();
		mGameRenderer.create();
		GolfAudioManager.addSound(AudioPaths.CLICK);
		GolfAudioManager.addSound(AudioPaths.SHOT);
		GolfAudioManager.addSound(AudioPaths.SHOT_FROM_SAND);
		GolfAudioManager.addSound(AudioPaths.CONTACT_HOLE);
		GolfAudioManager.addSound(AudioPaths.CONTACT_GROUND);
	}

	/**
	 * Reloads the assets that change for each level or chapter of the game.
	 */
	void createSpecific() {
		mGameRenderer.createSpecific();

		mPathToLevelMusic = mLevelLogicModel.getGameMusicPath();
		GolfAudioManager.addMusic(mPathToLevelMusic);
		final int obstacleCount = mGameModel.getObstraclesContenerModel().getObstaclesCount();
		for (int index = 0; index < obstacleCount; index++) {
			final String soundPath = mGameModel.getObstraclesContenerModel().getObstacleModel(index)
					.getCollisionSoundPath();
			if (soundPath != null) {
				GolfAudioManager.addSound(soundPath);
			}
		}
		Assets.get().finishLoading();

	}

	/**
	 * Rolls back the changes made by {@link #createSpecific()}.
	 */
	void disposeSpecific() {

		mGameRenderer.disposeSpecific();

		GolfAudioManager.disposeMusic(mPathToLevelMusic);
		final int obstacleCount = mGameModel.getObstraclesContenerModel().getObstaclesCount();
		for (int index = 0; index < obstacleCount; index++) {
			final String soundPath = mGameModel.getObstraclesContenerModel().getObstacleModel(index)
					.getCollisionSoundPath();
			if (soundPath != null) {
				GolfAudioManager.disposeSound(soundPath);
			}
		}
	}

	@Override
	public void show() {

		mHUDStage.show();

		if (mNeedsRecreating) {
			createSpecific();
			mNeedsRecreating = false;
		}
		mGameRenderer.show();
		final String pathToMusicFile = mLevelLogicModel.getGameMusicPath();

		GolfAudioManager.playMusic(pathToMusicFile);
		GolfAudioManager.setLoopingMusic(pathToMusicFile, true);

		Gdx.input.setInputProcessor(mInputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Shows the in-game menu.
	 */
	public void showGameMenu() {
		final String pathToMusicFile = mLevelLogicModel.getGameMusicPath();
		GolfAudioManager.pauseMusic(pathToMusicFile);
		final GameMenuScreen screen = (GameMenuScreen) findScreen(ScreenType.SCREEN_GAME_MENU);
		screen.setParent(this);
		screen.setGameModel(mGameModel);
		setScreen(screen);
	}

	/**
	 * Hides the game menu, restores input processor and resumes the game.
	 */
	public void hideGameMenu() {
		resume();
		// restore the original input processor
		Gdx.input.setInputProcessor(mInputMultiplexer);
	}

	/**
	 * Show the club selection screen.
	 */
/*	public void showClubSelectionScreen() {
		final ClubSelectionScreen screen = (ClubSelectionScreen) findScreen(ScreenType.SCREEN_CLUB_SELECTION);
		screen.setParent(this);
		screen.setCurrentClubType(mGameModel.getClubModel().getClubType());
		setScreen(screen);
	}*/

	@Override
	public void hide() {
		GolfAudioManager.stopMusic(mLevelLogicModel.getGameMusicPath());//eu
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		if (mGameModel.getGameState() == GameState.RUNNING) {
			mGameModel.setGameState(GameState.PAUSED);
		}
	}

	@Override
	public void resume() {
		mGameModel.setGameState(GameState.RUNNING);
	}

	@Override
	public void dispose() {
		mGameRenderer.dispose();
		mHUDStage.dispose();
		GolfAudioManager.disposeSound(AudioPaths.CLICK);
		GolfAudioManager.disposeSound(AudioPaths.SHOT);
		GolfAudioManager.disposeSound(AudioPaths.SHOT_FROM_SAND);
		GolfAudioManager.disposeSound(AudioPaths.CONTACT_HOLE);
		GolfAudioManager.disposeSound(AudioPaths.CONTACT_GROUND);
	}

	/**
	 * Sets the club type.
	 * 
	 * @param pClubType
	 *            new {@link ClubType} instance
	 */
	public void setClubType(ClubType pClubType) {
		if (pClubType != null && !mGameModel.getClubModel().getClubType().equals(pClubType)) {
			mGameModel.getClubModel().setClubType(pClubType);
			mGameModel.getArrowModel().onClubTypeChanged(pClubType);
			mGameRenderer.onClubTypeChanged();
		}
	}

	/**
	 * Returns true if the shoot button should be disabled, false otherwise.
	 * 
	 * @return true if the shoot button should be disabled, false otherwise
	 */
	public boolean isDisableShootButton() {
		return mDisableShootButton;
	}

	/**
	 * Sets if the shoot button should be disabled.
	 * 
	 * @param pDisableShootButton
	 *            true if the shoot button should be disabled, false otherwise
	 */
	public void setDisableShootButton(boolean pDisableShootButton) {
		mDisableShootButton = pDisableShootButton;
	}
}
