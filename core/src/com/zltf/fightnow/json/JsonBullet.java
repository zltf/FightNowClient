package com.zltf.fightnow.json;

import com.alibaba.fastjson.annotation.JSONField;

public class JsonBullet {
    public void setId(int id) {
        this.id = id;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDirection() {
        return direction;
    }

    @JSONField(name = "bullet_id")
    int id;

    @JSONField(name = "owner_id")
    int ownerId;

    @JSONField(name = "position_x")
    float x;

    @JSONField(name = "position_y")
    float y;

    @JSONField(name = "direction")
    float direction;
}
