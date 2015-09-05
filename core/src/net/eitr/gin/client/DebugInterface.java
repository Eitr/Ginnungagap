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
	
	Label fpsLabel;
	LabelStyle style;
	Viewport view;
	int gap = 20;
	int z = 1;
	
	ObjectMap<String,Label> map = new ObjectMap<String,Label>();
	
	public DebugInterface (Viewport v) {
		super(v);
		view = v;
		
		style = new LabelStyle();
		style.font = new BitmapFont();
		style.fontColor = Color.WHITE;
	}
	
	public void update () {
		act(Gdx.graphics.getDeltaTime());
		draw();
	}
	
	public void debug (String s, Object value) {
		if (s.equals("")) {
			return;
		}
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
