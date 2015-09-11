package net.eitr.gin.server.ship;

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
import net.eitr.gin.network.RectData;
import net.eitr.gin.network.ShipData;
import net.eitr.gin.server.WorldBody;
import net.eitr.gin.server.WorldManager;

public class Ship extends WorldBody {

	public boolean connectionReady;
	float width, height, thrust, rotationSpeed;
	private boolean thrusting, shooting, turningLeft, turningRight;
	Body body;
	IntMap<ShipPart> parts;
	private ShipBuilder shipBuilder;
	
	public ObjectMap<String,String> debugMap = new ObjectMap<String,String>();

	public Ship (Body b) {
		super(WorldBodyType.SHIP);
		
		body = b;
		width = 8;
		height = 4;
		thrust = 0f;
		rotationSpeed = 10.0f*32f;
		thrusting = false;
		connectionReady = true;

		body.setUserData(this);
		parts = new IntMap<ShipPart>();
		body.setAngularDamping(rotationSpeed*10/32f);
		shipBuilder = new ShipBuilder(this);

		shipBuilder.addNewPart(new PartThruster(getNewPartId(), body, new Vector2(0,0), width, height, 0));
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
		body.setLinearVelocity(0, 0);
//		body.setTransform(0, 0, 0); // MUST synchronize with world.step
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
	
	public Body getBody () {
		return body;
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
			float scale = 0.2f;
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
			case Input.Keys.NUM_3: shipBuilder.buildType = ShipPartType.THRUSTER; 
			shipBuilder.buildNewPart(); break;
			case Input.Keys.S:
				switch(shipBuilder.shape) {
				case RECT: shipBuilder.shape = DrawShapeType.CIRCLE; break;
				case CIRCLE: shipBuilder.shape = DrawShapeType.RECT; break;
				case POLYGON: break;
				}
				shipBuilder.buildNewPart(); break;
			case Input.Keys.LEFT:
				shipBuilder.width -= scale;
				shipBuilder.buildNewPart(); break;
			case Input.Keys.RIGHT:
				shipBuilder.width += scale;
				shipBuilder.buildNewPart(); break;
			case Input.Keys.UP:
				shipBuilder.height += scale;
				shipBuilder.buildNewPart(); break;
			case Input.Keys.DOWN:
				shipBuilder.height -= scale;
				shipBuilder.buildNewPart(); break;
			}
		}
		
		for (int key : input.keysUp) {
			switch(key) {
			case Input.Keys.W: thrusting = false; break;
			case Input.Keys.A: turningLeft = false; break;
			case Input.Keys.D: turningRight = false; break;
			}
		}
		
		for (int button : input.mouseDown) {
			switch(button) {
			case Input.Buttons.LEFT: 
				if (shipBuilder.isBuilding) {
					shipBuilder.buildShip();
				} else {
					shooting = true;
				}
				break;
			}
		}
		
		for (int button : input.mouseUp) {
			switch(button) {
			case Input.Buttons.LEFT: shooting = false; break;
			}
		}
	
	}

	public void update (WorldManager world) {
		if (thrusting) {
			thrust();
		}
		
		if (turningLeft) {
			rotateLeft();
		}
		
		if (turningRight) {
			rotateRight();
		}
		
		for (ShipPart part : parts.values()) {
			part.update(shooting, world);
		}

		debug("mouse","("+(int)shipBuilder.mouse.x+","+(int)shipBuilder.mouse.y+")");
		debug("collision",intersects(shipBuilder.newPart));
		debug("part type",shipBuilder.buildType);
		debug("parts",parts.size);
		debug("mass",(int)body.getMass());
	}
	
	public void getGraphics (GraphicsData g) {
		if (Vector2.dst(g.x, g.y, body.getPosition().x, body.getPosition().y) > Units.MAX_VIEW_DIST) { 
			return;
		}
		ShipData shipData = new ShipData(body.getPosition().x, body.getPosition().y, body.getAngle());
		shipBuilder.getGraphics(shipData);
		for (ShipPart part : parts.values()) {
			part.getGraphics(shipData);

			if (thrusting && part.type == ShipPartType.THRUSTER) {
				for (int t = 0; t < 5; t++) {
					RectData tr = new RectData(part.pos.x-MathUtils.random(0,part.width/2)-part.width/2,part.pos.y-MathUtils.random(-part.height/3,part.height/3),0.3f,0.3f);
					tr.setColor(1f, .8f, 0f, 1f);
					shipData.parts.add(tr);
				}
	 		}
		}

		g.ships.add(shipData);
	}

	public void debug (String s, Object value) {
		debugMap.put(s, s+": "+value.toString());
	}
	
}
