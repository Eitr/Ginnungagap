package net.eitr.gin;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;

public class Main implements ApplicationListener {

	OrthographicCamera camera;
	Viewport gameView, guiView;
	WorldManager world;
	static DebugInterface gui;
	InputHandler input;
	
	final int viewSize = 8;

	@Override
	public void create() {
		gameView = new FitViewport(viewSize*16, viewSize*9); // 16:9 aspect ratio
		guiView = new FitViewport(1600,900);
		camera = new OrthographicCamera(gameView.getWorldWidth(), gameView.getWorldHeight());
		gameView.setCamera(camera);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		gui = new DebugInterface(guiView);
		world = new WorldManager();
		input = new InputHandler(world.getPlayer(),camera);
		
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(gui);
		im.addProcessor(input);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render () {
		input.handleInput();
		camera.update();
		gui.debug("zoom", (int)(camera.zoom*100)/100f);

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.draw(camera);
		world.simulate();
		gui.update(world);
	}

	@Override
	public void resize(int width, int height) {
		gameView.update(width, height);
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
