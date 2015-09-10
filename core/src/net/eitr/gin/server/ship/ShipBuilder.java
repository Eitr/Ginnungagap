package net.eitr.gin.server.ship;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.eitr.gin.Units.*;
import net.eitr.gin.network.ShipData;

public class ShipBuilder {

	float width,height,rotation;
	DrawShapeType shape;
	ShipPartType buildType;
	ShipPart newPart;
	private Ship ship;
	protected Vector2 mouse;
	protected boolean isBuilding;

	public ShipBuilder (Ship s) {
		ship = s;
		width = 4;
		height = 4;
		rotation = 0;
		shape = DrawShapeType.RECT;
		buildType = ShipPartType.HULL;
		buildNewPart();
		mouse = new Vector2(0,0);
		isBuilding = false;
	}

	//TODO changing types
	void buildNewPart () {
		switch (shape) {
		case RECT:
			switch (buildType){
			case HULL: newPart = new ShipPart(ship.getNewPartId(), ship.body, ShipPartType.HULL, new Vector2(0,0), width, height, rotation); break;
			case WEAPON: newPart = new PartWeapon(ship.getNewPartId(), ship.body, new Vector2(0,0), width, height, rotation); break;
			}
			break;
		case CIRCLE:
			newPart = new ShipPart(ship.getNewPartId(), ship.body, ShipPartType.HULL, new Vector2(0,0), width/2);
			break;
		default:
			newPart = null;
			break;
		}
	}

	void buildShip () {
		if (!isBuilding) {
			return;
		}
		if (newPart != null && ship.intersects(newPart)) {
			addNewPart(newPart);
			buildNewPart();
		}
	}
	
	void addNewPart (ShipPart part) {
		FixtureDef fDef = new FixtureDef();
		fDef.density = 1f;
		fDef.friction = 0.4f;
		fDef.restitution = 0.3f;
		
		switch (part.drawType) {
		case CIRCLE:
			CircleShape circle = new CircleShape();
			circle.setRadius(part.radius);
			circle.setPosition(part.pos);
			fDef.shape = circle;
			ship.body.createFixture(fDef).setUserData(part);
			circle.dispose();
			break;
		case RECT:
			PolygonShape rect = new PolygonShape();
			rect.setAsBox(part.width/2f, part.height/2f, part.pos, part.angle);
			fDef.shape = rect;
			ship.body.createFixture(fDef).setUserData(part);
			rect.dispose();
			break;
		case POLYGON:
			break;
		default:
			return;
		}
		ship.parts.put(part.getId(), part);
	}

	void rotate (float angle) {
		rotation = (angle+360)%360;
	}

	void setMousePosition (Vector2 m) {
		mouse = m;
	}

	void getGraphics (ShipData shipData) {
		if (!isBuilding) {
			return;
		}
		float x = mouse.x-ship.getX();
		float y = mouse.y-ship.getY();
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		newPart.pos = new Vector2(MathUtils.cos(angle-ship.body.getAngle())*dist,MathUtils.sin(angle-ship.body.getAngle())*dist);

		newPart.getGraphics(shipData);
	}
}
