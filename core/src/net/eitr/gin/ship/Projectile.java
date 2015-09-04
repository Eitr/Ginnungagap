package net.eitr.gin.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.eitr.gin.Units.WorldBodyType;
import net.eitr.gin.network.GraphicsData;
import net.eitr.gin.network.RectData;
import net.eitr.gin.server.WorldBody;


public class Projectile extends WorldBody {

	public Body body;
	float size, speed;
	float timeAccumulator;
	float timeToLive;
	public boolean remove;
	float damage;

	public Projectile (Body b, Vector2 v, float a) {
		super(WorldBodyType.PROJECTILE);
		
		body = b;
		size = 0.5f;
		speed = 100f;
		timeAccumulator = 0;
		timeToLive = 2.0f;
		remove = false;
		damage = 5;
		body.setUserData(this);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size/2, size/2);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 0.1f;
		fDef.friction = 0.4f;
		fDef.restitution = 1.0f;
//		fDef.isSensor = true;
		body.createFixture(fDef);
		shape.dispose();
		
		body.setLinearVelocity(v.x+MathUtils.cos(a)*speed, v.y+MathUtils.sin(a)*speed);
	}
	
	public float getDamage () {
		return damage;
	}
	
	public void update () {
		timeAccumulator += Gdx.graphics.getDeltaTime();
		if (timeAccumulator >= timeToLive || remove) {
			remove = true;
		}
	}

	public void getGraphics (GraphicsData g) {
		RectData rect = new RectData(body.getPosition().x-size/2, body.getPosition().y-size/2, size, size);
		rect.setColor(1f, 0f, 0f, 1f);
		g.shapes.add(rect);
	}

}
