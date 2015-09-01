package net.eitr.gin.server;

import com.badlogic.gdx.ApplicationListener;

public class ServerMain implements ApplicationListener {

	public static WorldManager world;
	

	@Override
	public void create() {
		world = new WorldManager();
	}

	@Override
	public void render () {
		world.simulate();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {}

	@Override
	public void pause() {
	}

}
