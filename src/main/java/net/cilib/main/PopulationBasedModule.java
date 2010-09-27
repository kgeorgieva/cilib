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
package net.cilib.main;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import java.util.List;
import net.cilib.algorithm.Selector;
import net.cilib.algorithm.MockMutationProvider;
import net.cilib.algorithm.MutationProvider;
import net.cilib.algorithm.ReplacementSelector;
import net.cilib.annotation.Initialized;
import net.cilib.annotation.Unique;
import net.cilib.collection.Topology;
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.entity.Entity;
import net.cilib.pso.Guide;
import net.cilib.pso.NeighborhoodBest;
import net.cilib.pso.PersonalBest;
import net.cilib.pso.StandardVelocityProvider;
import net.cilib.pso.VelocityProvider;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 *
 * @author gpampara
 */
public class PopulationBasedModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Selector.class).to(ReplacementSelector.class);
        bind(MutationProvider.class).to(MockMutationProvider.class);
//        bind(PopulationBasedAlgorithm.class).to(DE.class);

        // PSO related bindings
        bind(VelocityProvider.class).to(StandardVelocityProvider.class);
        bind(Guide.class).annotatedWith(Names.named("global")).to(NeighborhoodBest.class);
        bind(Guide.class).annotatedWith(Names.named("local")).to(PersonalBest.class);
    }

    @Provides
    @Initialized
    Topology<Entity> getInitializedTopology(Provider<Topology<Entity>> t) {
        return t.get();
    }

    @Provides
    Topology<Entity> getTopology() {
        return ImmutableGBestTopology.of();
    }

    /**
     * This needs serious work... The API is in the air... No real concrete
     * need for it is exists....
     * @param selector
     * @param randomProvider
     * @return
     */
    @Provides
    @Unique
    Selector getSelector(Selector selector, final RandomProvider randomProvider) {
        return new Selector() {

            @Override
            public Entity select(Entity... elements) {
                return select(Lists.newArrayList(elements));
            }

            @Override
            public Entity select(Iterable<Entity> elements) {
                List<Entity> list = Lists.newArrayList(elements);
                return list.get(randomProvider.nextInt(list.size()));
            }
        };
    }
}
