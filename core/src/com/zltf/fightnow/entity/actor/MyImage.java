package com.zltf.fightnow.entity.actor;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class MyImage extends Actor {
	TextureRegion region;
	
	public MyImage() { }
	
	public MyImage(Texture texture) {
		this.region = new TextureRegion(texture);
	}
	
	public MyImage(TextureRegion region) {
		this.region = region;
	}
	
	public MyImage(Texture texture, Color color) {
		this.region = new TextureRegion(texture);
		this.setColor(color);
	}

	public MyImage(TextureRegion region, Color color) {
		this.region = region;
		this.setColor(color);
	}
	
	public void setTexture(Texture texture) {
		this.region = new TextureRegion(texture);
	}
	
	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO: Implement this method
		Color startColor = new Color(batch.getColor());
		batch.setColor(getColor());
		batch.draw(region,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
		batch.setColor(startColor);

		super.draw(batch, parentAlpha);
	}
	
}
