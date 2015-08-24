package net.eitr.gin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;

public class Main implements ApplicationListener {


	OrthographicCamera cam;
	Viewport vp;
	WorldManager world;

	@Override
	public void create() {
		vp = new FitViewport(128, 72);
		cam = new OrthographicCamera(vp.getWorldWidth(), vp.getWorldHeight());
		vp.setCamera(cam);
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		world = new WorldManager();
	}

	@Override
	public void render () {
		world.doInput(cam);
		cam.update();

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.doRender(cam);
		world.doPhysics();
	}

	@Override
	public void resize(int width, int height) {
		vp.update(width, height);
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		world.dispose();
	}

	@Override
	public void pause() {
	}

}