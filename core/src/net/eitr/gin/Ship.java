package net.eitr.gin;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Ship {

	float width, height, thrust, rotationSpeed;
	boolean thrusting;
	Body body;
	Array<ShipPart> parts;
	Sprite sprite;
    
    public Ship (Body b) {
    	body = b;
    	width = 4;
    	height = 8;
    	thrust = 20f;
    	rotationSpeed = 3.0f;
    	thrusting = false;
    	parts = new Array<ShipPart>();
    	
    	
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
    	

		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pix.setColor(0.8f, 0.0f, 0.8f, 1);
		pix.fill();
    	sprite = new Sprite(new Texture(pix));
    	sprite.setBounds(0,0,height,width);
//    	sprite.setOrigin(height/2f, width/2f);
    	sprite.setOriginCenter();
    	

//    	CircleShape circleShape = new CircleShape();
//    	circleShape.setRadius(width);
//    	circleShape.setPosition(new Vector2(height/2,0));
//    	Fixture circleFixture = body.createFixture(shape, 1f);
//    	circleShape.dispose();
//    	circleFixture.setUserData(""); //TODO set id and shape of body part
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
    
    
	public void draw (SpriteBatch g) {
		sprite.setRotation(body.getAngle()*180f/MathUtils.PI);
		sprite.setPosition(body.getPosition().x-height/2f, body.getPosition().y-width/2f);
		sprite.draw(g);
	}
    
    public void draw (ShapeRenderer g) {
    	g.identity();
    	//TODO: translate vs set position
        g.translate(body.getPosition().x, body.getPosition().y, 0);
        g.rotate(0, 0, 1, (float)(body.getAngle()/Math.PI*180f-90));
        g.setColor(1, 1, 0, 1);
//        g.rect(x, y, width, height);
        g.rect(-width/2,-height/2,width,height);
        if (thrusting) {
	        g.setColor(1, 0, 0, 1);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        g.rect(MathUtils.random(-width/2,width/2),-MathUtils.random(0,height/2)-height/2,0.5f,0.5f);
	        thrusting = false;
        }
        for (ShipPart p : parts) {
        	p.draw(g);
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
