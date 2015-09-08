package net.eitr.gin.desktop;

import javax.swing.JOptionPane;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.eitr.gin.client.ClientMain;

public class ClientLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.fullscreen = false;
		config.resizable = true;
		config.useGL30 = false;
		config.vSyncEnabled = true;
		config.width = 1366;
		config.height = 768;
		
		new OutputFrame("Client Output");

		String ip = JOptionPane.showInputDialog("Server ip?");
		if (ip.equals("")) {
			ip = "127.0.0.1";
		}
		
		new LwjglApplication(new ClientMain(ip), config);
	}
}
