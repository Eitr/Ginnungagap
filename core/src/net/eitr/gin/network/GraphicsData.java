package net.eitr.gin.network;

import com.badlogic.gdx.utils.Array;

public class GraphicsData {
	public Array<ShipData> ships;
	public Array<PolygonData> rocks;
	public Array<ShapeData> shapes;
	
	public GraphicsData () {
		ships = new Array<ShipData>();
		rocks = new Array<PolygonData>();
		shapes = new Array<ShapeData>();
	}
	
	public void reset () {
		ships.clear();
		rocks.clear();
		shapes.clear();
	}
}
