package com.zltf.fightnow.utils;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;

public class InfoManager {
	//屏幕宽高（像素）
	public static final float SCR_WIDTH = Gdx.graphics.getWidth();
	public static final float SCR_HEIGHT = Gdx.graphics.getHeight();
	
	//相机宽高
	public static final float CAM_WIDTH = 1000;
	public static final float CAM_HEIGHT = CAM_WIDTH/SCR_WIDTH*SCR_HEIGHT;
	
	public static final float WORlD_WIDTH = 1382;
	public static final float WORLD_HEIGHT = 784;

	public static final int SERVER_PORT = 54321;
	public static final int CLIENT_PORT = 54320;

	public static final int FONT_SIZE = 30;

	public static final float PLAYER_SPEED = 200;
	public static final float PLAYER_SIZE = 100;
	public static final float COOL_DOWN_TIME = 2;
	public static final float ATTACK_RANGE = 50;
	public static final float ATTACK_DISTANCE = 400;
	public static final float ATTACK_SPEED = 800;
	public static final float BULLET_LENGTH = 80;
	public static final float PLAYER_HP = 1000;
	public static final float PLAYER_HP_RESTORE = 10;
	public static final float BULLET_ATK = 100;

	public static final int PKG_NUM = 30;
	
	//精灵画笔，由于此对象占用内存较大，建议不要实例化新的画笔，一直用这个就行
	private SpriteBatch batch;
	//有时候用到的背景正交相机
	private OrthographicCamera cam;
	
	public InfoManager() {
		//实例化资源对象
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false,CAM_WIDTH,CAM_HEIGHT);
	}
	
	//获取资源
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public OrthographicCamera getCamera() {
		return cam;
	}
	
	public static BitmapFont newTtfFont(String text, int size) {
		//游戏字体(ttf字库)
		BitmapFont font;
		//字体生成器
		FreeTypeFontGenerator generator;
		FreeTypeFontGenerator.FreeTypeBitmapFontData fontData;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS +text;
		parameter.size = size;
		font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}

	public static float getTouchedX(int i) {
		return Gdx.input.getX(i)/SCR_WIDTH*CAM_WIDTH;
	}
	
	public static float getTouchedY(int i) {
		return (SCR_HEIGHT-Gdx.input.getY(i))/SCR_HEIGHT*CAM_HEIGHT;
	}
}
