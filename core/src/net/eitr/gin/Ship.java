package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.IntMap;


public class Ship {

	float width, height, thrust, rotationSpeed;
	boolean thrusting;
	Body body;
	IntMap<ShipPart> parts;
	ShipBuilder shipBuilder;

	public Ship (Body b) {
		body = b;
		width = 8;
		height = 4;
		thrust = 20f;
		rotationSpeed = 3.0f*32f;
		thrusting = false;
		parts = new IntMap<ShipPart>();
		body.setAngularDamping(rotationSpeed*10/32f);
		shipBuilder = new ShipBuilder(this);

		addNewPart(new ShipPart(getNewPartId(), body, new Vector2(0,0), width, height, 0));
//		addNewPart(new ShipPart(getNewPartId(), body, new Vector2(0,0), height/2, width, MathUtils.PI));
//		addNewPart(new ShipPart(getNewPartId(), body, new Vector2(0,width/2), width/2, height/4, 0));
//		addNewPart(new ShipPart(getNewPartId(), body, new Vector2(0,-width/2), width/2, height/4, 0));
//		addNewPart(new ShipPart(getNewPartId(), body, new Vector2(width/2,0), height/2));
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

	/** Used for checking collision */
	public void glide () {
		body.setLinearVelocity(0, 0);
		body.applyLinearImpulse(1f, 0f, body.getPosition().x, body.getPosition().y, true);
	}

	public void resetPosition () {
		body.setTransform(0, 0, 0);
		body.setLinearVelocity(0, 0);
	}

	public void draw (ShapeRenderer g) {
		g.identity();
		//TODO: translate vs set position
		//TODO: rotation slightly adjusts everything else in the world
		g.translate(body.getPosition().x, body.getPosition().y, 0);
		g.rotate(0, 0, 1, (float)(body.getAngle()/Math.PI*180f));
		shipBuilder.draw(g);
		g.setColor(1, 1, 0, 1);
		for (ShipPart p : parts.values()) {
			p.draw(g);
		}
		if (thrusting) {
			g.setColor(1, 0, 0, 1);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			thrusting = false;
		}
		g.rotate(0, 0, 1, -(float)(body.getAngle()/Math.PI*180f));
		g.translate(-body.getPosition().x, -body.getPosition().y, 0);
		
		Main.gui.debug("parts",parts.size);
		Main.gui.debug("mass",body.getMass());
	}
	
	public void addNewPart (ShipPart part) {

		FixtureDef fDef = new FixtureDef();
		fDef.density = 1f;
		fDef.friction = 0.4f;
		fDef.restitution = 0.3f;
		
		switch (part.type) {
		case CIRCLE:
			CircleShape circle = new CircleShape();
			circle.setRadius(part.data[0]);
			circle.setPosition(part.pos);
			fDef.shape = circle;
			body.createFixture(fDef);
			circle.dispose();
			break;
		case RECT:
			PolygonShape rect = new PolygonShape();
			rect.setAsBox(part.data[0]/2f, part.data[1]/2f, part.pos, part.data[2]);
			fDef.shape = rect;
			body.createFixture(fDef);
			rect.dispose();
			break;
		case POLYGON:
			break;
		default:
			return;
		}
		parts.put(part.getId(), part);
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
