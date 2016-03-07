
package com.lutu.golf.controlere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.lutu.golf.ecrane.GameScreen;
import com.lutu.golf.models.BallModel;
import com.lutu.golf.models.GameMapModel;
import com.lutu.golf.models.HoleModel;
import com.lutu.golf.models.obstacle.AbstractObstacleModel;
import com.lutu.golf.models.obstacle.AbstractObstacleModel.ObstacleType;
import com.lutu.golf.models.obstacle.BallDestroyingObstacleModel;
import com.lutu.golf.models.obstacle.ForceApplyingObstacleModel;
import com.lutu.golf.models.obstacle.ObstaclesContainerModel;
import com.lutu.golf.models.obstacle.SlowdownObstacleModel;
import com.lutu.golf.utils.AudioPaths;
import com.lutu.golf.utils.GolfAudioManager;

/**
 * Class which creates and represents maps in physics world.
 */
public class GameMapController {

	private final GameMapModel mGameMapModel;
	private final HoleModel mHolModel;
	private final BallModel mBallModel;
	private final ObstaclesContainerModel<AbstractObstacleModel> mObstaclesContainerModel;
	private final World mPhysicsWorld;

	private static final Vector2 GRAVITY = new Vector2(0.0f, -10.0f);

	private static final float TILE_OFFSET = -0.0f;
	private static final int TILE_SIZE = 32;

	private static final float CONTACT_GROUND_MIN_VELOCITY = 0.5f;

	/**
	 * Controller.
	 * 
	 * @param gameMapModel
	 *            model of map
	 * @param holeModel
	 *            hole model
	 * 
	 * @param obstaclesContainerModel
	 *            container which contains information about all obstacles
	 * @param ballModel
	 *            instance of {@link BallModel}
	 * 
	 * @param world
	 *            physics world
	 */
	public GameMapController(GameMapModel gameMapModel, HoleModel holeModel,
			ObstaclesContainerModel<AbstractObstacleModel> obstaclesContainerModel, BallModel ballModel, World world) {
		mGameMapModel = gameMapModel;
		mHolModel = holeModel;
		mBallModel = ballModel;
		mObstaclesContainerModel = obstaclesContainerModel;
		mPhysicsWorld = world;

		loadCollisions(gameMapModel.getBodiesDataFilePath());
		mPhysicsWorld.setGravity(GRAVITY);
		mPhysicsWorld.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// Do nothing
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// Do nothing
			}

			@Override
			public void endContact(Contact contact) {
				final IContactItem userAData = (IContactItem) contact.getFixtureA().getBody().getUserData();
				final IContactItem userBData = (IContactItem) contact.getFixtureB().getBody().getUserData();
				if (userAData != null) {
					userAData.onEndContact();
				}
				if (userBData != null) {
					userBData.onEndContact();
				}
				if (userAData == null && userBData == null) {
					final Body ballBody;
					if (BodyType.DynamicBody.equals(contact.getFixtureA().getBody().getType())) {
						ballBody = contact.getFixtureA().getBody();
					} else {
						ballBody = contact.getFixtureB().getBody();
					}
					if (ballBody.getLinearVelocity().len() > CONTACT_GROUND_MIN_VELOCITY) {
						GolfAudioManager.playSound(AudioPaths.CONTACT_GROUND);
					}

				}
			}

			@Override
			public void beginContact(Contact contact) {
				final IContactItem userAData = (IContactItem) contact.getFixtureA().getBody().getUserData();
				final IContactItem userBData = (IContactItem) contact.getFixtureB().getBody().getUserData();
				if (userAData != null) {
					userAData.onBeginContact();
				}
				if (userBData != null) {
					userBData.onBeginContact();
				}
			}
		});
	}

	private void loadCollisions(String collisionsFile) {

		final FileHandle fh = Gdx.files.internal(collisionsFile);
		final String collisionFile = fh.readString();
		final String lines[] = collisionFile.split("\\r?\\n");

		final Map<Integer, List<LineBodyCoordinates>> tileBodies = new HashMap<Integer, List<LineBodyCoordinates>>();

		tileBodies.put(Integer.valueOf(0), new ArrayList<LineBodyCoordinates>());

		for (int n = 0; n < lines.length; n++) {
			final String cols[] = lines[n].split(" ");
			final int tileIndex = Integer.parseInt(cols[0]);

			final List<LineBodyCoordinates> tmp = new ArrayList<LineBodyCoordinates>();

			for (int m = 1; m < cols.length; m++) {
				final String coords[] = cols[m].split(",");

				final String start[] = coords[0].split("x");
				final String end[] = coords[1].split("x");

				tmp.add(new LineBodyCoordinates(Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer
						.parseInt(end[0]), Integer.parseInt(end[1])));
			}

			tileBodies.put(Integer.valueOf(tileIndex), tmp);
		}

		mBallModel.loadPositionFromMap(mGameMapModel.getMap());

		final List<LineBodyCoordinates> lineBodiesCoordinates = new ArrayList<LineBodyCoordinates>();

		for (int y = 0; y < mGameMapModel.getMap().height; y++) {
			for (int x = 0; x < mGameMapModel.getMap().width; x++) {
				final int tileType = mGameMapModel.getMap().layers.get(0).tiles[(mGameMapModel.getMap().height - 1) - y][x];
				if (tileType == mHolModel.getHoleIndex(mGameMapModel.getMap())) {
					mHolModel.setPosition(x + TILE_OFFSET, y + TILE_OFFSET);
					mHolModel.setHoleFilePathFromMap(mGameMapModel.getAccessoriesDirPath(), mGameMapModel.getMap(),
							tileType);
				}
				for (final ObstacleType obsType : ObstacleType.values()) {
					for (final int obstacleIndex : AbstractObstacleModel.getObstacleTilesIndexes(obsType,
							mGameMapModel.getMap())) {
						if (obstacleIndex == tileType) {
							final AbstractObstacleModel obstacleModel;
							switch (obsType) {
							case BALL_DESTROYING:
								obstacleModel = new BallDestroyingObstacleModel();
								obstacleModel.setObstacleDataFromTiledMap(mGameMapModel.getMap(), tileType, x, y);
								mObstaclesContainerModel.addObstacleModel(obstacleModel);

								break;
							case FORCE_APPLYING:
								obstacleModel = new ForceApplyingObstacleModel();
								obstacleModel.setObstacleDataFromTiledMap(mGameMapModel.getMap(), tileType, x, y);
								mObstaclesContainerModel.addObstacleModel(obstacleModel);
								break;
							case SLOW_DOWN:
								obstacleModel = new SlowdownObstacleModel();
								obstacleModel.setObstacleDataFromTiledMap(mGameMapModel.getMap(), tileType, x, y);
								mObstaclesContainerModel.addObstacleModel(obstacleModel);
								break;
							default:
								throw new IllegalArgumentException();
							}
							obstacleModel.setObstacleFilePath(mGameMapModel.getAccessoriesDirPath(),
									mGameMapModel.getMap(), tileType);
							obstacleModel.setCollisionSoundPath(mGameMapModel.getSoundsDirPath(),
									mGameMapModel.getMap(), tileType);
						}
					}
				}

				final List<LineBodyCoordinates> tileBody = tileBodies.get(Integer.valueOf(tileType));
				if (tileBody == null) {
					throw new IllegalArgumentException();
				}
				for (int number = 0; number < tileBody.size(); number++) {
					final LineBodyCoordinates lineBodyCoord = tileBody.get(number);

					addOrExtendCollisionLineBodyCoordinates(
							x * mGameMapModel.getTileWidth() + lineBodyCoord.getBeginningVector().x,
							y * mGameMapModel.getTileHeight() - lineBodyCoord.getBeginningVector().y + TILE_SIZE, x
									* mGameMapModel.getTileWidth() + lineBodyCoord.getEndVector().x,
							y * mGameMapModel.getTileHeight() - lineBodyCoord.getEndVector().y + TILE_SIZE,
							lineBodiesCoordinates);
				}
			}
		}

		final BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		final Body groundBody = mPhysicsWorld.createBody(groundBodyDef);
		createInnerMap(groundBodyDef, groundBody, lineBodiesCoordinates);
		drawMapFrame(groundBodyDef, groundBody);
	}

	private void createInnerMap(BodyDef groundBodyDef, Body groundBody, List<LineBodyCoordinates> collisionLineSegments) {
		final EdgeShape environmentShape = new EdgeShape();
		for (LineBodyCoordinates lineSegment : collisionLineSegments) {
			environmentShape.set(
					lineSegment.getBeginningVector().div(mGameMapModel.getTileSize()).add(TILE_OFFSET, TILE_OFFSET),
					lineSegment.getEndVector().div(mGameMapModel.getTileSize()).add(TILE_OFFSET, TILE_OFFSET));
			groundBody.createFixture(environmentShape, 0);
		}
		environmentShape.dispose();
	}

	private void drawMapFrame(BodyDef groundBodyDef, Body groundBody) {
		final EdgeShape mapBounds = new EdgeShape();
		// bottom
		mapBounds.set(new Vector2(TILE_OFFSET, TILE_OFFSET), new Vector2(mGameMapModel.getMapWidth() + TILE_OFFSET,
				TILE_OFFSET));
		groundBody.createFixture(mapBounds, 0);

		// top
		mapBounds.set(new Vector2(TILE_OFFSET, mGameMapModel.getMapHeight() + TILE_OFFSET),
				new Vector2(mGameMapModel.getMapWidth() + TILE_OFFSET, mGameMapModel.getMapHeight() + TILE_OFFSET));
		groundBody.createFixture(mapBounds, 0);

		// left
		mapBounds.set(new Vector2(TILE_OFFSET, TILE_OFFSET), new Vector2(TILE_OFFSET, mGameMapModel.getMapHeight()
				+ TILE_OFFSET));
		groundBody.createFixture(mapBounds, 0);

		// right
		mapBounds.set(new Vector2(mGameMapModel.getMapWidth() + TILE_OFFSET, TILE_OFFSET),
				new Vector2(mGameMapModel.getMapWidth() + TILE_OFFSET, mGameMapModel.getMapHeight() + TILE_OFFSET));
		groundBody.createFixture(mapBounds, 0);

		mapBounds.dispose();
	}

	private void addOrExtendCollisionLineBodyCoordinates(float lsxb, float lsyb, float lsxe, float lsye,
			List<LineBodyCoordinates> lineBodiesCoordinaties) {

		final LineBodyCoordinates line = new LineBodyCoordinates(lsxb, lsyb, lsxe, lsye);

		boolean didextend = false;

		for (LineBodyCoordinates test : lineBodiesCoordinaties) {
			if (test.extendIfPossible(line)) {
				didextend = true;
				break;
			}
		}

		if (!didextend) {
			lineBodiesCoordinaties.add(line);
		}

	}

	/**
	 * {@link GameScreen#render(float)}.
	 */
	public void update() {
		// Do nothing.
	}

	/**
	 * This class represents line as coordinates.
	 */
	private static class LineBodyCoordinates {

		private static final double EPSILON = 1e-9;
		private static final double SQRT_2_PLUS_EPSILON = Math.sqrt(2) + EPSILON;

		private final Vector2 mBeginning;
		private final Vector2 mEnd;

		/**
		 * Constructs a {@link LineBodyCoordinates} with the given components.
		 * 
		 * @param bx
		 *            x coordinate of the beginning
		 * @param by
		 *            y coordinate of the beginning
		 * @param ex
		 *            x coordinate of the end
		 * @param ey
		 *            y coordinate of the end
		 */
		public LineBodyCoordinates(float x1, float y1, float x2, float y2) {
			mBeginning = new Vector2(x1, y1);
			mEnd = new Vector2(x2, y2);
		}

		/**
		 * Returns beginning vector.
		 * 
		 * @return beginning coordinates
		 */

		public Vector2 getBeginningVector() {
			return mBeginning;
		}

		/**
		 * Returns end vector.
		 * 
		 * @return beginning coordinates
		 */
		public Vector2 getEndVector() {
			return mEnd;
		}

		/**
		 * Checks that it is possible to extend line body. If it is possible, line is extended.
		 * 
		 * @param lineBodyCoordinates
		 * @return boolean true if line was extended, otherwise false.
		 */
		public boolean extendIfPossible(LineBodyCoordinates lineBodyCoordinates) {

			final double slope1 = Math.atan2(mEnd.y - mBeginning.y, mEnd.x - mBeginning.x);
			final double slope2 = Math.atan2(lineBodyCoordinates.mEnd.y - lineBodyCoordinates.mBeginning.y,
					lineBodyCoordinates.mEnd.x - lineBodyCoordinates.mBeginning.x);

			if (Math.abs(slope1 - slope2) > EPSILON) {
				return false;
			}

			if (mBeginning.dst(lineBodyCoordinates.mBeginning) <= SQRT_2_PLUS_EPSILON) {
				mBeginning.set(lineBodyCoordinates.mEnd);
				return true;
			} else if (mEnd.dst(lineBodyCoordinates.mBeginning) <= SQRT_2_PLUS_EPSILON) {
				mEnd.set(lineBodyCoordinates.mEnd);
				return true;
			} else if (mEnd.dst(lineBodyCoordinates.mEnd) <= SQRT_2_PLUS_EPSILON) {
				mEnd.set(lineBodyCoordinates.mBeginning);
				return true;
			} else if (mBeginning.dst(lineBodyCoordinates.mEnd) <= SQRT_2_PLUS_EPSILON) {
				mBeginning.set(lineBodyCoordinates.mBeginning);
				return true;
			}

			return false;
		}
	}

}
