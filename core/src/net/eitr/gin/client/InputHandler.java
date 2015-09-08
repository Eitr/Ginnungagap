package net.eitr.gin.client;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

import net.eitr.gin.network.InputData;

public class InputHandler implements InputProcessor {

	private OrthographicCamera camera;
	private Array<Integer> keysDown;
	private Array<Integer> keysUp;
	private boolean mouseDown, mouseUp;
	private float mx,my;
	private boolean send;

	public InputHandler (OrthographicCamera c) {
		camera = c;
		keysDown = new Array<Integer>();
		keysUp = new Array<Integer>();
	}
	
	//TODO
	public boolean shouldSend () {
		return send;
	}
	
	public void resetInput () {
		mouseDown = false;
		mouseUp = false;
		keysDown.clear();
		keysUp.clear();
		send = false;
	}
	
	public InputData getInputData () {
		InputData data = new InputData();
		data.mx = mx;
		data.my = my;
		data.mouseDown = mouseDown;
		data.mouseUp = mouseUp;
		data.keysDown = new Array<Integer>(keysDown);
		data.keysUp = new Array<Integer>(keysUp);
		
		resetInput();
		return data;
	}

	public void handleInput () {
		if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
			camera.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			camera.zoom += 0.02;
		}
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 m = camera.unproject(new Vector3(screenX,screenY,0));
		mx = m.x;
		my = m.y;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Vector3 m = camera.unproject(new Vector3(screenX,screenY,0));
		mx = m.x;
		my = m.y;
		return false;
	}


	@Override
	public boolean keyDown(int keycode) {
		keysDown.add(keycode);
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		keysUp.add(keycode);
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseDown = true;
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseUp = true;
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		camera.zoom += amount/10f*camera.zoom;
		return false;
	}

}
