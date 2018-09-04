package com.mygdx.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AnaSayfa extends Activity implements View.OnClickListener {
    Button baslaBtn, ayarlarBtn, reklamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ana_sayfa);

        baslaBtn = (Button) findViewById(R.id.basla_button);
        ayarlarBtn = (Button) findViewById(R.id.ayarlar_button);
        reklamBtn = (Button) findViewById(R.id.reklam_button);

        baslaBtn.setOnClickListener(this);
        ayarlarBtn.setOnClickListener(this);
        reklamBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int btn_id = view.getId();

        switch (btn_id) {
            case R.id.basla_button:
                Intent basla_intent = new Intent(getApplicationContext(), GameLauncher.class);
                startActivity(basla_intent);
                break;

            case R.id.ayarlar_button:
                Intent ayarlar_intent = new Intent(getApplicationContext(), Ayarlar.class);
                startActivity(ayarlar_intent);
                break;

            case R.id.reklam_button:
                Intent reklam_intent = new Intent(getApplicationContext(), Reklam.class);
                startActivity(reklam_intent);
                break;


        }
    }
}