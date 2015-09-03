package net.eitr.gin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.eitr.gin.*;
import net.eitr.gin.client.ClientMain;
import net.eitr.gin.server.ServerMain;

public class ClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.fullscreen = false;
		config.resizable = true;
		config.useGL30 = false;
		config.vSyncEnabled = true;
		config.width = 1600;
		config.height = 900;
		
		new LwjglApplication(new ClientMain(), config);
	}
}
