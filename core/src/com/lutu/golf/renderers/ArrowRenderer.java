
package com.lutu.golf.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.models.ArrowModel;
import com.lutu.golf.utils.Assets;

/**
 * Class used to draw the shot arrow and its indicator.
 */
public class ArrowRenderer {

	private static final String ARROW_PATH = "arrow.png";
	private static final String ARROW_INDICATOR_PATH = "arrow_indicator_null.png";
//	private static final String ARROW_INDICATOR_PATH = "hand.png";

	private final ArrowModel mArrowModel;

	private Texture mArrowTexture;
	private Texture mIndicatorTexture;

	private Sprite mArrowSprite;
	private Sprite mIndicatorSprite;
	private final Color mColorCache = new Color();

	/**
	 * Constructor.
	 * 
	 * @param arrowModel
	 *            object which contains information about arrow and its indicator
	 */
	public ArrowRenderer(ArrowModel arrowModel) {
		mArrowModel = arrowModel;
	}

	void create() {
		Assets.get().load(ARROW_PATH, Texture.class);
		Assets.get().load(ARROW_INDICATOR_PATH, Texture.class);
	}

	void dispose() {
		Assets.get().unload(ARROW_PATH);
		Assets.get().unload(ARROW_INDICATOR_PATH);
	}

	void show() {
		mArrowTexture = Assets.get().get(ARROW_PATH);
		mArrowSprite = new Sprite(mArrowTexture);

		mIndicatorTexture = Assets.get().get(ARROW_INDICATOR_PATH);
		mIndicatorSprite = new Sprite(mIndicatorTexture);
	}

	/**
	 * Render the {@link ArrowModel}.
	 * <p>
	 * This method should be called on render() method in {@link GameRenderer}.
	 * </p>
	 * <b> Do not allocate objects in this method. </b>
	 * 
	 * @param batch
	 *            {@link SpriteBatch} used for drawing
	 * @param camera
	 *            {@link Camera} instance that defines the transformation used to render the map
	 */
	public void render(SpriteBatch batch, Camera camera) {
		if (mArrowModel.isVisible()) {
			batch.setProjectionMatrix(camera.projection);
			batch.setTransformMatrix(camera.view);
			batch.begin();

			mArrowSprite.setOrigin(mArrowModel.getArrowRotationCenterX(), mArrowModel.getArrowRotationCenterY());
			mArrowSprite.setRotation(mArrowModel.getArrowAngle());
			mArrowSprite.setPosition((mArrowModel.getArrowPositionX()), (mArrowModel.getArrowPositionY()));
			mArrowSprite.setSize(mArrowModel.getArrowWidth(), mArrowModel.getArrowHeight());
			drawGradient(
					Color.GREEN,
					calculateIntermediateColor(Color.GREEN, Color.RED, mArrowModel.getArrowHeight()
							/ ArrowModel.MAX_ARROW_HEIGHT), batch, mArrowSprite.getTexture(),
					mArrowSprite.getVertices());

			mIndicatorSprite.setPosition(mArrowModel.getIndicatorPositionX(), mArrowModel.getIndicatorPositionY());
			mIndicatorSprite.setOrigin(mArrowModel.getIndicatorRotationCenterX(),
					mArrowModel.getIndicatorRotationCenterY());
			mIndicatorSprite.setRotation(mArrowModel.getIndicatorAngle() - ArrowModel.STRAIGHT_ANGLE);
			mIndicatorSprite.setSize(mArrowModel.getIndicatorWidth(), mArrowModel.getIndicatorHeight());
			mIndicatorSprite.draw(batch);

			batch.end();
		}
	}

	private Color calculateIntermediateColor(Color pColorStart, Color pColorEnd, float pFraction) {
		if (pFraction <= 0.0f) {
			return pColorStart;
		}
		if (pFraction >= 1.0f) {
			return pColorEnd;
		}

		final float r = pColorStart.r * (1 - pFraction) + pColorEnd.r * pFraction;
		final float g = pColorStart.g * (1 - pFraction) + pColorEnd.g * pFraction;
		final float b = pColorStart.b * (1 - pFraction) + pColorEnd.b * pFraction;
		final float a = pColorStart.a * (1 - pFraction) + pColorEnd.a * pFraction;
		mColorCache.set(r, g, b, a);

		return mColorCache;
	}

	private void drawGradient(Color pColorStart, Color pColorEnd, SpriteBatch pBatch, Texture pTexture,
			float[] pVertices) {
		final float colorStart = pColorStart.toFloatBits();
		final float colorEnd = pColorEnd.toFloatBits();

		pVertices[SpriteBatch.C1] = colorStart;
		pVertices[SpriteBatch.C2] = colorEnd;
		pVertices[SpriteBatch.C3] = colorEnd;
		pVertices[SpriteBatch.C4] = colorStart;

		pBatch.draw(pTexture, pVertices, 0, pVertices.length);
	}
}
