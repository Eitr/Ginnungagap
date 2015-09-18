package net.eitr.gin.network;

public class RectData extends ShapeData {
	public float width,height;
	
	public RectData () {}
	
	public RectData (float px, float py, float a, float w, float h) {
		super(px,py,a);
		width = w;
		height = h;
	}
}