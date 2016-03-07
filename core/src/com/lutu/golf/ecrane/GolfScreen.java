
package com.lutu.golf.ecrane;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.lutu.golf.ScreenGraph;
import com.lutu.golf.ScreenType;

/**
 * Base class for all golf game screens.
 */
public abstract class GolfScreen implements Screen {

	/** Common {@link Game} instance. */
	final Game mGame;

	private final Set<ScreenGraph.Node> mParentNodes = new HashSet<ScreenGraph.Node>();

	private final ScreenType mType;

	private ScreenGraph.Node mParentNode;

	/**
	 * Constructs the {@link GolfScreen}.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public GolfScreen(Game game, ScreenType type) {
		mGame = game;
		mType = type;
	}

	/**
	 * This method should perform the actual rendering.
	 * 
	 * @param delta
	 *            time since last frame
	 */
	abstract void onRender(float delta);

	/**
	 * Use this method to load the necessary assets.
	 * 
	 * <p>
	 * Results of calling this method should be reverted by {@link #dispose()}.
	 * </p>
	 */
	public abstract void create();

	public ScreenGraph.Node getParentNode() {
		return mParentNode;
	}

	public void setParentNode(ScreenGraph.Node node) {
		mParentNode = node;
	}

	public ScreenType getType() {
		return mType;
	}

	/**
	 * Transitions to another {@link GolfScreen}.
	 * 
	 * @param screenType
	 *            {@link GolfScreen} defined by {@link ScreenType} to be shown
	 */
	void setScreen(ScreenType screenType) {
		final GolfScreen screen = findScreen(screenType);
		setScreen(screen);
	}

	/**
	 * Transitions to another {@link GolfScreen}.
	 * 
	 * @param screen
	 *            {@link GolfScreen} to be shown
	 */
	void setScreen(GolfScreen screen) {
		GolfScreen nextScreen = screen;

		if (!getParentNode().equals(screen.getParentNode())) {
			// We are switching to another Node, let's show a loading screen.

			screen.getParentNode().createScreens();

			final LoadingScreen loadingScreen = (LoadingScreen) findScreen(ScreenType.SCREEN_LOADING);
			loadingScreen.setNextScreen(screen);
			loadingScreen.setParent(this);

			nextScreen = loadingScreen;
		}

		mGame.setScreen(nextScreen);
	}

	GolfScreen findScreen(ScreenType screenType) {
		if (mType.equals(screenType)) {
			return this;
		}

		final GolfScreen screen = mParentNode.find(screenType);
		if (screen == null) {
			throw new IllegalArgumentException("could not find a GolfScreen of the specified screenType: " + screenType);
		}

		return screen;
	}
}
