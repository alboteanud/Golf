package com.lutu.golf.desktop;

/*import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lutu.golf.GolfGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GolfGame(), config);
	}
}*/


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lutu.golf.GolfGame;

public class DesktopLauncher   {


	public static void main(String[] args) {

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Golf";
		cfg.width = 1280;
		cfg.height = 800;
		cfg.useGL20 = true;

		new LwjglApplication(new GolfGame(new ActionResolverDesktop()), cfg);
	}

}



