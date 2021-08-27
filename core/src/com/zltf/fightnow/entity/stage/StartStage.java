package com.zltf.fightnow.entity.stage;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.*;
import com.zltf.fightnow.entity.actor.MyImage;
import com.zltf.fightnow.entity.actor.MyImageButton;
import com.zltf.fightnow.utils.InfoManager;
import com.zltf.fightnow.json.JsonPlayer;
import com.zltf.fightnow.utils.StageManager;
import com.zltf.fightnow.utils.UDPManager;

import java.net.DatagramPacket;

public class StartStage extends Stage {
	OrthographicCamera cam;
	SpriteBatch batch;
	StageManager stageManager;

	TextField hostTextField;
	Label labelHost;
	MyImage backgroundImage;
	
	public StartStage(OrthographicCamera cam,SpriteBatch batch,StageManager stageManager) {
		super(new StretchViewport(InfoManager.CAM_WIDTH,InfoManager.CAM_HEIGHT,cam),batch);
		
		this.cam = cam;
		this.batch = batch;
		this.stageManager = stageManager;

		Texture background = new Texture(Gdx.files.internal("image/start_background.png"));
		backgroundImage = new MyImage(background);
		if(background.getHeight()*InfoManager.CAM_WIDTH/background.getWidth() > InfoManager.CAM_HEIGHT) {
			backgroundImage.setSize(InfoManager.CAM_WIDTH,background.getHeight()*InfoManager.CAM_WIDTH/background.getWidth());
		} else {
			backgroundImage.setSize(background.getWidth()*InfoManager.CAM_HEIGHT/background.getHeight(),InfoManager.CAM_HEIGHT);
		}
		backgroundImage.setPosition(0,0);

		TextField.TextFieldStyle style = new TextField.TextFieldStyle();
		style.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("image/text_field.png"))));
		BitmapFont font = InfoManager.newTtfFont("服务器地址", InfoManager.FONT_SIZE);
		style.font = font;
		style.fontColor = Color.WHITE;

		labelHost = new Label("服务器地址",new Label.LabelStyle(font,Color.ORANGE));
		labelHost.setPosition(InfoManager.CAM_WIDTH/2 - labelHost.getWidth()/2, InfoManager.CAM_HEIGHT - labelHost.getHeight()-30);

		hostTextField = new TextField("", style);
		hostTextField.setWidth(300);
		hostTextField.setHeight(50);
		hostTextField.setPosition(InfoManager.CAM_WIDTH/2 - hostTextField.getWidth()/2, labelHost.getY() - hostTextField.getHeight()-30);

		Texture btnTexture = new Texture(Gdx.files.internal("image/play_btn.png"));
		TextureRegion btnRegion1 = new TextureRegion(btnTexture,0,0,154,50);
		TextureRegion btnRegion2 = new TextureRegion(btnTexture,0,50,154,50);

		Button btn = new MyImageButton(new TextureRegionDrawable(btnRegion1),new TextureRegionDrawable(btnRegion2));
		btn.setSize(154,50);
		btn.setPosition(InfoManager.CAM_WIDTH-100-btn.getWidth(),50);
		btn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				startGame();
			}
		});

		addActor(backgroundImage);
		addActor(hostTextField);
		addActor(labelHost);
		addActor(btn);
	}

	private void startGame() {
		String host = hostTextField.getText();
		if(host.equals("")) {
			host = "localhost";
		}

		UDPManager udpManager = new UDPManager();
		// 加入房间
		udpManager.send(host, "j");


		char op = 0;
		String data = "";
		// 过滤可能发送更快的坐标数据包
		while (op != 'j') {
			DatagramPacket datagramPacket = udpManager.recv();
			String recv = UDPManager.getDataFromRecv(datagramPacket);
			op = recv.charAt(0);
			data = recv.substring(1);
		}
		JsonPlayer jsonPlayer = JSON.parseObject(data, JsonPlayer.class);
		udpManager.close();
		stageManager.open(new GameStage(cam, batch, stageManager, host, jsonPlayer));
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw() {
		super.draw();
	}
	
}
