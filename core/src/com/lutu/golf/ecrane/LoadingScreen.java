
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.lutu.golf.ScreenType;
import com.lutu.golf.utils.Assets;

/**
 * Shows a slowly rotating ball as the indeterminate loading screen.
 */
public class LoadingScreen extends AbstractTransparentScreen {

	private static final String TAG = LoadingScreen.class.getSimpleName();
	private static final boolean DEBUG_TABLE = false;
	private static final boolean DEBUG_STATISTICS = false;
	private static final float BG_ROTATION_SPEED = 150.0f;
//	private static final float BALL_ROTATION_SPEED = BG_ROTATION_SPEED * 2.0f;
	private static final String LOADING_BG = "mainmenu/quit_bg.png";
//	private static final String GOLF_BALL_SHADOW_PATH = "loading/golf_ball_shadow.png";
//	private static final String GOLF_BALL_PATH = "loading/golf_ball.png";
	private static final String GRASS_BG_PATH = "loading/grass_bg.png";
//	private static final float BALL_Y = 65.0f;

	private GolfScreen mNextScreen;
	private Image mGrassBg;
//	private Image mGolfBall;
//	private Image mGolfBallShadow;

	/**
	 * Constructs the {@link LoadingScreen} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public LoadingScreen(Game game, ScreenType type) {
		super(game, type);
	}

	/**
	 * Sets the next {@link GolfScreen} to be shown.
	 * 
	 * @param screen
	 *            {@link GolfScreen} instance that should be set after the {@link LoadingScreen} is hidden
	 */
	public void setNextScreen(GolfScreen screen) {
		mNextScreen = screen;
	}

	@Override
	public void create() {
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;
		Assets.get().load(LOADING_BG, Texture.class, param);
//		Assets.get().load(GOLF_BALL_PATH, Texture.class, param);
//		Assets.get().load(GOLF_BALL_SHADOW_PATH, Texture.class, param);
		Assets.get().load(GRASS_BG_PATH, Texture.class, param);
	}

	@Override
	protected void onShow() {
		super.onShow();
	}

	@Override
	void setScreen(GolfScreen screen) {
		mGame.setScreen(screen);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Assets.get().unload(LOADING_BG);
//		Assets.get().unload(GOLF_BALL_PATH);
//		Assets.get().unload(GOLF_BALL_SHADOW_PATH);
//		Assets.get().unload(GRASS_BG_PATH);
	}

	@Override
	protected void onHide() {
		super.onHide();

		getParent().getParentNode().disposeScreens(mNextScreen.getParentNode());

		if (DEBUG_STATISTICS) {
			Gdx.app.debug(TAG, "currently loaded assets: \n" + Assets.get().getDiagnostics());
		}
	}

	@Override
	void onRender(float delta) {
		super.onRender(delta);
		if (DEBUG_TABLE) {
			Table.drawDebug(getStage());
		}

		mGrassBg.rotate(BG_ROTATION_SPEED * delta);
//		mGolfBall.rotate(BALL_ROTATION_SPEED * delta);
//		mGolfBall.setPosition(-mGolfBall.getWidth() * 0.5f, -mGolfBall.getHeight() * 0.5f - BALL_Y);
//		mGolfBallShadow.setPosition(-mGolfBallShadow.getWidth() * 0.5f, -mGolfBallShadow.getHeight() * 0.5f - BALL_Y);

		if (Assets.get().update()) {
			setScreen(mNextScreen);
		}
	}

	@Override
	boolean onKeyUpClick(int keycode) {
		return false;
	}

	@Override
	boolean onKeyDownClick(int keycode) {
		return false;
	}

	@Override
	protected Actor onCreateLayout() {
		final Table root = new Table();
		root.setFillParent(true);
		root.setBackground(Assets.get().getTextureRegionDrawable(LOADING_BG));

		final WidgetGroup stack = new WidgetGroup();

		mGrassBg = new Image(Assets.get().get(GRASS_BG_PATH, Texture.class));
		mGrassBg.setOrigin(mGrassBg.getWidth() * 0.5f, mGrassBg.getHeight() * 0.5f);
		mGrassBg.translate(-mGrassBg.getWidth() * 0.5f, -mGrassBg.getHeight() * 0.5f);

//		mGolfBall = new Image(Assets.get().get(GOLF_BALL_PATH, Texture.class));
//		mGolfBall.setOrigin(mGolfBall.getWidth() * 0.5f, mGolfBall.getHeight() * 0.5f);
//		mGolfBall.setPosition(-mGolfBall.getWidth() * 0.5f, -mGolfBall.getHeight() * 0.5f - BALL_Y);
//
//		mGolfBallShadow = new Image(Assets.get().get(GOLF_BALL_SHADOW_PATH, Texture.class));
//		mGolfBallShadow.setOrigin(mGolfBallShadow.getWidth() * 0.5f, mGolfBallShadow.getHeight() * 0.5f);
//		mGolfBallShadow.setPosition(-mGolfBallShadow.getWidth() * 0.5f, -mGolfBallShadow.getHeight() * 0.5f - BALL_Y);
//
//		stack.addActor(mGolfBall);
//		stack.addActor(mGolfBallShadow);
		stack.addActor(mGrassBg);

		root.add(stack);

		if (DEBUG_TABLE) {
			root.debug();
		}

		return root;
	}
}
