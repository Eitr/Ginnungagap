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

	public Ship (Body b) {
		body = b;
		width = 8;
		height = 4;
		thrust = 20f;
		rotationSpeed = 3.0f;
		thrusting = false;
		parts = new IntMap<ShipPart>();
		body.setAngularDamping(rotationSpeed*10);

		addNewPart(new ShipPart(body, new Vector2(0,0), width, height, 0));
		addNewPart(new ShipPart(body, new Vector2(0,0), height/2, width, MathUtils.PI));
		addNewPart(new ShipPart(body, new Vector2(0,width/2), width/2, height/4, 0));
		addNewPart(new ShipPart(body, new Vector2(0,-width/2), width/2, height/4, 0));
		addNewPart(new ShipPart(body, new Vector2(width/2,0), height/2));
	}

	public void rotateLeft () {
		body.setAngularVelocity(rotationSpeed);
	}

	public void rotateRight () {
		body.setAngularVelocity(-rotationSpeed);
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
	}
	
	private void addNewPart (ShipPart part) {
		parts.put(getNewPartId(), part);
	}
	
	private int getNewPartId () {
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
}
