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

package logic.board;

import org.junit.Test;
import util.Coords;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static util.Coords.getCoords;

public class ChainTest {

	@Test
	public void contains() {
		Chain chain = new Chain(getCoords("D3"));

		assertThat(chain.contains(getCoords("D3")), is(true));
	}

	@Test
	public void adjacency() {
		Chain chain = new Chain(getCoords("D4"));
		Chain adjacent = new Chain(getCoords("D3"));
		Chain nonadjacent = new Chain(getCoords("C3"));

		assertThat(chain.isAdjacentTo(getCoords("D3")), is(true));
		assertThat(chain.isAdjacentTo(getCoords("C3")), is(false));

		assertThat(chain.isAdjacentTo(adjacent), is(true));
		assertThat(chain.isAdjacentTo(nonadjacent), is(false));
	}

	@Test
	public void liberties() {
		Chain chain = new Chain(getCoords("D4"));

		Collection<Coords> liberties = chain.getLiberties();

		assertThat(liberties.size(), is(4));
		assertThat(liberties, hasItems(getCoords("D3"), getCoords("C4"), getCoords("D5"), getCoords("E4")));

		chain = new Chain(getCoords("A1"));
		liberties = chain.getLiberties();

		assertThat(liberties.size(), is(2));
	}

	@Test
	public void size() {
		Chain chain = new Chain(getCoords("A1"));

		assertThat(chain.size(), is(1));
	}

	@Test
	public void merge() {
		Chain main = new Chain(getCoords("D4"));
		Chain other = new Chain(getCoords("D3"));

		main.mergeChain(other);

		assertThat(main.size(), is(2));
		assertThat(other.size(), is(0));

		assertThat(main.contains(getCoords("D4")), is(true));
		assertThat(main.contains(getCoords("D3")), is(true));

		assertThat(main.countLiberties(), is(6));
		assertThat(other.countLiberties(), is(0));
	}

	@Test
	public void throwOnBadMerge() {
		Chain main = new Chain(getCoords("D4"));
		Chain other = new Chain(getCoords("C3"));

		try {
			main.mergeChain(other);
			fail("Chain did not throw an exception when trying to merge with a non-adjacent chain.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void getOpenLiberties() {
		Chain blackChain = new Chain(getCoords("D4"));
		ChainSet whiteStones = new ChainSet();

		assertThat(blackChain.getOpenLiberties(whiteStones).size(), is(4));
		assertThat(blackChain.getOpenLiberties(whiteStones),
				   hasItems(getCoords("D3"), getCoords("D5"), getCoords("C4"), getCoords("E4")));

		whiteStones.add(getCoords("C3"));

		assertThat(blackChain.getOpenLiberties(whiteStones).size(), is(4));
		assertThat(blackChain.getOpenLiberties(whiteStones),
				   hasItems(getCoords("D3"), getCoords("D5"), getCoords("C4"), getCoords("E4")));

		whiteStones.add(getCoords("D3"));

		assertThat(blackChain.getOpenLiberties(whiteStones).size(), is(3));
		assertThat(blackChain.getOpenLiberties(whiteStones),
				   hasItems(getCoords("D5"), getCoords("C4"), getCoords("E4")));
	}
}
