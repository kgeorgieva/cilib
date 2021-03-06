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
package net.sourceforge.cilib.pso.crossover.operations;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.real.MultiParentCrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.ParticleCrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.ElitistParentReplacementStrategy;
import net.sourceforge.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * The crossover portion of the Novel Multi-Parent Crossover PSO.
 * <p>
 * H Wang, Z Wu, Y Liu and S. Zeng, "Particle Swarm Optimization with a Novel 
 * Multi-Parent Crossover operator", Fourth International Conference on Natural 
 * Computation, pp 664--668, 2008, doi:10.1109/ICNC.2008.643
 * </p>
 */
public class MultiParentCrossoverOperation extends PSOCrossoverOperation {
    
    private ParentReplacementStrategy parentReplacementStrategy;
    private ParticleCrossoverStrategy crossover;
    private ControlParameter crossoverProbability;
    private ProbabilityDistributionFuction random;
    private Selector<Particle> selector;
    
    public MultiParentCrossoverOperation() {
        this.crossover = new ParticleCrossoverStrategy();
        this.crossover.setCrossoverStrategy(new MultiParentCrossoverStrategy());
        
        this.selector = new RandomSelector();
        this.crossoverProbability = ConstantControlParameter.of(0.8);
        this.random = new UniformDistribution();
        this.parentReplacementStrategy = new ElitistParentReplacementStrategy();       
    }
    
    public MultiParentCrossoverOperation(MultiParentCrossoverOperation copy) {
        this.crossover = copy.crossover.getClone();
        this.random = copy.random;
        this.crossoverProbability = copy.crossoverProbability.getClone();
        this.parentReplacementStrategy = copy.parentReplacementStrategy;
        this.selector = copy.selector;
    }

    @Override
    public MultiParentCrossoverOperation getClone() {
        return new MultiParentCrossoverOperation(this);
    }

    @Override
    public Topology<Particle> f(PSO pso) {
        Topology<Particle> topology = pso.getTopology();
        
        for (int i = 0; i < topology.size(); i++) {
            Particle p = topology.get(i);
            
            if (random.getRandomNumber() < crossoverProbability.getParameter()) {
                List<Particle> parents = selector.on(topology).select(Samples.first(crossover.getNumberOfParents() - 1));
                parents.add(0, p);
                
                Particle offspring = crossover.crossover(parents).get(0);
                offspring.setNeighbourhoodBest(offspring);
                
                topology.set(i, parentReplacementStrategy.f(Arrays.asList(p), Arrays.asList(offspring)).get(0));
            }
        }
        
        return topology;
    }
    
    public ParticleCrossoverStrategy getCrossover() {
        return crossover;
    }

    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return parentReplacementStrategy;
    }

    public ProbabilityDistributionFuction getRandom() {
        return random;
    }

    public Selector<Particle> getSelector() {
        return selector;
    }

    public void setCrossover(ParticleCrossoverStrategy crossover) {
        this.crossover = crossover;
    }

    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public void setParentReplacementStrategy(ParentReplacementStrategy parentReplacementStrategy) {
        this.parentReplacementStrategy = parentReplacementStrategy;
    }

    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }

    public void setSelector(Selector<Particle> selector) {
        this.selector = selector;
    }    
}
