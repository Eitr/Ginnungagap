package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.eitr.gin.Units.DrawShapeType;

public class ShipPart {

	DrawShapeType type;
	
	float [] drawData;
	
	public ShipPart (DrawShapeType t, float[] data) {
		type = t;
		drawData = data;
	}

	public void draw (ShapeRenderer g) {
		switch(type) {
		case CIRCLE: 

			break;
		case POLYGON:
			break;
		case RECT:
			break;
		}
	}

}
