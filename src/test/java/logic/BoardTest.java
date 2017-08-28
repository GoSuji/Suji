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

import org.junit.Test;
import util.Coords;
import util.StoneColour;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static util.Coords.getCoords;

public class BoardTest {

	@Test
	public void equals() {
		Board board1 = new Board();
		Board board2 = new Board();

		board1.playStone(getCoords("D4"), StoneColour.BLACK);
		board1.playStone(getCoords("C4"), StoneColour.BLACK);
		board1.playStone(getCoords("M17"), StoneColour.WHITE);
		board1.playStone(getCoords("R4"), StoneColour.WHITE);
		board1.playStone(getCoords("B14"), StoneColour.BLACK);

		board2.playStone(getCoords("R4"), StoneColour.WHITE);
		board2.playStone(getCoords("B14"), StoneColour.BLACK);
		board2.playStone(getCoords("C4"), StoneColour.BLACK);
		board2.playStone(getCoords("D4"), StoneColour.BLACK);
		board2.playStone(getCoords("M17"), StoneColour.WHITE);

		assertThat(board1, is(board2));
	}

	@Test
	public void playMoves() {
		Board board = new Board();

		Coords coords = getCoords("D4");
		board.playStone(coords, StoneColour.BLACK);
		assertThat(board.getStones(StoneColour.BLACK), hasItems(coords));

		coords = getCoords("E5");
		board.playStone(coords, StoneColour.WHITE);
		assertThat(board.getStones(StoneColour.WHITE), hasItems(coords));
	}

	@Test
	public void findingChains() {
		Board board = new Board();

		String[] stones = {"D3", "D4", "D5", "E6"};

		for (String stone : stones)
			board.playStone(getCoords(stone), StoneColour.BLACK);

		stones = new String[]{"F5", "F6"};
		for (String stone : stones)
			board.playStone(getCoords(stone), StoneColour.WHITE);

		assertThat(board.getChainAtCoords(getCoords("D4")).getStones(),
				   hasItems(getCoords("D3"), getCoords("D4"), getCoords("D5")));

		assertThat(board.getChainAtCoords(getCoords("D3")).getStones(),
				   hasItems(getCoords("D3"), getCoords("D4"), getCoords("D5")));

		assertThat(board.getChainAtCoords(getCoords("E6")).getStones(), hasItem(getCoords("E6")));

		assertThat(board.getChainAtCoords(getCoords("D4")).size(), is(3));
		assertThat(board.getChainAtCoords(getCoords("D5")).size(), is(3));
		assertThat(board.getChainAtCoords(getCoords("E6")).size(), is(1));
		assertThat(board.getChainAtCoords(getCoords("F5")).size(), is(2));

		assertThat(board.getChainAtCoords(getCoords("F5")).getStones(), hasItems(getCoords("F5"), getCoords("F6")));

		assertThat(board.getChainAtCoords(getCoords("A1")), nullValue());
	}

	@Test
	public void blockOccupiedSameColourBlack() {
		Board board = new Board();

		board.playStone(getCoords("C4"), StoneColour.BLACK);

		try {
			board.playStone(getCoords("C4"), StoneColour.BLACK);
			fail("Board did not throw exception when playing on top of an existing stone with a stone of the same " +
						 "colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedDifferentColourBlack() {
		Board board = new Board();

		board.playStone(getCoords("C4"), StoneColour.BLACK);

		try {
			board.playStone(getCoords("C4"), StoneColour.WHITE);
			fail("Board did not throw exception when playing on top of an existing stone with a stone of a different "
						 + "colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedSameColourWhite() {
		Board board = new Board();

		board.playStone(getCoords("C4"), StoneColour.WHITE);

		try {
			board.playStone(getCoords("C4"), StoneColour.WHITE);
			fail("Board did not throw exception when playing on top of an existing stone with a stone of the same " +
						 "colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void blockOccupiedDifferentColourWhite() {
		Board board = new Board();

		board.playStone(getCoords("C4"), StoneColour.WHITE);

		try {
			board.playStone(getCoords("C4"), StoneColour.BLACK);
			fail("Board did not throw exception when playing on top of an existing stone with a stone of a different "
						 + "colour.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void playingOnOccupiedSpaceIsIllegal() {
		Board board = new Board();

		board.playStone(getCoords("D4"), StoneColour.BLACK);

		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		assertThat(board.isLegalMove(getCoords("D3"), StoneColour.BLACK), is(true));
		assertThat(board.isLegalMove(getCoords("D3"), StoneColour.WHITE), is(true));

		board = new Board();
		board.playStone(getCoords("D4"), StoneColour.WHITE);

		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		assertThat(board.isLegalMove(getCoords("D3"), StoneColour.BLACK), is(true));
		assertThat(board.isLegalMove(getCoords("D3"), StoneColour.WHITE), is(true));
	}

	@Test
	public void suicideIsIllegal() {
		Board board = new Board();

		board.playStone(getCoords("D5"), StoneColour.BLACK);
		board.playStone(getCoords("D3"), StoneColour.BLACK);
		board.playStone(getCoords("E4"), StoneColour.BLACK);
		board.playStone(getCoords("C4"), StoneColour.BLACK);

		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.WHITE), is(false));

		board = new Board();
		board.playStone(getCoords("D5"), StoneColour.WHITE);
		board.playStone(getCoords("D3"), StoneColour.WHITE);
		board.playStone(getCoords("E4"), StoneColour.WHITE);
		board.playStone(getCoords("C4"), StoneColour.WHITE);

		assertThat(board.isLegalMove(getCoords("D4"), StoneColour.BLACK), is(false));
	}

	@Test
	public void simpleCapturing() {
		Board board = new Board();

		assertThat(board.getCaptures(StoneColour.BLACK), is(0));
		assertThat(board.getCaptures(StoneColour.WHITE), is(0));

		board.playStone(getCoords("D4"), StoneColour.BLACK);
		board.playStone(getCoords("D3"), StoneColour.WHITE);
		board.playStone(getCoords("D5"), StoneColour.WHITE);
		board.playStone(getCoords("C4"), StoneColour.WHITE);

		assertThat(board.getStones(StoneColour.BLACK).size(), is(1));
		assertThat(board.getStones(StoneColour.WHITE).size(), is(3));
		assertThat(board.getCaptures(StoneColour.BLACK), is(0));
		assertThat(board.getCaptures(StoneColour.WHITE), is(0));

		board.playStone(getCoords("E4"), StoneColour.WHITE);

		assertThat(board.getCaptures(StoneColour.WHITE), is(1));
		assertThat(board.getStones(StoneColour.BLACK).size(), is(0));


		board = new Board();

		assertThat(board.getCaptures(StoneColour.BLACK), is(0));
		assertThat(board.getCaptures(StoneColour.WHITE), is(0));

		board.playStone(getCoords("D4"), StoneColour.WHITE);
		board.playStone(getCoords("D3"), StoneColour.BLACK);
		board.playStone(getCoords("D5"), StoneColour.BLACK);
		board.playStone(getCoords("C4"), StoneColour.BLACK);

		assertThat(board.getStones(StoneColour.BLACK).size(), is(3));
		assertThat(board.getStones(StoneColour.WHITE).size(), is(1));
		assertThat(board.getCaptures(StoneColour.BLACK), is(0));
		assertThat(board.getCaptures(StoneColour.WHITE), is(0));

		board.playStone(getCoords("E4"), StoneColour.BLACK);

		assertThat(board.getCaptures(StoneColour.BLACK), is(1));
		assertThat(board.getStones(StoneColour.WHITE).size(), is(0));
	}
}
