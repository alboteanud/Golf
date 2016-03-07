
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lutu.golf.controlere.GameController;
import com.lutu.golf.controlere.GameController.OnBallStoppedListener;
import com.lutu.golf.controlere.animation.FollowAnimation;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Class which represents Game HUD.
 *
 */
public class HUDStage extends Stage {

	/** UI stage height used to position all its elements. */
	private static final float LAYOUT_HEIGHT = 480.0f;

	private static final float LAYOUT_BUTTON_PADDING = 25.0f;

	private static final float LABEL_BG_SIZE_WIDTH = 116.0f;
	private static final float LABEL_BG_SIZE_HEIGHT = 40.0f;
	private static final float LABEL_PAD_RIGHT = 20.0f;
	private static final float LABEL_BG_PAD_BOTTOM = 5.0f;

	private static final String LABEL_FONT_COLOR = "efefef";	//"2b2600";

	private static final String BUTTON_PAUSE_PATH = "gamescreen/pause_button_up.png";
	private static final String BUTTON_PAUSE_PRESS_PATH = "gamescreen/pause_button_down.png";
	private static final String BUTTON_SHOOT_PATH = "gamescreen/button_shoot.png";
	private static final String BUTTON_SHOOT_PRESSED_PATH = "gamescreen/button_shoot_pressed.png";
	private static final String BUTTON_SHOOT_DISABLED_PATH = "gamescreen/button_shoot_disabled.png";
	private static final String BUTTON_FOLLOW_PATH = "gamescreen/button_follow.png";
	private static final String BUTTON_FOLLOW_PRESSED_PATH = "gamescreen/button_follow_pressed.png";
	private static final String BUTTON_CLUB_SELECTION_PATH = "gamescreen/button_club_selection.png";
	private static final String PAR_BG = "gamescreen/par_bg.png";

	private static final String HITS_BG = "lovituri_fundal.png";
	private static final String FONT_PATH = "levelsmenu/font.fnt";
	private final GameScreen mParent;
	private GameController mGameController;
	private Table mTable;
	private final OrthographicCamera mStageCamera;
	private GameModel mGameModel;

	/**
	 * Construct {@link HUDStage}.
	 *
	 * @param parent
	 *            instance of {@link GameScreen}
	 */
	public HUDStage(GameScreen parent) {
		mParent = parent;
		mStageCamera = new OrthographicCamera();
	}

	public void setGameModel(GameModel model) {
		mGameModel = model;
	}

	public void setGameController(GameController controller) {
		mGameController = controller;
	}

	/**
	 * Called when the screen hosting the {@link HUDStage} is shown.
	 */
	public void show() {
		mTable = new Table();
		mTable.setFillParent(true);
		mTable.padTop(LAYOUT_BUTTON_PADDING).padBottom(LAYOUT_BUTTON_PADDING).padRight(LAYOUT_BUTTON_PADDING);

		createGameStatusLayout();
//		initializeButtons();

		clear();
		addActor(mTable);
	}

	/**
	 * Called whenever the {@link HUDStage} should declare the assets to load. Calling {@link Assets#finishLoading()} is
	 * not necessary.
	 */
	public void create() {
		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;

/*		Assets.get().load(BUTTON_PAUSE_PATH, Texture.class, param);
		Assets.get().load(BUTTON_PAUSE_PRESS_PATH, Texture.class, param);
		Assets.get().load(BUTTON_SHOOT_PATH, Texture.class, param);
		Assets.get().load(BUTTON_SHOOT_PRESSED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_SHOOT_DISABLED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_FOLLOW_PATH, Texture.class, param);
		Assets.get().load(BUTTON_FOLLOW_PRESSED_PATH, Texture.class, param);
		Assets.get().load(BUTTON_CLUB_SELECTION_PATH, Texture.class, param);
		Assets.get().load(PAR_BG, Texture.class, param);*/
		Assets.get().load(HITS_BG, Texture.class, param);

		// Load the font
		final BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.minFitler = TextureFilter.Linear;
		fontParam.maxFilter = TextureFilter.Linear;
		Assets.get().load(FONT_PATH, BitmapFont.class, fontParam);
	}

	@Override
	public void dispose() {
/*		Assets.get().unload(BUTTON_PAUSE_PATH);
		Assets.get().unload(BUTTON_PAUSE_PRESS_PATH);
		Assets.get().unload(BUTTON_SHOOT_PATH);
		Assets.get().unload(BUTTON_SHOOT_PRESSED_PATH);
		Assets.get().unload(BUTTON_SHOOT_DISABLED_PATH);
		Assets.get().unload(BUTTON_FOLLOW_PATH);
		Assets.get().unload(BUTTON_FOLLOW_PRESSED_PATH);
		Assets.get().unload(BUTTON_CLUB_SELECTION_PATH);
		Assets.get().unload(PAR_BG);*/
		Assets.get().unload(HITS_BG);
		Assets.get().unload(FONT_PATH);
	}

	private void initializeButtons() {
		final Button pauseButton = new Button(Assets.get().getTextureRegionDrawable(BUTTON_PAUSE_PATH), Assets.get()
				.getTextureRegionDrawable(BUTTON_PAUSE_PRESS_PATH));
		final ButtonStyle shootButtonStyle = new ButtonStyle();
		shootButtonStyle.up = Assets.get().getTextureRegionDrawable(BUTTON_SHOOT_PATH);
		shootButtonStyle.down = Assets.get().getTextureRegionDrawable(BUTTON_SHOOT_PRESSED_PATH);
		shootButtonStyle.disabled = Assets.get().getTextureRegionDrawable(BUTTON_SHOOT_DISABLED_PATH);
		final Button shootButton = new Button(shootButtonStyle);
		shootButton.setDisabled(mParent.isDisableShootButton());
		mGameController.setOnBallStoppedListener(new OnBallStoppedListener() {

			@Override
			public void onBallStopped() {
				mParent.setDisableShootButton(false);
				shootButton.setDisabled(mParent.isDisableShootButton());
			}
		});
		final Button followButton = new Button(Assets.get().getTextureRegionDrawable(BUTTON_FOLLOW_PATH), Assets.get()
				.getTextureRegionDrawable(BUTTON_FOLLOW_PRESSED_PATH));
		final ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.up = Assets.get().getTextureRegionDrawable(BUTTON_CLUB_SELECTION_PATH);
		final Button clubSelectionButton = new Button(buttonStyle);
		final ClickListener clickListener = new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				final Button actor = (Button) event.getListenerActor();
				if (!shootButton.equals(actor)) {
					GolfAudioManager.playSound(AudioPaths.CLICK);
				}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Actor actor = event.getListenerActor();
				if (shootButton.equals(actor)) {
					if (!shootButton.isDisabled()) {
						if (mGameModel.getBallModel().isOnSlowdownObstacle()) {
							GolfAudioManager.playSound(AudioPaths.SHOT_FROM_SAND);
						} else {
							GolfAudioManager.playSound(AudioPaths.SHOT);
						}
					}

					mGameModel.onShootButtonClicked(true);
					mParent.setDisableShootButton(true);
					shootButton.setDisabled(mParent.isDisableShootButton());
				} else if (clubSelectionButton.equals(actor)) {
//					mParent.showClubSelectionScreen();
				} else if (followButton.equals(actor)) {
					mGameController.getCameraController().setAnimation(new FollowAnimation(mGameModel.getBallModel()));
					mGameController.getCameraController().startAnimation();
				} else if (pauseButton.equals(actor)) {
					mParent.showGameMenu();
				}
			}
		};

		shootButton.addListener(clickListener);
		clubSelectionButton.addListener(clickListener);
		clubSelectionButton.setChecked(false);
		followButton.addListener(clickListener);
		pauseButton.addListener(clickListener);

		mTable.add(pauseButton).right().top();
		mTable.row();
		mTable.add(shootButton).bottom().expand().left().padLeft(LAYOUT_BUTTON_PADDING);
		mTable.add(followButton).bottom().left().padLeft(LAYOUT_BUTTON_PADDING);
		mTable.add(clubSelectionButton).expand().bottom().right();
	}

	/**
	 * Should be called in {@link GameScreen#resize(int, int)}.
	 *
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 */
	public void onResize(int width, int height) {
		setViewport((float) width / height * LAYOUT_HEIGHT, LAYOUT_HEIGHT, false);
		mStageCamera.viewportHeight = LAYOUT_HEIGHT;
		mStageCamera.viewportWidth = (float) width / height * mStageCamera.viewportHeight;
		mStageCamera.update();
	}

	@Override
	public boolean keyDown(int keyCode) {
		switch (keyCode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			return true;
		default:
			return super.keyDown(keyCode);
		}
	}

	@Override
	public boolean keyUp(int keyCode) {
		switch (keyCode) {
		case Keys.BACK:
		case Keys.ESCAPE:
			mParent.showGameMenu();
			return true;
		default:
			return super.keyUp(keyCode);
		}
	}

	private void createGameStatusLayout() {
		final LabelStyle labelStyle = new LabelStyle((BitmapFont) Assets.get().get(FONT_PATH), Color.BLUE);

		labelStyle.fontColor = Color.valueOf(LABEL_FONT_COLOR);
		final Table hitTitleLabel = new Table();
		hitTitleLabel.setBackground(Assets.get().getTextureRegionDrawable(HITS_BG));
		final Label hitsLabel = new Label("", labelStyle) {
			@Override
			public void act(float delta) {
				super.act(delta);
				setText(String.valueOf(mGameModel.getHits()));
			}
		};

		final Table parTitleLabel = new Table();
/*		parTitleLabel.setBackground(Assets.get().getTextureRegionDrawable(PAR_BG));
		final Label parLabel = new Label("", labelStyle) {
			@Override
			public void act(float delta) {
				super.act(delta);
				setText(String.valueOf(mGameModel.getHits() - mGameModel.getPar()));
			}
		};*/

		final Table result = new Table();
		result.add(hitTitleLabel).size(LABEL_BG_SIZE_WIDTH, LABEL_BG_SIZE_HEIGHT).padBottom(LABEL_BG_PAD_BOTTOM).left();
		hitTitleLabel.add(hitsLabel).padRight(LABEL_PAD_RIGHT).expand().right();
		result.row();
//		result.add(parTitleLabel).size(LABEL_BG_SIZE_WIDTH, LABEL_BG_SIZE_HEIGHT).left();
//		parTitleLabel.add(parLabel).padRight(LABEL_PAD_RIGHT).expand().right();
		mTable.add(result).top().left().expand().colspan(2);

	}
}
