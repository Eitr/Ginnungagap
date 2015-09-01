package net.eitr.gin.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.eitr.gin.Units.*;
import net.eitr.gin.server.ServerMain;
import net.eitr.gin.server.WorldManager;

public class PartWeapon extends ShipPart {

	float timeAccumulator;
	float fireRate;
	
	
	public PartWeapon(int i, Body b, Vector2 p, float w, float h, float a) {
		super(i, b, ShipPartType.WEAPON, p, w, h, a);
		timeAccumulator = 0;
		fireRate = 5;
	}
		
	protected void update (boolean isShooting) {
		timeAccumulator += Gdx.graphics.getDeltaTime();
		while (timeAccumulator >= 1/fireRate && isShooting) {
			if (health > 0) {
				createProjectile();
			}
			timeAccumulator = 0;
		}
	}
	
	private void createProjectile () {
		BodyDef bulletDef = new BodyDef();
		bulletDef.type = BodyType.DynamicBody;
		
		float x = pos.x+width;
		float y = pos.y;
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		bulletDef.position.set(MathUtils.cos(angle+ship.getAngle())*dist+ship.getPosition().x,MathUtils.sin(angle+ship.getAngle())*dist+ship.getPosition().y);
		
		ServerMain.world.projectiles.add(new Projectile(WorldManager.world.createBody(bulletDef), ship.getLinearVelocity(), ship.getAngle()));
	}

}
