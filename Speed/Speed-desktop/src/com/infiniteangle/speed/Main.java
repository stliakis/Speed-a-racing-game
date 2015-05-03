package com.infiniteangle.speed;

import main.MainGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SimpleGame";
		cfg.vSyncEnabled=false;
		cfg.width = (int)( (800));
		cfg.height = (int) ((480));
		cfg.foregroundFPS=600;
		cfg.backgroundFPS=600;
		
		//cfg.width=1366;
		//cfg.height=768;
		//cfg.fullscreen=true;
		
		new LwjglApplication(new MainGame(), cfg);
	}
}
