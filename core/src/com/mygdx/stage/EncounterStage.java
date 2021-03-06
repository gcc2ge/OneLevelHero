package com.mygdx.stage;

import org.springframework.beans.factory.annotation.Autowired;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.enums.ScreenEnum;
import com.mygdx.factory.ScreenFactory;
import com.mygdx.state.Assets;

public class EncounterStage extends Stage {
	@Autowired
	private Assets assets;
	@Autowired
	private ScreenFactory screenFactory;
	private TextButton fightButton;
	private TextButton fleeButton;

	private Table selTable;

	public Stage makeStage() {
		fightButton = new TextButton("싸운다", assets.skin);
		fleeButton = new TextButton("도망친다", assets.skin);

		selTable = new Table(assets.skin);
		selTable.setFillParent(true);
		//selTable.row();
		selTable.add(fightButton);
		selTable.add(fleeButton);

		selTable.bottom();
		addActor(selTable); // show selTable 

		addListener(); // 리스너 할당
		return this;
	}

	private void addListener() {
		fightButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// BattleScreen으로 넘어가는 것이 전투를 시작하는 것을 의미
				screenFactory.show(ScreenEnum.BATTLE);
			}
		});
		fleeButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				screenFactory.show(ScreenEnum.MOVING);
			}
		});

	}

	public Assets getAssets() {
		return assets;
	}

	public void setAssets(Assets assets) {
		this.assets = assets;
	}

	public ScreenFactory getScreenFactory() {
		return screenFactory;
	}

	public void setScreenFactory(ScreenFactory screenFactory) {
		this.screenFactory = screenFactory;
	}

}