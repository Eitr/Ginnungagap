package net.eitr.gin;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;

import net.eitr.gin.ship.Ship;

public class InputHandler implements InputProcessor {

	private OrthographicCamera camera;
	private Ship ship;

	public InputHandler (Ship s, OrthographicCamera c) {
		ship = s;
		camera = c;
	}


	public void handleInput () {
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			ship.rotateLeft();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			ship.rotateRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {

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

		camera.position.set(ship.getX(), ship.getY(), 0);

		// Sets min/max for zoom
		camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 3f);

		// Keep the camera inside the boundaries of the map
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f - Units.WORLD_WIDTH/2F, Units.WORLD_WIDTH/2f - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f -Units.WORLD_HEIGHT/2F, Units.WORLD_HEIGHT/2f - effectiveViewportHeight / 2f);
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 m = camera.unproject(new Vector3(screenX,screenY,0));
		ship.shipBuilder.setMousePosition(new Vector2(m.x,m.y));
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 m = camera.unproject(new Vector3(screenX,screenY,0));
		ship.shipBuilder.setMousePosition(new Vector2(m.x,m.y));
		return false;
	}


	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.B: 
			ship.shipBuilder.isBuilding = !ship.shipBuilder.isBuilding;
			break;
		}
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			ship.shooting = true;
		}
		if (button == Input.Buttons.RIGHT) {
			ship.shipBuilder.buildShip();
		}
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		ship.shooting = false;
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		camera.zoom += amount/10f*camera.zoom;
		return false;
	}

}
