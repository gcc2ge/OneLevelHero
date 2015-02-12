package com.mygdx.manager;

import org.springframework.beans.factory.annotation.Autowired;

import com.mygdx.currentState.MovingInfo;
import com.mygdx.currentState.PositionInfo;
import com.mygdx.enums.PlaceEnum;
import com.mygdx.enums.ScreenEnum;
import com.mygdx.factory.ScreenFactory;
import com.mygdx.state.Assets;

public class MovingManager {
	@Autowired
	private Assets assets;
	@Autowired
	private ScreenFactory screenFactory;
	@Autowired
	private PositionInfo positionInfo;
	@Autowired
	private MovingInfo movingInfo;
	@Autowired
	private EncounterManager encounterManager;

	public void goForward() {
		if (isRoadLeft()) {
			minusLeftRoadLength();
			movingRoad();
		} else {
			positionInfo.setCurrentNode(movingInfo.getDestinationNode());
			goIntoCurrentNode();
		}
	}

	public void goBackward() {
		if (!isRoadFull()) {
			plusLeftRoadLength();
			movingRoad();
		} else {
			// 목적지 노드에 도착해서 현재 위치로 설정함
			positionInfo.setCurrentNode(movingInfo.getDestinationNode());
			goIntoCurrentNode();
		}
	}

	private void plusLeftRoadLength() {
		movingInfo.setLeftRoadLength(movingInfo.getLeftRoadLength() + 1);
	}

	public void minusLeftRoadLength() {
		movingInfo.setLeftRoadLength(movingInfo.getLeftRoadLength() - 1);
	}

	private void movingRoad() {
		if (encounterManager.isBattleOccured()) {
			encounterManager.encountEnemy();
		}
	}

	private void goIntoCurrentNode() {
		String placeType = assets.worldNodeInfoMap.get(
				positionInfo.getCurrentNode()).getType();
		switch (PlaceEnum.findPlaceEnum(placeType)) {
			case VILLAGE:
				screenFactory.show(ScreenEnum.VILLAGE);
				break;
			case DUNGEON:
				screenFactory.show(ScreenEnum.VILLAGE);
				break;
			case FORK:
				screenFactory.show(ScreenEnum.WORLD_MAP);
				break;
			default:
				screenFactory.show(ScreenEnum.VILLAGE);
				break;
		}
	}

	private boolean isRoadLeft() {
		return (movingInfo.getLeftRoadLength() > 0) ? true : false;
	}

	private boolean isRoadFull() {
		return (movingInfo.getRoadLength() <= movingInfo.getLeftRoadLength()) ? true
				: false;
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

	public MovingInfo getMovingInfo() {
		return movingInfo;
	}

	public void setMovingInfo(MovingInfo movingInfo) {
		this.movingInfo = movingInfo;
	}

	public EncounterManager getEncounterManager() {
		return encounterManager;
	}

	public void setEncounterManager(EncounterManager encounterManager) {
		this.encounterManager = encounterManager;
	}
}
