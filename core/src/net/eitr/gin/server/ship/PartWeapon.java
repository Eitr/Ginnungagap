package net.eitr.gin.server.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import net.eitr.gin.Units.*;
import net.eitr.gin.server.WorldManager;

public class PartWeapon extends ShipPart {

	private float timeAccumulator, fireRate;
	
	public PartWeapon (int i, Body b, Vector2 p, float w, float h, float a) {
		super(i, b, ShipPartType.WEAPON, p, w, h, a);
		timeAccumulator = 0;
		fireRate = 5;
		color = new Color(1,0,0,1);
	}
	
	@Override
	protected void update (Ship ship, WorldManager world) {
		timeAccumulator += Gdx.graphics.getDeltaTime();
		while (timeAccumulator >= 1/fireRate && ship.shooting) {
			if (health > 0) {
				createProjectile(world);
			}
			timeAccumulator = 0;
		}
	}
	
	private void createProjectile (WorldManager world) {
		BodyDef bulletDef = new BodyDef();
		bulletDef.type = BodyType.DynamicBody;
		
		float x = pos.x+width;
		float y = pos.y;
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		bulletDef.position.set(MathUtils.cos(angle+ship.getAngle())*dist+ship.getPosition().x,MathUtils.sin(angle+ship.getAngle())*dist+ship.getPosition().y);
		
		world.createNewProjectile(bulletDef, ship.getLinearVelocity(), ship.getAngle());
	}
	
}
