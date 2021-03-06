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

public enum StoneColour {
	BLACK, WHITE;

	public static StoneColour fromString(String colour) {
		if ( colour.equals("B") || colour.equals("black") )
			return BLACK;
		else if ( colour.equals("W") || colour.equals("white") )
			return WHITE;

		return null;
	}

	public StoneColour other() {
		if ( this == BLACK )
			return WHITE;
		else
			return BLACK;
	}
}
