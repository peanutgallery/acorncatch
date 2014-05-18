package com.peanutgallery.acornrungame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class AcornRunGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;

	Texture acornImage;
	Texture bucketImage;
	Sound acornSound;
	Music acornMusic;

	Rectangle bucket;
	Vector3 touchPos;
	Array<Rectangle> acorns;
	long lastDropTime;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();
		img = new Texture("images/acorn.png");
		touchPos = new Vector3();

		// load the images for the droplet and the bucket, 64x64 pixels each
		acornImage = new Texture(Gdx.files.internal("images/acorn.png"));
		bucketImage = new Texture(Gdx.files.internal("images/bucket.png"));

		// load the drop sound effect and the rain background "music"
		acornSound = Gdx.audio.newSound(Gdx.files.internal("sounds/catchacorn.wav"));
		acornMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background-music.mp3"));

		// start the playback of the background music immediately
		acornMusic.setLooping(true);
		acornMusic.play();	

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		acorns = new Array<Rectangle>();
		spawnAcorns();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle acorn: acorns) {
			batch.draw(acornImage, acorn.x, acorn.y);
		}
		batch.end();

		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnAcorns();

		Iterator<Rectangle> iter = acorns.iterator();
		while(iter.hasNext()) {
			Rectangle acorn = iter.next();
			acorn.y -= 200 * Gdx.graphics.getDeltaTime();
			if(acorn.y + 64 < 0) iter.remove();

			if(acorn.overlaps(bucket)) {
				acornSound.play();
				iter.remove();
			}
		}
	}

	private void spawnAcorns() {
		Rectangle acorn = new Rectangle();
		acorn.x = MathUtils.random(0, 800-64);
		acorn.y = 480;
		acorn.width = 64;
		acorn.height = 64;
		acorns.add(acorn);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose() {
		acornImage.dispose();
		bucketImage.dispose();
		acornSound.dispose();
		acornMusic.dispose();
		batch.dispose();
	}
}
