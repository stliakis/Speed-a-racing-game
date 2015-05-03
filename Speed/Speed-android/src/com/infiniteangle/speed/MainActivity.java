package com.infiniteangle.speed;

import main.MainGame;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
       // initialize((ApplicationListener) new MainGame(), cfg);
    }
}
