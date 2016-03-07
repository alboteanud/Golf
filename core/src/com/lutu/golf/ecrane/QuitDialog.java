
package com.lutu.golf.ecrane;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lutu.golf.ScreenType;
import com.lutu.golf.utils.Assets;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * An {@link AbstractTransparentScreen} implementation that shows a confirmation dialog.
 */
public class QuitDialog extends AbstractTransparentScreen {

	private static final boolean DEBUG_TABLE = false;
	private static final String QUIT_BG = "mainmenu/quit_bg.png";
	private static final String QUIT_DIALOG_BG = "mainmenu/quit.png";
	private static final String QUIT_OK_BUTTON_UP = "mainmenu/quit_ok_button_up.png";
	private static final String QUIT_OK_BUTTON_DOWN = "mainmenu/quit_ok_button_down.png";
	private static final String QUIT_CANCEL_BUTTON_UP = "mainmenu/quit_cancel_button_up.png";
	private static final String QUIT_CANCEL_BUTTON_DOWN = "mainmenu/quit_cancel_button_down.png";

	private static final int DIALOG_HEIGHT = 240;
	private static final int DIALOG_PADDING_BOTTOM = 125;
	private static final int BUTTON_SIZE = 145;
	private static final int BUTTON_SIDE_PADDING = 42;

	/**
	 * Constructs the {@link QuitDialog} instance.
	 * 
	 * @param game
	 *            {@link Game} instance
	 * @param type
	 *            {@link ScreenType} of this screen
	 */
	public QuitDialog(Game game, ScreenType type) {
		super(game, type);
	}

	@Override
	void onRender(float delta) {
		super.onRender(delta);
		if (DEBUG_TABLE) {
			Table.drawDebug(getStage());
		}
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

		Assets.get().unload(QUIT_BG);
		Assets.get().unload(QUIT_DIALOG_BG);
		Assets.get().unload(QUIT_OK_BUTTON_UP);
		Assets.get().unload(QUIT_OK_BUTTON_DOWN);
		Assets.get().unload(QUIT_CANCEL_BUTTON_UP);
		Assets.get().unload(QUIT_CANCEL_BUTTON_DOWN);
	}

	@Override
	boolean onKeyUpClick(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			showParent();
			return true;
		}
		return false;
	}



	@Override
	boolean onKeyDownClick(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			return true;
		}
		return false;
	}

	@Override
	public void create() {
		super.create();

		final TextureParameter param = new TextureParameter();
		param.minFilter = TextureFilter.MipMapLinearLinear;
		param.magFilter = TextureFilter.Linear;
		param.genMipMaps = true;
		Assets.get().load(QUIT_BG, Texture.class);
		Assets.get().load(QUIT_DIALOG_BG, Texture.class, param);
		Assets.get().load(QUIT_OK_BUTTON_UP, Texture.class, param);
		Assets.get().load(QUIT_OK_BUTTON_DOWN, Texture.class, param);
		Assets.get().load(QUIT_CANCEL_BUTTON_UP, Texture.class, param);
		Assets.get().load(QUIT_CANCEL_BUTTON_DOWN, Texture.class, param);
	}

	@Override
	protected Actor onCreateLayout() {
		final Table root = new Table();
		root.setFillParent(true);
		root.setBackground(Assets.get().getTextureRegionDrawable(QUIT_BG));

		final Table backgroundLayout = new Table();
		final Image dialogBackground = new Image(Assets.get().getTextureRegionDrawable(QUIT_DIALOG_BG));
		final float ratio = dialogBackground.getWidth() / dialogBackground.getHeight();
		backgroundLayout.add(dialogBackground).size(DIALOG_HEIGHT * ratio, DIALOG_HEIGHT).expand()
				.padBottom(DIALOG_PADDING_BOTTOM);

		final Table dialogLayout = new Table();
		final Button okButton = new Button(Assets.get().getTextureRegionDrawable(QUIT_OK_BUTTON_UP), Assets.get()
				.getTextureRegionDrawable(QUIT_OK_BUTTON_DOWN));
		final Button cancelButton = new Button(Assets.get().getTextureRegionDrawable(QUIT_CANCEL_BUTTON_UP), Assets
				.get().getTextureRegionDrawable(QUIT_CANCEL_BUTTON_DOWN));

		dialogLayout.add(okButton).size(BUTTON_SIZE).expand().bottom().right().padRight(BUTTON_SIDE_PADDING);
		dialogLayout.add(cancelButton).size(BUTTON_SIZE).expand().bottom().left().padLeft(BUTTON_SIDE_PADDING);

		// Handling button clicks

		final ClickListener listener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GolfAudioManager.playSound(AudioPaths.CLICK);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				final Actor button = event.getListenerActor();
				if (okButton.equals(button)) {
					GolfAudioManager.clearAudioRes();
					QuitDialog.this.exit();
				} else if (cancelButton.equals(button)) {
					showParent();
				}
			}
		};
		okButton.addListener(listener);
		cancelButton.addListener(listener);

		final Stack stack = new Stack();
		stack.add(backgroundLayout);
		stack.add(dialogLayout);

		root.add(stack).height(DIALOG_HEIGHT);

		if (DEBUG_TABLE) {
			root.debug();
			backgroundLayout.debug();
			dialogLayout.debug();
		}

		return root;
	}

	private void exit() {
		Assets.destroy();
		Gdx.app.exit();
	}
}
