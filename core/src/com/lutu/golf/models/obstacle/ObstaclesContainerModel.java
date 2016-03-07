
package com.lutu.golf.models.obstacle;

import java.util.LinkedList;
import java.util.List;

/**
 * Class which contains information about all obstacles.
 * 
 * @param <T>
 *            class which extends {@link AbstractObstacleModel}
 */
public class ObstaclesContainerModel<T extends AbstractObstacleModel> {

	private final List<T> mAbstractObstacleModels;

	/**
	 * Constructs {@link ObstaclesContainerModel}.
	 */
	public ObstaclesContainerModel() {
		mAbstractObstacleModels = new LinkedList<T>();
	}

	/**
	 * Returns {@link AbstractObstacleModel} instance.
	 * 
	 * @param index
	 *            index of obstacle
	 * 
	 * @return obstacle model instance
	 */
	public T getObstacleModel(int index) {
		final T result = mAbstractObstacleModels.get(index);
		if (result == null) {
			throw new IndexOutOfBoundsException();
		}
		return result;
	}

	/**
	 * Counts obstacles.
	 * 
	 * @return obstacle list size
	 */
	public int getObstaclesCount() {
		return mAbstractObstacleModels.size();
	}

	/**
	 * Adds obstacle to list.
	 * 
	 * @param obstacleModel
	 *            obstacle model one of
	 *            <ol>
	 *            <li> {@link com.lutu.golf.models.obstacle.SlowdownObstacleModel}</li>
	 *            <li> {@link com.lutu.golf.models.obstacle.ForceApplyingObstacleModel}</li>
	 *            <li> {@link com.lutu.golf.models.obstacle.BallDestroyingObstacleModel}</li>
	 *            </ol>
	 */
	public void addObstacleModel(T obstacleModel) {
		mAbstractObstacleModels.add(obstacleModel);
	}
}
