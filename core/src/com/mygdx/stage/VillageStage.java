package com.mygdx.stage;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.currentState.PositionInfo;
import com.mygdx.enums.PlaceEnum;
import com.mygdx.enums.ScreenEnum;
import com.mygdx.factory.ScreenFactory;
import com.mygdx.manager.CameraManager;
import com.mygdx.manager.CameraManager.CameraPosition;
import com.mygdx.model.Building;
import com.mygdx.model.Village;
import com.mygdx.state.Assets;
import com.uwsoft.editor.renderer.Overlap2DStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class VillageStage extends Overlap2DStage {
	@Autowired
	private Assets assets;
	@Autowired
	private ScreenFactory screenFactory;
	@Autowired
	private PositionInfo positionInfo;
	@Autowired
	private CameraManager cameraManager;

	private Village villageInfo;
	public TextButton shiftButton;
	private BackgroundDirection backgroundDirection;

	public enum BackgroundDirection {
		UP, DOWN, MOVE_UP, MOVE_DOWN;
	}

	public Stage makeStage() {
		setVillage();
		return this;
	}

	// 마을 정보에 맞게 스테이지 형성
	private void setVillage() {
		initSceneLoader();
		Gdx.app.debug("VillageStage",
				String.valueOf(positionInfo.getCurrentNode()));
		// 임시로 블랙우드 정보를 넣는다.
		// villageInfo = assets.villageMap.get(positionInfo.getCurrentNode());
		villageInfo = assets.villageMap.get("Blackwood");
		// 아직까진 블랙우드밖에 없으므로 블랙우드 sceneName을 넣어주자
		// sceneLoader.loadScene(villageInfo.getSceneName());
		sceneLoader.loadScene("blackwood_scene");
		cameraManager.setCameraSize(this, CameraPosition.ABOVE_GAME_UI);
		backgroundDirection = BackgroundDirection.DOWN;
		addActor(sceneLoader.getRoot());
		setBuildingButton();
		shiftButton = new TextButton("전환", assets.skin);
		shiftButton.center();
		shiftButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				if (backgroundDirection.equals(BackgroundDirection.DOWN)) {
					backgroundDirection = BackgroundDirection.MOVE_UP;
				} else if (backgroundDirection.equals(BackgroundDirection.UP)) {
					backgroundDirection = BackgroundDirection.MOVE_DOWN;
				}

				event.getListenerActor().setVisible(false);
			}
		});
		addActor(shiftButton);
	}

	@Override
	public void draw() {
		moveCam();
		super.draw();
	}

	private void moveCam() {
		int movingSpeed = 10;
		checkCameraPosition();
		if (backgroundDirection.equals(BackgroundDirection.MOVE_UP)) {
			this.getCamera().translate(0, movingSpeed, 0);
			shiftButton.moveBy(0, movingSpeed);
		} else if (backgroundDirection.equals(BackgroundDirection.MOVE_DOWN)) {
			this.getCamera().translate(0, -movingSpeed, 0);
			shiftButton.moveBy(0, -movingSpeed);
		} else {

		}
	}

	private void checkCameraPosition() {
		if (this.getCamera().position.y > sceneLoader.getRoot().getHeight()
				- this.getCamera().viewportHeight / 2 * 0.90f) {
			this.getCamera().position.y = sceneLoader.getRoot().getHeight()
					- this.getCamera().viewportHeight / 2 * 0.90f;
			backgroundDirection = BackgroundDirection.UP;

			shiftButton.setVisible(true);

		} else if (this.getCamera().position.y < sceneLoader.getRoot()
				.getHeight() * 0.25f) {
			this.getCamera().position.y = sceneLoader.getRoot().getHeight() * 0.25f;
			backgroundDirection = BackgroundDirection.DOWN;

			shiftButton.setVisible(true);
		}
	}

	private void setBuildingButton() {
		for (final Entry<String, Building> building : villageInfo.getBuilding()
				.entrySet()) {
			CompositeItem buildingButton = sceneLoader.getRoot()
					.getCompositeById(building.getValue().getBuildingPath());
			buildingButton.setTouchable(Touchable.enabled);
			buildingButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					positionInfo.setCurrentBuilding(building.getKey());
					positionInfo.setCurrentPlace(PlaceEnum.BUILDING);
					screenFactory.show(ScreenEnum.BUILDING);
				}
			});
		}
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

	public PositionInfo getPositionInfo() {
		return positionInfo;
	}

	public void setPositionInfo(PositionInfo positionInfo) {
		this.positionInfo = positionInfo;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}
}
