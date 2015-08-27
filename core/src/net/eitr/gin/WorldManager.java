package net.eitr.gin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class WorldManager {

	World world;

	//Array<Ship> players;
	Ship ship;
	Array<Rock> rocks;

	float frameTotalTime = 0;

	SpriteBatch sprites;
	PolygonSpriteBatch polygons;
	ShapeRenderer shapes;

	Box2DDebugRenderer debugRenderer;

	public WorldManager () {
		init();
		createPlayers();
		createRocks();
		createWorldEdges();

		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				if (contact.getFixtureA().getBody() == ship.body || contact.getFixtureB().getBody() == ship.body){

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
		sprites.setProjectionMatrix(cam.combined);
		shapes.setProjectionMatrix(cam.combined);
		polygons.setProjectionMatrix(cam.combined);

		sprites.begin();
		sprites.end();

		polygons.begin();
		for(Rock rock : rocks) {
			rock.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		//for(Ship ship : players) {
		//	ship.draw(shapes);
		//}
		ship.draw(shapes);
		shapes.end();

		debugRenderer.setDrawVelocities(true);
		debugRenderer.render(world, cam.combined);
	}

	public void simulate () {
		world.step(1/300f, 6, 2);
		frameTotalTime += Gdx.graphics.getDeltaTime();
		while (frameTotalTime >= Units.TIME_STEP) {
			world.step(Units.TIME_STEP,6,2);
			frameTotalTime -= Units.TIME_STEP;
		}
	}
	
	public void buildShip () {
		ship.shipBuilder.buildShip(ship);
	}
	
	public void setMousePosition (Vector3 pos) {
		ship.shipBuilder.setMousePosition(new Vector2(pos.x,pos.y));
	}

	public Ship getPlayer () {
		return ship;
	}

	private void createPlayers () {
		//		players = new Array<Ship>();
		BodyDef shipDef = new BodyDef();
		shipDef.type = BodyType.DynamicBody;
		shipDef.position.set(0,0);
		//players.add(new Ship(world.createBody(shipDef)));
		ship = new Ship(world.createBody(shipDef));
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
		sprites = new SpriteBatch();
		polygons = new PolygonSpriteBatch();
		shapes = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();
	}

	private void createWorldEdges () {
		BodyDef edgeDef = new BodyDef();
		edgeDef.type = BodyType.StaticBody;
		edgeDef.position.set(0, 0);
		Body edge = world.createBody(edgeDef);
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

	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
	}
}
