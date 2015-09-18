package net.eitr.gin.server.ship;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import net.eitr.gin.Units.ShipPartType;

public class PartHull extends ShipPart {

	public PartHull (int i, Body b, Vector2 p, float w, float h, float a) {
		super(i, b, ShipPartType.HULL, p, w, h, a);
		armor = 4;
	}

	public PartHull (int i, Body b, Vector2 p, float r) {
		super(i, b, ShipPartType.HULL, p, r);
		armor = 4;
	}
}
