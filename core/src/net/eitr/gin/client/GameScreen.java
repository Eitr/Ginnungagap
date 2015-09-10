package net.eitr.gin.client;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.*;

import net.eitr.gin.Units;
import net.eitr.gin.network.*;

public class GameScreen implements Screen {

	private OrthographicCamera camera;
	private Viewport gameView, guiView;
	private InputHandler input;
	private Client client;
	private GraphicsManager graphics;
	private long lastPacketTime = System.currentTimeMillis();
	private long lastTimeAccumulator = 0;

	static DebugInterface gui;
	
	public GameScreen (String ip) {
		gameView = new FitViewport(Units.VIEW_SIZE, Units.VIEW_SIZE/16*9); // 16:9 aspect ratio
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
	
	/** Setup client socket and data receiver */
	private void networkConnection (String ip) {
		try {
			client = new Client(Units.NETWORK_BUFFER_SIZE, Units.NETWORK_OBJECT_SIZE);
			Network.registerClasses(client.getKryo());
		    client.start();
		    client.connect(5000, ip, Units.TCP_PORT);//, Units.UDP_PORT);
		    
		    client.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof GraphicsData) {
		        		graphics.setGraphicsData((GraphicsData)object);
		        		client.updateReturnTripTime();
		        		lastTimeAccumulator += System.currentTimeMillis()-lastPacketTime;
		        		if (lastTimeAccumulator > 1000) {
			        		gui.debug("latency", ((int)(1.0/(System.currentTimeMillis()-lastPacketTime)*1000.0))+" fps");
			        		gui.debug("ping", client.getReturnTripTime()+" ms");
			        		lastTimeAccumulator -= 1000;
		        		}
		        		lastPacketTime = System.currentTimeMillis();
		        	}
		        }
		     });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(float delta) {
		input.handleInput();
		cameraControl();
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		client.sendTCP(input.getInputData());

		graphics.render(camera);
		gui.update();
	}
	
	private void cameraControl () {
		camera.position.set(graphics.data.x, graphics.data.y, 0);
		
		// Sets min/max for zoom
		camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 3f);

		// Keep the camera inside the boundaries of the map
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f - Units.WORLD_WIDTH/2F, Units.WORLD_WIDTH/2f - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f - Units.WORLD_HEIGHT/2F, Units.WORLD_HEIGHT/2f - effectiveViewportHeight / 2f);

		camera.update();
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
		graphics.dispose();
		client.stop();
	}

}
