package net.eitr.gin.ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

import net.eitr.gin.*;
import net.eitr.gin.Units.*;
import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.network.InputData;
import net.eitr.gin.network.ShipData;
import net.eitr.gin.server.WorldBody;

public class Ship extends WorldBody {

	float width, height, thrust, rotationSpeed;
	private boolean thrusting, shooting, turningLeft, turningRight;
	public Body body;
	IntMap<ShipPart> parts;
	public ShipBuilder shipBuilder;
	
	public ObjectMap<String,String> debugMap = new ObjectMap<String,String>();

	public Ship (Body b) {
		super(WorldBodyType.SHIP);
		
		body = b;
		width = 8;
		height = 4;
		thrust = 20f;
		rotationSpeed = 10.0f*32f;
		thrusting = false;

		body.setUserData(this);
		parts = new IntMap<ShipPart>();
		body.setAngularDamping(rotationSpeed*10/32f);
		shipBuilder = new ShipBuilder(this);

		shipBuilder.addNewPart(new ShipPart(getNewPartId(), body, ShipPartType.HULL, new Vector2(0,0), width, height, 0));
		shipBuilder.addNewPart(new ShipPart(getNewPartId(), body, ShipPartType.HULL, new Vector2(0,0), height/2, width, MathUtils.PI));
		shipBuilder.addNewPart(new PartWeapon(getNewPartId(), body, new Vector2(0,width/2), width/2, height/4, 0));
		shipBuilder.addNewPart(new PartWeapon(getNewPartId(), body, new Vector2(0,-width/2), width/2, height/4, 0));
		shipBuilder.addNewPart(new ShipPart(getNewPartId(), body, ShipPartType.HULL, new Vector2(width/2,0), height/2));
	}
	
	public void damagePart (int id, float damage) {
		parts.get(id).damage(damage);
	}

	private void rotateLeft () {
		body.setAngularVelocity(rotationSpeed/body.getMass());
	}

	private void rotateRight () {
		body.setAngularVelocity(-rotationSpeed/body.getMass());
	}

	private void thrust () {
		float xforce = (float)(Math.cos(body.getAngle())*thrust);
		float yforce = (float)(Math.sin(body.getAngle())*thrust);
		//    	body.applyForceToCenter(xforce,yforce,true);
		body.applyLinearImpulse(xforce, yforce, body.getPosition().x, body.getPosition().y, true);
		thrusting = true;
	}
	
	public void resetPosition () {
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
	}

	public int getNewPartId () {
		int id;
		do {
			id = MathUtils.random(1,Units.MAX_SHIP_PARTS*10);
		} while (parts.containsKey(id));
		return id;
	}
	
	public Vector2 getPosition () {
		return body.getPosition();
	}

	public float getX () {
		return body.getPosition().x;
	}

	public float getY () {
		return body.getPosition().y;
	}
	
	public boolean intersects (ShipPart part) {
		for (ShipPart p : parts.values()) {
			if (p.intersects(part)) {
				return true;
			}
		}
		return false;
	}
	
	public void handleInput (InputData input) {
		shipBuilder.mouse = new Vector2(input.mx, input.my);
		
		for (int key : input.keysDown) {
			switch(key) {
			case Input.Keys.W: thrusting = true; break;
			case Input.Keys.A: turningLeft = true; break;
			case Input.Keys.D: turningRight = true; break;
			case Input.Keys.R: resetPosition(); break;
			case Input.Keys.B: shipBuilder.isBuilding = !shipBuilder.isBuilding;
				shipBuilder.buildNewPart(); break;
			case Input.Keys.NUM_1: shipBuilder.buildType = ShipPartType.HULL; 
				shipBuilder.buildNewPart(); break;
			case Input.Keys.NUM_2: shipBuilder.buildType = ShipPartType.WEAPON; 
				shipBuilder.buildNewPart(); break;
			case Input.Keys.S: 
				switch(shipBuilder.shape) {
				case RECT: shipBuilder.shape = DrawShapeType.CIRCLE; break;
				case CIRCLE: shipBuilder.shape = DrawShapeType.RECT; break;
				case POLYGON: break;
				}
				shipBuilder.buildNewPart(); break;
			}
//			float scale = 0.2f;
//			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//				ship.shipBuilder.width -= scale;
//				ship.shipBuilder.buildNewPart();
//			}
//			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//				ship.shipBuilder.width += scale;
//				ship.shipBuilder.buildNewPart();
//			}
//			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//				ship.shipBuilder.height += scale;
//				ship.shipBuilder.buildNewPart();
//			}
//			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//				ship.shipBuilder.height -= scale;
//				ship.shipBuilder.buildNewPart();
//			}
		}
		
		for (int key : input.keysUp) {
			switch(key) {
			case Input.Keys.W: thrusting = false; break;
			case Input.Keys.A: turningLeft = false; break;
			case Input.Keys.D: turningRight = false; break;
			}
		}
		
		if (input.mouseDown) {
			if (shipBuilder.isBuilding) {
				shipBuilder.buildShip();
			} else {
				shooting = true;
			}
		}
		if (input.mouseUp) {
			shooting = false;
		}
	
		if (thrusting) {
			thrust();
		}
		
		if (turningLeft) {
			rotateLeft();
		}
		
		if (turningRight) {
			rotateRight();
		}
	}

	public void update () {
		for (ShipPart part : parts.values()) {
			part.update(shooting);
		}

		debug("mouse","("+(int)shipBuilder.mouse.x+","+(int)shipBuilder.mouse.y+")");
		debug("collision",intersects(shipBuilder.newPart));
		debug("part type",shipBuilder.buildType);
		debug("parts",parts.size);
		debug("mass",(int)body.getMass());
	}
	
	public void getGraphics (GraphicsData g, Vector2 pos) {
		if (Vector2.dst(pos.x, pos.y, body.getPosition().x, body.getPosition().y) > Units.MAX_VIEW_DIST) { 
			return;
		}
		ShipData shipData = new ShipData(body.getPosition().x, body.getPosition().y, body.getAngle());
		shipBuilder.getGraphics(shipData);
		for (ShipPart part : parts.values()) {
			part.getGraphics(shipData);
		}
		g.ships.add(shipData);
	}

	public void debug (String s, Object value) {
		debugMap.put(s, s+": "+value.toString());
	}
	
}
