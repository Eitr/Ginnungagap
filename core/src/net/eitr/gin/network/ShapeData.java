package net.eitr.gin.network;

import com.badlogic.gdx.graphics.Color;

public class ShapeData {
	public Color color;
	public float x,y;
	
	public ShapeData () {}
	
 	public ShapeData (float px, float py) {
		x = px;
		y = py;
		color = new Color(1,1,1,1);
	}
	
	public void setColor (float r, float g, float b, float a) {
		color = new Color(r,g,b,a);
	}
}

