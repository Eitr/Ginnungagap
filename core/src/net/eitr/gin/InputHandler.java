package net.eitr.gin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;

public class InputHandler implements InputProcessor {

	private OrthographicCamera camera;
	private WorldManager world;

	public InputHandler (WorldManager w, OrthographicCamera c) {
		world = w;
		camera = c;
		Gdx.input.setInputProcessor(this);
	}


	public void handleInput (Ship ship, OrthographicCamera cam) {
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			cam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			ship.rotateLeft();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			ship.rotateRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			ship.glide();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			ship.thrust();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			ship.resetPosition();
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
		cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f - Units.WORLD_WIDTH/2F, Units.WORLD_WIDTH/2f - effectiveViewportWidth / 2f);
		cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f -Units.WORLD_HEIGHT/2F, Units.WORLD_HEIGHT/2f - effectiveViewportHeight / 2f);
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


	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		world.buildShip();
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
