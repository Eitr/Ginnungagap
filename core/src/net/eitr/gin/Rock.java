package net.eitr.gin;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Rock {

	private float size;
	private PolygonSprite sprite;
	private Body body;

	public Rock (Body b) {
		body = b;
		size = MathUtils.random(10, 20);
		float [] vertices = createPolygon(size);
		
		// Create the fixture definition for this body
		PolygonShape shape = new PolygonShape();
		shape.set(vertices);
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.density = 1.0f;
		fDef.friction = 0.4f;
		fDef.restitution = 0.0f;
		body.createFixture(fDef);
		shape.dispose();

		// Create sprite for drawing
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] += (i%2==0)? body.getPosition().x:body.getPosition().y;
		}
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pix.setColor(0.8f, 0.8f, 0.8f, 1);
		pix.fill();
		PolygonRegion region = new PolygonRegion(new TextureRegion(new Texture(pix)),vertices,new short[]
				{0,1,2,0,2,3,0,3,4,0,4,5,0,5,6,0,6,7,1,2,4,3,4,6});
		sprite = new PolygonSprite(region);
	}

	public void draw (PolygonSpriteBatch g) {
		sprite.draw(g);
	}

	private float[] createPolygon (float s) {
		float [] vertices = new float[16];
		int i = 0;
		vertices[i++] = MathUtils.random(0,s/2);
		vertices[i++] = 0;
		vertices[i++] = MathUtils.random(s/2,s);
		vertices[i++] = 0;
		vertices[i++] = s;
		vertices[i++] = MathUtils.random(0,s/2);
		vertices[i++] = s;
		vertices[i++] = MathUtils.random(0,s/2);
		vertices[i++] = MathUtils.random(s/2,s);
		vertices[i++] = s;
		vertices[i++] = MathUtils.random(0,s/2);
		vertices[i++] = s;
		vertices[i++] = 0;
		vertices[i++] = MathUtils.random(s/2,s);
		vertices[i++] = 0;
		vertices[i++] = MathUtils.random(0,s/2);
		
		return vertices;
	}
}
