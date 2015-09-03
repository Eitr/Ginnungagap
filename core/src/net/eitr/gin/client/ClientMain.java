package net.eitr.gin.client;

import com.badlogic.gdx.Game;

public class ClientMain extends Game {

	@Override
	public void create() {
		// TODO Auto-generated method stub
		setScreen(new GameScreen());
	}

	public void render () {
		super.render(); // important to render screens!
	}
}
