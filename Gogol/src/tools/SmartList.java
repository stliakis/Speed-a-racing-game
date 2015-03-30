package tools;

import java.util.ArrayList;
import java.util.List;

import tools.general.Tools;
import tools.ui.Button;
import tools.ui.Screen;
import tools.ui.UIitem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SmartList extends UIitem {
	public static class SmartListItem extends UIitem {
		public Button button;
		boolean isCompact;
		public List<UIitem> items = new ArrayList<UIitem>();

		public SmartListItem(Screen screen, String name, String desciption,
				String buttonIcon, String icon, boolean compact,
				UIitem... items) {
			super(screen);
			button = new Button(0, 0, screen.x(20), screen.x(6), name,
					buttonIcon, screen);
			size.set(button.size);
			isCompact = compact;
			for (UIitem item : items) {
				this.items.add(item);
			}
		}

		public void Render(SpriteBatch sb, boolean isSelected) {
			button.Render(sb);
			for (UIitem item : items) {
				item.Render(sb);
			}

		}

		public void Update(boolean isSelected) {
			super.Update();
			if (!isCompact) {
				float posOfset = pos.y;
				for (UIitem item : items) {
					item.pos.x = button.screen.x(60);
					item.pos.y = posOfset;
					posOfset -= item.size.y;
					item.color.set(color);
				}
			}

			if (!isSelected) {
			//	button.colormb.a = 0.5f;
			} else {
			//	button.colormb.a = 1;
			}
			button.setPos(pos);
		}

		@Override
		public void Render(SpriteBatch sb) {
			// TODO Auto-generated method stub
			
		}
	}

	public List<SmartListItem> items = new ArrayList<SmartListItem>();
	float itemsOfset = 0, itemsOfsetMb = 0;
	float lastKeyPress = 0;
	public Screen screen;

	public int selectedItem = 0;

	public SmartList(Screen screen) {
		super(screen);
	}

	public int distanceFromItem(SmartListItem sItem) {
		int distance = 0;
		for (SmartListItem item : items) {
			if (item == sItem) {
				return distance;
			}
			distance++;
		}
		return -1;
	}

	@Override
	public void Render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		for (SmartListItem item : items) {
			if (items.get(selectedItem) == item) {
				item.Render(sb, true);
			}
			item.Render(sb, false);
		}
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		super.Update();

		itemsOfsetMb = 0;
		for (int c = 0; c < selectedItem; c++) {
			itemsOfsetMb += items.get(c).size.y;
		}

		itemsOfset += ((itemsOfsetMb - itemsOfset) / 10) * Director.delta;

		float y = screen.y(80) + itemsOfset;
		for (int c = 0; c < items.size(); c++) {
			SmartListItem item = items.get(c);

			item.pos.x = screen.x(25);
			item.pos.y = y;
			y -= item.size.y;

			if (items.get(selectedItem) == item) {
				// item.color.a=Math.abs(selectedItem-items.size())
				item.Update(true);
			}
			item.Update(false);
		}

		lastKeyPress += Director.delta;

		if (lastKeyPress > 8) {
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				if (selectedItem != items.size() - 1) {
					selectedItem++;
				}
			}
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				if (selectedItem != 0) {
					selectedItem--;
				}
			}
			lastKeyPress = 0;
		}

	}
}
