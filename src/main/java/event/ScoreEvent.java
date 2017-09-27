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

package event;

import javafx.event.EventTarget;
import javafx.event.EventType;
import logic.score.Scorer;
import util.StoneColour;

public class ScoreEvent extends SujiEvent {

	public static final EventType<ScoreEvent> SCORE = new EventType<ScoreEvent>(ANY, "SCORE");
	public static final EventType<ScoreEvent> DONE = new EventType<ScoreEvent>(SCORE, "DONE");

	private ScoreEvent(Scorer source, EventTarget eventTarget, EventType<? extends ScoreEvent> eventType) {
		super(source, eventTarget, eventType);
	}

	public static void fireScoreEvent(Scorer scorer) {
		fireScoreEvent(scorer, SCORE);
	}

	public static void fireScoreEvent(Scorer scorer, EventType<? extends ScoreEvent> eventType) {
		EventBus bus = EventBus.getInstance();
		ScoreEvent event = new ScoreEvent(scorer, bus, eventType);
		bus.fireEvent(event);
	}

	public double getScore(StoneColour colour) {
		return getScorer().getScore(colour);
	}

	public Scorer getScorer() {
		return (Scorer) getSource();
	}
}
