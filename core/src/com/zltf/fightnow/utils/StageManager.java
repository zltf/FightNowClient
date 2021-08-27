package com.zltf.fightnow.utils;

import com.badlogic.gdx.scenes.scene2d.*;

import java.util.*;
import com.badlogic.gdx.*;

public class StageManager {
	//舞台
	private Stage stage = null;
	
	//记录来路Stage
	Stack<Stage> stageStack;

	public StageManager() {
		stageStack = new Stack<>();
	}
	
	//切换当前舞台
	public void open(Stage stage) {
		if(this.stage != null) {
			stageStack.push(this.stage);
		}
		this.stage = stage;
		//监听当前舞台
		Gdx.input.setInputProcessor(stage);
	}

	//获取舞台
	public Stage getStage() {
		return stage;
	}

	public void finish() {
		if(!stageStack.isEmpty()) {
			Stage lastStage = this.stage;
			this.stage = stageStack.pop();
			lastStage.dispose();
			Gdx.input.setInputProcessor(stage);
		} else {
			Gdx.app.exit();
		}
	}
}
