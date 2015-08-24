package net.eitr.gin;

public class InputHandler implements InputProcessor {
	
	private OrthographicCamera camera;
	private World world;
	
	public InputHandler (OrthographicCamera c, World w) {
		camera = c;
		world = w;
		Gdx.input.setInputProcessor(this);
	}
	
	
	private void handleInput (Ship ship, OrthographicCamera cam) {
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			cam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			ship.rotateLeft();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			ship.rotateRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			ship.thrust();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			ship.reset();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			System.out.println("Pos: "+ship.getPosition());
		}

		cam.position.set(ship.getX(), ship.getY(), 0);

		// Sets min/max for zoom
		cam.zoom = MathUtils.clamp(cam.zoom, 0.5f, 3f);

		// Keep the camera inside the boundaries of the map
		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;
		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f - WORLD_WIDTH/2F, WORLD_WIDTH/2f - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f - WORLD_HEIGHT/2F, WORLD_HEIGHT/2f - effectiveViewportHeight / 2f);
	}
	
	
  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
  	world.setMousePosition(camera.unproject(new Vector3(screenX,screenY,0)));
    return false;
  }
  
}
