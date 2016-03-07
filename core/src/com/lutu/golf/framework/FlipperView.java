
package com.lutu.golf.framework;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

/**
 * Class which represents control to display many actors on one view with flip effect.
 * 
 * @param <A>
 *            type of object which extends AbstractFlipperViewAdapter<T,V>
 * @param <T>
 *            type of object which extends {@link Actor}
 * @param <V>
 *            type of value for object which extends {@link Actor}
 * 
 */
public class FlipperView<A extends AbstractFlipperViewAdapter<T, V>, T extends Actor, V> extends Table {

	/**
	 * Defines {@link FlipperView}'s element position.
	 */
	public enum FlipperElementPosition {
		/** Left. */
		LEFT,
		/** Center. */
		CENTER,
		/** Right. */
		RIGHT
	}

	private int mFlipProcessFlag = 0;
	private OnFlippingFinishListener mOnFlippingFinishListener;
	private final A mFlipperAdapter;

	private float mAccumulator = 0.0f;
	private List<T> mActors;

	private static final float CLIP_BOUNDS_WIDTH = 400f;
	private static final float CLIP_BOUNDS_HEIGHT = 350f;
	private static final float FLIP_TIME = 0.2f;
	private Rectangle mScissors = new Rectangle();
	private Rectangle mClipBounds = new Rectangle();

	/**
	 * Construct instance of {@link AbstractFlipperViewAdapter}.
	 * 
	 * @param flipperAdapter
	 *            instance of {@link AbstractFlipperViewAdapter}
	 */
	public FlipperView(A flipperAdapter) {
		mFlipperAdapter = flipperAdapter;
		mActors = new LinkedList<T>();
		prepareFlipperTable();
		setValue(mFlipperAdapter.getCurrentIndex());
	}

	/**
	 * Returns {@link AbstractFlipperViewAdapter}.
	 * 
	 * @return instance of {@link AbstractFlipperViewAdapter}
	 */
	public A getFlipperAdapter() {
		return mFlipperAdapter;
	}

	/**
	 * Sets {@link OnFlippingFinishListener}.
	 * 
	 * @param onFlippingFinishListener
	 *            instance of {@link OnFlippingFinishListener}.
	 */
	public void setOnFlippingFinishListener(OnFlippingFinishListener onFlippingFinishListener) {
		mOnFlippingFinishListener = onFlippingFinishListener;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		final Camera camera = getStage().getCamera();
		mClipBounds.width = CLIP_BOUNDS_WIDTH;
		mClipBounds.height = CLIP_BOUNDS_HEIGHT;
		mClipBounds.x = camera.viewportWidth / 2 - mClipBounds.width / 2;
		mClipBounds.y = camera.viewportHeight / 2 - mClipBounds.height / 2;
		ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), mClipBounds, mScissors);
		ScissorStack.pushScissors(mScissors);
		super.draw(batch, parentAlpha);
		batch.flush();
		ScissorStack.popScissors();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (mFlipProcessFlag != 0) {
			mAccumulator = mAccumulator + delta;
			if (mAccumulator > FLIP_TIME) {
				mAccumulator = 0.0f;
				setValue(mFlipperAdapter.getCurrentIndex());
				flipAction(mFlipProcessFlag, 0);
				if (mOnFlippingFinishListener != null) {
					mOnFlippingFinishListener.onFlippingFinish();
				}
				mFlipProcessFlag = 0;
			}
		}
	}

	/**
	 * Flips in right.
	 */
	public void flippingRight() {
		if (mFlipProcessFlag == 0) {

			final int curentIndex = mFlipperAdapter.getCurrentIndex();
			final int countValue = mFlipperAdapter.getValueCount();
			if (curentIndex < countValue - 1) {

				mFlipProcessFlag = 1;
				flipAction(-1, FLIP_TIME);
				mFlipperAdapter.setCurrentIndex(curentIndex + 1);
			}
		}
	}

	/**
	 * Flips in left.
	 */
	public void flippingLeft() {
		if (mFlipProcessFlag == 0) {
			final int curentIndex = mFlipperAdapter.getCurrentIndex();
			if (curentIndex > 0) {
				mFlipProcessFlag = -1;
				flipAction(1, FLIP_TIME);
				mFlipperAdapter.setCurrentIndex(curentIndex - 1);
			}
		}
	}

	private void prepareFlipperTable() {

		final T itemLeft = mFlipperAdapter.getActor(FlipperElementPosition.LEFT);
		add(itemLeft).center().padLeft(-getWidth());
		mActors.add(itemLeft);
		final T itemCenter = mFlipperAdapter.getActor(FlipperElementPosition.CENTER);
		add(itemCenter).center();
		mActors.add(itemCenter);

		final T itemRight = mFlipperAdapter.getActor(FlipperElementPosition.RIGHT);
		add(itemRight).center().padRight(getWidth());
		mActors.add(itemRight);

	}

	private void setValue(int index) {
		final int count = mFlipperAdapter.getValueCount();
		if (count == 0) {
			return;
		}
		if (count == 1) {
			mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(0));
		} else if (count == 2) {
			if (index == 0) {
				mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(0));
				mFlipperAdapter.fillValue(mActors.get(2), mFlipperAdapter.getObjectAt(1));
			} else {
				mFlipperAdapter.fillValue(mActors.get(0), mFlipperAdapter.getObjectAt(0));
				mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(1));
			}
		} else {
			final int countValue = mFlipperAdapter.getValueCount();
			if (index == 0) {
				mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(index));
				mFlipperAdapter.fillValue(mActors.get(2), mFlipperAdapter.getObjectAt(index + 1));
			} else if (index == countValue - 1) {
				mFlipperAdapter.fillValue(mActors.get(0), mFlipperAdapter.getObjectAt(index - 1));
				mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(index));
			} else {
				mFlipperAdapter.fillValue(mActors.get(0), mFlipperAdapter.getObjectAt(index - 1));
				mFlipperAdapter.fillValue(mActors.get(1), mFlipperAdapter.getObjectAt(index));
				mFlipperAdapter.fillValue(mActors.get(2), mFlipperAdapter.getObjectAt(index + 1));
			}
		}
	}

	private void flipAction(int flipProcessFlag, float time) {
		addAction(Actions.moveTo(getX() + (flipProcessFlag * getCells().get(0).getWidgetWidth()), getY(), time));
	}

	/**
	 * Sets page at index to displayed.
	 * 
	 * @param indexOfPage
	 *            index of page which will be displayed
	 */
	public void setPageAt(int indexOfPage) {
		setValue(indexOfPage);
	}

	/**
	 * On flip effect finish listener.
	 * 
	 */
	public interface OnFlippingFinishListener {
		/**
		 * Calls when flip effect is finished.
		 * 
		 */
		void onFlippingFinish();
	}

}
