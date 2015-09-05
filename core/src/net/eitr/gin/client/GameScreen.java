package net.eitr.gin.client;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.*;

import net.eitr.gin.Units;
import net.eitr.gin.network.*;

public class GameScreen implements Screen {

	OrthographicCamera camera;
	Viewport gameView, guiView;
	static DebugInterface gui;
	InputHandler input;
	private Client client;
	private GraphicsManager graphics;
	
	public GameScreen (String ip) {
		gameView = new FitViewport(Units.VIEW_SIZE*16, Units.VIEW_SIZE*9); // 16:9 aspect ratio
		guiView = new FitViewport(1366,768);//1600,900);
		camera = new OrthographicCamera(gameView.getWorldWidth(), gameView.getWorldHeight());
		gameView.setCamera(camera);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();
		graphics = new GraphicsManager();
		
		gui = new DebugInterface(guiView);
		input = new InputHandler(camera);
//		
//		InputMultiplexer im = new InputMultiplexer();
//		im.addProcessor(gui);
//		im.addProcessor(input);
//		Gdx.input.setInputProcessor(im);
		Gdx.input.setInputProcessor(input);
		
		networkConnection(ip);
	}
	
	private void networkConnection (String ip) {
		try {
			client = new Client(2048000,1024000);
			Network.registerClasses(client.getKryo());
		    client.start();
		    client.connect(5000, ip, Units.TCP_PORT, Units.UDP_PORT);
		    
		    client.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof GraphicsData) {
		        		graphics.setGraphicsData((GraphicsData)object);
		        	}
		        }
		     });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(float delta) {
		input.handleCameraInput();
		camera.position.set(graphics.data.x, graphics.data.y, 0);
		camera.update();
		
		client.sendTCP(input.getInputData());

		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		graphics.render(camera);
		
		gui.update();
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
		client.stop();
	}

}
