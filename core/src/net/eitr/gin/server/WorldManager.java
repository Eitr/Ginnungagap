package net.eitr.gin.server;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import net.eitr.gin.Units;
import net.eitr.gin.Units.WorldBodyType;
import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.network.InputData;
import net.eitr.gin.ship.*;

public class WorldManager {

	public static World world;

	IntMap<Ship> players;
	Array<Rock> rocks;

	float timeAccumulator = 0;
	public Array<Projectile> projectiles;

	Box2DDebugRenderer debugRenderer;

	public WorldManager () {
		init();
		createRocks();
		createWorldEdges();

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				WorldBodyType fa = ((WorldBody)contact.getFixtureA().getBody().getUserData()).worldBodyType;
				WorldBodyType fb = ((WorldBody)contact.getFixtureB().getBody().getUserData()).worldBodyType;
				
				if ((fa == WorldBodyType.SHIP && fb == WorldBodyType.PROJECTILE) || (fa == WorldBodyType.PROJECTILE && fb == WorldBodyType.SHIP)) {
					if (fa == WorldBodyType.SHIP) {
						Ship s = (Ship)contact.getFixtureA().getBody().getUserData();
						ShipPart p = (ShipPart)contact.getFixtureA().getUserData();
						Projectile b = (Projectile)contact.getFixtureB().getBody().getUserData();
						s.damagePart(p.getId(),b.getDamage());
						b.remove = true;
					} else {
						Ship s = (Ship)contact.getFixtureB().getBody().getUserData();
						ShipPart p = (ShipPart)contact.getFixtureB().getUserData();
						Projectile b = (Projectile)contact.getFixtureA().getBody().getUserData();
						s.damagePart(p.getId(),b.getDamage());
						b.remove = true;
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}


	public void draw (OrthographicCamera cam) {
		debugRenderer.setDrawVelocities(true);
		debugRenderer.render(world, cam.combined);
	}
	
	public void getGraphics (int playerId, GraphicsData g) {
		Ship player = players.get(playerId);
		
		for(Rock rock : rocks) {
			rock.getGraphics(g, player.getPosition());
		}
		for(Ship ship : players.values()) {
			ship.getGraphics(g, player.getPosition());
		}
		Iterator<Projectile> ps = projectiles.iterator();
		while (ps.hasNext()) {
			Projectile p = ps.next();
			p.getGraphics(g, player.getPosition());
		}
		
		player.debug("bullets", projectiles.size);
		Iterator<String> keys = player.debugMap.keys();
		String [] labels = new String[player.debugMap.size];
		int i = 0;
		while (keys.hasNext()) {
			labels[i++] = player.debugMap.get(keys.next());
		}
		g.debug = labels;
	}

	public void simulate () {
		world.step(1/300f, 6, 2);
		timeAccumulator += Gdx.graphics.getDeltaTime();
		while (timeAccumulator >= Units.TIME_STEP) {
			world.step(Units.TIME_STEP,6,2);
			timeAccumulator -= Units.TIME_STEP;
		}
		for (Ship ship : players.values()) {
			ship.update();
		}
		Iterator<Projectile> ps = projectiles.iterator();
		while (ps.hasNext()) {
			Projectile p = ps.next();
			p.update();
			if (p.remove) {
				world.destroyBody(p.body);
				ps.remove();
			}
		}
	}

	public void doPlayerInput (int id, InputData input) {
		players.get(id).handleInput(input);
	}
	
	public Vector2 getPlayerPosition (int id) {
		return players.get(id).getPosition();
	}
		
	public void createPlayer (int id) {
		BodyDef shipDef = new BodyDef();
		shipDef.type = BodyType.DynamicBody;
		shipDef.position.set(0,0);
		//players.add(new Ship(world.createBody(shipDef)));
		players.put(id, new Ship(world.createBody(shipDef)));
	}
	
	public void removePlayer (int id) {
		world.destroyBody(players.get(id).body);
		players.remove(id);
	}

	private void createRocks () {
		rocks = new Array<Rock>();
		BodyDef rockDef = new BodyDef();
		rockDef.type = BodyType.DynamicBody;
		for (int i = 0; i < Units.WORLD_WIDTH*Units.WORLD_HEIGHT/10000; i++) {
			rockDef.position.set(MathUtils.random(-Units.WORLD_WIDTH/2f,Units.WORLD_WIDTH/2f),MathUtils.random(-Units.WORLD_HEIGHT/2f,Units.WORLD_HEIGHT/2f));
			rocks.add(new Rock(world.createBody(rockDef)));
		}
	}

	private void init () {
		world = new World(new Vector2(0,0), true);
		debugRenderer = new Box2DDebugRenderer();
		projectiles = new Array<Projectile>();
		players = new IntMap<Ship>();
	}

	private void createWorldEdges () {
		BodyDef edgeDef = new BodyDef();
		edgeDef.type = BodyType.StaticBody;
		edgeDef.position.set(0, 0);
		Body edge = world.createBody(edgeDef);
		edge.setUserData(new WorldBody(WorldBodyType.EDGE));
		// Create the fixture definition for this body
		EdgeShape edgeShape = new EdgeShape();
		FixtureDef fDef = new FixtureDef();
		int w = Units.WORLD_WIDTH/2;
		int h = Units.WORLD_HEIGHT/2;
		edgeShape.set(-w,-h,w,-h);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(w,-h,w,h);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(w,h,-w,h);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(-w,h,-w,-h);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.dispose();
	}

}
