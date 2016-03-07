
package com.lutu.golf.controlere;

import com.lutu.golf.models.ArrowModel;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.ClubModel;

/**
 * Class responsible for golf club control.
 */
public class ClubController {

	private final ClubModel mClubModel;
	private final BallModel mBallModel;
	private final ArrowModel mArrowModel;

	private float mDiffAngle;

	private static final float ANIMATION_FRAMES_COUNT = 10.0f;

	/**
	 * Constructor.
	 * 
	 * @param clubModel
	 *            object which contains information about club
	 * @param ballModel
	 *            object which contains information about ball
	 * @param arrowModel
	 *            object which contains information about shot arrow
	 */
	public ClubController(ClubModel clubModel, BallModel ballModel, ArrowModel arrowModel) {
		mClubModel = clubModel;
		mBallModel = ballModel;
		mArrowModel = arrowModel;

		// Informs arrow model about the initial club type
		mArrowModel.onClubTypeChanged(mClubModel.getClubType());
	}

	/**
	 * Sets club position basing on position of the ball.
	 */
	public void setPosition() {
		if (mClubModel.getDirectionRight()) {
			mClubModel.setPosition(mBallModel.getPositionX() - mBallModel.getRadius() - mClubModel.getWidth(),
					mBallModel.getPositionY() - mBallModel.getRadius());
		} else {
			mClubModel.setPosition(mBallModel.getPositionX() + mBallModel.getRadius(), mBallModel.getPositionY()
					- mBallModel.getRadius());
		}

	}

	/**
	 * Sets club orientation.
	 */
	public void updateOrientation() {
		if (mArrowModel.getShotVectorX() < 0) {
			mClubModel.setDirectionRight(false);
		} else {
			mClubModel.setDirectionRight(true);
		}
	}

	/**
	 * Sets club rotation angle.
	 */
	public void setAngle() {
		float angle;
		if (mArrowModel.getShotVectorX() < 0) {
			mClubModel.setRotationCenterX(ClubModel.LEFT_ROTATION_OFFSET * mClubModel.getWidth());
			angle = ArrowModel.STRAIGHT_ANGLE;

		} else {
			mClubModel.setRotationCenterX(ClubModel.RIGHT_ROTATION_OFFSET * mClubModel.getWidth());
			angle = -ArrowModel.STRAIGHT_ANGLE;
		}

		mClubModel.setAngle(mArrowModel.getArrowHeight() / ArrowModel.MAX_ARROW_HEIGHT * angle);
		mDiffAngle = mClubModel.getAngle() / ANIMATION_FRAMES_COUNT;
	}

	/**
	 * Animates club behavior after shot.
	 * 
	 * @return true if animation is finished, false otherwise
	 */
	public boolean shotAnimation() {
		if (Math.abs(mClubModel.getAngle()) > Math.abs(mDiffAngle)) {
			mClubModel.setAngle(mClubModel.getAngle() - mDiffAngle);
			return false;
		} else {
			mClubModel.setAngle(0.0f);
			return true;
		}

	}

	/**
	 * Updates state of the club.
	 */
	public void update() {
		setPosition();
		updateOrientation();
		setAngle();
		mClubModel.setShowClub(!mBallModel.isBallMoving());
	}
}
