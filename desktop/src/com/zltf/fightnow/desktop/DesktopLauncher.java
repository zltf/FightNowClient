package com.zltf.fightnow.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zltf.fightnow.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// 禁用声音防止抛出异常
		LwjglApplicationConfiguration.disableAudio = true;
		new LwjglApplication(new Game(), config);
	}
}
