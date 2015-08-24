package net.eitr.gin;

public class DebugInterface extends Stage {
	
	Skin skin;
	Label fpsLabel;
	
	public DebugInterface (Viewport view) {
		super(view);
		
		skin = new Skin();
		
		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());
		fpsLabel = new Label("fps",skin);
		this.addActor(fpsLabel);
	}
	
	public void update () {
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		act(Gdx.graphics.getDeltaTime());
		draw();
	}
	
	@Override
	public void dispose () {
		super();
		skin.dispose();
	}
}
