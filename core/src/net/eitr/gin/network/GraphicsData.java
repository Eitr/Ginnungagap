package net.eitr.gin.network;

public class GraphicsData {
	public Array<ShipData> ships;
	public Array<PolygonData> rocks;
	public Array<ShapeData> shapes;
	
	public GraphicsData () {
		ships = new Array<ShipData>();
		rocks = new Array<PolygonData>();
		shapes = new Array<ShapeData>();
	}
}
