package net.eitr.gin.network;

public class CircleData extends ShapeData {
	public float radius;
	
	public CircleData () {}
	
	public CircleData (float px, float py, float r) {
		super(px,py,0);
		radius = r;
	}
}