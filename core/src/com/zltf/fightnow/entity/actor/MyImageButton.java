package com.zltf.fightnow.entity.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;

public class MyImageButton extends ImageButton {
	//两张drawable
	TextureRegionDrawable background;
	TextureRegionDrawable click;
	//构造方法
	public MyImageButton(TextureRegionDrawable background,TextureRegionDrawable click) {
		//调用父类构造方法
		super(background,click);
		//接收参数
		this.background = background;
		this.click = click;
	}

	//重写draw方法
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color startColor = new Color(batch.getColor());
		batch.setColor(getColor());
		if(!isPressed()) {
			//非按下绘制背景图
			background.draw(batch,getX(),getY(),getWidth(),getHeight());
		} else {
			//按下绘制背景图
			click.draw(batch,getX(),getY(),getWidth(),getHeight());
		}
		batch.setColor(startColor);
	}
}
