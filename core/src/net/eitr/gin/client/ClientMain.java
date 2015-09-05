package net.eitr.gin.client;

import com.badlogic.gdx.Game;

public class ClientMain extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

	public void render () {
		super.render(); // important to render screens!
	}
}
