package net.eitr.gin;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;

import net.eitr.gin.Units.DrawShapeType;
import net.eitr.gin.Units.ShipPartType;
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
		float scale = 0.2f;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			ship.shipBuilder.width -= scale;
			ship.shipBuilder.buildNewPart();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			ship.shipBuilder.width += scale;
			ship.shipBuilder.buildNewPart();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			ship.shipBuilder.height += scale;
			ship.shipBuilder.buildNewPart();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			ship.shipBuilder.height -= scale;
			ship.shipBuilder.buildNewPart();
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
		case Input.Keys.NUM_1: ship.shipBuilder.buildType = ShipPartType.HULL; break;
		case Input.Keys.NUM_2: ship.shipBuilder.buildType = ShipPartType.WEAPON; break;
		case Input.Keys.S: 
			switch(ship.shipBuilder.shape) {
			case RECT: ship.shipBuilder.shape = DrawShapeType.CIRCLE; break;
			case CIRCLE: ship.shipBuilder.shape = DrawShapeType.RECT; break;
			case POLYGON: break;
			}
			break;
		}
		ship.shipBuilder.buildNewPart();
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
