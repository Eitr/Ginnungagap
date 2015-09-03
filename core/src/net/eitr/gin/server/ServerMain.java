package net.eitr.gin.server;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.esotericsoftware.kryonet.*;

import net.eitr.gin.network.Network;

public class ServerMain implements ApplicationListener {

	public static WorldManager world;

	private Server server;

	@Override
	public void create() {
		try {
			server = new Server();
			Network.registerClasses(server.getKryo());
			server.start();
			server.bind(54555, 54777);
			
			// PLAYER INPUT
			server.addListener(new Listener() {
				public void received (Connection connection, Object object) {
//					if (object instanceof SomeRequest) {
//						SomeRequest request = (SomeRequest)object;
//						System.out.println(request.text);
//
//						SomeResponse response = new SomeResponse();
//						response.text = "Thanks";
//						connection.sendTCP(response);
//					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		world = new WorldManager();
	}

	@Override
	public void render () {
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
