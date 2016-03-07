
package com.lutu.golf.models.obstacle;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

/**
 * Class which contains informations about obstacle which destroys ball.
 */
public class BallDestroyingObstacleModel extends AbstractObstacleModel {

	private static final String DESTROY_POSITION_OFFSET = "destroy_position_offset";
	private static final String FALL_VELOCITY = "fall_velocity";
	private float mDestroyPositionOffset;

	private float mFallVelocity;

	/**
	 * Constructs {@link BallDestroyingObstacleModel}.
	 */
	public BallDestroyingObstacleModel() {
		// Empty
	}

	/**
	 * Returns destroying position offset.
	 * 
	 * @return destroying position offset from the bottom of the tile.
	 */
	public float getDestroyPositionOffset() {
		return mDestroyPositionOffset;
	}

	/**
	 * Sets destroying position offset.
	 * 
	 * @param destroyPositionOffset
	 *            destroying position offset from the bottom of the tile.
	 */
	public void setDestroyPositionOffset(float destroyPositionOffset) {
		mDestroyPositionOffset = destroyPositionOffset;
	}

	/**
	 * Returns fall velocity.
	 * 
	 * @return fall velocity
	 */
	public float getFallVelocity() {
		return mFallVelocity;
	}

	/**
	 * Sets fall velocity.
	 * 
	 * @param fallVelocity
	 *            fall velocity
	 */
	public void setFallVelocity(float fallVelocity) {
		mFallVelocity = fallVelocity;
	}

	@Override
	public ObstacleType getTypeObstacle() {
		return ObstacleType.BALL_DESTROYING;
	}

	@Override
	public void setObstacleDataFromTiledMap(TiledMap map, int obstacleIndex, int x, int y) {
		super.setObstacleDataFromTiledMap(map, obstacleIndex, x, y);
		final String destroyPositionOffset = map.getTileProperty(obstacleIndex, DESTROY_POSITION_OFFSET);
		final String fallForceStr = map.getTileProperty(obstacleIndex, FALL_VELOCITY);

		setDestroyPositionOffset(Float.parseFloat(destroyPositionOffset));
		setFallVelocity(Float.parseFloat(fallForceStr));

	}

}
