/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Standard DE update strategy where the individual receied is updated using the 
 * normal DE operators.
 */
public class StandardDEUpdateStrategy implements UpdateStrategy{

    private Selector targetVectorSelectionStrategy;
    private CreationStrategy trialVectorCreationStrategy;
    private CrossoverStrategy crossoverStrategy;
    
    /*
     * Default Constructor for the StandardDEUpdateStrategy
     */
    public StandardDEUpdateStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }
    
    /*
     * Copy constructor for the StandardDEUpdateStrategy
     */
    public StandardDEUpdateStrategy(StandardDEUpdateStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }
    
    /*
     * Clone method for the StandardDEUpdateStrategy
     * @return A new instance of this StandardDEUpdateStrategy
     */
    public StandardDEUpdateStrategy getClone() {
        return new StandardDEUpdateStrategy(this);
    }

    /*
     * Updates the parameter using the standard DE operators
     * 
     */
    public Entity update(Entity currentEntity, SinglePopulationBasedAlgorithm algorithm) {
            Topology topology = algorithm.getTopology();
            Entity targetEntity = (Entity) targetVectorSelectionStrategy.on(topology).exclude(currentEntity).select();
            
            Entity trialEntity = trialVectorCreationStrategy.create(targetEntity, currentEntity, topology);
            
            List<Entity> offspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(currentEntity, trialEntity)); // Order is VERY important here!!

            return offspring.get(0);
    }
    
    
}
