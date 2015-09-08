package net.eitr.gin.server;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.IntMap;

import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.network.InputData;
import net.eitr.gin.ship.Ship;

public class PlayerManager {

	private IntMap<Ship> players;
	
	public PlayerManager () {
		players = new IntMap<Ship>();
	}
	
	protected void doPlayerInput (int id, InputData input) {
		players.get(id).handleInput(input);
	}
	
	protected int[] getConnectedPlayers () {
		return players.keys().toArray().items;
	}
	

	protected Vector2 getPlayerPosition (int id) {
		return players.get(id).getPosition();
	}
	
	protected void createPlayer (int id) {
		BodyDef shipDef = new BodyDef();
		shipDef.type = BodyType.DynamicBody;
		shipDef.position.set(0,0);
		players.put(id, new Ship(WorldManager.world.createBody(shipDef)));
	}
	
	protected void removePlayer (int id) {
		WorldManager.world.destroyBody(players.get(id).getBody());
		players.remove(id);
	}

	protected void simulate () {
		for (Ship ship : players.values()) {
			ship.update();
		}
	}
	
	protected void getGraphics (int id, GraphicsData g) {
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
	
	protected void setConnectionReady (int id, boolean ready) {
		players.get(id).connectionReady = ready;
	}
	
	protected boolean isConnectionReady (int id) {
		return players.get(id).connectionReady;
	}
	
}
