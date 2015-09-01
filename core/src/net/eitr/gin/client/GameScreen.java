package net.eitr.gin.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.eitr.gin.Units;
import net.eitr.gin.server.WorldManager;

public class GameScreen implements Screen {

	OrthographicCamera camera;
	Viewport gameView, guiView;
	static DebugInterface gui;
	InputHandler input;
	
	
	public GameScreen () {
		gameView = new FitViewport(Units.VIEW_SIZE*16, Units.VIEW_SIZE*9); // 16:9 aspect ratio
		guiView = new FitViewport(1600,900);
		camera = new OrthographicCamera(gameView.getWorldWidth(), gameView.getWorldHeight());
		gameView.setCamera(camera);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		gui = new DebugInterface(guiView);
		input = new InputHandler(world.getPlayer(),camera);
		
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(gui);
		im.addProcessor(input);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void render(float delta) {
		input.handleInput();
		camera.update();
		gui.debug("zoom", (int)(camera.zoom*100)/100f);

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.draw(camera);
		gui.update(world);
	}

	@Override
	public void resize(int width, int height) {
		gameView.update(width, height);
	}
	
	@Override
	public void show() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		gui.dispose();
	}

}
