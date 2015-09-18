package net.eitr.gin.server.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import net.eitr.gin.Units.ShipPartType;
import net.eitr.gin.server.WorldManager;

public class PartThruster extends ShipPart {

	float thrust;
	
	public PartThruster (int i, Body b, Vector2 p, float w, float h, float a) {
		super(i, b, ShipPartType.THRUSTER, p, w, h, a);
		thrust = w*h;
		color = new Color(1,1,0,1);
	}

	@Override
	protected void update (Ship ship, WorldManager world) {
		if (ship.thrusting && health > 0) {
			ship.thrust(thrust, pos);
		}
	}
}
