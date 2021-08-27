package com.zltf.fightnow.entity.actor;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
import com.zltf.fightnow.utils.InfoManager;

public class DirPad extends Actor {
	private TextureRegion regionPad,regionKey;
	private float keyX,keyY,keySize;
	private float latestAngle = 0;
	private boolean isTouched = false;
    
    //触点ID用于多点触控的逻辑处理
	private byte touchedID;
	
	public DirPad(TextureRegion regionPad,TextureRegion regionKey,float size) {
		this.regionPad = regionPad;
		this.regionKey = regionKey;
		
		setSize(size);
		keySize = size/2;
	}
	
    //若使用此构造方法，自动生成默认的资源图片
	public DirPad() {
		Pixmap pixmapPad = new Pixmap(1024,1024,Pixmap.Format.RGBA8888);
		pixmapPad.setColor(0.6f,0.6f,1,1);
		pixmapPad.fillCircle(512,512,512);
		
		Pixmap pixmapKey = new Pixmap(512,512,Pixmap.Format.RGBA8888);
		pixmapKey.setColor(0.2f,0.2f,1,1);
		pixmapKey.fillCircle(256,256,256);
		
		regionPad = new TextureRegion(new Texture(pixmapPad));
		regionKey = new TextureRegion(new Texture(pixmapKey));
		
		setSize(InfoManager.CAM_WIDTH/6);
		keySize = getSize()/2;
	}
	
	@Override
	public void act(float delta) {
		for(byte i = 0;i<20;i++) {
			
			if(Gdx.input.isTouched(i) && !isTouched) {
				float touchedX = InfoManager.getTouchedX(i)-getX()-getSize()/2;
				float touchedY = InfoManager.getTouchedY(i)-getY()-getSize()/2;
				
				if(touchedX>-getSize()/2 && touchedX<getSize()/2 && touchedY>-getSize()/2 && touchedY<getSize()/2) {
					isTouched = true;
					touchedID = i;
					
					break;
				}
			}
		}
		
		if(isTouched) {
			if(Gdx.input.isTouched(touchedID)) {
				float touchedX = InfoManager.getTouchedX(touchedID)-getX()-getSize()/2;
				float touchedY = InfoManager.getTouchedY(touchedID)-getY()-getSize()/2;
				float r = getSize()/2;
				
				if(touchedX*touchedX+touchedY*touchedY<r*r) {
					keyX = touchedX;
					keyY = touchedY;
				} else {
					keyX = r*(float)Math.cos(toAngle(touchedX,touchedY));
					keyY = r*(float)Math.sin(toAngle(touchedX,touchedY));
				}

				latestAngle = toAngle(touchedX,touchedY);
			} else {
				isTouched = false;
				keyX = 0;
				keyY = 0;
			}
		}
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(regionPad,getX(),getY(),getWidth(),getHeight());
		batch.draw(regionKey,getX()+(getSize()-keySize)/2+keyX,getY()+(getSize()-keySize)/2+keyY,keySize,keySize);
		
		super.draw(batch, parentAlpha);
	}
	
	//获取当前角度(弧度制)
	public float getAngle() {
		return latestAngle;
	}
	
	//获取拖动距离
	public float getDistance() {
		return (float)Math.sqrt(keyX*keyX + keyY*keyY);
	}
	
	//转化为角度(弧度制)
	private static float toAngle(float x,float y) {
		return (x>0)?(float)Math.atan(y/x):(float)(Math.PI+Math.atan(y/x));
	}
	
	//获取X轴应乘以的系数
	public float getParaX() {
		return (float)Math.cos(getAngle());
	}
	
	//获取Y轴应乘以的系数
	public float getParaY() {
		return (float)Math.sin(getAngle());
	}
	
	//获取当前角度(角度制)
	public float getAngleDegrees() {
		return (float)Math.toDegrees(getAngle());
	}
	
	//获取相对于摇杆中心的X坐标
	public float getKeyX() {
		return keyX;
	}
	
	//获取相对于摇杆中心的Y坐标
	public float getKeyY() {
		return keyY;
	}
	
	//获取摇杆按钮大小
	public float getKeySize() {
		return keySize;
	}
	
	//设置摇杆按钮大小
	public void setKeySize(float keySize) {
		this.keySize = keySize;
	}
	
	//获取是否按下
	public boolean isTouched() {
		return isTouched;
	}
	
	//获取按下的触摸点id
	public float getTouchedID() {
		if(isTouched) {
			return touchedID;
		}
		return -1;
	}
	
	//设置大小
	public void setSize(float size) {
		setSize(size, size);
	}
	
	//获取大小
	public float getSize() {
		return getWidth();
	}

	public void setRegionPad(TextureRegion regionPad) {
		this.regionPad = regionPad;
	}

	public void setRegionKey(TextureRegion regionKey) {
		this.regionKey = regionKey;
	}

	public TextureRegion getRegionPad() {
		return regionPad;
	}

	public TextureRegion getRegionKey() {
		return regionKey;
	}
}
