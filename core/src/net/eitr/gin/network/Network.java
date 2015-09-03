package net.eitr.gin.network;

import com.esotericsoftware.kryo.Kryo;

public class Network {

	public static void registerClasses (Kryo kryo) {
//		Kryo kryo = server.getKryo();
//	    kryo.register(SomeRequest.class);
//	    kryo.register(SomeResponse.class);
//	    Kryo kryo = client.getKryo();
//	    kryo.register(SomeRequest.class);
//	    kryo.register(SomeResponse.class);
		
		kryo.register(ShapeData.class);
		kryo.register(InputData.class);
		
	}
}
