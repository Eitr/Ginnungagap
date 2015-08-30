package net.eitr.gin;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import net.eitr.gin.Units.WorldBodyType;

public class Rock extends WorldBody {

	private float size;
	private PolygonSprite sprite;
	private Body body;

	public Rock (Body b) {
		super(WorldBodyType.ROCK);
		
		body = b;
		body.setUserData(this);
		size = MathUtils.random(6, 24);
		
		//		float [] vertices = generateConcavePolygon(size);
		float [] vertices = generateCircularPolygon(size);

		PolygonShape shape = new PolygonShape();
		shape.set(vertices);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 10.0f;
		fDef.friction = 0.4f;
		fDef.restitution = 0.1f;
		body.createFixture(fDef);
		shape.dispose();

		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pix.setColor(0.8f, 0.8f, 0.8f, 1);
		pix.fill();
		PolygonRegion region = new PolygonRegion(new TextureRegion(new Texture(pix)),vertices, getTriangles(vertices));

		//		EarClippingTriangulator ect = new EarClippingTriangulator();
		//		PolygonRegion region = new PolygonRegion(new TextureRegion(new Texture(pix)),vertices,ect.computeTriangles(vertices).toArray());
		
		sprite = new PolygonSprite(region);
		sprite.setOrigin(0, 0);
	}

	private short[] getTriangles (float[] v) {
		short[] points = new short[(v.length-4)/2*3];
		for (int i=0; i < points.length/3; i++) {
			points[i*3] = 0;
			points[i*3+1] = (short)(i+1);
			points[i*3+2] = (short)((i+2));
		}
		return points;
	}

	public void draw (PolygonSpriteBatch g) {
		sprite.setRotation(body.getAngle()*180f/MathUtils.PI);
		sprite.setPosition(body.getPosition().x, body.getPosition().y);
		sprite.draw(g);
	}

	@SuppressWarnings("unused")
	private float[] generateConcavePolygon (float r) {
		int p = 8; // total points around polygon
		float [] vertices = new float[p*2];
		float radians = MathUtils.PI*2f/p; // angle between vertices

		for (int i=0; i<p; i++) {
			float dist = MathUtils.random(r/2,r);
			vertices[i*2] = MathUtils.cos(i*radians)*dist;
			vertices[i*2+1] = MathUtils.sin(i*radians)*dist;
		}
		return vertices;
	}


	private float[] generateCircularPolygon (float r) {
		int p = 8; // total points around polygon (LIMIT 8 VERTICES PER POLYGON)
		float [] vertices = new float[p*2];
		float radians = MathUtils.PI*2f/p; // angle between vertices

		for (int i=0; i < p; i++) {
			float angle = MathUtils.random(0f,radians);
			vertices[i*2] = MathUtils.cos(angle+i*radians)*r;
			vertices[i*2+1] = MathUtils.sin(angle+i*radians)*r;
		}
		return vertices;
	}

}
