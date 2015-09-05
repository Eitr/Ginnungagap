package net.eitr.gin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.eitr.gin.server.ServerMain;

public class ServerLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.fullscreen = false;
		config.resizable = true;
		config.useGL30 = false;
		config.vSyncEnabled = true;
		config.width = 1366;
		config.height = 768;
//		config.title = Main.version; //TODO

		new OutputFrame("Server Output");
		
		new LwjglApplication(new ServerMain(), config);
	}
}
