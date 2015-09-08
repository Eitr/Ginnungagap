package net.eitr.gin.network;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ShipData extends ShapeData {
	public float angle;
	public Array<ShapeData> parts;
	
	public ShipData () {}
	
 	public ShipData (float px, float py, float a) {
		super(px,py);
		angle = (float)(a*180f/MathUtils.PI);
		parts = new Array<ShapeData>();
	}
}