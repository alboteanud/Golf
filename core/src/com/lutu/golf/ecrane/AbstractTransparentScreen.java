
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.lutu.golf.ScreenType;

/**
 * Base class for semi-transparent Screens displayed on top of a parent Screen.
 */
public abstract class AbstractTransparentScreen extends UiScreen implements InputProcessor {

	private com.lutu.golf.ecrane.GolfScreen mParent;

	/**
	 * Constructs {@link AbstractTransparentScreen}.
	 * 
	 * @param game
	 *            instance of {@link Game}
	 * @param parent
	 *            instance of {@link com.lutu.golf.ecrane.GolfScreen}
	 */
	AbstractTransparentScreen(Game game, ScreenType type) {
		super(game, type);
	}

	public void setParent(com.lutu.golf.ecrane.GolfScreen parent) {
		mParent = parent;
	}

	public com.lutu.golf.ecrane.GolfScreen getParent() {
		return mParent;
	}

	@Override
	public boolean keyDown(int keycode) {
		return onKeyDownClick(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		return onKeyUpClick(keycode);
	}

	/**
	 * Calls when some key is up.
	 * 
	 * @param keycode
	 *            code of button
	 * @return true if action is handled, otherwise false
	 */
	abstract boolean onKeyUpClick(int keycode);

	/**
	 * Calls when some key is down.
	 * 
	 * @param keycode
	 *            code of button
	 * @return true if action is handled, otherwise false
	 */
	abstract boolean onKeyDownClick(int keycode);

	@Override
	public boolean keyTyped(char character) {
		// Do nothing
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// Do nothing
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Do nothing
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// Do nothing
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// Do nothing
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// Do nothing
		return false;
	}

	@Override
	public void resize(int width, int height) {
		mParent.resize(width, height);
		super.resize(width, height);
	}

	@Override
	void onRender(float delta) {
		if (!equals(mParent)) {
			mParent.onRender(delta);
		}
		super.onRender(delta);
	}

	@Override
	protected void onShow() {
		getInputMultiplexer().addProcessor(this);
	}

	@Override
	protected void onHide() {
		getInputMultiplexer().removeProcessor(this);
	}

	/**
	 * Shows the parent screen.
	 */
	protected void showParent() {
		setScreen(mParent);
	}
}
