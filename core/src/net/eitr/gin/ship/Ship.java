package net.eitr.gin.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.IntMap;

import net.eitr.gin.*;
import net.eitr.gin.Units.*;
import net.eitr.gin.server.WorldBody;

public class Ship extends WorldBody {

	float width, height, thrust, rotationSpeed;
	public boolean thrusting, shooting;
	public Body body;
	IntMap<ShipPart> parts;
	public ShipBuilder shipBuilder;

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

	public void rotateLeft () {
		body.setAngularVelocity(rotationSpeed/body.getMass());
	}

	public void rotateRight () {
		body.setAngularVelocity(-rotationSpeed/body.getMass());
	}

	public void thrust () {
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
}
