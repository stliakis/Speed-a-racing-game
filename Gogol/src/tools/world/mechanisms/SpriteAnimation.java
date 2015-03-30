package tools.world.mechanisms;

import tools.Director;
import tools.general.Pool;
import tools.general.Tools;
import tools.general.Vector;
import tools.general.gColor;
import tools.world.Entity;
import tools.world.WorldRenderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteAnimation {
	public Animation animation;
	public TextureRegion currentFrame;
	Entity entity;
	boolean finished = true;
	public TextureRegion[] frames;
	int playingState;
	Sprite sprite;
	public float stateTime;
	Texture texture;
	public Vector origin=new Vector();
	float tileX,tileY;
	float scale=1;
	public Vector offset=new Vector(0,0,0);
	public SpriteAnimation(int tileSizeX, int col, int row, int framesCount,
			float speed, String textu){
		this(tileSizeX, tileSizeX, col, row, framesCount, speed, textu);
	}
	public SpriteAnimation(int tileSizeX,int tileSizeY, int col, int row, int framesCount,
			float speed, String textu) {
		texture = Director.getAsset(textu, Texture.class);
		TextureRegion[][] tmp = TextureRegion
				.split(texture, tileSizeX, tileSizeY);
		frames = new TextureRegion[framesCount];
		int index = 0;

		int x = col;
		int y = row;
		for (int framesC = col; framesC < framesCount + col; framesC++) {
			if (x == tmp[0].length) {
				y++;
				x = 0;
			}
			frames[index++] = tmp[y][x++];
		}
		
		tileX=tileSizeX;
		tileY=tileSizeY;
		animation = new Animation(speed, frames); // #11
		stateTime = 0f;
		this.speed=speed;
		playingState = -1;
	}
	public SpriteAnimation(int tileSizeX, int col, int row,
			float speed, String textu,int...allFrames){
		this(tileSizeX, tileSizeX, col, row, speed, textu,false,false,allFrames);
	}
	public SpriteAnimation(float scale,int tileSizeX,int tileSizeY, int col, int row,float speed, String textu,int...allFrames) {
		this(tileSizeX, tileSizeY, col, row, speed, textu,false,false,allFrames);
		this.scale=scale;
	}
	public SpriteAnimation(float scale,int tileSizeX,int tileSizeY,float speed, String textu,int...allFrames) {
		this(tileSizeX, tileSizeY, 0, 0, speed, textu,false,false,allFrames);
		this.scale=scale;
	}
	public SpriteAnimation(int tileSizeX,int tileSizeY,float speed, String textu,int...allFrames) {
		this(tileSizeX, tileSizeY, 0, 0, speed, textu,false,false,allFrames);
		this.scale=1;
	}
	public SpriteAnimation(int tileSizeX,int tileSizeY, int col, int row,float speed, String textu,int...allFrames) {
		this(tileSizeX, tileSizeY, col, row, speed, textu,false,false,allFrames);
	}
	float speed;
	public SpriteAnimation(int tileSizeX,int tileSizeY, int col, int row,float speed, String textu,boolean flipX,boolean flipY,int...allFrames) {
		texture = Director.getAsset(textu, Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(texture, tileSizeX, tileSizeY);
		
		frames = new TextureRegion[allFrames.length];
		int index = 0;

		for(int f:allFrames){
			int x = col;
			int y = row;
			for (int framesC = col; framesC < f + col-1; framesC++) {
				x++;
				if (x == tmp[0].length) {
					y++;
					x = 0;
				}
			}
			frames[index] = new TextureRegion(tmp[y][x]);
			frames[index].flip(flipX, flipY);
			index++;
		}
		
		tileX=tileSizeX;
		tileY=tileSizeY;
		animation = new Animation(speed, frames); // #11
		stateTime = 0f;
		this.speed=speed;
		playingState = -1;
	}
	public boolean fliped=false;
	public static Animation.PlayMode MODE=null;
	public void play() {
		lastRegion=null;
		finished = false;
		playingState = 1;
		animation.setPlayMode(Animation.PlayMode.NORMAL);
		if(MODE!=null)animation.setPlayMode(MODE);
		stateTime=0;
		Vector.vector2.set(entity.getScale());
		Vector.vector2.x*=scale;
		Vector.vector2.y*=scale;
		origin.set((Vector.vector2.x/2)*scale,(Vector.vector2.y/2)*scale);
		MODE=null;
	}

	public void playLooping() {
		lastRegion=null;
		finished = false;
		playingState = 0;
		animation.setPlayMode(Animation.PlayMode.LOOP);
		if(MODE!=null)animation.setPlayMode(MODE);
		stateTime=0;
		Vector.vector2.set(entity.getScale());
		Vector.vector2.x*=scale;
		Vector.vector2.y*=scale;
		origin.set((Vector.vector2.x/2)*scale,(Vector.vector2.y/2)*scale);
		MODE=null;
	}
	public void setOrigin(float x,float y){
		origin.x=x;
		origin.y=y;
	}
	public static float SPEED=1;
	OnFinishedListener listener=null;
	OnFrameChange frameChangeListener;
	public void setListener(OnFinishedListener listener) {
		this.listener = listener;
	}
	public static boolean USING_RATIO=false;
	TextureRegion region;
	TextureRegion lastRegion=null;
	public float speedScale=1;
	public void render(WorldRenderer renderer,float delta) {
		if (finished) {
			return;
		}
		stateTime += delta*SPEED*speedScale;
		region=animation.getKeyFrame(stateTime,(playingState == 0) ? true : false);
		renderFrame(renderer,region);
		if (playingState == 1) {
			if (animation.isAnimationFinished(stateTime)) {
				if(listener!=null)listener.finished();
				finished=true;
			}else{
				if(lastRegion!=region && lastRegion!=null && frameChangeListener!=null){
					frameChangeListener.changed(0);
				}
			}
		}else if(playingState == 0){
			if(lastRegion!=region && lastRegion!=null && frameChangeListener!=null){
				frameChangeListener.changed(0);
			}
		}
		lastRegion=region;
	}
	public void renderFrame(WorldRenderer renderer,TextureRegion region){
		renderFrame( renderer ,region, 1,entity.pos,fliped);
	}
	public void renderFrame(WorldRenderer renderer,TextureRegion region,float alpha,Vector pos,boolean flipped){
		if(region==null)return;
		sprite.setRegion(region);
		Vector.vector.set(pos);
		
	//	if(USING_RATIO)entity.getScale().set(entity.genScale*(sprite.getTexture().getWidth()/sprite.getTexture().getHeight()),
	//			entity.genScale*(sprite.getTexture().getHeight()/sprite.getTexture().getWidth()));
		Vector.vector2.set(entity.getScale());
		Vector.vector2.x*=scale;
		Vector.vector2.y*=scale;
		Vector.vector.plus(offset);
		//Vector.vector.y-=Vector.vector2.y*(1-scale);
		
		sprite.setOrigin(origin.x,origin.y);
		
		//Vector.vector.y+=Vector.vector2.y/2-origin.y;
		
		sprite.flip(flipped, false);
		renderer.DrawCenter(sprite, Vector.vector,Vector.vector2 ,
				gColor.color2.set(entity.getColor()), entity.getAngle());
		sprite.flip(flipped, false);

	}
	public SpriteAnimation setOffset(float x,float y){
		offset.set(x, y);
		return this;
	}
	public void next(){
		stateTime+=speed;
	}
	public void renderLast(WorldRenderer renderer){
		renderFrame(renderer,region);
	}
	
	public static class FrameTail{
		TextureRegion tr;
		float a;
		public void set(TextureRegion tr,float a){
			this.tr=tr;
			this.a=a;
		}
	}
	public static interface OnFinishedListener{
		public void finished();
	}
	public static interface OnFrameChange{
		public void changed(int id);
	}
}
