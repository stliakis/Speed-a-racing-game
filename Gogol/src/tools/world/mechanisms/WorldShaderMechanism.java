package tools.world.mechanisms;

import tools.Shader;
import tools.Shader.ShaderParrametersListener;
import tools.general.Tools;
import tools.world.Entity;
import tools.world.gWorld.TaskToDo;

public abstract class WorldShaderMechanism extends WorldMechanism{
	boolean firstUpdate=false;
	Shader shader;
	public WorldShaderMechanism(Entity entity,final Shader shader) {
		super(entity);
		
		shader.setListener(new ShaderParrametersListener() {
			public void setParameters(Shader shader) {
			
				setShaderParameters(shader);
			}
		});
		this.shader=shader;
	}
	public abstract void setShaderParameters(Shader shader);
	public void deactivate(){
		entity.world.worldrenderer.setClearWorldShader();
	}
	public void activate(){
		entity.world.worldrenderer.worldShader=shader;
		entity.world.worldrenderer.defaultShader=shader;
	}
}

