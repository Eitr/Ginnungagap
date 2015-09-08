package net.eitr.gin.server;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import net.eitr.gin.Units;
import net.eitr.gin.Units.WorldBodyType;
import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.ship.*;

public class WorldManager {

	protected static World world;

	private Array<Rock> rocks;

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


	protected void draw (OrthographicCamera cam) {
		debugRenderer.setDrawVelocities(true);
		debugRenderer.render(world, cam.combined);
	}
	
	protected void getGraphics (GraphicsData g) {
		for(Rock rock : rocks) {
			rock.getGraphics(g);
		}
		Iterator<Projectile> ps = projectiles.iterator();
		while (ps.hasNext()) {
			Projectile p = ps.next();
			p.getGraphics(g);
		}
		
	}

	protected void simulate () {
		timeAccumulator += Gdx.graphics.getDeltaTime();
		synchronized (world) {
			while (timeAccumulator >= Units.TIME_STEP) {
				world.step(Units.TIME_STEP,6,2);
				timeAccumulator -= Units.TIME_STEP;
			}
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
		fDef.friction = 0f;
		fDef.restitution = 0.5f;
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

	public static Body getNewWorldBody (BodyDef def) {
		return world.createBody(def);
	}
}
