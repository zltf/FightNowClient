package com.zltf.fightnow.json;

import com.alibaba.fastjson.annotation.JSONField;

public class JsonPlayer {
    @JSONField(name = "player_id")
    int id;

    @JSONField(name = "position_x")
    float x;

    @JSONField(name = "position_y")
    float y;

    @JSONField(name = "rotation")
    float rotation;

    @JSONField(name = "health_point")
    float healthPoint;

    @JSONField(name = "time_stamp")
    long timeStamp;

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRotation() {
        return rotation;
    }

    public float getHealthPoint() {
        return healthPoint;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setHealthPoint(float healthPoint) {
        this.healthPoint = healthPoint;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
