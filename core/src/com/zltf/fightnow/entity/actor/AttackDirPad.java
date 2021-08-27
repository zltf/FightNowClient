package com.zltf.fightnow.entity.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.zltf.fightnow.utils.InfoManager;

import java.text.DecimalFormat;

public class AttackDirPad extends DirPad {
    float coolDownTime = 0;

    Player myPlayer;

    // 用于判断松手状态
    boolean touchedFlag = false;
    boolean useAble = true;

    BitmapFont font;

    UsedListener usedListener;

    public AttackDirPad(Player myPlayer) {
        this.myPlayer = myPlayer;

        font = InfoManager.newTtfFont("", InfoManager.FONT_SIZE);
        font.setColor(Color.WHITE);

        Texture texture = new Texture(Gdx.files.internal("image/attack_dirpad.png"));
        TextureRegion pad = new TextureRegion(texture,0,0,512,512);
        TextureRegion key = new TextureRegion(texture,512,0,128,128);
        setRegionPad(pad);
        setRegionKey(key);

        setSize(InfoManager.CAM_WIDTH/6);
        setKeySize(getSize()/2);

        setPosition(InfoManager.CAM_WIDTH - getWidth() - 50, 50);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (coolDownTime <= 0) {
            coolDownTime = 0;
        } else {
            coolDownTime -= delta;
        }

        // 冷却时间为0且可用
        if(coolDownTime == 0 && isUseAble()) {
            if (isTouched()) {
                myPlayer.isShowAttackDir(true);
                myPlayer.setAttackDirRotation(getAngleDegrees());
                touchedFlag = true;
            } else {
                myPlayer.isShowAttackDir(false);
                if (touchedFlag) {
                    usedListener.used();
                    setCoolDownTime(InfoManager.COOL_DOWN_TIME);
                    touchedFlag = false;
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        DecimalFormat df = new DecimalFormat("0.0");
        String coolDownStr = df.format(Math.abs(coolDownTime));

        if (isTouched()) {
            batch.draw(getRegionPad(),getX(),getY(),getWidth(),getHeight());
        }
        batch.draw(getRegionKey(),getX()+(getSize()-getKeySize())/2+getKeyX(),
                getY()+(getSize()-getKeySize())/2+getKeyY(),getKeySize(),getKeySize());

        // 显示冷却时间
        if(coolDownTime != 0) {
            font.draw(batch, coolDownStr, getX()+getWidth()/2-InfoManager.FONT_SIZE/2f*1.5f,
                    getY()+getHeight()/2-getKeySize()/2-InfoManager.FONT_SIZE/2f);
        }
    }

    public void setCoolDownTime(float coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public float getCoolDownTime() {
        return coolDownTime;
    }

    public boolean isUseAble() {
        return useAble;
    }

    public void setUseAble(boolean useAble) {
        this.useAble = useAble;
    }

    public void addUsedListener(UsedListener usedListener) {
        this.usedListener = usedListener;
    }

    public abstract static class UsedListener {
        public abstract void used();
    }
}
