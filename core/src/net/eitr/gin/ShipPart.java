package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import net.eitr.gin.Units.DrawShapeType;

public class ShipPart {

	Body ship;
	DrawShapeType type;
	Vector2 origin;

	float [] drawData;


	public ShipPart (Body b, Vector2 o, float w, float h, float a) {
		ship = b;
		origin = o;

		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		setMaterialProperties(fDef);

		shape.setAsBox(w/2f, h/2f, o, a);
		fDef.shape = shape;
		ship.createFixture(fDef);
		shape.dispose();

		type = DrawShapeType.RECT;
		drawData = new float[]{w,h};
	}

	public ShipPart (Body b, Vector2 o, float r) {
		ship = b;
		origin = o;

		CircleShape shape = new CircleShape();
		FixtureDef fDef = new FixtureDef();
		setMaterialProperties(fDef);

		shape.setRadius(r);
		shape.setPosition(new Vector2(o.x,o.y));

		fDef.shape = shape;
		ship.createFixture(fDef);
		shape.dispose();

		type = DrawShapeType.CIRCLE;
		drawData = new float[]{r};
	}

	private void setMaterialProperties (FixtureDef def) {
		def.density = 1f;
		def.friction = 0.4f;
		def.restitution = 0.3f;
	}

	public void draw (ShapeRenderer g) {
		switch(type) {
		case CIRCLE: 
			g.circle(origin.x, origin.y, drawData[0]);
			break;
		case POLYGON:
			break;
		case RECT:
			float w = drawData[0];
			float h = drawData[1];
			g.rect(-origin.x-w/2f, -origin.y-h/2f, w, h);
			break;
		}
	}

}
