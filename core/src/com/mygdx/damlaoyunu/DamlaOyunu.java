package com.mygdx.damlaoyunu;

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
	private Texture arkaplan, kova, damla;
	private Rectangle rctKova;
	private Array<Rectangle> damlalar;
	private long son_damlama_zamani;
	private Sound damla_sesi;
	private Sound kacirma_sesi;


	@Override
	public void create () {
		sayfa = new SpriteBatch();

		kamera = new OrthographicCamera();
		kamera.setToOrtho(false, 800, 480);

		arkaplan = new Texture("arkaplan2.jpg");

		kova = new Texture("kova.png");
		rctKova = new Rectangle();
		rctKova.width = 64;
		rctKova.height = 64;
		rctKova.x = 800 / 2 - rctKova.width / 2;
		rctKova.y = 20;

		damla = new Texture("damla.png");
		damlalar = new Array<Rectangle>();

		damla_uret();

		damla_sesi = Gdx.audio.newSound(Gdx.files.internal("drop_sound.mp3"));
		kacirma_sesi = Gdx.audio.newSound(Gdx.files.internal("fail_sound.mp3"));
	}

	private void damla_uret() {
		Rectangle rctDamla =new Rectangle();
		rctDamla.height = 32;
		rctDamla.width = 32;
		rctDamla.x = MathUtils.random(0, 800 - 32);
		rctDamla.y = 480;
		damlalar.add(rctDamla);
		son_damlama_zamani = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		kamera.update();

		sayfa.setProjectionMatrix(kamera.combined);

		sayfa.begin();
		sayfa.draw(arkaplan, 0, 0);
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

		if (TimeUtils.nanoTime() - son_damlama_zamani > 500000000){
			damla_uret();
		}

		Iterator<Rectangle> damla = damlalar.iterator();
		while (damla.hasNext()){
			Rectangle rctDamla = damla.next();
			rctDamla.y -= 200 * Gdx.graphics.getDeltaTime();

			if (rctDamla.y + 32 < 0){
				kacirma_sesi.play();
				damla.remove();
			}

			if (rctDamla.overlaps(rctKova)){
				damla_sesi.play();
				damla.remove();
			}
		}
	}

	@Override
	public void dispose(){
		arkaplan.dispose();
		kova.dispose();
		damla.dispose();
		damla_sesi.dispose();
		kacirma_sesi.dispose();
	}
}