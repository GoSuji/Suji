/*
 * Copyright (c) 2017
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ui.controller;

import event.GameEvent;
import event.ScoreEvent;
import javafx.scene.input.MouseEvent;
import logic.gamehandler.GameHandler;
import logic.score.Scorer;
import ui.controller.sidebar.GameMenuController;
import ui.controller.sidebar.ReviewPaneController;
import ui.controller.sidebar.ScorePaneController;
import ui.controller.strategy.BoardStrategy;
import ui.controller.strategy.GamePlay;
import ui.controller.strategy.Scoring;
import ui.drawer.GameScoreDrawer;

import java.net.URL;
import java.util.ResourceBundle;

public class LocalGameController extends BoardController {

	private Scorer boardScorer;

	private ScorePaneController scorePaneController;
	private GameMenuController gameMenuController;
	private ReviewPaneController reviewPaneController;

	private BoardStrategy strategy;

	public LocalGameController(GameHandler handler) {
		super(handler, "/fxml/localGame.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		super.initialize(url, resourceBundle);
		strategy = new GamePlay(boardCanvas, getPublisher(), gameDrawer);
		setupEventHandlers();
	}

	private void setupEventHandlers() {
	}

	@Override
	void setupPanes() {
		super.setupPanes();
		loadGameMenu();
		loadScorePane();
		loadReviewPanel();
	}

	@Override
	void canvasClicked(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasClicked(mouseEvent);
	}

	@Override
	void canvasHover(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasHover(mouseEvent);
	}

	@Override
	void canvasExit(MouseEvent mouseEvent) {
		if ( strategy != null )
			strategy.canvasExit(mouseEvent);
	}

	@Override
	void enterScoring(GameEvent event) {
		boardScorer = getPublisher().getScorer();
		gameMenuController.enterScoring();
		scorePaneController.setScorer(boardScorer);
		scorePaneController.setVisible(true);

		gameDrawer = buildBoardScoreDrawer();
		strategy = new Scoring(boardCanvas, boardScorer, scorePaneController);
		gameDrawer.draw(getPublisher().getBoard());
	}

	private GameScoreDrawer buildBoardScoreDrawer() {
		return new GameScoreDrawer(buildGameDrawer(), getPublisher(), boardScorer);
	}

	@Override
	void reviewStart(GameEvent event) {
		gameDrawer = buildGameDrawer();
	}

	private void loadScorePane() {
		scorePaneController = new ScorePaneController();

		sideBar.getChildren().add(scorePaneController.getRoot());
	}

	private void loadGameMenu() {
		gameMenuController = new GameMenuController();
		gameMenuController.setGameHandler(getPublisher());

		sideBar.getChildren().add(gameMenuController.getRoot());
	}

	private void loadReviewPanel() {
		reviewPaneController = new ReviewPaneController(getPublisher());

		sideBar.getChildren().add(reviewPaneController.getRoot());
	}

	private void doneScoring(ScoreEvent event) {
		if ( event.getSource() != boardScorer )
			return;

		strategy = null;

		gameMenuController.enableEndGameButtons();
	}
}