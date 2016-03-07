
package com.lutu.golf.models.obstacle;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

/**
 * Contains information about an obstacle which slows down the ball.
 */
public class SlowdownObstacleModel extends AbstractObstacleModel {

	private static final String DEPTH = "depth";
	private float mDepth;

	/**
	 * Constructs {@link SlowdownObstacleModel}.
	 */
	public SlowdownObstacleModel() {
		// Empty
	}

	/**
	 * Returns information how deep is obstacle.
	 * 
	 * @return deep
	 */
	public float getDepth() {
		return mDepth;
	}

	/**
	 * Sets information how deep is obstacle.
	 * 
	 * @param depth
	 *            depth of obstacle
	 */
	public void setDepth(float depth) {
		mDepth = depth;
	}

	@Override
	public ObstacleType getTypeObstacle() {
		return ObstacleType.SLOW_DOWN;
	}

	@Override
	public void setObstacleDataFromTiledMap(TiledMap map, int obstacleIndex, int x, int y) {
		super.setObstacleDataFromTiledMap(map, obstacleIndex, x, y);
		final String deepStr = map.getTileProperty(obstacleIndex, DEPTH);
		setDepth(Float.parseFloat(deepStr));
	}
}
