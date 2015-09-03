package net.eitr.gin.network;

public class ShapeData {
	Color color;
	float x,y;
	
	public ShapeData (float px, float py) {
		x = px;
		y = py;
		color = new Color(1,1,1,1);
	}
	
	public void setColor (float r, float g, float b, float a) {
		color = new Color(r,g,b,a);
	}
}

//TODO
public class RectData extends ShapeData {
	float width,height;
	
	public RectData (float px, float py, float w, float h) {
		super(px,py);
		width = w;
		height = h;
	}
}

public class CircleData extends ShapeData {
	float radius;
	
	public CircleData (float px, float py, float r) {
		super(px,py);
		radius = r;
	}
}

public class ShipData extends ShapeData {
	float angle;
	Array<ShapeData> parts;
	
	public ShipData (float px, float py, float a) {
		super(px,py)
		angle = (float)(a*180f/MathUtils.PI);
		parts = new Array<ShapeData>();
	}
}

public class PolygonData extends ShapeData {
	PolygonSprite sprite;
	float angle;
	
	public PolygonData (float px, float py, float a, PolygonSprite s) {
		super(px,py);
		angle = (float)(a*180f/MathUtils.PI);
		sprite = s;
		sprite.setRotation(angle);
		sprite.setPosition(x,y);
	}
}
