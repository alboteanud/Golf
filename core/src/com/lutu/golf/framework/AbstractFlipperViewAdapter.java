
package com.lutu.golf.framework;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lutu.golf.framework.FlipperView.FlipperElementPosition;

/**
 * Class which represents adapter which is used in {@link FlipperView}.
 * 
 * @param <T>
 *            type of {@link Actor} which will be displayed on pages
 * @param <V>
 *            type of value above control
 */
public abstract class AbstractFlipperViewAdapter<T extends Actor, V> {

	private List<V> mList;

	private int mCurrentIndex = 0;

	/**
	 * Constructs {@link AbstractFlipperViewAdapter}.
	 * 
	 * @param list
	 *            list of values which will be represented by {@link Actor}
	 */
	public AbstractFlipperViewAdapter(final List<V> list) {
		mList = new LinkedList<V>(list);
	}

	/**
	 * Returns object at index.
	 * 
	 * @param index
	 *            index of object which will be returned.
	 * @return object value
	 */
	public V getObjectAt(int index) {
		return mList.get(index);
	}

	/**
	 * Returns number of values.
	 * 
	 * @return number of values
	 */
	public int getValueCount() {
		return mList.size();
	}

	/**
	 * Returns current index.
	 * 
	 * @return current index.
	 */
	public int getCurrentIndex() {
		return mCurrentIndex;
	}

	/**
	 * Sets current index.
	 * 
	 * @param currentIndex
	 *            current index
	 */
	public void setCurrentIndex(int currentIndex) {
		mCurrentIndex = currentIndex;
	}

	/**
	 * Returns instance of object which extends {@link Actor}.
	 * 
	 * @param position
	 *            instance of {@link FlipperElementPosition}
	 * 
	 * 
	 * @return instance of object which extends {@link Actor}
	 */
	public abstract T getActor(FlipperElementPosition position);

	/**
	 * {@link Actor} should be filled value on this method.
	 * 
	 * @param actor
	 *            instance of object which extends {@link Actor}
	 * @param value
	 *            value for object which extends {@link Actor}
	 */
	public abstract void fillValue(T actor, V value);
}
