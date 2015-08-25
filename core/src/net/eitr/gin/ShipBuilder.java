package net.eitr.gin;

import com.badlogic.gdx.math.MathUtils;

import net.eitr.gin.Units.DrawShapeType;

public class ShipBuilder {

	float scale,rotation;
	DrawShapeType shape;
	
	public ShipBuilder () {
		
	}

	
	public void scale (float size) {
		scale += size;
		scale = MathUtils.clamp(scale, 0, 10);
	}
	
	public void rotate (float angle) {
		rotation = (angle+360)%360;
	}
}
