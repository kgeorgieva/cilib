/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec.update;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 *
 * @author Kris
 */
public class StandardDEUpdateStrategy implements UpdateStrategy{

    private Selector targetVectorSelectionStrategy;
    private CreationStrategy trialVectorCreationStrategy;
    private CrossoverStrategy crossoverStrategy;
    
    public StandardDEUpdateStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }
    
    public StandardDEUpdateStrategy(StandardDEUpdateStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }
    
    public StandardDEUpdateStrategy getClone() {
        return new StandardDEUpdateStrategy(this);
    }

    public Entity update(Entity currentEntity, Topology topology) {
            Entity targetEntity = (Entity) targetVectorSelectionStrategy.on(topology).exclude(currentEntity).select();
            
            Entity trialEntity = trialVectorCreationStrategy.create(targetEntity, currentEntity, topology);
            
            List<Entity> offspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(currentEntity, trialEntity)); // Order is VERY important here!!

            return offspring.get(0);
    }
    
    
}
