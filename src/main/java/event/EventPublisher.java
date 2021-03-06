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

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;

public interface EventPublisher extends EventTarget {

	<T extends SujiEvent> void fireEvent(T event);

	<T extends SujiEvent> void subscribe(EventType<T> eventType, EventHandler<? super T> eventHandler);

	<T extends SujiEvent> void unsubscribe(EventType<T> eventType, EventHandler<? super T> eventHandler);
}
