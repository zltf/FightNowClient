package com.zltf.fightnow.entity.actor;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.zltf.fightnow.utils.InfoManager;

public class Player extends Actor {
	int id;
	TextureRegion region;
	TextureRegion attackDir;
	TextureRegion healthBackground;
	TextureRegion health;

	float speed = InfoManager.PLAYER_SPEED;
	float targetX, targetY, targetRotation;
	boolean showAttackDir = false;
	float attackDirRotation;
	float attackDistance = InfoManager.ATTACK_DISTANCE;
	float attackRange = InfoManager.ATTACK_RANGE;
	float healthPoint = InfoManager.PLAYER_HP;

	Color hpColor;

	public Player() {
		
		region = new TextureRegion(new Texture(Gdx.files.internal("image/player.png")));
		attackDir = new TextureRegion(new Texture(Gdx.files.internal("image/skill_range.png")));

		healthBackground = new TextureRegion(new Texture(Gdx.files.internal("image/rectangle.png")));
		health = new TextureRegion(new Texture(Gdx.files.internal("image/rectangle.png")));

		setSize(InfoManager.PLAYER_SIZE);

		//InfoManager.CAM_WIDTH/2-getWidth()/2,InfoManager.CAM_HEIGHT/2-getHeight()/2);
	}
	
	public void act(float delta) {
		// TODO: Implement this method
		super.act(delta);

		float distanceX = targetX-getX();
		float distanceY = targetY-getY();
		// 处理角度周期
		float distanceRotation;
		if(targetRotation<-45 && getRotation()>200) {
			distanceRotation = targetRotation-getRotation()+360;
		} else if(targetRotation>200 && getRotation()<-45) {
			distanceRotation = targetRotation-getRotation()-360;
		} else {
			distanceRotation = targetRotation-getRotation();
		}
		float pkgTime = 1f/InfoManager.PKG_NUM;

		setX(getX()+distanceX*delta/pkgTime);
		setY(getY()+distanceY*delta/pkgTime);
		setRotation(getRotation()+distanceRotation*delta/pkgTime);
	}

	public void draw(Batch batch, float parentAlpha) {
		// TODO: Implement this method

		if(showAttackDir) {
			batch.draw(attackDir,getX()+getWidth()/2,getY()+getHeight()/2- attackRange /2f,
					0, attackRange /2f, attackDistance, attackRange,getScaleX(),getScaleY(),attackDirRotation);
		}

		Color startColor = new Color(batch.getColor());
		batch.setColor(getColor());
		batch.draw(region,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation()-90);
		batch.setColor(startColor);

		startColor = new Color(batch.getColor());
		batch.setColor(Color.BLACK);
		batch.draw(healthBackground,getX(),getY()+getHeight()*9/8,getOriginX(),getOriginY(),getWidth(),getHeight()/8,getScaleX(),getScaleY(),0);
		batch.setColor(startColor);

		startColor = new Color(batch.getColor());
		batch.setColor(hpColor);
		batch.draw(health,getX(),getY()+getHeight()*9/8,getOriginX(),getOriginY(),getWidth()*healthPoint/InfoManager.PLAYER_HP,getHeight()/8,getScaleX(),getScaleY(),0);
		batch.setColor(startColor);

		super.draw(batch, parentAlpha);
	}

	// 初始化后设置坐标
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		targetX = x;
		targetY = y;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setSize(float size) {
		super.setSize(size,size);
		setOriginX(getWidth()/2);
		setOriginY(getHeight()/2);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void moveTo(float targetX, float targetY, float targetRotation) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetRotation = targetRotation;
	}

	// 本地移动预测
	public void move(float delta, float paraX,float paraY) {
		float speed = InfoManager.PLAYER_SPEED*delta;
		setX(getX() + speed*paraX);
		setY(getY() + speed*paraY);
		if(getX()<0) {
			setX(0);
		}
		if(getX()+InfoManager.PLAYER_SIZE>InfoManager.WORlD_WIDTH) {
			setX(InfoManager.WORlD_WIDTH - InfoManager.PLAYER_SIZE);
		}
		if(getY()<0) {
			setY(0);
		}
		if(getY()+InfoManager.PLAYER_SIZE>InfoManager.WORLD_HEIGHT) {
			setY(InfoManager.WORLD_HEIGHT - InfoManager.PLAYER_SIZE);
		}
		setRotation((float)Math.toDegrees((paraX>0)?Math.atan(paraY/paraX):Math.PI+Math.atan(paraY/paraX)));
	}

	public boolean isShowAttackDir(boolean showAttackDir) {
		return this.showAttackDir = showAttackDir;
	}

	public float getAttackDirRotation() {
		return attackDirRotation;
	}

	public void setAttackDirRotation(float attackDirRotation) {
		this.attackDirRotation = attackDirRotation;
	}

	public float getHealthPoint() {
		return healthPoint;
	}

	public Color getHpColor() {
		return hpColor;
	}

	public void setHpColor(Color hpColor) {
		this.hpColor = hpColor;
	}

	public void setHealthPoint(float healthPoint) {
		this.healthPoint = healthPoint;
	}
}
