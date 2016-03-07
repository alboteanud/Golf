
package com.lutu.golf.models.obstacle;

import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Class which contains informations about obstacle which applies force.
 */
public class ForceApplyingObstacleModel extends AbstractObstacleModel {

	private static final String ANGLE = "angle";
	private static final String RANGE = "range";
	private static final String INTENSITY = "intensity";
	private static final String X_FIRST_ARM_ANGLE = "x_first_arm_angle";
	private static final String Y_FIRST_ARM_ANGLE = "y_first_arm_angle";
	private static final String X_FORCE_BEGGINING = "x_force_beggining";
	private static final String Y_FORCE_BEGGINING = "y_force_beggining";
	private static final String TYPE = "type";
	/**
	 * Force will be attract.
	 */
	public static final int ATTRACTIVE_FORCE_TYPE = -1;
	/**
	 * Force will be repel.
	 */
	public static final int REPULSIVE_FORCE_TYPE = 1;

	private float mAngle;
	private final Vector2 mFirstArmAngleForceField;
	private final Vector2 mSecondArmAngleForceField;

	private int mForceType;

	private final Vector2 mForceBeginningPoints;

	private float mForceRange;

	private float mForceIntensity;

	private Vector2 mDebugCircle = new Vector2(0, 0);

	/**
	 * Constructs {@link ForceApplyingObstacleModel} object.
	 */
	public ForceApplyingObstacleModel() {
		mFirstArmAngleForceField = new Vector2();
		mSecondArmAngleForceField = new Vector2();
		mForceBeginningPoints = new Vector2();
	}

	/**
	 * Sets beginning points of force in general coordinates system.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setForceBeginningPoints(float x, float y) {
		mForceBeginningPoints.x = x;
		mForceBeginningPoints.y = y;
	}

	/**
	 * Returns force beginning point x coordinate.
	 * 
	 * @return force beginning point x coordinate
	 */
	public float getForceBeginningPointX() {
		return mForceBeginningPoints.x;
	}

	/**
	 * Returns force beginning point y coordinate.
	 * 
	 * @return force beginning point y coordinate
	 */
	public float getForceBeginningPointY() {
		return mForceBeginningPoints.y;
	}

	/**
	 * Sets force range value.
	 * 
	 * @param forceRange
	 *            force range value - one of range value equals one of tile size
	 */
	public void setForceRange(float forceRange) {
		mForceRange = forceRange;
	}

	/**
	 * Returns a force range.
	 * 
	 * @return force range value
	 */
	public float getForceRange() {
		return mForceRange;
	}

	/**
	 * Sets a force intensity.
	 * 
	 * @param forceIntensity
	 *            force intensity value
	 */
	public void setForceIntensity(float forceIntensity) {
		mForceIntensity = forceIntensity;
	}

	/**
	 * Returns a force intensity.
	 * 
	 * @return force intensity
	 */
	public float getForceIntensity() {
		return mForceIntensity;
	}

	/**
	 * Sets force type. Force type defines direction of force which will be affected for ball.
	 * 
	 * @param forceType
	 *            force type {@link ForceApplyingObstacleModel#ATTRACTIVE_FORCE_TYPE} or
	 *            {@link ForceApplyingObstacleModel#REPULSIVE_FORCE_TYPE}
	 */
	public void setForceType(int forceType) {
		mForceType = forceType;
	}

	/**
	 * Returns force type which defines direction of force which affects for ball.
	 * 
	 * @return force type {@link ForceApplyingObstacleModel#ATTRACTIVE_FORCE_TYPE} or
	 *         {@link ForceApplyingObstacleModel#REPULSIVE_FORCE_TYPE}
	 */
	public int getForceType() {
		return mForceType;
	}

	/**
	 * Sets angle which will comprise an area in which there is a force.
	 * 
	 * @param angle
	 *            angle in degrees
	 */
	public void setAngle(float angle) {
		mAngle = angle;
	}

	/**
	 * Returns angle which will comprise an area in which there is a force.
	 * 
	 * @return angle value in degrees
	 */
	public float getAngle() {
		return mAngle;
	}

	/**
	 * Sets point which defines first arm angle which comprises an area in which there is a force.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setFirstArmAngleForceField(float x, float y) {
		mFirstArmAngleForceField.x = x;
		mFirstArmAngleForceField.y = y;
	}

	/**
	 * Return x coordinate of point which defines first arm angle which comprises an area in which there is a force.
	 * 
	 * @return x coordinate
	 */
	public float getFirstArmAngleForceFieldX() {
		return mFirstArmAngleForceField.x;
	}

	/**
	 * Return y coordinate of point which defines first arm angle which comprises an area in which there is a force.
	 * 
	 * @return y coordinate
	 */
	public float getFirstArmAngleForceFieldY() {
		return mFirstArmAngleForceField.y;
	}

	/**
	 * Sets point which defines second arm angle which comprises an area in which there is a force.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setSecondArmAngleForceField(float x, float y) {
		mSecondArmAngleForceField.x = x;
		mSecondArmAngleForceField.y = y;
	}

	/**
	 * Return x coordinate of point which defines second arm angle which comprises an area in which there is a force.
	 * 
	 * @return x coordinate
	 */
	public float getSecondArmAngleForceFieldX() {
		return mSecondArmAngleForceField.x;
	}

	/**
	 * Return y coordinate of point which defines second arm angle which comprises an area in which there is a force.
	 * 
	 * @return y coordinate
	 */
	public float getSecondArmAngleForceFieldY() {
		return mSecondArmAngleForceField.y;
	}

	@Override
	public ObstacleType getTypeObstacle() {
		return ObstacleType.FORCE_APPLYING;
	}

	@Override
	public void setObstacleDataFromTiledMap(TiledMap map, int obstacleIndex, int x, int y) {
		super.setObstacleDataFromTiledMap(map, obstacleIndex, x, y);
		final String angleStr = map.getTileProperty(obstacleIndex, ANGLE);
		final String rangeStr = map.getTileProperty(obstacleIndex, RANGE);
		final String intensityStr = map.getTileProperty(obstacleIndex, INTENSITY);
		final String xFirstArmAngleStr = map.getTileProperty(obstacleIndex, X_FIRST_ARM_ANGLE);
		final String yFirstArmAngleStr = map.getTileProperty(obstacleIndex, Y_FIRST_ARM_ANGLE);
		final String xForceBeggining = map.getTileProperty(obstacleIndex, X_FORCE_BEGGINING);
		final String yForceBeggining = map.getTileProperty(obstacleIndex, Y_FORCE_BEGGINING);
		final String typeStr = map.getTileProperty(obstacleIndex, TYPE);

		setAngle(Float.parseFloat(angleStr));
		setForceRange(Float.parseFloat(rangeStr));
		setForceIntensity(Float.parseFloat(intensityStr));
		setForceBeginningPoints(x + Float.parseFloat(xForceBeggining) / TILE_SIZE,
				y + Float.parseFloat(yForceBeggining) / TILE_SIZE);
		setFirstArmAngleForceField(x + Float.parseFloat(xFirstArmAngleStr) / TILE_SIZE,
				y + Float.parseFloat(yFirstArmAngleStr) / TILE_SIZE);
		setForceType(Integer.parseInt(typeStr));
	}

	public Vector2 getDebugCircle() {
		return mDebugCircle;
	}

}
