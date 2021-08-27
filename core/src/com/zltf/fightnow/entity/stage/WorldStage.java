package com.zltf.fightnow.entity.stage;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import com.zltf.fightnow.entity.actor.Bullet;
import com.zltf.fightnow.entity.actor.Player;
import com.zltf.fightnow.entity.actor.MyImage;
import com.zltf.fightnow.utils.InfoManager;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldStage extends Stage {
	
	SpriteBatch batch;

	Texture background;
	MyImage backgroundImage;
	// 由于子弹需要在子线程中创建，texture现在主线程准备好。
	TextureRegion bulletRegion;

	CopyOnWriteArrayList<Player> players;
	CopyOnWriteArrayList<Bullet> bullets;

	public WorldStage(SpriteBatch batch) {
		super(new StretchViewport(InfoManager.CAM_WIDTH,InfoManager.CAM_HEIGHT),batch);
		this.batch = batch;
		
		background = new Texture(Gdx.files.internal("image/background.png"));
		backgroundImage = new MyImage(background);
		backgroundImage.setSize(InfoManager.WORlD_WIDTH,InfoManager.WORLD_HEIGHT);
		backgroundImage.setPosition(0,0);

		bulletRegion = new TextureRegion(new Texture(Gdx.files.internal("image/bullet.png")));

		players = new CopyOnWriteArrayList<>();
		bullets = new CopyOnWriteArrayList<>();

		Player myPlayer = new Player();
		myPlayer.setHpColor(Color.GREEN);
		myPlayer.setVisible(false);
		players.add(myPlayer);

		Player anotherPayer = new Player();
		anotherPayer.setHpColor(Color.RED);
		anotherPayer.setVisible(false);
		players.add(anotherPayer);

		this.addActor(backgroundImage);
		addActor(myPlayer);
		addActor(anotherPayer);
	}

	public void setSize(float width) {
		this.getCamera().viewportWidth = width;
		this.getCamera().viewportHeight = width/InfoManager.CAM_WIDTH*InfoManager.CAM_HEIGHT;
	}

	public void setSize(float width,float height) {
		this.getCamera().viewportWidth = width;
		this.getCamera().viewportHeight = height;
	}

	@Override
	public float getWidth() {
		// TODO: Implement this method
		return this.getCamera().viewportWidth;
	}

	@Override
	public float getHeight() {
		// TODO: Implement this method
		return this.getCamera().viewportHeight;
	}
	
	public float getX() {
		return this.getCamera().position.x;
	}
	
	public float getY() {
		return this.getCamera().position.y;
	}
	
	public void setPosition(float x,float y) {
		this.getCamera().position.x = x;
		this.getCamera().position.y = y;
	}

	public void setX(float x) {
		this.getCamera().position.x = x;
	}

	public void setY(float y) {
		this.getCamera().position.y = y;
	}

	public Player getMyPlayer() {
		return players.get(0);
	}

	public Player getAnotherPayer() {
		return players.get(1);
	}

	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}

	public CopyOnWriteArrayList<Bullet> getBullets() {
		return bullets;
	}

	public Player getPlayerById(int id) {
		for(Player player: players) {
			if(player.getId() == id) {
				return player;
			}
		}
		return null;
	}

	public Bullet getBulletById(int id) {
		for(Bullet bullet: bullets) {
			if(bullet.getId() == id) {
				return bullet;
			}
		}
		return null;
	}

	@Override
	public void act() {
		// TODO: Implement this method
		super.act();
	}

	public void checkCamPosition() {
		if (getMyPlayer() != null) {
			setPosition(getMyPlayer().getX()+getMyPlayer().getWidth()/2, getMyPlayer().getY()+ getMyPlayer().getHeight()/2);
			// 视角半锁定，实际操作体验不好
//			if(getX()<getWidth()/2) {
//				setX(getWidth()/2);
//			}
//			if(getX()+getWidth()/2>InfoManager.WORlD_WIDTH) {
//				setX(InfoManager.WORlD_WIDTH - getWidth()/2);
//			}
//			if(getY()<getHeight()) {
//				setY(getHeight());
//			}
//			if(getY()+getHeight()/2>InfoManager.WORLD_HEIGHT) {
//				setY(InfoManager.WORLD_HEIGHT - getHeight()/2);
//			}
		}
	}

	public void initMyPlayer(int id, float x, float y, float rotation) {
		getMyPlayer().setId(id);
		getMyPlayer().setPosition(x, y);
		getMyPlayer().setRotation(rotation);
		getMyPlayer().setVisible(true);
	}

	public void joinPlayer(int id, float x, float y, float rotation) {
		getAnotherPayer().setId(id);
		getAnotherPayer().setPosition(x, y);
		getAnotherPayer().setRotation(rotation);
		getAnotherPayer().setVisible(true);
	}

	public Bullet attack(float direction) {
		Player myPlayer = getMyPlayer();
		Bullet bullet = new Bullet(myPlayer.getId(), direction, bulletRegion);
		bullet.setPosition(myPlayer.getX()+myPlayer.getWidth()/2, myPlayer.getY()+myPlayer.getHeight()/2-bullet.getHeight()/2);
		bullets.add(bullet);
		addActor(bullet);
		return bullet;
	}

	public void anotherAttack(int ownerId, int bulletId, float direction) {
		Bullet bullet = new Bullet(ownerId, bulletId, direction, bulletRegion);
		Player player = getPlayerById(ownerId);
		bullet.setPosition(player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2-bullet.getHeight()/2);
		bullets.add(bullet);
		addActor(bullet);
	}

	public void removeBullet(int bulletId) {
		Bullet bullet = getBulletById(bulletId);
		if(bullet != null) {
			getRoot().removeActor(bullet);
			getBullets().remove(bullet);
		}
	}
}
