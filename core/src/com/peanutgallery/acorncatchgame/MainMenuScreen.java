package com.peanutgallery.acorncatchgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainMenuScreen implements Screen {

	final AcornCatchGame game;
	Stage stage;

	OrthographicCamera camera;
	private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private ImageButton startButton; //** the button - the only actor in program **//


	public MainMenuScreen(final AcornCatchGame game) {
		this.game = game;
		stage = new Stage();
		stage.clear();
		Gdx.input.setInputProcessor(stage);
		
		buttonsAtlas = new TextureAtlas(Gdx.files.internal("images/buttons/startbutton.pack")); //** button atlas image **// 
	    buttonSkin = new Skin();
	    buttonSkin.addRegions(buttonsAtlas);

		ImageButtonStyle style = new ImageButtonStyle();
		style.up = buttonSkin.getDrawable("startbuttonup");
		style.down = buttonSkin.getDrawable("startbuttondown");
		
		startButton = new ImageButton(style);
		startButton.setPosition(400-140/2, 240-62/2);
		startButton.setWidth(140);
		startButton.setHeight(62);
		
		startButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        
        stage.addActor(startButton);

		

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to AcornCatch!!! ", 100, 150);
        game.font.draw(game.batch, "Touch PLAY to start.", 100, 100);
        game.batch.end();
        
        game.batch.begin();
        stage.draw();
        game.batch.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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

	}

}
