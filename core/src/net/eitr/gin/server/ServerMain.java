package net.eitr.gin.server;

import java.io.IOException;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.IntMap;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

import net.eitr.gin.network.*;
import net.eitr.gin.ship.Ship;

public class ServerMain implements ApplicationListener {

	public static WorldManager world;

	private Server server;
	private GraphicsData data;

	@Override
	public void create() {
		data = new GraphicsData();
		try {
			server = new Server();
			Network.registerClasses(server.getKryo());
			server.start();
			server.bind(54555, 54777);
			
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
		data.reset();
		world.getGraphics(data);
		IntMap.Keys keys = world.players.keys();
		while (keys.hasNext) {
			server.sendToTCP(keys.next(),data);
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
		server.stop();
	}


}
