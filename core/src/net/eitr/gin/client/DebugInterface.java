package net.eitr.gin.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DebugInterface extends Stage {
	
	private LabelStyle style;
	private Viewport view;
	private int gap = 20;
	private int z = 1;
	
	private ObjectMap<String,Label> map = new ObjectMap<String,Label>();
	
	public DebugInterface (Viewport v) {
		super(v);
		view = v;
		
		style = new LabelStyle();
		style.font = new BitmapFont();
		style.fontColor = Color.WHITE;
	}
	
	void update () {
		act(Gdx.graphics.getDeltaTime());
		synchronized (map){
			draw();
		}
	}
	
	void debug (String s, Object value) {
		if (s.equals("")) {
			return;
		}
		synchronized (map) {
			if (map.containsKey(s)) {
				map.get(s).setText(s+": "+value.toString());
				
			} else {
				Label label = new Label(s+": "+value.toString(), style);
				label.setPosition(10, view.getWorldHeight()-gap*z++);
				addActor(label);
				map.put(s, label);
			}
		}
	}
}
