package net.eitr.gin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;

public class Main implements ApplicationListener {

	OrthographicCamera camera;
	Viewport view;
	WorldManager world;
	Stage gui; //TODO
	InputHandler input;
	
	final int viewSize = 8;

	@Override
	public void create() {
		view = new FitViewport(vieSize*16, viewSize*9); // 16:9 aspect ratio
		camera = new OrthographicCamera(vp.getWorldWidth(), vp.getWorldHeight());
		vp.setCamera(cam);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		world = new WorldManager();
		gui = new Stage(view);
		input = new InputHandler(world,camera);
		
		InputMultiplexor im = new InputMultiplexor();
		im.add(gui);
		im.add(input);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render (float delta) {
		input.handleInput(player,camera);
		camera.update();

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gui.act(delta);
		gui.draw();
		world.draw(camera);
		world.simulate();
	}

	@Override
	public void resize(int width, int height) {
		view.update(width, height);
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		world.dispose();
		gui.dispose();
	}

	@Override
	public void pause() {
	}

}
