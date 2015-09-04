package net.eitr.gin.client;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.esotericsoftware.minlog.Log;

import net.eitr.gin.network.*;

public class GraphicsManager {

	SpriteBatch sprites;
	PolygonSpriteBatch polygons;
	ShapeRenderer shapes;
	GraphicsData data;
	
	public GraphicsManager () {
		sprites = new SpriteBatch();
		polygons = new PolygonSpriteBatch();
		shapes = new ShapeRenderer();
		data = new GraphicsData();
	}
	
	public void setGraphicsData (GraphicsData d) {
		data = d;
	}
	
	public void render (OrthographicCamera camera) {
		sprites.setProjectionMatrix(camera.combined);
		shapes.setProjectionMatrix(camera.combined);
		polygons.setProjectionMatrix(camera.combined);

		//sprites.begin();
		//sprites.end();

		polygons.begin();
		for(PolygonData rock : data.rocks) {
			Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGB888);
			pix.setColor(rock.color);
			pix.fill();
			PolygonRegion region = new PolygonRegion(new TextureRegion(new Texture(pix)), rock.vertices, Rock.getTriangles(rock.vertices));
			PolygonSprite sprite = new PolygonSprite(region);
			sprite.setOrigin(0, 0);
			sprite.setRotation(rock.angle);
			sprite.setPosition(rock.x,rock.y);
			sprite.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		for(ShipData ship : data.ships) {
			shapes.identity();
			shapes.translate(ship.x, ship.y, 0);
			shapes.rotate(0, 0, 1, ship.angle);
			
			for (ShapeData part : ship.parts) {
				shapes.setColor(part.color);
				
				if (part instanceof RectData) {
					shapes.rect(part.x,part.y,((RectData)part).width,((RectData)part).height);
				} else if (part instanceof CircleData) {
					shapes.circle(part.x,part.y,((CircleData)part).radius);
				} else {
					System.err.println("Unknown shape: "+part.toString());
				}
			}
			shapes.rotate(0, 0, 1, -ship.angle);
			shapes.translate(-ship.x, -ship.y, 0);
	
		}
		for (ShapeData projectile : data.shapes) {
			shapes.setColor(projectile.color);
			if (projectile instanceof RectData) {
				shapes.rect(projectile.x,projectile.y,((RectData)projectile).width,((RectData)projectile).height);
			} else if (projectile instanceof CircleData) {
				shapes.circle(projectile.x,projectile.y,((CircleData)projectile).radius);
			} else {
				System.err.println("Unknown shape: "+projectile.toString());
			}
		}
		shapes.end();
		
		//TODO pass in the debug gui
		//TODO latency
//		ServerMain.gui.debug("builder","("+(int)newPart.pos.x+","+(int)newPart.pos.y+")");
//		ServerMain.gui.debug("mouse","("+(int)mouse.x+","+(int)mouse.y+")");
//		ServerMain.gui.debug("collision",ship.intersects(newPart));
//		ServerMain.gui.debug("part type",buildType);
//		ServerMain.gui.debug("parts",parts.size);
//		ServerMain.gui.debug("mass",body.getMass());
//		ServerMain.gui.debug("bullets", projectiles.size);
//		ServerMain.gui.debug("zoom", (int)(camera.zoom*100)/100f);
	}


	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
	}
}
