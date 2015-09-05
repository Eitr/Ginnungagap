package net.eitr.gin.network;

import com.esotericsoftware.kryo.Kryo;
import com.badlogic.gdx.graphics.Color;
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
		kryo.register(Object[].class);
		kryo.register(float[].class);
		kryo.register(Color.class);
		kryo.register(InputData.class);
		kryo.register(String[].class);
	}
}
