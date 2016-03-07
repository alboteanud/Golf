
package com.lutu.golf.models;

import com.lutu.golf.models.gamelogic.LevelLogicModel;
import com.lutu.golf.models.obstacle.AbstractObstacleModel;
import com.lutu.golf.models.obstacle.ObstaclesContainerModel;
import com.lutu.golf.utils.GolfPreferences;

/**
 * This model is the main game model.
 * <p>
 * It contains all sub models (e.g. BallModel, ClubModel). These models contain information which will be set in
 * controllers and used in renderers.
 * </p>
 */
public class GameModel {
	private static final String TAG = GameModel.class.getSimpleName();
	private static final boolean DEBUG_ALLOW_MULTIPLE_SHOTS = false;

	/**
	 * Enum containing possible game states.
	 */
	public static enum GameState {
		/**
		 * Game is running.
		 */
		RUNNING,
		/**
		 * Game is paused.
		 */
		PAUSED
	}

	private int mHits = 0;
	private float mStarsCount;
	private boolean mLevelFinished;
	private boolean mIsShoot;

	private GameState mGameState;

	private final BallModel mBallModel;

	private final ClubModel mClubModel;

	private final HoleModel mHoleModel;

	private final GameMapModel mGameMapModel;

	private final ArrowModel mArrowModel;

	private final ObstaclesContainerModel<AbstractObstacleModel> mObstaclesContainerModel;

	private final LevelLogicModel mLevelLogicModel;

	private int mPar;

	private boolean mLastShot;

	/**
	 * Constructs {@link GameModel} instance containing scene data.
	 * 
	 * @param levelLogicModel
	 *            instance of LevelLogicModel
	 */
	public GameModel(LevelLogicModel levelLogicModel) {
		mLevelLogicModel = levelLogicModel;
		mGameState = GameState.RUNNING;
		mBallModel = new BallModel();
		mClubModel = new ClubModel();
		mArrowModel = new ArrowModel();
		mHoleModel = new HoleModel();
		mGameMapModel = new GameMapModel(levelLogicModel.getPathToTmxFile(), levelLogicModel.getPackerDirPath(),
				levelLogicModel.getBodiesDataFilePath(),
				levelLogicModel.getChapterLogicModel().getAccessoriesDirPath(), levelLogicModel.getChapterLogicModel()
						.getAudioDirPath());
		mObstaclesContainerModel = new ObstaclesContainerModel<AbstractObstacleModel>();

	}

	/**
	 * Returns current game state.
	 * 
	 * @return game state
	 */
	public GameState getGameState() {
		return mGameState;
	}

	/**
	 * Sets game state.
	 * 
	 * @param gameState
	 *            game state to set
	 */
	public void setGameState(GameState gameState) {
		mGameState = gameState;
	}

	/**
	 * Returns information about whether a shot should be fired.
	 * 
	 * @return true if shot should be fired, false otherwise
	 */
	public boolean isShoot() {
		return mIsShoot;
	}

	/**
	 * Sets information that shoot button was clicked - allows the shot if the ball is not moving.
	 * 
	 * @param isShoot
	 *            true if shoot button was clicked, false otherwise
	 */
	public void onShootButtonClicked(boolean isShoot) {
		if (DEBUG_ALLOW_MULTIPLE_SHOTS) {
			mIsShoot = isShoot;
		} else if (!isShoot || !mBallModel.isBallMoving()) {
			mIsShoot = isShoot;
		}
	}

	/**
	 * Returns {@link HoleModel} instance.
	 * 
	 * @return hole model instance
	 */
	public HoleModel getHoleModel() {
		return mHoleModel;
	}

	/**
	 * Returns {@link BallModel} instance.
	 * 
	 * @return ball model instance
	 */
	public BallModel getBallModel() {
		return mBallModel;
	}

	/**
	 * Returns {@link ClubModel} instance.
	 * 
	 * @return club model instance
	 */
	public ClubModel getClubModel() {
		return mClubModel;
	}

	/**
	 * Returns {@link ArrowModel} instance.
	 * 
	 * @return arrow model instance
	 */
	public ArrowModel getArrowModel() {
		return mArrowModel;
	}

	/**
	 * Returns {@link GameMapModel} instance.
	 * 
	 * @return game map model instance
	 */
	public GameMapModel getGameMapModel() {
		return mGameMapModel;
	}

	/**
	 * Returns {@link ObstaclesContainerModel} instance.
	 * 
	 * @return obstacles container model instance
	 */
	public ObstaclesContainerModel<AbstractObstacleModel> getObstraclesContenerModel() {
		return mObstaclesContainerModel;
	}

	/**
	 * Returns {@link LevelLogicModel} instance.
	 * 
	 * @return obstacles level model instance
	 */
	public LevelLogicModel getLevelLogicModel() {
		return mLevelLogicModel;
	}

	/**
	 * Gets the hits count.
	 * 
	 * @return number of hits
	 */
	public int getHits() {
		return mHits;
	}

	/**
	 * Adds one to number which represents hits count.
	 */
	public void addHit() {
		mHits++;
	}

	/**
	 * Returns information that level is finished.
	 * 
	 * @return true if level is finished, otherwise false
	 */
	public boolean isLevelFinished() {
		return mLevelFinished;
	}

	/**
	 * Sets information that level is finished.
	 * 
	 * @param levelFinished
	 *            true if level is finished, false otherwise
	 */
	public void setLevelFinished(boolean levelFinished) {
		mLevelFinished = levelFinished;
	}

	/**
	 * Returns information that the shot was the last shot.
	 * 
	 * @return true if the shot was the last, false otherwise
	 */
	public boolean isLastShot() {
		return mLastShot;
	}

	/**
	 * Sets information that the shot was the last.
	 * 
	 * @param lastShot
	 *            true if the shot was the last, false otherwise
	 */
	public void setLastShot(boolean lastShot) {
		mLastShot = lastShot;
	}

	/**
	 * Gets the star count.
	 * 
	 * @return number of stars
	 */
	public float getStarsCount() {
		return mStarsCount;
	}

	/**
	 * Sets number of stars.
	 * 
	 * @param starsCount
	 *            number of stars
	 */
	public void setAndCalculateStarsCount(float starsCount) {
		mStarsCount = (int) Math.floor(starsCount);
		if (starsCount - mStarsCount >= 0.5f) {
			mStarsCount = mStarsCount + 0.5f;
		}
	}

	/**
	 * Sets course par.
	 * 
	 * @param par
	 *            course par
	 */
	public void setPar(int par) {
		mPar = par;
	}

	/**
	 * Returns course par.
	 * 
	 * @return course par
	 */
	public int getPar() {
		return mPar;
	}

	/**
	 * Saves the information about the current level.
	 */
	public void saveLevelData() {
		final int chapterNumber = mLevelLogicModel.getChapterLogicModel().getChapterNumber();
		final int lvlNumber = mLevelLogicModel.getLevelNumber();
		final float lastStars = mLevelLogicModel.getStarsHighScore();
		if (lastStars < mStarsCount) {
			mLevelLogicModel.setStarsHighScore(mStarsCount);
			GolfPreferences.INSTANCE.setStarsCount(chapterNumber, lvlNumber, mStarsCount);
		}

		final int lastScore = mLevelLogicModel.getMinHits();
		if (mHits < lastScore || lastScore == -1) {
			mLevelLogicModel.setMinHits(mHits);
			GolfPreferences.INSTANCE.setBestScore(chapterNumber, lvlNumber, mHits);
		}
		if (mLevelLogicModel.getLevelNumber() < mLevelLogicModel.getChapterLogicModel().getLevelCount() - 1) {
			mLevelLogicModel.getChapterLogicModel().getLevelAt(mLevelLogicModel.getLevelNumber() + 1).setEnabled(true);
			GolfPreferences.INSTANCE.setLevelUnblocked(mLevelLogicModel.getChapterLogicModel().getChapterNumber(),
					mLevelLogicModel.getLevelNumber() + 1, true);
		}
	}
}
