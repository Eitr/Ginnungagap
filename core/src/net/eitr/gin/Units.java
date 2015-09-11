package net.eitr.gin;

public class Units {
	
	public static final String version = "v0.1.0";
	
	public static final int WORLD_WIDTH = 500;
	public static final int WORLD_HEIGHT = 500;

	public static final int VIEW_SIZE = 8*16;
	public static final int MAX_VIEW_DIST = VIEW_SIZE;
	
	public static final int MAX_SHIP_PARTS = 30;

	public static final float TIME_STEP = 1/300f;
	
	public static enum DrawShapeType {POLYGON,CIRCLE,RECT};
	public static enum ShipPartType {WEAPON,HULL,THRUSTER};
	
	/** Every physical object must extend WorldBody (used for collision) */
	public static enum WorldBodyType {EDGE,SHIP,ROCK,PROJECTILE};

	public static final int TCP_PORT = 54555;
	public static final int UDP_PORT = 54777;

	public static final int NETWORK_OBJECT_SIZE = 10240;
	public static final int NETWORK_BUFFER_SIZE = NETWORK_OBJECT_SIZE * 8;
}
