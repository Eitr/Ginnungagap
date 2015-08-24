package net.eitr.gin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class WorldManager {

	static final int WORLD_WIDTH = 1000;
	static final int WORLD_HEIGHT = 1000;

	World world;

	Array<Ship> players;
	Array<Rock> rocks;

	private float frameTotalTime = 0;
	final float TIME_STEP = 1/300f;

	SpriteBatch sprites;
	PolygonSpriteBatch polygons;
	ShapeRenderer shapes;

	Box2DDebugRenderer debugRenderer;

	public WorldManager () {
		world = new World(new Vector2(0,0), true);

		players = new Array<Ship>();
		rocks = new Array<Rock>();

		BodyDef shipDef = new BodyDef();
		shipDef.type = BodyType.DynamicBody;
		shipDef.position.set(0,0);
		players.add(new Ship(world.createBody(shipDef)));

		BodyDef rockDef = new BodyDef();
		rockDef.type = BodyType.StaticBody;
		for (int i = 0; i < WORLD_WIDTH*WORLD_HEIGHT/10000; i++) {
			rockDef.position.set(MathUtils.random(-WORLD_WIDTH/2f,WORLD_WIDTH/2f),MathUtils.random(-WORLD_HEIGHT/2f,WORLD_HEIGHT/2f));
			rocks.add(new Rock(world.createBody(rockDef)));
		}
		
		BodyDef edgeDef = new BodyDef();
		edgeDef.type = BodyType.StaticBody;
		edgeDef.position.set(0, 0);
		Body edge = world.createBody(edgeDef);
		// Create the fixture definition for this body
		EdgeShape edgeShape = new EdgeShape();
		FixtureDef fDef = new FixtureDef();
		edgeShape.set(-WORLD_WIDTH/2,-WORLD_HEIGHT/2,WORLD_WIDTH/2,-WORLD_HEIGHT/2);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(WORLD_WIDTH/2,-WORLD_HEIGHT/2,WORLD_WIDTH/2,WORLD_HEIGHT/2);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,-WORLD_WIDTH/2,WORLD_HEIGHT/2);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.set(-WORLD_WIDTH/2,WORLD_HEIGHT/2,-WORLD_WIDTH/2,-WORLD_HEIGHT/2);
		fDef.shape = edgeShape;
		edge.createFixture(fDef);
		edgeShape.dispose();

		sprites = new SpriteBatch();
		polygons = new PolygonSpriteBatch();
		shapes = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();
	}

	public void doRender (OrthographicCamera cam) {
		sprites.setProjectionMatrix(cam.combined);
		shapes.setProjectionMatrix(cam.combined);
		polygons.setProjectionMatrix(cam.combined);

		polygons.begin();
		for(Rock rock : rocks) {
			rock.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		for(Ship ship : players) {
			ship.draw(shapes);
		}
		shapes.end();

		debugRenderer.setDrawVelocities(true);
		debugRenderer.render(world, cam.combined);
	}

	public void doPhysics () {
		world.step(1/300f, 6, 2);
		frameTotalTime += Gdx.graphics.getDeltaTime();
		while (frameTotalTime >= TIME_STEP) {
			world.step(TIME_STEP,6,2);
			frameTotalTime -= TIME_STEP;
		}
	}

	public void doInput (OrthographicCamera cam) {
		for(Ship ship : players) {
			handleInput(ship, cam);
		}
	}

	private void handleInput (Ship ship, OrthographicCamera cam) {
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			cam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			ship.rotateLeft();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			ship.rotateRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			ship.thrust();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			ship.reset();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			System.out.println("Pos: "+ship.getPosition());
		}

		cam.position.set(ship.getX(), ship.getY(), 0);

		// Sets min/max for zoom
		cam.zoom = MathUtils.clamp(cam.zoom, 0.5f, 3f);

		// Keep the camera inside the boundaries of the map
		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f - WORLD_WIDTH/2F, WORLD_WIDTH/2f - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f - WORLD_HEIGHT/2F, WORLD_HEIGHT/2f - effectiveViewportHeight / 2f);
	}

	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
	}
}
