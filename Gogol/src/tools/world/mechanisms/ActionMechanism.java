package tools.world.mechanisms;

import tools.Action;
import tools.Actions;
import tools.world.Entity;


public class ActionMechanism extends WorldMechanism {
	public ActionMechanism(Entity entity) {
		super(entity);
	}
	
	public void onReceive(Action action) {
		if (action.action == Actions.ACTION_CREATE) {
			entity.alive = true;
			entity.init();
		} else if (action.action == Actions.ACTION_CREATE_POS) {
			entity.alive = true;
			entity.getPos().set(action.par1, action.par2);
			entity.init();
		} else if (action.action == Actions.ACTION_CREATE_SCALE) {
			entity.alive = true;
			entity.getPos().set(action.par1, action.par2);
			entity.getVel().set(action.par3, action.par4);
			entity.getScale().set(action.par5, action.par6);
			entity.init();
		} else if (action.action == Actions.ACTION_CREATE_VEL) {
			entity.alive = true;
			entity.getPos().set(action.par1, action.par2);
			entity.getVel().set(action.par3, action.par4);
			entity.init();
		} else if (action.action == Actions.ACTION_CREATE_COLOR) {
			entity.alive = true;
			entity.getPos().set(action.par1, action.par2);
			entity.getColor().set(action.par3, action.par4, action.par5,
					action.par6);
			entity.init();
		}
		else if (action.action == Actions.ACTION_SET_COLOR) {
			entity.getColor().set(action.par1, action.par2, action.par3,
					action.par4);
		}

		else if (action.action == Actions.ACTION_DIE) {
			entity.alive = false;
			entity.die();
		} else if (action.action == Actions.ACTION_SET_POS) {
			entity.getPos().set(action.par1, action.par2);
		} else if (action.action == Actions.ACTION_SET_VEL) {
			entity.getVel().set(action.par1, action.par2);
		} else if (action.action == Actions.ACTION_SET_SCALE) {
			entity.getScale().set(action.par1, action.par2);
			entity.getColScale().set(action.par1, action.par2);
		}  else if (action.action == Actions.ACTION_SET_COL_SCALE) {
			entity.getColScale().set(action.par1, action.par2);
		} 
		else if (action.action == Actions.ACTION_UPDATE) {
			entity.getPos().set(action.par1, action.par2);
			// entity.getVel().set(action.par3, action.par4);
			// entity.getVel2().set(action.par5, action.par6);
		} else if (action.action == Actions.ACTION_SET_ROOT) {
			entity.world.setRootEntity(entity);
		} else if (action.action == Actions.ACTION_CHILD) {
			action.id = (short) action.par1;
			action.action = (byte) action.par2;
			action.par1 = action.par3;
			action.par2 = action.par4;
			action.par3 = action.par5;
			action.par4 = action.par6;
			action.par5 = action.par7;
			action.par6 = action.par8;
			action.par7 = 0;
			action.par8 = 0;
			// entity.world.getEntity((short)fatherID).getEntity(action.id).actionSystem.onReceive(action);

		}
	}
}
