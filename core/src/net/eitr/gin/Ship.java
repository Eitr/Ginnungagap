package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Ship {

	private float width, height, thrust, rotationSpeed;
	private boolean thrusting;
	private Body body;
    
    public Ship (Body b) {
    	body = b;
    	width = 4;
    	height = 8;
    	thrust = 20f;
    	rotationSpeed = 3.0f;
    	thrusting = false;
    	
    	// Create the fixture definition for this body
    	PolygonShape shape = new PolygonShape();
    	shape.set(new float[]{-height/2,-width/2,-height/2,width/2,height/2,width/2,height/2,-width/2});
    	FixtureDef fDef = new FixtureDef();
    	fDef.shape = shape;
    	fDef.density = 1f;
    	fDef.friction = 0.4f;
    	fDef.restitution = 0.3f;
    	body.createFixture(fDef);
    	shape.dispose();
    	body.setAngularDamping(rotationSpeed*10);

    	CircleShape circleShape = new CircleShape();
    	circleShape.setRadius(width);
    	circleShape.setPosition(new Vector2(height/2,0));
    	Fixture circleFixture = body.createFixture(shape, 1f);
    	circleShape.dispose();
    	circleFixture.setUserData(""); //TODO set id and shape of body part
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
    
    public void reset () {
    	body.setTransform(0, 0, 0);
    	body.setLinearVelocity(0, 0);
    }
    
    public void draw (ShapeRenderer g) {
    	g.identity();
        g.translate(body.getPosition().x, body.getPosition().y, 0);
        g.rotate(0, 0, 1, (float)(body.getAngle()/Math.PI*180f-90));
        g.setColor(1, 1, 0, 1);
        g.rect(-width/2,-height/2,width,height);
        if (thrusting) {
	        g.setColor(1, 0, 0, 1);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        thrusting = false;
        }
        for (Fixture f : body.getFixtureList()) {
        	f.getUserData().draw();
        }
        g.rotate(0, 0, 1, -(float)(body.getAngle()/Math.PI*180f-90));
        g.translate(-body.getPosition().x, -body.getPosition().y, 0);
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
