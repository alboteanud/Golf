
package com.lutu.golf.framework;

import java.util.EnumMap;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Widget which represents score via three graphics.
 */
public class RatingBar extends Table {
	private final Image[] mElements;

	private final EnumMap<StarType, TextureRegionDrawable> mStarTextures;

	private static final int ELEMENTS_COUNT = 3;

	private float mValue = 0.0f;

	/**
	 * Enum which defines star type.
	 */
	private enum StarType {
		FULL, HALF, EMPTY, DISABLE
	}

	/**
	 * Constructs {@link RatingBar}.
	 * 
	 * @param fullStar
	 *            texture region which represents full point
	 * @param halfStar
	 *            texture region which represents half point
	 * @param emptyStar
	 *            texture region which represents no point
	 */
	public RatingBar(TextureRegionDrawable fullStar, TextureRegionDrawable halfStar, TextureRegionDrawable emptyStar) {
		this(fullStar, halfStar, emptyStar, emptyStar);
	}

	/**
	 * Constructs {@link RatingBar}.
	 * 
	 * @param fullStar
	 *            texture region which represents full point
	 * @param halfStar
	 *            texture region which represents half point
	 * @param emptyStar
	 *            texture region which represents no point
	 * @param disableStar
	 *            texture region which represents disable point
	 */
	public RatingBar(TextureRegionDrawable fullStar, TextureRegionDrawable halfStar, TextureRegionDrawable emptyStar,
			TextureRegionDrawable disableStar) {
		mElements = new Image[ELEMENTS_COUNT];
		mStarTextures = new EnumMap<StarType, TextureRegionDrawable>(StarType.class);
		mStarTextures.put(StarType.FULL, fullStar);
		mStarTextures.put(StarType.HALF, halfStar);
		mStarTextures.put(StarType.EMPTY, emptyStar);
		mStarTextures.put(StarType.DISABLE, disableStar);
		prepareElementsImage();

	}

	private void prepareElementsImage() {
		for (int index = 0; index < ELEMENTS_COUNT; index++) {
			mElements[index] = new Image();
			mElements[index].setDrawable(mStarTextures.get(StarType.EMPTY));
			add(mElements[index]);
		}
		row();
	}

	/**
	 * Sets enabled.
	 * 
	 * @param isEnabled
	 *            if true is enabled otherwise is disabled.
	 */
	public void setEnabled(boolean isEnabled) {
		if (isEnabled) {
			setValue(mValue);
		} else {
			for (int index = 0; index < ELEMENTS_COUNT; index++) {
				mElements[index].setDrawable(mStarTextures.get(StarType.DISABLE));
			}
		}
	}

	/**
	 * Sets value which will be displayed. <b> "value" has to be between 0.0f and 3.0f, otherwise method throws
	 * exception </b>.
	 * 
	 * @param value
	 *            value which will be represents via graphics
	 */
	public void setValue(float value) {
		mValue = 0.0f;
		for (int index = 0; index < ELEMENTS_COUNT; index++) {
			mElements[index].setDrawable(mStarTextures.get(StarType.EMPTY));
		}
		if (value < 0 || value > ELEMENTS_COUNT) {
			throw new IllegalArgumentException();
		}
		final int floor = (int) Math.floor(value);
		for (int index = 0; index < floor; index++) {
			mElements[index].setDrawable(mStarTextures.get(StarType.FULL));
		}
		mValue = floor;
		if (value - floor >= 0.5f) {
			mValue = mValue + 0.5f;
			mElements[floor].setDrawable(mStarTextures.get(StarType.HALF));
		}
	}

	/**
	 * Returns value of gained stars.
	 * 
	 * @return value of gained stars.
	 */
	public float getValue() {
		return mValue;
	}
}
