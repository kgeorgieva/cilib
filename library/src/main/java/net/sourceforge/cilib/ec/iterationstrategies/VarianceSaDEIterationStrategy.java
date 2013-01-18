/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.ComponentBasedRandToBestCreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.ComponentBasedBinomialCrossover;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ZaharieComponentVariance;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 *
 * @author Kris
 */
public class VarianceSaDEIterationStrategy extends AbstractIterationStrategy<EC> {
    protected Selector targetVectorSelectionStrategy;
    protected CreationStrategy trialVectorCreationStrategy;
    protected CrossoverStrategy crossoverStrategy;
    protected ZaharieComponentVariance variance;
    
    public VarianceSaDEIterationStrategy() {
        super();
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new ComponentBasedRandToBestCreationStrategy();
        this.crossoverStrategy = new ComponentBasedBinomialCrossover();
        variance = new ZaharieComponentVariance();
    }

    public VarianceSaDEIterationStrategy(VarianceSaDEIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        variance = copy.variance;
    }
    
    @Override
    public AbstractIterationStrategy<EC> getClone() {
        return new VarianceSaDEIterationStrategy(this);
    }

    @Override
    public void performIteration(EC algorithm) {
        Topology<Entity> topology = (Topology<Entity>) algorithm.getTopology();

        Vector previousVariance = variance.calculateVariance(topology);
        
        for (int i = 0; i < topology.size(); i++) {
            Entity current = topology.get(i);
            current.calculateFitness();

            // Create the trial vector by applying mutation
            Entity targetEntity = (Entity) targetVectorSelectionStrategy.on(topology).exclude(current).select();

            // Create the trial vector / entity
            Entity trialEntity = trialVectorCreationStrategy.create(targetEntity, current, topology);

            // Create the offspring by applying cross-over
            List<Entity> offspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!

            // Replace the parent (current) if the offspring is better
            Entity offspringEntity = offspring.get(0);
            boundaryConstraint.enforce(offspringEntity);
            offspringEntity.calculateFitness();

            if (offspringEntity.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(i, offspringEntity); // Replace the parent with the offspring individual
            }
            
        }
        
        Vector currentVariance = variance.calculateVariance(topology);
        Vector zaharieVariance = variance.getZaharieMeasure(currentVariance, previousVariance);
        
        for(int i = 0; i < topology.size(); i++) {
            ComponentBasedIndividual current = (ComponentBasedIndividual) topology.get(i);
            current.updateParameters(zaharieVariance, topology.size());
        }
    }

    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
   
}
