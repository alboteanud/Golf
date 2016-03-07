
package com.lutu.golf.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lutu.golf.models.ClubModel;
import com.lutu.golf.models.ClubModel.ClubType;
import com.lutu.golf.utils.Assets;

/**
 * Class used to draw the golf club.
 */
public class ClubRenderer {

	private static final String TAG = ClubRenderer.class.getSimpleName();

	private static final String CLUB_IRON_PATH = "club_iron.png";
//	private static final String CLUB_WOODEN_PATH = "club_wooden.png";
//	private static final String CLUB_PUTTER_PATH = "club_putter.png";
	private final ClubModel mClubModel;

	private ClubType mCurrentClubType;
	private Texture mClubTexture;
	private Texture mClubIronTexture;
	private Texture mClubWoodenTexture;
	private Texture mClubPutterTexture;

	private Sprite mClubSprite;
	private Sprite mClubSpriteFlipped;

	private boolean mTexturesLoaded = false;

	/**
	 * Constructor.
	 * 
	 * @param clubModel
	 *            object which contains information about the club
	 */
	public ClubRenderer(ClubModel clubModel) {
		mClubModel = clubModel;
		mCurrentClubType = mClubModel.getClubType();
	}

	void create() {
		Assets.get().load(CLUB_IRON_PATH, Texture.class);
//		Assets.get().load(CLUB_WOODEN_PATH, Texture.class);
//		Assets.get().load(CLUB_PUTTER_PATH, Texture.class);
	}

	void dispose() {
		Assets.get().unload(CLUB_IRON_PATH);
//		Assets.get().unload(CLUB_WOODEN_PATH);
//		Assets.get().unload(CLUB_PUTTER_PATH);

		mTexturesLoaded = false;
	}

	void show() {
		if (!mTexturesLoaded) {
			mClubIronTexture = Assets.get().get(CLUB_IRON_PATH);
//			mClubWoodenTexture = Assets.get().get(CLUB_WOODEN_PATH);
//			mClubPutterTexture = Assets.get().get(CLUB_PUTTER_PATH);
			mClubTexture = getClubTexture(mCurrentClubType);
			mTexturesLoaded = true;

			mClubSprite = new Sprite(mClubTexture);
			mClubSpriteFlipped = new Sprite(mClubTexture);
			mClubSpriteFlipped.flip(true, false);
		}
	}

	private Texture getClubTexture(ClubType pClubType) {
		Gdx.app.debug(TAG, "loadClubTexture pClubType: " + pClubType);
		switch (pClubType) {
		case IRON:
			return mClubIronTexture;
		case WOODEN:
			return mClubWoodenTexture;
		case PUTTER:
			return mClubPutterTexture;
		default:
			Gdx.app.error(TAG, "No club selected - returning default texture!");
			return mClubIronTexture;
		}
	}

	/**
	 * Render the {@link ClubModel}
	 * <p>
	 * This method should be called on render() method in GameRenderer.
	 * </p>
	 * <b> Do not allocate objects in this method. </b>
	 * 
	 * @param batch
	 *            {@link SpriteBatch} used for drawing
	 * @param camera
	 *            {@link Camera} instance that defines the transformation used to render the map
	 */
	public void render(SpriteBatch batch, Camera camera) {
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();

		if (mClubModel.isShowClub()) {
			if (mClubModel.getDirectionRight()) {
				setClubSprite(mClubSprite);
				mClubSprite.draw(batch);
			} else {
				setClubSprite(mClubSpriteFlipped);
				mClubSpriteFlipped.draw(batch);
			}
		}
		batch.end();
	}

	/**
	 * Sets the rotation center, angle and position for the selected Sprite object.
	 * 
	 * @param clubShape
	 *            {@link Sprite} object to set
	 */
	private void setClubSprite(Sprite clubShape) {
		clubShape.setOrigin(mClubModel.getRotationCenterX(), mClubModel.getRotationCenterY());
		clubShape.setRotation(mClubModel.getAngle());
		clubShape.setPosition((mClubModel.getPositionX()), (mClubModel.getPositionY()));
		clubShape.setSize(mClubModel.getWidth(), mClubModel.getHeight());
	}

	/**
	 * Called when renderer needs to be informed about golf club type change.
	 */
	public void invalidateClubType() {
		Gdx.app.debug(TAG, "invalidateClubType start, eq: " + mCurrentClubType.equals(mClubModel.getClubType()));
		if (!mCurrentClubType.equals(mClubModel.getClubType())) {
			mCurrentClubType = mClubModel.getClubType();
			mClubTexture = getClubTexture(mCurrentClubType);
			mClubSprite.setTexture(mClubTexture);
			mClubSpriteFlipped.setTexture(mClubTexture);
		}
	}
}
