package net.eitr.gin.server;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

import net.eitr.gin.Units;
import net.eitr.gin.network.*;

public class ServerMain implements ApplicationListener {

	public static WorldManager world;

	private Server server;
	private GraphicsData data;

	public ServerMain () {
		super();
	}
	
	@Override
	public void create() {
		data = new GraphicsData();
		try {
			server = new Server(Units.NETWORK_BUFFER_SIZE, Units.NETWORK_OBJECT_SIZE);
			Network.registerClasses(server.getKryo());
			server.start();
			server.bind(Units.TCP_PORT, Units.UDP_PORT);
			
			// PLAYER INPUT
			server.addListener(new Listener() {
				public void connected (Connection client) {
					world.createPlayer(client.getID());
				}
				
				public void disconnected (Connection client) {
					world.removePlayer(client.getID());
				}
				
				public void received (Connection client, Object object) {
					int id = client.getID();
					if (object instanceof InputData) {
						InputData input = (InputData)object;
						world.doPlayerInput(id,input);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		world = new WorldManager();
	}

	@Override
	public void render () {
		IntMap.Keys keys = world.players.keys();
		while (keys.hasNext) {
			int id = keys.next();
			data.reset();
			world.getGraphics(id, data);
			data.setPlayerPosition(world.getPlayerPosition(id));
			try {
				server.sendToTCP(id,data);
//				server.sendToUDP(id,data); // jittery
			} catch (KryoException e) {
				Log.error("Network exception: "+e.getLocalizedMessage());
			}
		}
		world.simulate();
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
