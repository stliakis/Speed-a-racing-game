package tools.ui;

import java.util.ArrayList;
import java.util.List;

import tools.Director;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ListUI extends UIitem{
	public List<Button> buttons=new ArrayList<Button>();
	public Button selectedButton;
	Button[][] buttonsTable;
	int selectedIndexX,selectedIndexY;
	public ListUI(Screen screen,Button[][] buttons){
		super(screen);
		selectedIndexX=0;
		selectedIndexY=0;
		touchable=true;
		
		for(Button[] buttonPos:buttons){
			for(Button button:buttonPos){
				if(!this.buttons.contains(button)){
					this.buttons.add(button);
				}
			}
		}
		buttonsTable=buttons;
	}
	public void Render(SpriteBatch sb) {
		for(int c=0;c<buttons.size();c++){
			Button button=buttons.get(c);
			button.Render(sb);
		}
	}
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		super.reload();
		for(Button b:buttons)b.reload();
	}
	public float lastKeyPress=0;
	public void Update() {
		super.Update();
		for(int c=0;c<buttons.size();c++){
			Button button=buttons.get(c);
			
			button.Update();
			
			
			
			if(button==selectedButton){
				if(button.scaleup<1.05f)button.scaleup+=((1.05f-button.scaleup)/5)*Director.delta;
			}
			else if(button.scaleup>0)button.scaleup+=((1-button.scaleup)/5)*Director.delta;
		}
		lastKeyPress+=Gdx.graphics.getDeltaTime()*1000;
		if(lastKeyPress>200){
			if(Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.DPAD_DOWN) ){
				down();
				lastKeyPress=0;
			}
			if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DPAD_UP)){
				up();
				lastKeyPress=0;
			}
			if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)){
				right();
				lastKeyPress=0;
			}
			if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.DPAD_LEFT)){
				left();
				lastKeyPress=0;
			}
			
			
			for(Controller controller: Controllers.getControllers()) {
				
				if(Director.runningOnOuya){
					if(controller.getButton(Ouya.BUTTON_DPAD_DOWN) || controller.getButton(0)){
						down();
						lastKeyPress=0;
					}
					if(controller.getButton(Ouya.BUTTON_DPAD_UP)){
						up();
						lastKeyPress=0;
					}
					if(controller.getButton(Ouya.BUTTON_DPAD_LEFT)){
						left();
						lastKeyPress=0;
					}
					if(controller.getButton(Ouya.BUTTON_DPAD_RIGHT)){
						right();
						lastKeyPress=0;
					}
				}
				else{
					
				}
			}
			
			selectedButton=buttonsTable[selectedIndexY][selectedIndexX];
			
			if(Gdx.input.isKeyPressed(Keys.ENTER)){
				if(selectedButton.getOnEventListener()!=null)selectedButton.getOnEventListener().onEventEnd(null,null);
				lastKeyPress=0;
			}
			
		}

		if(!Director.runningOnOuya){
			selectedButton=null;
		}
	
		
		
	}
	@Override
	public boolean Touch(Input input) {
		for(Button button:buttons){
			if(button.Touch(input)){
				selectedButton=button;
				for(int y=0;y<buttonsTable.length;y++){
					for(int x=0;x<buttonsTable[y].length;x++){
						if(buttonsTable[y][x]==button){
							selectedIndexX=x;
							selectedIndexY=y;
						}
					}
				}
			}
		}
		return false;
	}
	void down(){
		if(selectedIndexY+1<buttonsTable.length){
			selectedIndexY++;
			try{
				buttonsTable[selectedIndexY][selectedIndexX].getSize();
			}
			catch(Exception ex){
				selectedIndexY--;
			}
		}
	}
	void up(){
		if(selectedIndexY-1>=0){
			selectedIndexY--;
		}
	}
	void left(){
		if(selectedIndexX-1>=0){
			selectedIndexX--;
			lastKeyPress=0;
		}
	}
	void right(){
		if(selectedIndexX+1<buttonsTable[selectedIndexY].length){
			selectedIndexX++;
			lastKeyPress=0;
		}
	}
}
