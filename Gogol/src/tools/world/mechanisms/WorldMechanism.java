package tools.world.mechanisms;

import java.util.ArrayList;
import java.util.List;

import tools.world.Entity;
import tools.world.WorldRenderer;


import com.badlogic.gdx.Input;

public class WorldMechanism {
	public Entity entity;
	boolean hasSystem = false;
	public List<WorldMechanism> systems;

	public WorldMechanism(Entity entity) {
		this.entity = entity;
		systems = new ArrayList<WorldMechanism>();
		createSystem();
	}

	public WorldMechanism addMechanism(WorldMechanism system) {
		hasSystem = true;
		system.setEntity(entity);
		systems.add(system);
		return system;
	}

	public void createSystem() {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).createSystem();
		}
	}

	public void die() {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).die();
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void init() {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).init();
		}
	}

	public void reload(){
		
	}
	public <T extends WorldMechanism> T getSystemInside(Class<T> clazz) {
		for (int c = 0; c < systems.size(); c++) {
			if (clazz.isInstance(systems.get(c))) {
				return clazz.cast(systems.get(c));
			}
		}
		return null;
	}


	public void render(WorldRenderer renderer) {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).render(renderer);
		}
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void touch(Input input) {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).touch(input);
		}
	}

	public void touchDown(int x, int y, int pointer) {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).touchDown(x, y, pointer);
		}
	}

	public void touchUp(int x, int y, int pointer) {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).touchUp(x, y, pointer);
		}
	}

	public void update() {
		if (!hasSystem) {
			return;
		}
		for (int c = 0; c < systems.size(); c++) {
			systems.get(c).update();
		}
	}

}
