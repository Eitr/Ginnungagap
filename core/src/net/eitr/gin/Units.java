package net.eitr.gin;

public class Units {

	public static final int WORLD_WIDTH = 1000;
	public static final int WORLD_HEIGHT = 1000;

	public static final int VIEW_SIZE = 8;
	
	public static final int MAX_SHIP_PARTS = 30;

	public static final float TIME_STEP = 1/300f;
	
	public static enum DrawShapeType {POLYGON,CIRCLE,RECT};
	public static enum ShipPartType {WEAPON,HULL};
	public static enum WorldBodyType {EDGE,SHIP,ROCK,PROJECTILE};
}
