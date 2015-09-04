package net.eitr.gin.network;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.MathUtils;

public class PolygonData extends ShapeData {
	public PolygonSprite sprite;
	public float angle;
	
	public PolygonData (float px, float py, float a, PolygonSprite s) {
		super(px,py);
		angle = (float)(a*180f/MathUtils.PI);
		sprite = s;
		sprite.setRotation(angle);
		sprite.setPosition(x,y);
	}
}
