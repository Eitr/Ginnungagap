package net.eitr.gin.network;

public class RectData extends ShapeData {
	public float width,height;
	
	public RectData () {}
	
	public RectData (float px, float py, float w, float h) {
		super(px,py);
		width = w;
		height = h;
	}
}