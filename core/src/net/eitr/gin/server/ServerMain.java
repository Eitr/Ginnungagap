package net.eitr.gin.server;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

import net.eitr.gin.Units;
import net.eitr.gin.network.*;

public class ServerMain implements ApplicationListener {

	private float timeAccumulator;
	
	private WorldManager world;
	private PlayerManager players;

	private Server server;

	public ServerMain () {
		super();
	}
	
	@Override
	public void create() {
		networkConnection();
		world = new WorldManager();
		players = new PlayerManager();
		timeAccumulator = 0;
	}
	
	/** Setup server socket and data receiver */
	private void networkConnection () {
		try {
			server = new Server(Units.NETWORK_BUFFER_SIZE, Units.NETWORK_OBJECT_SIZE);
			Network.registerClasses(server.getKryo());
			server.start();
			server.bind(Units.TCP_PORT);//, Units.UDP_PORT);
			
			// PLAYER INPUT
			server.addListener(new Listener() {
				public void connected (Connection connection) {
					players.createPlayer(connection.getID(), world.getNewShipBody());
				}
				
				public void disconnected (Connection connection) {
					world.destroyBody(players.removePlayer(connection.getID()));
				}
				
				public void received (Connection connection, Object object) {
					int id = connection.getID();
					if (object instanceof InputData) {
						InputData input = (InputData)object;
						players.doPlayerInput(id,input);
					}
				}
				
				public void idle (Connection connection) {
					players.setConnectionReady(connection.getID(),true);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		timeAccumulator += Gdx.graphics.getDeltaTime();
		while (timeAccumulator >= Units.NETWORK_TIME_STEP) {
			timeAccumulator -= Units.NETWORK_TIME_STEP;
			GraphicsData data = new GraphicsData();
			int[] keys = players.getConnectedPlayers();
			for (int id : keys) {
				try {
					if (players.isConnectionReady(id)) {
						data.reset();
						data.setPlayerPosition(players.getPlayerPosition(id));
						players.getGraphics(id,data);
						world.getGraphics(data);
						server.sendToTCP(id,data);
						players.setConnectionReady(id, false);
					}
				} catch (NullPointerException e) {
					Log.error("Null player (probably disconnected)"); //TODO
					world.destroyBody(players.removePlayer(id));
				}
			}
		}
		players.update(world);
		world.update();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void resume() {}

	@Override
	public void pause() {}

	@Override
	public void dispose() {
		for (Connection client : server.getConnections()) {
			client.close();
		}
		server.stop();
	}


}
