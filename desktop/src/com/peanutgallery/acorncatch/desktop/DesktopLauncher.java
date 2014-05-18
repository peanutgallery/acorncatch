package com.peanutgallery.acorncatch.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.peanutgallery.acorncatchgame.AcornCatchGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "acorncatch";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new AcornCatchGame(), config);
	}
}
