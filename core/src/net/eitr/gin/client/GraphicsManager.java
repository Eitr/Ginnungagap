package net.eitr.gin.client;

import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import net.eitr.gin.server.Rock;
import net.eitr.gin.server.ServerMain;
import net.eitr.gin.ship.Projectile;
import net.eitr.gin.ship.ShipPart;

public class GraphicsManager {

	SpriteBatch sprites;
	PolygonSpriteBatch polygons;
	ShapeRenderer shapes;
	
	public GraphicsManager () {

		sprites = new SpriteBatch();
		polygons = new PolygonSpriteBatch();
		shapes = new ShapeRenderer();
	}
	
	public void render () {
		sprites.setProjectionMatrix(cam.combined);
		shapes.setProjectionMatrix(cam.combined);
		polygons.setProjectionMatrix(cam.combined);

		//sprites.begin();
		//sprites.end();

		polygons.begin();
		for(PolygonData rock : g.rocks) {
			rock.sprite.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		for(ShipData ship : g.ships) {
			shapes.identity();
			shapes.translate(ship.x, ship.y, 0);
			shapes.rotate(0, 0, 1, ship.angle);
			
			for (ShapeData part : ship.parts) {
				shapes.setColor(part.color);
				
				if (part instanceof RectData) {
					shapes.rect(part.x,part.y,part.width,part.height);
				} else if (part instanceof CircleData) {
					shapes.circle(part.x,part.y,part.radius);
				} else {
					throw new Exception("Unknown shape: "+part.toString());
				}
				
				p.draw(shapes);
			}
			shapes.rotate(0, 0, 1, -ship.angle);
			shapes.translate(-ship.x, -ship.y, 0);
	
		}
		for (ShapeData projectile : g.shapes) {
			shapes.setColor(projectile.color);
			if (part instanceof RectData) {
				shapes.rect(projectile.x,projectile.y,projectile.width,projectile.height);
			} else if (part instanceof CircleData) {
				shapes.circle(projectile.x,projectile.y,projectile.radius);
			} else {
				throw new Exception("Unknown shape: "+part.toString());
			}
		}
		shapes.end();
		
		ServerMain.gui.debug("builder","("+(int)newPart.pos.x+","+(int)newPart.pos.y+")");
		ServerMain.gui.debug("mouse","("+(int)mouse.x+","+(int)mouse.y+")");
		ServerMain.gui.debug("collision",ship.intersects(newPart));
		ServerMain.gui.debug("part type",buildType);
		ServerMain.gui.debug("parts",parts.size);
		ServerMain.gui.debug("mass",body.getMass());
		ServerMain.gui.debug("bullets", projectiles.size);
	}

	//TODO: server side
	GraphicsData g = new GraphicsData();
		
	public void WORLDdraw (GraphicsData g) {
		for(Rock rock : rocks) {
			rock.draw(g);
		}
		ship.draw(g);
		Iterator<Projectile> ps = projectiles.iterator();
		while (ps.hasNext()) {
			Projectile p = ps.next();
			p.draw(g);
		}
	}
	
	public void SHIPdraw (GraphicsData g) {
		ShipData shipData = new ShipData(body.getPosition().x, body.getPosition().y, body.getAngle());
		shipBuilder.draw(shipData);
		for (ShipPart p : parts.values()) {
			p.draw(g);
			p.update(shooting);//TODO move to simulation
		}
		g.ships.add(shipData);
	}
	

	public void BUILDERdraw (ShipData shipData) {
		if (!isBuilding) {
			return;
		}
		float x = mouse.x-ship.getX();
		float y = mouse.y-ship.getY();
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		newPart.pos = new Vector2(MathUtils.cos(angle-ship.body.getAngle())*dist,MathUtils.sin(angle-ship.body.getAngle())*dist);

		newPart.draw(shipData);
	}
	

	public void PARTdraw (ShipData shipData) {
		ShapeData shape;
		switch(drawType) {
		case CIRCLE: shape = new CircleData(pos.x, pos.y, radius); break;
		case RECT: shape = new RectData(pos.x-width/2f, pos.y-height/2f, width, height); break;
		default: break;
		}
		shape.setColor(0, health/100f, health/100f, 1);
		shipData.parts.add(shape);
	}

	public void ROCKdraw (GraphicsData g) {
		g.rocks.add(new PolygonData(body.getPosition().x, body.getPosition().y, body.getAngle(), sprite));
	}

	public void PROJECTILEdraw (GraphicsData g) {
		RectData rect = new RectData(body.getPosition().x-size/2, body.getPosition().y-size/2, size, size);
		rect.setColor(1f, 0f, 0f, 1f);
		g.shapes.add(rect);
	}


	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
	}
}
