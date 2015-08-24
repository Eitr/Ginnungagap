package net.eitr.gin.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.*;

public class Drop extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // defaults to Arial
//		this.setScreen(new MainMenuScreen(this));
		this.setScreen(new GameScreen(this));
	}
	
	public void render () {
		super.render(); // important to render screens!
	}
	
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

}
