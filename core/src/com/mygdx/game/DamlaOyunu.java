package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

import java.util.Iterator;

public class DamlaOyunu extends ApplicationAdapter {
	private SpriteBatch sayfa;
	private OrthographicCamera kamera;
	private Texture kova, damla;
	private Rectangle rctKova;
	private Array<Rectangle> damlalar;
	private long sonDamlamaZamani;
	private Sound damlaSesi;
	private Sound kacirmaSesi;


	@Override
	public void create () {
		sayfa = new SpriteBatch();

		kamera = new OrthographicCamera();
		kamera.setToOrtho(false, 800, 480);

		kova = new Texture("img/kova.png");
		rctKova = new Rectangle();
		rctKova.width = 64;
		rctKova.height = 64;
		rctKova.x = 800 / 2 - rctKova.width / 2;
		rctKova.y = 20;

		damla = new Texture("img/damla.png");
		damlalar = new Array<Rectangle>();

		damla_uret();

		damlaSesi = Gdx.audio.newSound(Gdx.files.internal("sounds/drop_sound.mp3"));
		kacirmaSesi = Gdx.audio.newSound(Gdx.files.internal("sounds/fail_sound.mp3"));
	}

	private void damla_uret() {
		Rectangle rctDamla =new Rectangle();
		rctDamla.height = 32;
		rctDamla.width = 32;
		rctDamla.x = MathUtils.random(0, 800 - 32);
		rctDamla.y = 480;
		damlalar.add(rctDamla);
		sonDamlamaZamani = TimeUtils.millis();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 0, 0, 0);

		kamera.update();

		sayfa.setProjectionMatrix(kamera.combined);

		sayfa.begin();

		sayfa.draw(kova, rctKova.x, rctKova.y);

		for (Rectangle rctDamla : damlalar){
			sayfa.draw(damla, rctDamla.x, rctDamla.y);
		}

		sayfa.end();

		if(Gdx.input.isTouched()){
			Vector3 dokunmaP = new Vector3();
			dokunmaP.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			kamera.unproject(dokunmaP);
			rctKova.x = dokunmaP.x - rctKova.width / 2;
		}

		if (TimeUtils.millis() - sonDamlamaZamani > 500){
			damla_uret();
		}

		Iterator<Rectangle> damla = damlalar.iterator();
		while (damla.hasNext()){
			Rectangle rctDamla = damla.next();
			rctDamla.y -= 200 * Gdx.graphics.getDeltaTime();

			if (rctDamla.y + 32 < 0){
				kacirmaSesi.play();
				damla.remove();
			}

			if (rctDamla.overlaps(rctKova)){
				damlaSesi.play();
				damla.remove();
			}
		}
	}

	@Override
	public void dispose(){
		kova.dispose();
		damla.dispose();
		damlaSesi.dispose();
		kacirmaSesi.dispose();
	}
}