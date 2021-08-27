package com.zltf.fightnow.entity.stage;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.zltf.fightnow.entity.actor.AttackDirPad;
import com.zltf.fightnow.entity.actor.Bullet;
import com.zltf.fightnow.entity.actor.DirPad;
import com.zltf.fightnow.json.ActionAttack;
import com.zltf.fightnow.json.ActionMove;
import com.zltf.fightnow.utils.InfoManager;
import com.zltf.fightnow.json.JsonBullet;
import com.zltf.fightnow.json.JsonPlayer;
import com.zltf.fightnow.utils.StageManager;
import com.zltf.fightnow.utils.UDPManager;

import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

public class GameStage extends Stage {
	OrthographicCamera cam;
	SpriteBatch batch;
	StageManager stageManager;
	
	DirPad dirPad;
	WorldStage worldStage;
	Label labelFPS;
	Label labelDelay;
	Label labelGameOver;

	AttackDirPad attackDirPad;

	UDPManager udpManager;

	long delayTime = 0;

	String host;
	
	public GameStage(OrthographicCamera cam, SpriteBatch batch, StageManager stageManager, final String host, JsonPlayer jsonPlayer) {
		super(new StretchViewport(InfoManager.CAM_WIDTH,InfoManager.CAM_HEIGHT,cam),batch);
		
		this.cam = cam;
		this.batch = batch;
		this.stageManager = stageManager;
		this.host = host;

		udpManager = new UDPManager();
		
		Texture texture = new Texture(Gdx.files.internal("image/dirpad.png"));
		TextureRegion pad = new TextureRegion(texture,0,0,512,512);
		TextureRegion key = new TextureRegion(texture,512,0,128,128);
		
		dirPad = new DirPad(pad,key,InfoManager.CAM_WIDTH/6);
		dirPad.setPosition(50,50);
		
		worldStage = new WorldStage(batch);
		worldStage.initMyPlayer(jsonPlayer.getId(), jsonPlayer.getX(), jsonPlayer.getY(), jsonPlayer.getRotation());
		
		BitmapFont font = InfoManager.newTtfFont("", InfoManager.FONT_SIZE);
		labelFPS = new Label("0000000",new Label.LabelStyle(font,Color.ORANGE));
		labelFPS.setPosition(InfoManager.CAM_WIDTH-labelFPS.getWidth()-50,InfoManager.CAM_HEIGHT-labelFPS.getHeight()-50);
		labelDelay = new Label("",new Label.LabelStyle(font,Color.ORANGE));
		labelDelay.setPosition(labelFPS.getX(),labelFPS.getY()-labelDelay.getHeight()-50);

		BitmapFont fontGameOver = InfoManager.newTtfFont("胜利失败", (int)InfoManager.CAM_HEIGHT/3);
		labelGameOver = new Label("胜利",new Label.LabelStyle(fontGameOver,Color.BLACK));
		labelGameOver.setPosition(InfoManager.CAM_WIDTH/2-labelGameOver.getWidth()/2,
				InfoManager.CAM_HEIGHT/2-labelGameOver.getHeight()/2);
		labelGameOver.setVisible(false);

		attackDirPad = new AttackDirPad(worldStage.getMyPlayer());
		attackDirPad.addUsedListener(new AttackDirPad.UsedListener() {
			@Override
			public void used() {
				Bullet bullet = worldStage.attack(attackDirPad.getAngleDegrees());
				sendAttackAction(bullet.getId(), bullet.getDirection());
			}
		});
		
		this.addActor(dirPad);
		this.addActor(labelFPS);
		// this.addActor(labelDelay);
		this.addActor(labelGameOver);
		this.addActor(attackDirPad);

		// 消息接收线程
		new Thread() {
			@Override
			public void run() {
				while(true) {
					DatagramPacket datagramPacket = udpManager.recv();
					String recv = UDPManager.getDataFromRecv(datagramPacket);
					char op = recv.charAt(0);
					String data = recv.substring(1);
					handleRecv(op, data);
				}
			}
		}.start();

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if(dirPad.isTouched()) {
					sendMoveAction();
				}
			}
		},0, 1000/InfoManager.PKG_NUM);
	}

	@Override
	public void act(float delta) {
		labelFPS.setText("FPS:"+Gdx.graphics.getFramesPerSecond());
		labelDelay.setText("Delay:"+delayTime+"ms");

		// 本地移动预测
		if(dirPad.isTouched()) {
			worldStage.getMyPlayer().move(delta, dirPad.getParaX(), dirPad.getParaY());
		}

		worldStage.act();
		
		super.act(delta);
	}

	@Override
	public void draw() {
		worldStage.checkCamPosition();
		worldStage.draw();
		super.draw();
	}

	public void handleRecv(char op, String data) {
		switch (op) {
			// 玩家状态
			case 'p':
				JsonPlayer player = JSON.parseObject(data, JsonPlayer.class);
				delayTime = System.currentTimeMillis() - player.getTimeStamp();
				if(player.getId() == worldStage.getMyPlayer().getId()) {
					worldStage.getMyPlayer().moveTo(player.getX(), player.getY(), player.getRotation());
					worldStage.getMyPlayer().setHealthPoint(player.getHealthPoint());
				} else if(!worldStage.getAnotherPayer().isVisible()) {
					worldStage.joinPlayer(player.getId(), player.getX(), player.getY(), player.getRotation());
				} else {
					worldStage.getAnotherPayer().moveTo(player.getX(), player.getY(), player.getRotation());
					worldStage.getAnotherPayer().setHealthPoint(player.getHealthPoint());
				}
				break;
			// 攻击反馈
			case 'a':
				ActionAttack actionAttack = JSON.parseObject(data, ActionAttack.class);
				if(actionAttack.getPlayerId() != worldStage.getMyPlayer().getId()) {
					worldStage.anotherAttack(actionAttack.getPlayerId(), actionAttack.getBulletId(), actionAttack.getDirection());
				}
				break;
			// 子弹状态
			case 'b':
				JsonBullet jsonBullet = JSON.parseObject(data, JsonBullet.class);
				for(Bullet bullet: worldStage.getBullets()) {
					if(bullet.getId() == jsonBullet.getId() && bullet.getOwnerId() == jsonBullet.getOwnerId()) {
						bullet.setPosition(jsonBullet.getX(), jsonBullet.getY());
					}
				}
				break;
			// 子弹删除
			case 'r':
				JsonBullet removedBullet = JSON.parseObject(data, JsonBullet.class);
				worldStage.removeBullet(removedBullet.getId());
				break;
			// 游戏结束
			case 'd':
				JsonPlayer playDefault = JSON.parseObject(data, JsonPlayer.class);
				labelGameOver.setVisible(true);
				if(playDefault.getId() == worldStage.getMyPlayer().getId()) {
					labelGameOver.setText("失败");
				} else {
					labelGameOver.setText("胜利");
				}
				attackDirPad.setUseAble(false);
				break;
		}
	}

	public void sendMoveAction() {
		ActionMove actionMove = new ActionMove();
		actionMove.setRoomId(0);
		actionMove.setPlayerId(worldStage.getMyPlayer().getId());
		actionMove.setDirPadParaX(dirPad.getParaX());
		actionMove.setDirPadParaY(dirPad.getParaY());

		String str = JSON.toJSONString(actionMove);
		udpManager.send(host, "m" + str);
	}

	public void sendAttackAction(int bulletId, float direction) {
		ActionAttack actionAttack = new ActionAttack();
		actionAttack.setRoomId(0);
		actionAttack.setPlayerId(worldStage.getMyPlayer().getId());
		actionAttack.setBulletId(bulletId);
		actionAttack.setDirection(direction);

		String str = JSON.toJSONString(actionAttack);
		udpManager.send(host, "a" + str);
	}
	
}
