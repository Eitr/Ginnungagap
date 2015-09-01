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
	

	public void WORLDdraw (OrthographicCamera cam) {
		sprites.setProjectionMatrix(cam.combined);
		shapes.setProjectionMatrix(cam.combined);
		polygons.setProjectionMatrix(cam.combined);

		sprites.begin();
		sprites.end();

		polygons.begin();
		for(Rock rock : rocks) {
			rock.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		ship.draw(shapes);
		Iterator<Projectile> ps = projectiles.iterator();
		while (ps.hasNext()) {
			Projectile p = ps.next();
			p.draw(shapes);
		}
		Main.gui.debug("bullets", projectiles.size);
		shapes.end();
	}
	
	public void SHIPdraw (ShapeRenderer g) {
		g.identity();
		//TODO: translate vs set position (parts are local position based)
		//TODO: rotation slightly adjusts everything else in the world
		g.translate(body.getPosition().x, body.getPosition().y, 0);
		g.rotate(0, 0, 1, (float)(body.getAngle()/Math.PI*180f));
		shipBuilder.draw(g);
		g.setColor(1, 1, 0, 1);
		for (ShipPart p : parts.values()) {
			p.draw(g);
			p.update(shooting);
		}
		if (thrusting) {
			g.setColor(1, 0, 0, 1);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			g.rect(-MathUtils.random(0,width/2)-width/2,-MathUtils.random(-height/2,height/2),0.5f,0.5f);
			thrusting = false;
		}
		g.rotate(0, 0, 1, -(float)(body.getAngle()/Math.PI*180f));
		g.translate(-body.getPosition().x, -body.getPosition().y, 0);

		Main.gui.debug("parts",parts.size);
		Main.gui.debug("mass",body.getMass());
	}
	

	public void BUILDERdraw (ShapeRenderer g) {
		if (!isBuilding) {
			return;
		}
		float x = mouse.x-ship.getX();
		float y = mouse.y-ship.getY();
		float dist = (float) Math.sqrt(x*x+y*y);
		float angle = MathUtils.atan2(y, x);
		newPart.pos = new Vector2(MathUtils.cos(angle-ship.body.getAngle())*dist,MathUtils.sin(angle-ship.body.getAngle())*dist);

		newPart.draw(g);

		ServerMain.gui.debug("builder","("+(int)newPart.pos.x+","+(int)newPart.pos.y+")");
		ServerMain.gui.debug("mouse","("+(int)mouse.x+","+(int)mouse.y+")");
		ServerMain.gui.debug("collision",ship.intersects(newPart));
		ServerMain.gui.debug("part type",buildType);
	}
	

	public void PARTdraw (ShapeRenderer g) {
		g.setColor(0, health/100f, health/100f, 1);
		switch(drawType) {
		case CIRCLE: 
			g.circle(pos.x, pos.y, radius);
			break;
		case RECT:
			float w = width;
			float h = height;
			g.rect(pos.x-w/2f, pos.y-h/2f, w, h);
			break;
		case POLYGON:
			break;
		}
	}

	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
	}
}
