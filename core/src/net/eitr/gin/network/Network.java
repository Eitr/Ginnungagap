package net.eitr.gin.network;

import com.esotericsoftware.kryo.Kryo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Network {

	public static void registerClasses (Kryo kryo) {
		kryo.register(GraphicsData.class);
		kryo.register(Array.class);
		kryo.register(ShapeData.class);
		kryo.register(RectData.class);
		kryo.register(CircleData.class);
		kryo.register(PolygonData.class);
		kryo.register(ShipData.class);
		kryo.register(PolygonSprite.class);
		kryo.register(Object[].class);
		kryo.register(Color.class);
		
		//TODO: send polygons by vertices not sprites
		kryo.register(Rectangle.class);
		kryo.register(PolygonRegion.class);
		kryo.register(TextureRegion.class);
		kryo.register(Texture.class);
		kryo.register(PixmapTextureData.class);
		kryo.register(Gdx2DPixmap.class);
		kryo.register(Pixmap.class);
		kryo.register(Pixmap.Format.class);
	}
}
