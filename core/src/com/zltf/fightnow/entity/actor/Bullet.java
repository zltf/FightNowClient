package com.zltf.fightnow.entity.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zltf.fightnow.utils.InfoManager;

public class Bullet extends Actor {
    float speed;
    TextureRegion region;
    int ownerId;
    int id;

    static int newId = 0;

    float direction;

    public Bullet(int ownerId, float direction, TextureRegion region) {
        this(ownerId, newId++, direction, region);
    }

    public Bullet(int ownerId, int bulletId, float direction, TextureRegion region) {
        this.ownerId = ownerId;
        this.direction = direction;
        id = bulletId;

        this.region = region;
        setRotation(direction);
        setSize(InfoManager.BULLET_LENGTH,InfoManager.ATTACK_RANGE);
        setSpeed(InfoManager.ATTACK_SPEED);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setSize(float width, float height) {
        super.setSize(width,height);
        setOriginY(getHeight()/2);
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void act(float delta) {
        // TODO: Implement this method
        super.act(delta);

        // 本地移动预测
        float moveX = speed*delta*(float)Math.cos(Math.toRadians(getDirection()));
        float moveY = speed*delta*(float)Math.sin(Math.toRadians(getDirection()));
        setX(getX()+moveX);
        setY(getY()+moveY);
    }

    public void draw(Batch batch, float parentAlpha) {
        // TODO: Implement this method
        Color startColor = new Color(batch.getColor());
        batch.setColor(getColor());
        batch.draw(region,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),getScaleX(),getScaleY(),getRotation());
        batch.setColor(startColor);

        super.draw(batch, parentAlpha);
    }
}
