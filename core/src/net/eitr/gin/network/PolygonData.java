package net.eitr.gin.network;

import com.badlogic.gdx.math.MathUtils;

public class PolygonData extends ShapeData {
	public float [] vertices;
	public float angle;
	
	public PolygonData () {}
	
	public PolygonData (float px, float py, float a, float[] v) {
		super(px,py);
		angle = (float)(a*180f/MathUtils.PI);
		vertices = v;
	}
}
