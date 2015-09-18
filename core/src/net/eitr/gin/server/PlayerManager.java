package net.eitr.gin.server;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.IntMap;

import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.network.InputData;
import net.eitr.gin.server.ship.Ship;

public class PlayerManager {

	/** Mapping of connected players with their client id. */
	private IntMap<Ship> players;
	
	PlayerManager () {
		players = new IntMap<Ship>();
	}
	
	/** Called when input data is received from the client. */
	void doPlayerInput (int id, InputData input) {
		players.get(id).handleInput(input);
	}
	
	int[] getConnectedPlayers () {
		return players.keys().toArray().items;
	}
	
	Vector2 getPlayerPosition (int id) {
		return players.get(id).getPosition();
	}
	
	//TODO: load player from db
	void createPlayer (int id, Body body) {
		players.put(id, new Ship(body));
	}
	
	/** Removes player from connection list. Make sure the world destroys the returned physics body. */
	Body removePlayer (int id) {
		return players.remove(id).getBody();
	}

	/** Update ship parts, such as weapons/thrusters firing */
	void update (WorldManager world) {
		for (Ship ship : players.values()) {
			ship.update(world);
		}
	}
	
	void getGraphics (int id, GraphicsData g) {
		for(Ship ship : players.values()) {
			ship.getGraphics(g);
		}

		Ship player = players.get(id);
		
		// Generate debug labels
		Iterator<String> keys = player.debugMap.keys();
		String [] labels = new String[player.debugMap.size];
		int i = 0;
		while (keys.hasNext()) {
			labels[i++] = player.debugMap.get(keys.next());
		}
		g.debug = labels;
	}
	
	void setConnectionReady (int id, boolean ready) {
		players.get(id).connectionReady = ready;
	}
	
	boolean isConnectionReady (int id) {
		return players.get(id).connectionReady;
	}
	
}
