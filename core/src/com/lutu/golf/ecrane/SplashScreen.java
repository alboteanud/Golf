
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lutu.golf.ScreenType;
import com.lutu.golf.controlere.XmlGolfGameReader;
import com.lutu.golf.models.gamelogic.ChapterLogicModel;
import com.lutu.golf.models.gamelogic.GameLogicModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.GolfAudioManager;
import com.lutu.golf.utils.GolfPreferences;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents splash screen of the game.
 */
public class SplashScreen extends UiScreen {

	private static final float SPLASH_TIME = 2.0f; //3.0f;
	private static final String SPLASH_IMAGE_PATH = "splash.png";
	private float mTime;
	private com.lutu.golf.ecrane.GolfScreen mNextScreen;
	private GameLogicModel mGameLogicModel; //eu

	/**
	 * Constructor.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public SplashScreen(Game game, ScreenType type) {
		super(game, type);

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void create() {
		super.create();
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;

		Assets.get().load(SPLASH_IMAGE_PATH, Texture.class, param);
		Assets.get().finishLoading();

		final Image background = new Image(Assets.get().get(SPLASH_IMAGE_PATH, Texture.class));
		setBackground(background);

		mGameLogicModel=new GameLogicModel();  //eu
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.get().unload(SPLASH_IMAGE_PATH);
	}

	@Override
	protected Actor onCreateLayout() {
		return null;
	}

	@Override
	void onRender(float delta) {
		super.onRender(delta);

		mTime += delta;

		if (Assets.get().update() && (Gdx.input.justTouched() || mTime > SPLASH_TIME)) {

			// All assets loaded
			mGame.setScreen(mNextScreen);
		}
	}

	@Override
	protected void onShow() {
		GolfAudioManager.setAndSwitchMusicsOn(GolfPreferences.INSTANCE.isMusicOn());
		GolfAudioManager.setSoundsOn(GolfPreferences.INSTANCE.isSoundOn());

		// mNextScreen = findScreen(ScreenType.SCREEN_MAIN_MENU);
		// mNextScreen.getParentNode().createScreens();

		//eu
		XmlGolfGameReader.fillGameModel(mGameLogicModel);
		final List<ChapterLogicModel> chapterLogicModels = new LinkedList<ChapterLogicModel>();
		final int size = mGameLogicModel.getChapterCount();
		for (int index = 0; index < size; index++)
			chapterLogicModels.add(mGameLogicModel.getChapterAt(index));
		final com.lutu.golf.ecrane.LevelSelectionScreen screen = (com.lutu.golf.ecrane.LevelSelectionScreen) findScreen(ScreenType.SCREEN_LEVEL_SELECTION);
		screen.setChapterLogicModel(chapterLogicModels.get(2));   // 0= cap1   1=cap2
		mNextScreen=screen;
		mNextScreen.getParentNode().createScreens();
	}

	@Override
	protected void onHide() {
		dispose();
	}
}
