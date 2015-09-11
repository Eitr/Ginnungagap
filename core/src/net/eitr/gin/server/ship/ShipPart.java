package net.eitr.gin.server.ship;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import net.eitr.gin.Units.*;
import net.eitr.gin.network.CircleData;
import net.eitr.gin.network.RectData;
import net.eitr.gin.network.ShapeData;
import net.eitr.gin.network.ShipData;
import net.eitr.gin.server.WorldManager;

public class ShipPart {

	protected Body ship;
	protected DrawShapeType drawType;
	protected ShipPartType type;
	protected Vector2 pos;
	private int id;
	protected float width,height,angle,radius;
	protected float health;
	protected Color color; //TODO individual part color

	// RECT shape
	public ShipPart (int i, Body b, ShipPartType t, Vector2 p, float w, float h, float a) {
		init(i,b,t,p);
		drawType = DrawShapeType.RECT;
		width = w;
		height = h;
		angle = a;
	}

	// CIRCLE shape
	public ShipPart (int i, Body b, ShipPartType t, Vector2 p, float r) {
		init(i,b,t,p);
		drawType = DrawShapeType.CIRCLE;
		radius = r;
	}

	private void init (int i, Body b, ShipPartType t, Vector2 p) {
		id = i;
		ship = b;
		type = t;
		pos = p;
		health = 100;
		color = new Color(0,0,1,1);
	}
	
	protected void update (boolean isShooting, WorldManager world) {}
	
	//TODO polygon intersection
	boolean intersects (ShipPart part) {
		if (drawType == DrawShapeType.CIRCLE && part.drawType == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(pos,radius), new Circle(part.pos,part.radius));
		}
		else if (drawType == DrawShapeType.RECT && part.drawType == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(part.pos,part.radius),new Rectangle(pos.x-width/2,pos.y-height/2,width,height));
		}
		else if (drawType == DrawShapeType.CIRCLE && part.drawType == DrawShapeType.RECT) {
			return Intersector.overlaps(new Circle(pos,radius),new Rectangle(part.pos.x-part.width/2,part.pos.y-part.height/2,part.width,part.height));
		}
		else if (drawType == DrawShapeType.RECT && part.drawType == DrawShapeType.RECT) {
			return Intersector.overlaps(new Rectangle(pos.x-width/2,pos.y-height/2,width,height),new Rectangle(part.pos.x-part.width/2,part.pos.y-part.height/2,part.width,part.height));
		}
		return false;
	}
	
	void damage (float damage) {
		health = MathUtils.clamp(health-damage, 0, 100);
	}
	
	public int getId () {
		return id;
	}

	void getGraphics (ShipData shipData) {
		ShapeData shape = null;
		switch(drawType) {
		case CIRCLE: shape = new CircleData(pos.x, pos.y, radius); break;
		case RECT: shape = new RectData(pos.x-width/2f, pos.y-height/2f, width, height); break;
		default: return;
		}
		shape.setColor(health/100f*color.r, health/100f*color.g, health/100f*color.b, 1);
		shipData.parts.add(shape);
	}
}
