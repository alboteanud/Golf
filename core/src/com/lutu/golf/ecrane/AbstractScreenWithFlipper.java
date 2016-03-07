
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lutu.golf.ScreenType;
import com.lutu.golf.framework.AbstractFlipperViewAdapter;
import com.lutu.golf.framework.FlipperView;
import com.lutu.golf.framework.FlipperView.OnFlippingFinishListener;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Abstract screen which contains FlipperView.
 * 
 * 
 * @param <A>
 *            see {@link AbstractFlipperViewAdapter}
 * @param <T>
 *            see {@link FlipperView}
 * @param <V>
 *            see {@link FlipperView}
 */
public abstract class AbstractScreenWithFlipper<A extends AbstractFlipperViewAdapter<T, V>, T extends Actor, V> extends
		UiScreen implements InputProcessor {
	// Background
	private static final String BACKGROUND = "levelsmenu/menu_bg.png";

	// Next button
	private static final String LEVELS_NEXT_BUTTON_DOWN = "levelsmenu/levels_next_button_down.png";
	private static final String LEVELS_NEXT_BUTTON_UP = "levelsmenu/levels_next_button_up.png";
	private static final String LEVELS_NEXT_BUTTON_DISABLE = "levelsmenu/levels_next_button_disable.png";
	// Previous button
	private static final String LEVELS_PREV_BUTTON_DOWN = "levelsmenu/levels_prev_button_down.png";
	private static final String LEVELS_PREV_BUTTON_UP = "levelsmenu/levels_prev_button_up.png";
	private static final String LEVELS_PREV_BUTTON_DISABLE = "levelsmenu/levels_prev_button_disable.png";

	private static final float BUTTON_SIZE = 80f;
	private static final float BUTTON_PADDING = 30f;

	private static final float TITLE_VIEW_HEIGHT = 60f;
	private static final float TITLE_VIEW_TOP_PADDING = 10f;
	private Stage mBackgroundStage;

	private int mIndexOfSelectedView = -1;

	/**
	 * Constructs instance of {@link AbstractScreenWithFlipper}.
	 * 
	 * @param game
	 *            instance of {@link Game}
	 */
	AbstractScreenWithFlipper(Game game, ScreenType screenType) {
		super(game, screenType);
	}

	@Override
	protected void onRender(float delta) {
		mBackgroundStage.act();
		mBackgroundStage.draw();
		super.onRender(delta);

	}

	/**
	 * Sets index of selected page.
	 * 
	 * @param index
	 *            index of selected page
	 */
	public void setIndexOfSelectedPage(int index) {
		mIndexOfSelectedView = index;
	}

	@Override
	public void resize(int width, int height) {
		mBackgroundStage.setViewport((float) width / height * LAYOUT_HEIGHT, LAYOUT_HEIGHT, false);
		getStage().getCamera().viewportHeight = LAYOUT_HEIGHT;
		getStage().getCamera().viewportWidth = (float) width / height * getStage().getCamera().viewportHeight;
		getStage().getCamera().update();
		super.resize(width, height);
	}

	/**
	 * Returns instance of {@link AbstractFlipperViewAdapter}.
	 * 
	 * @return instance of {@link AbstractFlipperViewAdapter}
	 */
	abstract A getFlipperAdapter();

	/**
	 * Returns title view. It can be null.
	 * 
	 * @return title view
	 */
	abstract Actor prepareTitleView();

	@Override
	public void create() {
		super.create();
		Assets.get().load(BACKGROUND, Texture.class);
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;
		Assets.get().load(LEVELS_NEXT_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(LEVELS_NEXT_BUTTON_UP, Texture.class, param);
		Assets.get().load(LEVELS_NEXT_BUTTON_DISABLE, Texture.class, param);
		Assets.get().load(LEVELS_PREV_BUTTON_UP, Texture.class, param);
		Assets.get().load(LEVELS_PREV_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(LEVELS_PREV_BUTTON_DISABLE, Texture.class, param);
	}

	private Stage prepareBackgroundStage() {
		final Stage result = new Stage();
		final Image image = new Image(Assets.get().getTextureRegionDrawable(BACKGROUND));
		image.setFillParent(true);
		image.setSize(image.getWidth() / image.getHeight() * LAYOUT_HEIGHT, LAYOUT_HEIGHT);
		result.setCamera(getStage().getCamera());
		result.addActor(image);
		return result;
	}

	@Override
	protected Actor onCreateLayout() {

		final Stack mainLayoutWidget = new Stack();
		mainLayoutWidget.setFillParent(true);

		final FlipperView<AbstractFlipperViewAdapter<T, V>, T, V> flipper = prepareFlipper();
		final Table menuHudTable = prepareHudTable(flipper);
		mainLayoutWidget.add(flipper);
		mainLayoutWidget.add(menuHudTable);
		return mainLayoutWidget;
	}

	private FlipperView<AbstractFlipperViewAdapter<T, V>, T, V> prepareFlipper() {
		// @formatter:off
		final AbstractFlipperViewAdapter<T, V> adapter = getFlipperAdapter();
		
		final FlipperView<AbstractFlipperViewAdapter<T, V>, T, V> flipper = new FlipperView<AbstractFlipperViewAdapter<T, V>, T, V>(
				adapter);
		// @formatter:on
		if (mIndexOfSelectedView != -1) {
			adapter.setCurrentIndex(mIndexOfSelectedView);
			flipper.setPageAt(mIndexOfSelectedView);
			mIndexOfSelectedView = -1;
		}
		flipper.setFillParent(true);
		return flipper;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	private Table prepareHudTable(final FlipperView<AbstractFlipperViewAdapter<T, V>, T, V> flipper) {
		final Table result = new Table();
		result.setFillParent(true);
		final ButtonStyle nextBtnStyle = new ButtonStyle();
		nextBtnStyle.down = Assets.get().getTextureRegionDrawable(LEVELS_NEXT_BUTTON_DOWN);
		nextBtnStyle.up = Assets.get().getTextureRegionDrawable(LEVELS_NEXT_BUTTON_UP);
		nextBtnStyle.disabled = Assets.get().getTextureRegionDrawable(LEVELS_NEXT_BUTTON_DISABLE);
		final Button nextPage = new Button(nextBtnStyle);
		final ButtonStyle prevBtnStyle = new ButtonStyle();
		prevBtnStyle.down = Assets.get().getTextureRegionDrawable(LEVELS_PREV_BUTTON_DOWN);
		prevBtnStyle.up = Assets.get().getTextureRegionDrawable(LEVELS_PREV_BUTTON_UP);
		prevBtnStyle.disabled = Assets.get().getTextureRegionDrawable(LEVELS_PREV_BUTTON_DISABLE);
		final Button prevPage = new Button(prevBtnStyle);
		final ClickListener clickListener = new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				final Actor buttonSelected = event.getListenerActor();
				if (!((Button) buttonSelected).isDisabled()) {
					GolfAudioManager.playSound(AudioPaths.CLICK);
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Button button = (Button) event.getListenerActor();
				if (button.equals(nextPage) && !button.isDisabled()) {
					button.setDisabled(true);
					flipper.flippingRight();
				} else if (button.equals(prevPage)) {
					button.setDisabled(true);
					flipper.flippingLeft();
				}
			}

		};

		setButtonsEnabled(flipper.getFlipperAdapter(), prevPage, nextPage);

		flipper.setOnFlippingFinishListener(new OnFlippingFinishListener() {

			@Override
			public void onFlippingFinish() {
				setButtonsEnabled(flipper.getFlipperAdapter(), prevPage, nextPage);
			}
		});
		nextPage.addListener(clickListener);
		prevPage.addListener(clickListener);

		final Actor titleView = prepareTitleView();
		float buttonsPadding = 0.0f;
		if (titleView != null) {
			result.add(titleView).fillX().height(TITLE_VIEW_HEIGHT).top().padTop(TITLE_VIEW_TOP_PADDING).colspan(2);
			buttonsPadding = TITLE_VIEW_HEIGHT + TITLE_VIEW_TOP_PADDING;
		}
		result.row().expand().center().padTop(-buttonsPadding);
		result.add(prevPage).size(BUTTON_SIZE, BUTTON_SIZE).padLeft(BUTTON_PADDING).left();
		result.add(nextPage).size(BUTTON_SIZE, BUTTON_SIZE).padRight(BUTTON_PADDING).right();
		return result;
	}

	private void setButtonsEnabled(AbstractFlipperViewAdapter<T, V> adapter, Button prevPageBtn, Button nextPageBtn) {
		final int currentIndex = adapter.getCurrentIndex();
		final int countValue = adapter.getValueCount();
		prevPageBtn.setDisabled(currentIndex == 0);
		nextPageBtn.setDisabled(currentIndex == countValue - 1);
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	protected void onShow() {
		mBackgroundStage = prepareBackgroundStage();
		getInputMultiplexer().addProcessor(this);
	}

	@Override
	protected void onHide() {
		getInputMultiplexer().removeProcessor(this);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		super.dispose();

		Assets.get().unload(BACKGROUND);

		Assets.get().unload(LEVELS_NEXT_BUTTON_DOWN);
		Assets.get().unload(LEVELS_NEXT_BUTTON_UP);
		Assets.get().unload(LEVELS_NEXT_BUTTON_DISABLE);
		Assets.get().unload(LEVELS_PREV_BUTTON_UP);
		Assets.get().unload(LEVELS_PREV_BUTTON_DOWN);
		Assets.get().unload(LEVELS_PREV_BUTTON_DISABLE);
	}

}
