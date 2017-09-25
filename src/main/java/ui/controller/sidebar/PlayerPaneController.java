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

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ogs.GameList;
import ogs.REST;
import ui.controller.DockNodeController;
import ui.controller.SelfBuildingController;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerPaneController extends SelfBuildingController implements Initializable {

	public ImageView blackAvatar;
	public ImageView whiteAvatar;
	public Label blackName;
	public Label whiteName;

	private GameList.Game game;

	public PlayerPaneController(GameList.Game gameMeta) {
		game = gameMeta;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		blackName.setText(game.getBlackName());
		whiteName.setText(game.getWhiteName());

		int black = game.getBlackPlayer().getId();
		int white = game.getWhitePlayer().getId();

		REST.requestPlayerIcon(black, 64, blackAvatar::setImage);
		REST.requestPlayerIcon(white, 64, whiteAvatar::setImage);
	}

	@Override
	protected String getResourcePath() {
		return "/playerInfoPane.fxml";
	}
}