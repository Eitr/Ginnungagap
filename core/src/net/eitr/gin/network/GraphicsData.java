package net.eitr.gin.network;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GraphicsData {
	public Array<ShipData> ships;
	public Array<PolygonData> rocks;
	public Array<ShapeData> shapes;
	public float x,y;
	public String[] debug;
	
	public GraphicsData () {
		ships = new Array<ShipData>();
		rocks = new Array<PolygonData>();
		shapes = new Array<ShapeData>();
		debug = new String[0];
	}
	
	public void setPlayerPosition (Vector2 pos) {
		x = pos.x;
		y = pos.y;
	}
	
	public void reset () {
		ships.clear();
		rocks.clear();
		shapes.clear();
	}
}
