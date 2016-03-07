package com.lutu.golf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
//import com.lutu.golf.ecrane.AboutScreen;
//import com.lutu.golf.ecrane.ChapterSelectionScreen;
//import com.lutu.golf.ecrane.ClubSelectionScreen;
import com.lutu.golf.ecrane.EndLevelDialog;
import com.lutu.golf.ecrane.GameMenuScreen;
import com.lutu.golf.ecrane.GameScreen;
import com.lutu.golf.ecrane.GolfScreen;
//import com.lutu.golf.ecrane.HelpScreen;
import com.lutu.golf.ecrane.LevelSelectionScreen;
import com.lutu.golf.ecrane.LoadingScreen;
//import com.lutu.golf.ecrane.MainMenuScreen;
//import com.lutu.golf.ecrane.OptionsScreen;
import com.lutu.golf.ecrane.QuitDialog;
import com.lutu.golf.ecrane.SplashScreen;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.GolfAudioManager;

public class GolfGame extends Game {
	private ActionResolver actionResolver;

	public GolfGame(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;

	}

	@Override
	public void create() {

		// Create the screen flow graph

		final GolfScreen splashScreen = new SplashScreen(this, ScreenType.SCREEN_SPLASH);
//		final GolfScreen mainMenuScreen = new MainMenuScreen(this, ScreenType.SCREEN_MAIN_MENU);
//		final GolfScreen helpScreen = new HelpScreen(this, ScreenType.SCREEN_HELP);
//		final GolfScreen optionsScreen = new OptionsScreen(this, ScreenType.SCREEN_OPTIONS);
//		final GolfScreen aboutScreen = new AboutScreen(this, ScreenType.SCREEN_ABOUT);
		final GolfScreen quitDialog = new QuitDialog(this, ScreenType.SCREEN_QUIT_DIALOG);
//		final GolfScreen chapterSelectionScreen = new ChapterSelectionScreen(this, ScreenType.SCREEN_CHAPTER_SELECTION);
		final GolfScreen loadingScreen = new LoadingScreen(this, ScreenType.SCREEN_LOADING);
		final GolfScreen levelSelectionScreen = new LevelSelectionScreen(this, ScreenType.SCREEN_LEVEL_SELECTION);
		final GolfScreen gameScreen = new GameScreen(this, ScreenType.SCREEN_GAME);
		final GolfScreen gameMenuScreen = new GameMenuScreen(this, ScreenType.SCREEN_GAME_MENU);
		final GolfScreen endLevelDialog = new EndLevelDialog(this, ScreenType.SCREEN_END_LEVEL_DIALOG, actionResolver);
//		final GolfScreen clubSelectionScreen = new ClubSelectionScreen(this, ScreenType.SCREEN_CLUB_SELECTION);

		final ScreenGraph graph = new ScreenGraph();
		graph.add(loadingScreen);

		final ScreenGraph.Node splashNode = graph.createNode();
		splashNode.add(splashScreen);

//		final ScreenGraph.Node mainNode = graph.createNode();
//		mainNode.add(mainMenuScreen);
//		mainNode.add(helpScreen);
//		mainNode.add(optionsScreen);
//		mainNode.add(aboutScreen);
//		mainNode.add(quitDialog);
//		mainNode.add(chapterSelectionScreen);

		final ScreenGraph.Node levelGroup = graph.createNode();
		levelGroup.add(levelSelectionScreen);

		final ScreenGraph.Node gameGroup = graph.createNode();
		gameGroup.add(gameScreen);
		gameGroup.add(gameMenuScreen);
		gameGroup.add(endLevelDialog);
//		gameGroup.add(clubSelectionScreen);

		// Enable game-wide logging

		Gdx.app.setLogLevel(Logger.ERROR);

		splashScreen.create();
		loadingScreen.create();
		setScreen(splashScreen);

//        actionResolver.showInterstital();            //first interstitial load
	}


    public void showInterrst(){

    }

	@Override
	public void dispose() {
		super.dispose();

		GolfAudioManager.clearAudioRes();
		Assets.get().clear();
	}
}
