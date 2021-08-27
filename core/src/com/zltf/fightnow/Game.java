package com.zltf.fightnow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.zltf.fightnow.entity.stage.GameStage;
import com.zltf.fightnow.entity.stage.StartStage;
import com.zltf.fightnow.utils.InfoManager;
import com.zltf.fightnow.utils.StageManager;

public class Game extends ApplicationAdapter {
	// 舞台管理器
	public StageManager stageManager;
	// 资源管理器
	public InfoManager infoManager;

	boolean backFlag = true;

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		stageManager = new StageManager();
		infoManager = new InfoManager();
		stageManager.open(new StartStage(infoManager.getCamera(), infoManager.getSpriteBatch(), stageManager));
	}

	@Override
	public void resize(int p1, int p2) {
		// TODO: Implement this method
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);	// 背景
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//清屏

		// 获取当前舞台并表演绘制
		stageManager.getStage().act();
		stageManager.getStage().draw();

		if(Gdx.input.isKeyPressed(Input.Keys.BACK) && backFlag) {
			backFlag = false;
			stageManager.finish();
		} else if(!Gdx.input.isKeyPressed(Input.Keys.BACK) && !backFlag) {
			backFlag = true;
		}
	}

	@Override
	public void pause() {
		// TODO: Implement this method
	}

	@Override
	public void resume() {
		// TODO: Implement this method
	}

	@Override
	public void dispose() {
		// TODO: Implement this method
	}
}
