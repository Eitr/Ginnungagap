package net.eitr.gin;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.eitr.gin.Units.DrawShapeType;

public class ShipBuilder {

	float scale,rotation;
	DrawShapeType shape;
	ShipPart newPart;
	Ship ship;

	public ShipBuilder (Ship s) {
		ship = s;
		scale = 4;
		rotation = 0;
//		shape = DrawShapeType.RECT;
		shape = DrawShapeType.CIRCLE;
		buildNewPart();
	}
	
	private void buildNewPart () {

		switch (shape) {
		case RECT:
			newPart = new ShipPart(ship.getNewPartId(), ship.body, new Vector2(0,0), scale, scale, rotation);
			break;
		case CIRCLE:
			newPart = new ShipPart(ship.getNewPartId(), ship.body, new Vector2(0,0), scale);
			break;
		case POLYGON:
			break;
		default:
			newPart = null;
			break;
		}
	}

	public void buildShip (Ship ship) {
		if (newPart != null && ship.intersects(newPart)) {
			ship.addNewPart(newPart);
			buildNewPart();
		}
	}

	public void scale (float size) {
		scale += size;
		scale = MathUtils.clamp(scale, 0, 10);
	}

	public void rotate (float angle) {
		rotation = (angle+360)%360;
	}

	public void draw (ShapeRenderer g) {
		newPart.draw(g);
		Main.gui.debug("builder","("+(int)newPart.pos.x+","+(int)newPart.pos.y+")");
		Main.gui.debug("mouse","("+(int)m.x+","+(int)m.y+")");
		Main.gui.debug("collision",ship.intersects(newPart));
	}
	Vector2 m = new Vector2(0,0);
	public void setMousePosition (Vector2 m) {
		this.m = m;
		//TODO figure out rotation 
//		newPart.pos = new Vector2(MathUtils.cos(ship.body.getAngle())*(m.x-ship.getX())+m.x, MathUtils.sin(ship.body.getAngle())*(m.y-ship.getY())+m.y);
		newPart.pos = new Vector2(m.x, m.y);
	}
}
