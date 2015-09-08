package net.eitr.gin.client;

import com.badlogic.gdx.Game;

public class ClientMain extends Game {

	private String serverIp;
	
	public ClientMain (String ip) {
		serverIp = ip;
	}
	
	@Override
	public void create() {
		setScreen(new GameScreen(serverIp));
	}

	public void render () {
		super.render(); // important to render screens!
	}
}
