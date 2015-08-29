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
	Vector2 mouse;

	public ShipBuilder (Ship s) {
		ship = s;
		scale = 4;
		rotation = 0;
//		shape = DrawShapeType.RECT;
		shape = DrawShapeType.CIRCLE;
		buildNewPart();
		mouse = new Vector2(0,0);
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

	public void buildShip () {
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
		float x = mouse.x-ship.getX();
		float y = mouse.y-ship.getY();
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		newPart.pos = new Vector2(MathUtils.cos(angle-ship.body.getAngle())*dist,MathUtils.sin(angle-ship.body.getAngle())*dist);
		
		newPart.draw(g);
		
		Main.gui.debug("builder","("+(int)newPart.pos.x+","+(int)newPart.pos.y+")");
		Main.gui.debug("mouse","("+(int)mouse.x+","+(int)mouse.y+")");
		Main.gui.debug("collision",ship.intersects(newPart));
	}
	
	public void setMousePosition (Vector2 m) {
		mouse = m;
	}
}
