
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lutu.golf.ScreenType;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * A common {@link GolfScreen} implementation that holds a {@link Stage} and performs the necessary scaling operations
 * on it.
 * 
 * <p>
 * Remember not to use {@link com.badlogic.gdx.Input#setInputProcessor(com.badlogic.gdx.InputProcessor)
 * Gdx.input.setInputProcessor(com.badlogic.gdx.InputProcessor)} directly as this class does it on its own in
 * {@link #show()} and {@link #hide()} methods. Use the {@link UiScreen#getInputMultiplexer()} instead.
 * </p>
 */
public abstract class UiScreen extends GolfScreen {

	/** UI stage height used to position all its elements. */
	protected static final float LAYOUT_HEIGHT = 480.0f;

	private final Stage mStage;

	/** {@link OrthographicCamera} used to render the {@link Stage}. */
	private final OrthographicCamera mStageCamera;

	private final InputMultiplexer mInputMultiplexer;

	private Image mBackground;

	private Actor mLayout;
	private boolean mLayoutCreated = false;

	/**
	 * Constructs the {@link UiScreen} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public UiScreen(Game game, ScreenType type) {
		super(game, type);

		mStage = new Stage();
		mStageCamera = new OrthographicCamera();

		mStage.setCamera(mStageCamera);

		mInputMultiplexer = new InputMultiplexer();
		mInputMultiplexer.addProcessor(mStage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		mStage.act(delta);
		onRender(delta);
	}

	@Override
	void onRender(float delta) {
		mStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// Set up the Stage to render properly.

		mStage.setViewport((float) width / height * LAYOUT_HEIGHT, LAYOUT_HEIGHT, false);
		mStageCamera.viewportHeight = LAYOUT_HEIGHT;
		mStageCamera.viewportWidth = (float) width / height * mStageCamera.viewportHeight;
		mStageCamera.update();

		if (mBackground != null) {
			// Align the background.

			final float xOffset = (-mBackground.getWidth() + getStage().getWidth()) * 0.5f;
			mBackground.setX(xOffset);
			mBackground.invalidate();
		}
	}

	@Override
	public void show() {
		if (!isLayoutCreated()) {
			// Makes sure that the layout is created just once

			mLayout = onCreateLayout();

			if (mLayout != null) {
				mStage.clear();
				if (mBackground != null) {
					mStage.addActor(mBackground);
				}
				mStage.addActor(mLayout);
			}
			mLayoutCreated = true;
		}

		onShow();

		Gdx.input.setInputProcessor(mInputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void create() {
		GolfAudioManager.addSound(AudioPaths.CLICK);
	}

	@Override
	public void dispose() {
		mLayoutCreated = false;
		GolfAudioManager.disposeSound(AudioPaths.CLICK);

	}

	boolean isLayoutCreated() {
		return mLayoutCreated;
	}

	@Override
	public void hide() {
		onHide();
		Gdx.input.setCatchBackKey(false);
		Gdx.input.setInputProcessor(null);
	}

	/**
	 * Sets the background of this {@link com.badlogic.gdx.Screen}.
	 * 
	 * @param backgroundImage
	 *            the {@link Image} set as the background
	 */
	protected void setBackground(Image backgroundImage) {
		if (mBackground != null && mBackground.equals(backgroundImage)) {
			return;
		}

		if (mBackground != null) {
			mBackground.remove();
		}

		mBackground = backgroundImage;

		if (mBackground != null) {
			mBackground
					.setSize(backgroundImage.getWidth() / backgroundImage.getHeight() * LAYOUT_HEIGHT, LAYOUT_HEIGHT);
			mStage.addActor(mBackground);
		}
	}

	/**
	 * Gets the {@link Stage} instance.
	 * 
	 * @return {@link Stage} instance
	 */
	protected Stage getStage() {
		return mStage;

	}

	/**
	 * Gets the {@link InputMultiplexer} instance.
	 * 
	 * @return {@link InputMultiplexer} instance.
	 */
	protected InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}

	/**
	 * Fills the {@link Stage} with the {@link Actor Actors} hierarchy. If the method returns null then no layout
	 * hierarchy will be created.
	 * 
	 * @return {@link Actor} containing the layout hierarchy
	 */
	protected abstract Actor onCreateLayout();

	/**
	 * Called when the {@link com.badlogic.gdx.Screen} is shown.
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	protected abstract void onShow();

	/**
	 * Called when the {@link com.badlogic.gdx.Screen} gets hidden.
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	protected abstract void onHide();
}
