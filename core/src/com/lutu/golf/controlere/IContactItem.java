
package com.lutu.golf.controlere;

/**
 * This interface should be implemented by all the elements that should be notified about ball contact.
 * 
 */
public interface IContactItem {
	/**
	 * Calls in
	 * {@link com.badlogic.gdx.physics.box2d.ContactListener# beginContact(com.badlogic.gdx.physics.box2d.Contact)}
	 * method.
	 */
	void onBeginContact();

	/**
	 * Calls in
	 * {@link com.badlogic.gdx.physics.box2d.ContactListener# endContact(com.badlogic.gdx.physics.box2d.Contact)}
	 * method.
	 */
	void onEndContact();
}
