package net.eitr.gin.client;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

import net.eitr.gin.network.InputData;

public class InputHandler implements InputProcessor {

	private OrthographicCamera camera;
	private Array<Integer> keysDown, keysUp;
	private Array<Integer> mouseDown, mouseUp;
	private float mx,my;
	private boolean send;

	public InputHandler (OrthographicCamera c) {
		camera = c;
		keysDown = new Array<Integer>();
		keysUp = new Array<Integer>();
		mouseDown = new Array<Integer>();
		mouseUp = new Array<Integer>();
	}
	
	//TODO whether new input has arrived
	private boolean shouldSend () {
		return send;
	}
	
	private void resetInput () {
		keysDown.clear();
		keysUp.clear();
		mouseDown.clear();
		mouseUp.clear();
		send = false;
	}
	
	/** Used to send player input to server */
	InputData getInputData () {
		InputData data = new InputData();
		data.mx = mx;
		data.my = my;
		data.keysDown = new Array<Integer>(keysDown);
		data.keysUp = new Array<Integer>(keysUp);
		data.mouseDown = new Array<Integer>(mouseDown);
		data.mouseUp = new Array<Integer>(mouseUp);
		
		resetInput();
		return data;
	}

	void handleInput () {
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
		mouseDown.add(button);
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		mouseUp.add(button);
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		camera.zoom += amount/10f*camera.zoom;
		return false;
	}

}
