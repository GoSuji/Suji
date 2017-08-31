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

package logic;

import util.Coords;
import util.StoneColour;

public class LocalGameHandler extends GameHandler {

	private GameTree gameTree;

	public LocalGameHandler() {
		gameTree = new GameTree();
	}

	@Override
	public boolean isLegalMove(Coords move, StoneColour colour) {
		boolean isLegal;

		isLegal = !gameTree.getPosition().isOccupied(move);
		isLegal &= !gameTree.getPosition().isSuicide(move, colour);

		if ( isLegal && gameTree.getNumberOfMoves() > 2 ) {
			Board previous = gameTree.getLastPosition();
			Board future = gameTree.getPosition();
			future.playStone(move, colour);

			isLegal = !previous.equals(future);
		}

		return isLegal;
	}

	@Override
	public void playStone(Coords move, StoneColour colour) {
		gameTree.playMove(move, colour);
	}

	@Override
	public Board getBoard() {
		return gameTree.getPosition();
	}
}