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

package util;

import javafx.util.Pair;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static util.Coords.fromSGFString;
import static util.Coords.getCoords;

public class CoordsTest {

	@Test
	public void throwOnInvalidInput() {
		try {
			getCoords(1, 20);
			fail("Coords did not throw exception when given an invalid coordinate pair.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords(0, 10);
			fail("Coords did not throw exception when given an invalid coordinate pair.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords(25, 2);
			fail("Coords did not throw exception when given an invalid coordinate pair.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords(18, -5);
			fail("Coords did not throw exception when given an invalid coordinate pair.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void equalTo() {
		Coords c1 = getCoords(4, 3);
		Coords c2 = getCoords(4, 3);
		Pair<Integer, Integer> notCoords = new Pair<>(4, 3);

		assertThat(c1, is(c2));
		assertThat(c2, is(c1));
		assertThat(c1, is(c1));
		assertThat(c1, not(notCoords));
	}

	@Test
	public void coordsGetter() {
		Coords coords = getCoords(4, 3);

		assertThat(coords.getX(), is(4));
		assertThat(coords.getY(), is(3));
	}

	@Test
	public void convertToString() {
		assertThat(getCoords(3, 4).toString(), is("(3, 4)"));
	}

	@Test
	public void neighbours() {
		Coords center = getCoords(10, 10);
		Coords topLeft = getCoords(1, 1);
		Coords bottomRight = getCoords(19, 19);

		assertThat(center.getNeighbours().size(), is(4));
		assertThat(center.getNeighbours(),
				   hasItems(getCoords(10, 9), getCoords(9, 10), getCoords(10, 11), getCoords(11, 10)));

		assertThat(topLeft.getNeighbours().size(), is(2));
		assertThat(topLeft.getNeighbours(), hasItems(getCoords(1, 2), getCoords(2, 1)));

		assertThat(bottomRight.getNeighbours().size(), is(2));
		assertThat(bottomRight.getNeighbours(), hasItems(getCoords(19, 18), getCoords(18, 19)));
	}

	@Test
	public void symbolicCoordinates() {
		assertThat(getCoords("K10"), is(getCoords(10, 10)));
		assertThat(getCoords("k19"), is(getCoords(10, 19)));
		assertThat(getCoords("a1"), is(getCoords(1, 1)));
		assertThat(getCoords("T19"), is(getCoords(19, 19)));
		assertThat(getCoords("A10"), is(getCoords(1, 10)));
		assertThat(getCoords("t10"), is(getCoords(19, 10)));

		assertThat(getCoords("h14"), is(getCoords(8, 14)));
		assertThat(getCoords("j8"), is(getCoords(9, 8)));
		assertThat(getCoords("H9"), is(getCoords(8, 9)));
		assertThat(getCoords("J1"), is(getCoords(9, 1)));

		try {
			getCoords("I1");
			fail("Coords did not throw an exception when given bad symbolic coordinates.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords("i10");
			fail("Coords did not throw an exception when given bad symbolic coordinates.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords("A0");
			fail("Coords did not throw an exception when given bad symbolic coordinates.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords("k20");
			fail("Coords did not throw an exception when given bad symbolic coordinates.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}

		try {
			getCoords("Z5");
			fail("Coords did not throw an exception when given bad symbolic coordinates.");
		}
		catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
		}
	}

	@Test
	public void sgfString() {
		assertThat(getCoords("A1").toSGFString(), is("aa"));
		assertThat(getCoords("T19").toSGFString(), is("ss"));

		assertThat(fromSGFString("aa"), is(getCoords("A1")));
		assertThat(fromSGFString("ss"), is(getCoords("T19")));
		assertThat(fromSGFString("as"), is(getCoords("A19")));
	}
}
