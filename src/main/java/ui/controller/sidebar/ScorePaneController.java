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

package ui.controller.sidebar;

import event.ScoreEvent;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.gamehandler.GameHandler;
import ui.controller.SelfBuildingController;
import util.StoneColour;

import java.net.URL;
import java.util.ResourceBundle;

public class ScorePaneController extends SelfBuildingController implements Initializable {

	@FXML
	private Pane scorePane;
	@FXML
	private Button blackDone;
	@FXML
	private Button whiteDone;
	@FXML
	private Label blackScore;
	@FXML
	private Label whiteScore;

	private GameHandler game;
	private EventHandler<ScoreEvent> scoreChangeHandler = this::updateScore;
	private EventHandler<ScoreEvent> doneScoringHandler = this::displayFinalScore;

	public ScorePaneController(GameHandler gameHandler) {
		game = gameHandler;

		game.subscribe(ScoreEvent.DONE, doneScoringHandler);
		game.subscribe(ScoreEvent.SCORE, scoreChangeHandler);
	}

	@Override
	protected String getResourcePath() {
		return "/fxml/scorePane.fxml";
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		scorePane.widthProperty().addListener(this::resizeScore);
		enableButtons();
		setButtonActions();
		setVisible(false);
	}

	public void setVisible(boolean visible) {
		scorePane.setVisible(visible);
	}

	public void enableButtons() {
		blackDone.setDisable(false);
		whiteDone.setDisable(false);
	}

	private void setButtonActions() {
		blackDone.setOnAction(event -> {
			blackDone.setDisable(true);
			if ( whiteDone.isDisabled() )
				endScoring();
		});

		whiteDone.setOnAction(event -> {
			whiteDone.setDisable(true);
			if ( blackDone.isDisabled() )
				endScoring();
		});
	}

	private void endScoring() {
		ScoreEvent event = new ScoreEvent(game, ScoreEvent.DONE);
		game.fireEvent(event);
	}

	private void displayFinalScore(ScoreEvent event) {
		unsubscribeEvents();
		double finalScore = event.getScorer().getScore();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");
		if ( finalScore == 0 ) {
			alert.setContentText("Game ends in a draw.");
			alert.showAndWait();
			return;
		}

		String message;

		if ( finalScore > 0 )
			message = "Black";
		else
			message = "White";

		finalScore = Math.abs(finalScore);

		message += " wins by " + Double.toString(finalScore) + " points.";

		alert.setContentText(message);
		alert.showAndWait();
	}

	private void unsubscribeEvents() {
		game.unsubscribe(ScoreEvent.DONE, doneScoringHandler);
		game.unsubscribe(ScoreEvent.SCORE, scoreChangeHandler);
	}

	private void updateScore(ScoreEvent event) {
		blackScore.setText(Double.toString(event.getScore(StoneColour.BLACK)));
		whiteScore.setText(Double.toString(event.getScore(StoneColour.WHITE)));
	}

	private void resizeScore(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
		VBox blackScoreBox = null;
		Separator separator = null;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof Separator )
				separator = (Separator) node;
		}

		if ( separator == null )
			return;

		for (Node node : scorePane.getChildren()) {
			if ( node instanceof VBox && node.getLayoutX() < separator.getLayoutX() )
				blackScoreBox = (VBox) node;
		}

		if ( blackScoreBox == null )
			return;

		double width = (scorePane.getWidth() - separator.getWidth()) / 2;

		blackScoreBox.setMinWidth(width);
	}
}
