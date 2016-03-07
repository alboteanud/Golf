
package com.lutu.golf.controlere;

import java.util.EnumMap;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.models.GameModel;
import com.lutu.golf.models.HoleModel;
import com.lutu.golf.models.HoleModel.HoleElementType;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Class which represents hole controller - this class creates hole's body and register them in physics world.
 */
public class HoleController implements IContactItem {

	private static final int LEFT_RANGE = -3;
	private static final int RIGHT_RANGE = 2;
	private static final float MAX_STARS = 3.0f;

	private final World mPhysicsWorld;

	private final HoleModel mHoleModel;

	private final EnumMap<HoleElementType, Body> mHoleBodyElements;

	private final GameModel mGameModel;

	/**
	 * Constructor.
	 * 
	 * @param holeModel
	 *            hole model
	 * @param gameModel
	 *            information about current level
	 * @param world
	 *            physics world
	 */
	public HoleController(HoleModel holeModel, GameModel gameModel, World world) {
		mPhysicsWorld = world;
		mHoleModel = holeModel;
		mGameModel = gameModel;
		mHoleBodyElements = new EnumMap<HoleElementType, Body>(HoleElementType.class);
		createHoleElementBody(HoleElementType.LEFT_WALL);
		createHoleElementBody(HoleElementType.RIGHT_WALL);
		createHoleElementBody(HoleElementType.BOTTOM);
	}

	private PolygonShape createHoleElelementShape(HoleElementType key) {
		final PolygonShape mPolygonShape = new PolygonShape();
		mPolygonShape.setAsBox(mHoleModel.getElementWidth(key) / 2, mHoleModel.getHoleElementHeight(key) / 2);
		return mPolygonShape;
	}

	private BodyDef createHoleElelementBodyDef(HoleElementType key) {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(mHoleModel.getHoleElementPositionX(key) + mHoleModel.getElementWidth(key) / 2,
				mHoleModel.getHoleElementPositionY(key) + mHoleModel.getHoleElementHeight(key) / 2);
		return bodyDef;
	}

	private void createHoleElementBody(HoleElementType key) {
		final PolygonShape polygonShape = createHoleElelementShape(key);
		final BodyDef bodyDef = createHoleElelementBodyDef(key);
		final Body result = mPhysicsWorld.createBody(bodyDef);
		result.createFixture(polygonShape, 0.0f);
		if (HoleElementType.BOTTOM.equals(key)) {
			result.setUserData(this);
		}
		polygonShape.dispose();
		mHoleBodyElements.put(key, result);
	}

	/**
	 * Method that is invoked every step in box2d. <b> Invoked by the GameController#update(float) to update the
	 * BallModel based on the Box2D step output. Do not allocate objects in this method.</b>
	 * 
	 */
	public void update() {

	}

	@Override
	public void onBeginContact() {
		GolfAudioManager.playSound(AudioPaths.CONTACT_HOLE);
	}

	@Override
	public void onEndContact() {
		mGameModel.setLevelFinished(true);

		final int starsRange = mGameModel.getHits() - mGameModel.getPar();
		if (starsRange < LEFT_RANGE) {
			mGameModel.setAndCalculateStarsCount(MAX_STARS);
		} else if (starsRange > RIGHT_RANGE) {

			mGameModel.setAndCalculateStarsCount(0.0f);
		} else {

			mGameModel.setAndCalculateStarsCount(MAX_STARS - (starsRange + MAX_STARS) * 0.5f);
		}

		mGameModel.saveLevelData();
	}
}
