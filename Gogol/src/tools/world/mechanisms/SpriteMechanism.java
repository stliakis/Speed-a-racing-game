package tools.world.mechanisms;

import java.util.ArrayList;
import java.util.List;

import tools.Director;
import tools.Shader;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;
import tools.world.gWorld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SpriteMechanism extends WorldMechanism {
	public static List<Shader> shaders = new ArrayList<Shader>(0);
	public gColor color;
	private boolean isUsingShader = false;
	public Vector origin;
	public Vector pos, scale, rotation;
	public Shader shader = null;
	public Sprite sprite;
	public Texture texture;
	public int col,row,tileSize;
	public boolean visible = true;
	public String texturePath;
	public int layer=0;
	public int assetID=-1;
	public void createSprite(String textu){
		
		if(textu!=null){
			texture = Director.getAsset(textu, Texture.class);
			sprite = new Sprite(texture);
		}
		else{
			assetID=entity.world.screen.addAsset(textu, Texture.class);
			sprite=new Sprite();
		}
	}
	public void reload() {
		super.reload();
		if(assetID!=-1){
			this.texture =(Texture)entity.world.screen.getAsset(assetID);
			if(sprite==null){
				sprite = new Sprite(this.texture);
			}else{
				sprite.setTexture(this.texture);
			}
			
		}
	}
	public SpriteMechanism(Entity entity, String textu,int layer){
		this(entity,textu);
		this.layer=layer;
	}
	public SpriteMechanism(Entity entity, String textu) {
		super(entity);
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		texturePath=textu;
	}
	public void setShaderParams(ShaderProgram shader) {
	}
	public SpriteMechanism(Entity entity, String textu,String vertShader, String fragShader,boolean createNew) {
		super(entity);
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		texturePath=textu;
		
		boolean containsShader = false;
		if (!createNew) {
			for (Shader shader : shaders) {
				if (shader.compare(fragShader, vertShader)) {
					this.shader = shader;
					containsShader = true;
				}
			}
			if (!containsShader) {
				shader = new Shader(vertShader, fragShader);
				shaders.add(shader);
			}
		} else {
			shader = new Shader(vertShader, fragShader);
			shaders.add(shader);
		}

		isUsingShader = true;
	}
	
	public SpriteMechanism(Entity entity,Sprite sprite) {
		super(entity);
		this.sprite = sprite;
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,int tileSize,int layer) {
		this(entity,textu,col,row,tileSize);
		this.layer=layer;
	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,int tileSize) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		this.col=col;this.row=row;this.tileSize=tileSize;
		isUsingShader = false;
		texturePath=textu;
	}
	public SpriteMechanism(Entity entity,SpriteMechanism sm) {
		this(entity,sm.texturePath,sm.col,sm.row,sm.tileSize);
		color=new gColor(sm.color);
		pos=new Vector(sm.pos);
		rotation=new Vector(sm.rotation);
		scale=new Vector(sm.scale);
	}
	public boolean followingCamera=false;;
	public SpriteMechanism(Entity entity, String textu, int col, int row,int tileSize, int tileSizeX, int tileSizeY,int layer) {
		this(entity,textu,col,row,tileSize,tileSizeX,tileSizeY);
		this.layer=layer;
	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,int tileSize, int tileSizeX, int tileSizeY) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		sprite.setRegion(col * tileSize, row * tileSize, tileSizeX * tileSize,tileSizeY * tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
		this.col=col;this.row=row;this.tileSize=tileSize;
	}

	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, int tileSizeX, int tileSizeY, String vertShader,
			String fragShader, boolean createNew) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		sprite.setRegion(col * tileSize, row * tileSize, tileSizeX * tileSize,
				tileSizeY * tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);

		boolean containsShader = false;
		if (!createNew) {
			for (Shader shader : shaders) {
				if (shader.compare(fragShader, vertShader)) {
					this.shader = shader;
					containsShader = true;
				}
			}
			if (!containsShader) {
				shader = new Shader(vertShader, fragShader);
				shaders.add(shader);
			}
		} else {
			shader = new Shader(vertShader, fragShader);
			shaders.add(shader);
		}

		isUsingShader = true;

	}

	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, int tileSizeX, int tileSizeY, Vector pos,
			Vector scale, Vector rotation, gColor color) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		this.color = color;
		this.scale = scale;
		this.rotation = rotation;
		this.pos = pos;
		sprite.setRegion(col * tileSize, row * tileSize, tileSizeX * tileSize,
				tileSizeY * tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, int tileSizeX, int tileSizeY, Vector pos,
			Vector scale, Vector rotation, gColor color,int layer) {
		super(entity);
		this.layer=layer;
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		this.color = color;
		this.scale = scale;
		this.rotation = rotation;
		this.pos = pos;
		sprite.setRegion(col * tileSize, row * tileSize, tileSizeX * tileSize,
				tileSizeY * tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
	}


	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, String vertShader, String fragShader,
			boolean createNew) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		color = entity.getColor();
		scale = entity.getScale();
		rotation = entity.getRotation();
		pos = entity.getPos();
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);

		boolean containsShader = false;
		if (!createNew) {
			for (Shader shader : shaders) {
				if (shader.compare(fragShader, vertShader)) {
					this.shader = shader;
					containsShader = true;
				}
			}
			if (!containsShader) {
				shader = new Shader(vertShader, fragShader);
				shaders.add(shader);
			}
		} else {
			shader = new Shader(vertShader, fragShader);
			shaders.add(shader);
		}

		isUsingShader = true;

	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, Vector pos, Vector scale, Vector rotation,
			gColor color,int layer) {
		super(entity);
		this.layer=layer;
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		this.color = color;
		this.scale = scale;
		this.rotation = rotation;
		this.pos = pos;
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
	}
	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, Vector pos, Vector scale, Vector rotation,
			gColor color,String vertShader, String fragShader,
			boolean createNew) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		this.color = color;
		this.scale = scale;
		this.rotation = rotation;
		this.pos = pos;
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
		boolean containsShader = false;
		if (!createNew) {
			for (Shader shader : shaders) {
				if (shader.compare(fragShader, vertShader)) {
					this.shader = shader;
					containsShader = true;
				}
			}
			if (!containsShader) {
				shader = new Shader(vertShader, fragShader);
				shaders.add(shader);
			}
		} else {
			shader = new Shader(vertShader, fragShader);
			shaders.add(shader);
		}

		isUsingShader = true;
	}
	
	public SpriteMechanism(Entity entity, String textu, int col, int row,
			int tileSize, Vector pos, Vector scale, Vector rotation,
			gColor color) {
		super(entity);
		// texture=new Texture(Gdx.files.internal(textu));
		createSprite(textu);
		this.color = color;
		this.scale = scale;
		this.rotation = rotation;
		this.pos = pos;
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
		origin = new Vector(entity.getScale().x / 2, entity.getScale().y / 2);
		isUsingShader = false;
	}

	public void enableShader(WorldRenderer renderer) {
		if (isUsingShader) {
			setShader(renderer);
		} else {
			if (renderer.currentShader != renderer.defaultShader) {
				renderer.setDefaultShader();
			}
		}
	}

	public ShaderProgram getShader() {
		return shader.getShader();
	}

	public boolean isUsingShader() {
		return isUsingShader;
	}
	public boolean invalidLayer(){
		return gWorld.LAYERED_RENDERING && entity.world.renderingLayer!=layer;
	}
	public static class CustomRender{
		public void onRender(WorldRenderer renderer,SpriteMechanism sm){
			
		}
	}
	public static CustomRender renderFunction=new CustomRender(){
		@Override
		public void onRender(WorldRenderer renderer, SpriteMechanism sm) {
			// TODO Auto-generated method stub
			super.onRender(renderer, sm);
			if(gWorld.LAYERED_RENDERING && sm.entity.world.renderingLayer!=sm.layer)return;
			if(sm.shader!=null)sm.setShaderParams(sm.shader.getShader());
			sm.enableShader(renderer);
			sm.origin.set(sm.scale.x/2,sm.scale.y/2);
			sm.sprite.setOrigin(sm.origin.x, sm.origin.y);
			Vector.vector.set(sm.pos);
			if(sm.followingCamera){
				Vector.vector.x-=sm.entity.world.cameraPos.x;
				Vector.vector.y-=sm.entity.world.cameraPos.y;
			}
			renderer.DrawCenter(sm.sprite,Vector.vector, sm.scale, sm.color, sm.rotation.z);
		}
	};;
	@Override
	public void render(WorldRenderer renderer) {
		renderFunction.onRender(renderer, this);
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public void setShader(WorldRenderer renderer) {
		if (shader == null) {
			if (renderer.currentShader != renderer.defaultShader) {
				renderer.setDefaultShader();
			}
		} else {
			if (renderer.currentShader != shader) {
				renderer.currentShader=shader;
				renderer.getSpritebatch().setShader(renderer.currentShader.getShader());
			}
		}
	}

	public void setSpriteRegion(int col, int row, int tileSize) {
		sprite.setRegion(col * tileSize, row * tileSize, tileSize, tileSize);
	}
	public void setSpriteRegion(int col, int row,int tileSize, int tileSizeX, int tileSizeY) {
		sprite.setRegion(col * tileSize, row * tileSize, tileSizeX * tileSize,
				tileSizeY * tileSize);
	}
	public void setUsingShader(boolean isUsingShader) {
		this.isUsingShader = isUsingShader;
	}
}
