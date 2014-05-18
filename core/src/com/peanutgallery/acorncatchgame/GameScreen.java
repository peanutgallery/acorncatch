package com.peanutgallery.acorncatchgame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	final AcornCatchGame game;

	Texture acornImage;
	Texture bucketImage;
	Sound acornSound;
	Music acornMusic;

	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> acorns;

	long lastDropTime;

	public GameScreen(final AcornCatchGame game2) {
		this.game = game2;

		game.lives = 5;
		game.acornsGathered = 0;

		// load the images for the acorn and the bucket, 64x64 pixels each
		acornImage = new Texture(Gdx.files.internal("images/acorn.png"));
		bucketImage = new Texture(Gdx.files.internal("images/bucket.png"));

		// load the drop sound effect and the acorn background "music"
		acornSound = Gdx.audio.newSound(Gdx.files.internal("sounds/catchacorn.wav"));
		acornMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background-music.mp3"));
		acornMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
		// the bottom screen edge
		bucket.width = 64;
		bucket.height = 64;

		// create the acorns array and spawn the first acorn
		acorns = new Array<Rectangle>();
		spawnAcorns();
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
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		game.font.draw(game.batch, "Acorns Collected: " + game.acornsGathered, 0, 480);
		game.font.draw(game.batch, "Lives: " + game.lives, 700, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle acorn : acorns) {
			game.batch.draw(acornImage, acorn.x, acorn.y, acorn.width, acorn.height);
		}
		game.batch.end();


		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		// check if we need to create a new acorn
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnAcorns();

		// move the acorns, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the 
		// value our acorns counter and add a sound effect.

		Iterator<Rectangle> iter = acorns.iterator();
		while(iter.hasNext()) {
			Rectangle acorn = iter.next();
			acorn.width = 38;
			acorn.height = 48;
			acorn.y -= 200 * Gdx.graphics.getDeltaTime();
			if(acorn.y + 64 < 0) {
				iter.remove();
				game.lives--; 
				if (game.lives < 1) {
					game.setScreen(new EndGameScreen(game));
					//dispose();
				}
			}

			if(acorn.overlaps(bucket)) {
				acornSound.play();
				iter.remove();
				game.acornsGathered++;
			}
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		acornMusic.play();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		acornImage.dispose();
		bucketImage.dispose();
		acornSound.dispose();
		acornMusic.dispose();
		game.batch.dispose();
	}

}
