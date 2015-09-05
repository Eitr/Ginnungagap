package net.eitr.gin.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import net.eitr.gin.network.*;
import net.eitr.gin.server.Rock;

public class GraphicsManager {

	SpriteBatch sprites;
	PolygonSpriteBatch polygons;
	ShapeRenderer shapes;
	GraphicsData data;
	TextureRegion rockTexture;
	
	public GraphicsManager () {
		sprites = new SpriteBatch();
		polygons = new PolygonSpriteBatch();
		shapes = new ShapeRenderer();
		data = new GraphicsData();
		
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pix.setColor(new Color(1,1,1,1));
		pix.fill();
		rockTexture = new TextureRegion(new Texture(pix));
	}
	
	public void setGraphicsData (GraphicsData d) {
		data.rocks.clear();
		data.shapes.clear();
		data.ships.clear();
		data = d;
	}
	
	public void render (OrthographicCamera camera) {
		sprites.setProjectionMatrix(camera.combined);
		shapes.setProjectionMatrix(camera.combined);
		polygons.setProjectionMatrix(camera.combined);
		
		//sprites.begin();
		//sprites.end();

		polygons.begin();
		Array<PolygonData> rocks = new Array<PolygonData>(data.rocks);
		for(PolygonData rock : rocks) {
			if (rockTexture == null) {
				System.out.println("TEXTURE");
			} else if (rock.vertices == null) {
				System.out.println("TEXTURE");
			}
			PolygonRegion region = new PolygonRegion(rockTexture, rock.vertices, Rock.getTriangles(rock.vertices));
			PolygonSprite sprite = new PolygonSprite(region);
			sprite.setColor(rock.color);
			sprite.setOrigin(0, 0);
			sprite.setRotation(rock.angle);
			sprite.setPosition(rock.x,rock.y);
			sprite.draw(polygons);
		}
		polygons.end();

		shapes.begin(ShapeType.Filled);
		for (ShipData ship : data.ships) {
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
		
		//TODO latency
		GameScreen.gui.debug("FPS", Gdx.graphics.getFramesPerSecond());
		GameScreen.gui.debug("zoom", (int)(camera.zoom*100)/100f);
		
		for (String s : data.debug) {
			GameScreen.gui.debug(s.substring(0,s.indexOf(":")), s.substring(s.indexOf(":")+2));
		}
	}


	public void dispose() {
		sprites.dispose();
		shapes.dispose();
		polygons.dispose();
		rockTexture.getTexture().dispose();
	}
}
