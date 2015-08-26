package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import net.eitr.gin.Units.DrawShapeType;

public class ShipPart {

	Body ship;
	DrawShapeType type;
	Vector2 pos;

	float [] data;


	public ShipPart (Body b, Vector2 p, float w, float h, float a) {
		ship = b;
		pos = p;

		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		setMaterialProperties(fDef);

		shape.setAsBox(w/2f, h/2f, p, a);
		fDef.shape = shape;
		ship.createFixture(fDef);
		shape.dispose();

		type = DrawShapeType.RECT;
		data = new float[]{w,h};
	}

	public ShipPart (Body b, Vector2 p, float r) {
		ship = b;
		pos = p;

		CircleShape shape = new CircleShape();
		FixtureDef fDef = new FixtureDef();
		setMaterialProperties(fDef);

		shape.setRadius(r);
		shape.setPosition(p);

		fDef.shape = shape;
		ship.createFixture(fDef);
		shape.dispose();

		type = DrawShapeType.CIRCLE;
		data = new float[]{r};
	}

	private void setMaterialProperties (FixtureDef def) {
		def.density = 1f;
		def.friction = 0.4f;
		def.restitution = 0.3f;
	}

	public void draw (ShapeRenderer g) {
		switch(type) {
		case CIRCLE: 
			g.circle(pos.x, pos.y, data[0]);
			break;
		case POLYGON:
			break;
		case RECT:
			float w = data[0];
			float h = data[1];
			g.rect(-pos.x-w/2f, -pos.y-h/2f, w, h);
			break;
		}
	}
	
	//TODO polygon intersection
	public boolean intersects (ShipPart part) {
		if (type == DrawShapeType.CIRCLE && part.type == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(pos,data[0]), new Circle(part.pos,part.data[0]));
		}
		else if (type == DrawShapeType.RECT && part.type == DrawShapeType.CIRCLE) {
			return Intersector.overlaps(new Circle(part.pos,part.data[0]),new Rectangle(pos.x-data[0]/2,pos.y-data[1]/2,data[0],data[1]));
		}
		else if (type == DrawShapeType.CIRCLE && part.type == DrawShapeType.RECT) {
			return Intersector.overlaps(new Circle(pos,data[0]),new Rectangle(part.pos.x-part.data[0]/2,part.pos.y-part.data[1]/2,part.data[0],part.data[1]));
		}
		return false;
	}

}
