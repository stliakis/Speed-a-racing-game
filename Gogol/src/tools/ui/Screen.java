package tools.ui;

import java.util.ArrayList;
import java.util.List;

import tools.Director;
import tools.RenderOnTexture;
import tools.Shader;
import tools.general.Vector;
import tools.general.gColor;
import tools.ui.UIRenderer.OnFocus;
import tools.ui.UIRenderer.OnLeaving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Screen {
	private boolean preRenderingEffect=false;;
	public static int CAMERA_2D = 0;
	public static int CAMERA_3D = 1;
	public UIRenderer uirenderer;
	private int cameraType;
	public Vector cameraSize;
	public Vector posIn3d = new Vector(0, 0, 0);
	public Vector screenSize = new Vector(0, 0, 0);
	public PerspectiveCamera cameraPer;
	public OrthographicCamera cameraOrtho;
	public PerspectiveCamera effectCamera;
	public float effscale = 1, effx = 0;
	public List<Layer> layers;
	public int id;
	private Director director;
	public Shader[] shaders=null;
	RenderOnTexture textureRenderer;
	public Vector effectCameraPos=new Vector(0,0,2);
	public Vector effectCameraRot=new Vector(0,0,0);
	public gColor effectCameraColor=new gColor(1,1,1,1);
	public Screen() {
		layers = new ArrayList<Layer>();
	}
	
	public Screen(float preRenderScale) {
		layers = new ArrayList<Layer>();
		this.preRenderScale=preRenderScale;
		if(textureRenderer==null)textureRenderer=new RenderOnTexture(preRenderScale);
		float aspectRatio = Gdx.graphics.getWidth()/ Gdx.graphics.getHeight();
		if(effectCamera==null)effectCamera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
	}
	public void Action(int id) {
		for (int i = 0; i < layers.size(); ++i) {
			Layer layer = layers.get(i);
			layer.action(id);
		}
	}
	public void AddLayer(Layer layer) {
		layers.add(layer);
		layer.setDirector(this.director);
	}
	public Layer getLayer(Class<? extends Layer> cl) {
		for (int c = 0; c < layers.size(); c++) {
			if (layers.get(c).getClass().equals(cl))
				return layers.get(c);
		}
		return null;
	}
	public void destroy() {
	}
	public Camera getCamera() {
		if (cameraType == CAMERA_2D)
			return cameraOrtho;
		else
			return cameraPer;
	}
	public Director getDirector() {
		return director;
	}
	int currentShaderID=0;
	public Shader getCurrentShader(){
		return shaders[currentShaderID];
	}

	public void gotFocus() {
		if(uirenderer!=null){
			reloadAssets();
			for(OnFocus of=uirenderer.listeners.begin(OnFocus.class);of!=null;of=uirenderer.listeners.next()){
				of.onFocus(uirenderer);
			}
			
		}
	}
	public void layersGotFocus(){
		for (int i = 0; i < layers.size(); ++i) {
			Layer layer = layers.get(i);
			layer.screenGotFocus();
		}
	}
	public void lostFocus(){
		if(uirenderer!=null ){
			for(OnLeaving of=uirenderer.listeners.begin(OnLeaving.class);of!=null;of=uirenderer.listeners.next()){
				of.onLeaving(uirenderer);
			}
		}
		
	}
	public void CustumRender(){
		
	}
	public void Update(){
		
	}
	public void PreRender(){
		
	}
	public void Render() {
		if(preRenderingEffect){
			textureRenderer.begin();
		}
		if(uirenderer!=null){
			PreRender();
			uirenderer.Render(getCamera());
		}
		CustumRender();
		if(preRenderingEffect){
			TextureRegion region=textureRenderer.end();
			if(region!=null){
				Director.spriteBatch.setProjectionMatrix(effectCamera.combined);
				Director.spriteBatch.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				Director.spriteBatch.enableBlending();
				Director.spriteBatch.begin();
				if(shaders!=null && currentShaderID!=-1 &&  shaders[currentShaderID]!=null){
					Director.spriteBatch.setShader(shaders[currentShaderID].getShader());
					ShaderParameters(getCurrentShader().getShader());
				}
				Vector3 size=new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),0);
				effectCamera.unproject(size);
				effectCamera.position.set(0,0,effectCameraPos.z);
				effectCamera.update();
				Director.spriteBatch.setColor(effectCameraColor.r, effectCameraColor.g, effectCameraColor.b,effectCameraColor.a);
				Director.spriteBatch.draw(region,-size.x*2+effectCameraPos.x, -size.y*2+effectCameraPos.y, size.x*4,size.y*4);
				Director.spriteBatch.end();
				if(shaders!=null && currentShaderID!=-1 && shaders[currentShaderID]!=null)Director.spriteBatch.setShader(Director.defaultShader);
			}else{
			}
		}
		Update();
	}
	public void ShaderParameters(ShaderProgram shader){
		
	}
	public void RenderLayers() {
		for (int i = 0; i < layers.size(); ++i) {
			Layer layer = layers.get(i);
			layer.render();
		}

	}

	public void Resize(float sx, float sy) {
		if (cameraType == CAMERA_2D) {
			screenSize.x = sx;
			screenSize.y = sy;
		} else {
			float aspectRatio = sx / sy;
			cameraPer = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
			cameraPer.near = 1;
			cameraPer.far = 200;
		}
		for (Layer layer : layers)
			layer.resize(sx, sy);
	}

	public float rx(float percent, float width) {
		return (percent / 100f) * width;
	}

	public float ry(float percent, float height) {
		return (percent / 100f) * height;
	}

	public float s2wX(float x, float screenwidth) {
		return (x / screenwidth) * cameraSize.x;
	}

	public float s2wY(float y, float screenheight) {
		return (y / screenheight) * cameraSize.y;
	}

	public Screen setCamera2D(Vector size) {
		this.cameraSize = size;
		this.cameraType = CAMERA_2D;
		this.cameraOrtho = new OrthographicCamera();
		this.cameraOrtho.setToOrtho(false, cameraSize.x, cameraSize.y);
		this.screenSize.set(size);
		return this;
	}

	public Screen setCamera3D(Vector size) {
		this.cameraType = CAMERA_3D;
		this.cameraSize = size;
		float aspectRatio = size.x / size.y;
		cameraPer = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
		return this;
	}

	public void setDirector(Director director) {
		this.director = director;
		for (int c = 0; c < layers.size(); c++) {
			layers.get(c).setDirector(director);
		}
	}

	float preRenderScale=1;
	public void setPreRenderingEffect(boolean preRenderingEffect) {
		this.preRenderingEffect=preRenderingEffect;
		if(textureRenderer==null)textureRenderer=new RenderOnTexture(preRenderScale);
		float aspectRatio = Gdx.graphics.getWidth()/ Gdx.graphics.getHeight();
		if(effectCamera==null)effectCamera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
	}
	public void setShaders(Shader... shaders){
		this.shaders=shaders;
	}
	public void setScreenShader(int id){
		this.currentShaderID=id;
	}

	public int getCurrentShaderID() {
		return currentShaderID;
	}
	public void setCurrentShaderID(int currentShaderID) {
		this.currentShaderID = currentShaderID;
	}
	public boolean isPreRenderingEffect() {
		return preRenderingEffect;
	}
	public void Touch(Input input) {
		if(uirenderer!=null)uirenderer.Touch(input);
	}

	public void touchDown(int x, int y, int pointer) {
	}

	public void touchDownCaller(int x, int y, int pointer) {
		touchDown(x, y, pointer);
		if(uirenderer!=null)uirenderer.TouchDown(x, y, pointer);
		for (Layer layer : layers)
			layer.touchDown(x, y, pointer);
	}

	public void TouchLayers(Input input) {
		for (int i = 0; i < layers.size(); ++i) {
			Layer layer = layers.get(i);
			layer.touch(input);
		}
	}

	public void touchUp(int x, int y, int pointer) {

	}

	public void touchUpCaller(int x, int y, int pointer) {
		touchUp(x, y, pointer);
		if(uirenderer!=null)uirenderer.TouchUp(x, y, pointer);
		for (Layer layer : layers)
			layer.touchUp(x, y, pointer);
	}

	public void Update(Director director) {
	}

	public void UpdateLayers(Director director, Screen screen) {
		for (int i = 0; i < layers.size(); ++i) {
			Layer layer = layers.get(i);
			layer.update(director, screen);
		}
	}
	
	public UIRenderer getUirenderer() {
		return uirenderer;
	}

	public void setUirenderer(UIRenderer uirenderer) {
		this.uirenderer = uirenderer;
	}

	public float x(float percent) {
		return (percent / 100f) * cameraSize.x;
	}

	public float y(float percent) {
		return (percent / 100f) * cameraSize.y;
	}
	
	
	List<FetchAsset> assets=new ArrayList<FetchAsset>();
	public int addAsset(String asset,Class type){
		for(FetchAsset fa:assets){
			if(fa.file==asset){
				return fa.id;
			}
		}
		FetchAsset fa=new FetchAsset(asset, type);
		assets.add(fa);
		int id=assets.indexOf(fa);
		fa.id=id;
		return id;
	}
	public boolean hasAssets(){
		return assets.size()==0?false:true;
	}
	public void fetchAssets(){
		for(FetchAsset fa:assets){
			director.assets.load(fa.file, fa.type);
		}
	}
	
	public void reloadAssets(){
		for(int c=0;c<uirenderer.items.size();c++){
			UIitem item=uirenderer.items.get(c);
			item.reload();
		}
	}
	public Object getAsset(int id){
		for(FetchAsset fa:assets){
			if(fa.id!=id)continue;
			return director.assets.get(fa.file);
		}
		return null;
	}
	
	
	public void dropAssets(){
		for(FetchAsset fa:assets){
			director.assets.unload(fa.file);
		}
	}
	private static class FetchAsset{
		String file;
		int id;
		String name;
		Class type;
		Object asset;
		public FetchAsset(String file,Class type){
			this.file=file;
			this.type=type;
		}
		
	}
}
