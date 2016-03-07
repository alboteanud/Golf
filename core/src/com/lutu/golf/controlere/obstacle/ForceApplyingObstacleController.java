
package com.lutu.golf.controlere.obstacle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lutu.golf.controlere.BallController;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.obstacle.ForceApplyingObstacleModel;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Object which controls obstacle forces.
 */
public class ForceApplyingObstacleController {

	private final ForceApplyingObstacleModel mForceApplyingObstacleModel;
	private final BallModel mBallModel;
	private final BallController mBallController;

	private boolean mPlaySound;

	/**
	 * Constructs object which controls obstacle forces.
	 * 
	 * @param ballController
	 *            object which controls a ball {@link BallController}
	 * @param forceApplyingObstacleModel
	 *            obstacle model {@link ForceApplyingObstacleModel}
	 * @param ballModel
	 *            ball model {@link BallModel}
	 */
	public ForceApplyingObstacleController(BallController ballController,
			ForceApplyingObstacleModel forceApplyingObstacleModel, BallModel ballModel) {
		mForceApplyingObstacleModel = forceApplyingObstacleModel;
		mBallModel = ballModel;
		mBallController = ballController;
		calculateFirstAngleArm();
		calculateSecondAngleArm();

	}

	private void calculateSecondAngleArm() {

		final float angle = mForceApplyingObstacleModel.getAngle() * MathUtils.degreesToRadians;
		// Move start vector force to point 0.0
		final float xPrim = mForceApplyingObstacleModel.getFirstArmAngleForceFieldX()
				- mForceApplyingObstacleModel.getForceBeginningPointX();
		final float yPrim = mForceApplyingObstacleModel.getFirstArmAngleForceFieldY()
				- mForceApplyingObstacleModel.getForceBeginningPointY();

		final float secondArmXOffset = (float) (xPrim * Math.cos(angle) - yPrim * Math.sin(angle));
		final float secondArmYOffset = (float) (xPrim * Math.sin(angle) + yPrim * Math.cos(angle));
		mForceApplyingObstacleModel.setSecondArmAngleForceField(mForceApplyingObstacleModel.getForceBeginningPointX()
				+ secondArmXOffset, mForceApplyingObstacleModel.getForceBeginningPointY() + secondArmYOffset);

	}

	private void calculateFirstAngleArm() {
		final float range = mForceApplyingObstacleModel.getForceRange();
		// Move start vector force to point 0.0
		final float startAngleForceVectorXPrim = mForceApplyingObstacleModel.getFirstArmAngleForceFieldX()
				- mForceApplyingObstacleModel.getForceBeginningPointX();
		final float startAngleForceVectorYPrim = mForceApplyingObstacleModel.getFirstArmAngleForceFieldY()
				- mForceApplyingObstacleModel.getForceBeginningPointY();

		if (Math.abs(startAngleForceVectorXPrim) > 0.0f || Math.abs(startAngleForceVectorXPrim) > 0) {
			Vector2.tmp.x = startAngleForceVectorXPrim;
			Vector2.tmp.y = startAngleForceVectorYPrim;
			final float vector = Vector2.tmp.len();
			final float xOffset = range * startAngleForceVectorXPrim / vector;
			final float yOffset = range * startAngleForceVectorYPrim / vector;

			mForceApplyingObstacleModel.setFirstArmAngleForceField(
					mForceApplyingObstacleModel.getForceBeginningPointX() + xOffset,
					mForceApplyingObstacleModel.getForceBeginningPointY() + yOffset);
		}

	}

	/**
	 * Method that is invoked every step in box2d. <b> Invoked by the GameController#update(float) to update the
	 * BallModel based on the Box2D step output. Do not allocate objects in this method.</b>
	 */
	public void update() {
		final float xBall = mBallModel.getPositionX();
		final float yBall = mBallModel.getPositionY();

		final float xForceStartPos = mForceApplyingObstacleModel.getForceBeginningPointX();
		final float yForceStartPos = mForceApplyingObstacleModel.getForceBeginningPointY();

		final float xDistance = xBall - xForceStartPos;
		final float yDistance = yBall - yForceStartPos;

		final double distanceBallFromForcePoint = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
		if (distanceBallFromForcePoint - mForceApplyingObstacleModel.getForceRange() < 0) {
			// ball is inside of circle
			if (isBallBetweenAngleArms()) {
				if (mPlaySound) {
					mPlaySound = false;
					final String pathToSound = mForceApplyingObstacleModel.getCollisionSoundPath();
					if (pathToSound != null) {
						GolfAudioManager.playSound(pathToSound);
					}
				}
				final int forceXMark = (int) (xDistance / Math.abs(xDistance))
						* mForceApplyingObstacleModel.getForceType();
				final int forceyMark = (int) (yDistance / Math.abs(yDistance))
						* mForceApplyingObstacleModel.getForceType();

				final float forceVectorLength = mForceApplyingObstacleModel.getForceRange()
						- (float) distanceBallFromForcePoint * mForceApplyingObstacleModel.getForceIntensity();

				final float xImpulseValue = xBall - (forceVectorLength * forceXMark);
				final float yImpulseValue = yBall - (forceVectorLength * forceyMark);
				mBallController.applyForce(xImpulseValue, yImpulseValue);
			}
		} else {
			mPlaySound = true;
		}
	}

	private boolean isBallBetweenAngleArms() {
		// Move start vector force to point 0.0
		final float range = mForceApplyingObstacleModel.getForceRange();
		final float xBallPrim = mBallModel.getPositionX() - mForceApplyingObstacleModel.getForceBeginningPointX();
		final float yBallPrim = mBallModel.getPositionY() - mForceApplyingObstacleModel.getForceBeginningPointY();

		if (Float.compare(xBallPrim, 0.0f) == 0 && Float.compare(yBallPrim, 0.0f) == 0) {
			return true;
		}
		Vector2.tmp.x = xBallPrim;
		Vector2.tmp.y = yBallPrim;
		final float vector = Vector2.tmp.len();

		final float xBallPointOnCircle = range * xBallPrim / vector;
		final float yBallPointOnCircle = range * yBallPrim / vector;

		final float xFirstArmPoint = mForceApplyingObstacleModel.getFirstArmAngleForceFieldX()
				- mForceApplyingObstacleModel.getForceBeginningPointX();
		final float yFirstArmPoint = mForceApplyingObstacleModel.getFirstArmAngleForceFieldY()
				- mForceApplyingObstacleModel.getForceBeginningPointY();

		final float xSecondArm = mForceApplyingObstacleModel.getSecondArmAngleForceFieldX()
				- mForceApplyingObstacleModel.getForceBeginningPointX();
		final float ySecondArm = mForceApplyingObstacleModel.getSecondArmAngleForceFieldY()
				- mForceApplyingObstacleModel.getForceBeginningPointY();

		mForceApplyingObstacleModel.getDebugCircle().x = xBallPointOnCircle
				+ mForceApplyingObstacleModel.getForceBeginningPointX();
		mForceApplyingObstacleModel.getDebugCircle().y = yBallPointOnCircle
				+ mForceApplyingObstacleModel.getForceBeginningPointY();

		return isBallInBetweenAngleArms(xBallPointOnCircle, yBallPointOnCircle, xFirstArmPoint, yFirstArmPoint,
				xSecondArm, ySecondArm);
	}

	private boolean isBallInBetweenAngleArms(float xBall, float yBall, float xFirstArm, float yFirstArm,
			float xSecondArm, float ySecondArm) {
		if ((detMatrix(Vector2.Zero.x, Vector2.Zero.y, xBall, yBall, xFirstArm, yFirstArm))
				* (detMatrix(Vector2.Zero.x, Vector2.Zero.y, xBall, yBall, xSecondArm, ySecondArm)) >= 0) {
			return false;
		} else if ((detMatrix(xFirstArm, yFirstArm, xSecondArm, ySecondArm, Vector2.Zero.x, Vector2.Zero.y))
				* (detMatrix(xFirstArm, yFirstArm, xSecondArm, ySecondArm, xBall, yBall)) >= 0) {
			return false;
		} else {
			return true;
		}

	}

	private float detMatrix(float xA, float yA, float xB, float yB, float xC, float yC) {
		return (xA * yB + xB * yC + xC * yA - xC * yB - xA * yC - xB * yA);
	}

}
