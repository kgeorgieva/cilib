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
package net.sourceforge.cilib.algorithm.initialisation;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Create a collection of {@linkplain net.sourceforge.cilib.entity.Entity entities}
 * by cloning the given prototype {@link net.sourceforge.cilib.entity.Entity}.
 * The prototype Entity must be a {@link Particle}. All particles cloned from
 * the given Particle are assigned different behavriors from a behavior pool.
 * The manner in which behaviors are selected from the behavior pool is governed
 * by the {@link Selector} (random by default).
 *
 * @param <E> The {@code Entity} type.
 */
public class HeterogeneousPopulationInitialisationStrategy implements PopulationInitialisationStrategy<Particle> {

    private List<ParticleBehavior> behaviorPool;
    private Selector<ParticleBehavior> selectionRecipe;
    private PopulationInitialisationStrategy<Particle> delegate;

    /**
     * Create an instance of the {@code ChargedPopulationInitialisationStrategy}.
     */
    public HeterogeneousPopulationInitialisationStrategy() {
        behaviorPool = new ArrayList<ParticleBehavior>();
        selectionRecipe = new RandomSelector<ParticleBehavior>();
        delegate = new ClonedPopulationInitialisationStrategy<Particle>();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public HeterogeneousPopulationInitialisationStrategy(HeterogeneousPopulationInitialisationStrategy copy) {
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.selectionRecipe = copy.selectionRecipe;
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HeterogeneousPopulationInitialisationStrategy getClone() {
        return new HeterogeneousPopulationInitialisationStrategy(this);
    }

    /**
     * Perform the required initialization, using the provided <tt>Topology</tt> and
     * <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialization of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     * @throws InitialisationException if the initialization cannot take place.
     */
    @Override
    public Iterable<Particle> initialise(OptimisationProblem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkState(behaviorPool.size() > 0, "No particle behaviors have been added to the behavior pool.");

        Iterable<Particle> clones = delegate.initialise(problem);

        for (Particle p : clones) {
            p.setParticleBehavior(selectionRecipe.on(behaviorPool).select());
        }

        return clones;
    }

    /**
     * Add a {@link ParticleBehavior} to the behavior pool.
     * @param behavior The {@link ParticleBehavior} to add to the behavior pool.
     */
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
    }

    /**
     * Set the {@link ParticleBehavior} pool.
     * @param pool A {@link List} of {@link ParticleBehavior} objects.
     */
    public void setBehaviorPool(List<ParticleBehavior> pool) {
        behaviorPool = pool;
    }

    /**
     * Get the current behavior pool.
     * @return The current {@link List} of {@link ParticleBehavior} objects.
     */
    public List<ParticleBehavior> getBehaviorPool() {
        return behaviorPool;
    }

    /**
     * Set the prototype {@link Entity} for the copy process.
     * @param entityType The {@link Entity} to use for the cloning process. This must be a {@link Particle}.
     */
    @Override
    public void setEntityType(Entity entityType) {
        Preconditions.checkArgument(entityType instanceof Particle, "The entityType of a HeterogeneousPopulationInitialisationStrategy must be a Particle");
        delegate.setEntityType(entityType);
    }

    /**
     * Get the {@linkplain net.sourceforge.cilib.entity.Entity entity} that has been defined as
     * the prototype to for the copies.
     * @see ChargedPopulationInitialisationStrategy#getPrototypeEntity()
     * @return The prototype {@code Entity}.
     */
    @Override
    public Entity getEntityType() {
        return delegate.getEntityType();
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return delegate.getEntityNumber();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        delegate.setEntityNumber(entityNumber);
    }

    public void setDelegate(PopulationInitialisationStrategy<Particle> delegate) {
        this.delegate = delegate;
    }

    public PopulationInitialisationStrategy<Particle> getDelegate() {
        return delegate;
    }

    public void setSelectionRecipe(Selector<ParticleBehavior> selectionRecipe) {
        this.selectionRecipe = selectionRecipe;
    }

    public Selector<ParticleBehavior> getSelectionRecipe() {
        return selectionRecipe;
    }
}
