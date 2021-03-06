/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.games.measurement;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.games.states.ListGameState;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Record each location an Agent has occupied at each timestep in the game. This could also be used
 * to measure locations a specific item type has occupied.
 *
 */
public class RecordItemLocationMeasure extends SingleAgentMeasure {
    private static final long serialVersionUID = -7742916583743476119L;
    private Vector locations;

    public RecordItemLocationMeasure() {
        locations = Vector.of();
    }

    public RecordItemLocationMeasure(Enum itemToken) {
        super(itemToken);
        locations = Vector.of();
    }

    public RecordItemLocationMeasure(RecordItemLocationMeasure other) {
        super(other);
        locations = Vector.copyOf(locations);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearData() {
        locations.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecordItemLocationMeasure getClone() {
        return new RecordItemLocationMeasure(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getMeasuredData() {
        return locations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void measure(Game<GameState> game) {
        GameState state = game.getCurrentState();
        if(!(state instanceof ListGameState))
            throw new RuntimeException("Impliment for other state types");
        GameItem item = ((ListGameState)state).getItem(itemToken);
        locations.add((Numeric) item.getLocation().getClone());
    }

}
