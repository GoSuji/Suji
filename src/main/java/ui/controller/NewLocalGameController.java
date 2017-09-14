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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.util.StringConverter;
import org.dockfx.DockNode;
import util.KomiSpinnerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewLocalGameController extends BuildableController implements Initializable {

	public Spinner<Integer> handicapSpinner;
	public Button startButton;
	public Spinner<Double> komiSpinner;

	private DockNode dockNode;

	public NewLocalGameController() {
		dockNode = null;
	}

	public DockNode getNode() {
		dockNode = new DockNode(getRoot(), "New Local Game");

		return dockNode;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupHandicapSpinner();
		setupKomiSpinner();

		startButton.setOnAction(this::start);
	}

	private void setupHandicapSpinner() {
		ObservableList<Integer> handicaps = FXCollections.observableArrayList(new ArrayList<>());
		handicaps.add(0);
		for (int i = 2; i < 10; i++)
			handicaps.add(i);

		SpinnerValueFactory<Integer> handicapFactory = new ListSpinnerValueFactory<>(handicaps);
		handicapFactory.setWrapAround(true);
		handicapFactory.setConverter(new HandicapConverter());

		handicapSpinner.setValueFactory(handicapFactory);
		handicapSpinner.setEditable(true);
	}


	private void setupKomiSpinner() {
		komiSpinner.setValueFactory(new KomiSpinnerFactory());
		komiSpinner.setEditable(true);
	}

	private void start(ActionEvent event) {
		Parent root = buildLocalGame();

		if ( root == null )
			return;

		dockNode.setTitle("Local Game");
		dockNode.setContents(root);
	}

	private Parent buildLocalGame() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/localGame.fxml"));
		loader.setControllerFactory(this::buildController);

		Parent root = null;
		try {
			root = loader.load();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if ( root == null )
			return null;

		return root;
	}

	private BoardController buildController(Class<?> type) {
		LocalGameController controller = new LocalGameController();

		controller.setHandicap(handicapSpinner.getValue());
		controller.setKomi(komiSpinner.getValue());

		return controller;
	}

	@Override
	protected String getResourcePath() {
		return "/newLocalGame.fxml";
	}

	private class HandicapConverter extends StringConverter<Integer> {

		@Override
		public String toString(Integer object) {
			return object.toString();
		}

		@Override
		public Integer fromString(String string) {
			Integer result = Integer.parseInt(string);
			if ( result == 0 || (result > 1 && result < 10) )
				return result;
			else
				return 0;
		}
	}
}
