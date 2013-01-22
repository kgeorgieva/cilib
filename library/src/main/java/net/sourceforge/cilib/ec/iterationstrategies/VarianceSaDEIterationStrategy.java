/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
 * This is the iteration strategy described by Zaharie in her 2003 paper "Control 
 * of Population Diversity and Adaptation in Differential Evolution Algorithms"
 * published in the Proceedings of Mendel 2003, 9th International Conference on
 * Soft Computing.
 * 
 * It is a component based method which adapts the parameters of the algorithm
 * at the end of every iteration. These parameters are adapted using a variance
 * value determined using the current topology.
 */
public class VarianceSaDEIterationStrategy extends AbstractIterationStrategy<EC> {
    protected Selector targetVectorSelectionStrategy;
    protected CreationStrategy trialVectorCreationStrategy;
    protected CrossoverStrategy crossoverStrategy;
    protected ZaharieComponentVariance variance;
    
    /*
     * Default constructor for VarianceSaDEIterationStrategy
     */
    public VarianceSaDEIterationStrategy() {
        super();
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new ComponentBasedRandToBestCreationStrategy();
        this.crossoverStrategy = new ComponentBasedBinomialCrossover();
        variance = new ZaharieComponentVariance();
    }

    /*
     * Copy constructor for VarianceSaDEIterationStrategy
     * @param copy The VarianceSaDEIterationStrategy to be copied
     */
    public VarianceSaDEIterationStrategy(VarianceSaDEIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        variance = copy.variance;
    }
    
    /*
     * Clone method for VarianceSaDEIterationStrategy
     * @return A new instance of this VarianceSaDEIterationStrategy
     */
    @Override
    public AbstractIterationStrategy<EC> getClone() {
        return new VarianceSaDEIterationStrategy(this);
    }

    /*
     * The iteration strategy which frist apdapts the particles in a component-based manner
     * and proceeds to adapt the parameters using the variance of the topology.
     * @param algorithm The current algorithm
     */
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

    /*
     * Gets the selection strategy of the target vector
     * @return The target vector selection strategy
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /*
     * Sets the selection strategy of the target vector
     * @param targetVectorSelectionStrategy The new target vector selection strategy
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    /*
     * Gets the creation strategy of the trial vector
     * @return The trial vector creation strategy
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

     /*
     * Sets the creation strategy of the trial vector
     * @param trialVectorCreationStrategy The trial vector creation strategy
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }
    
    /*
     * Gets the crossover strategy
     * @return The crossover strategy
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /*
     * Sets the crossover strategy
     * @return crossoverStrategy The new crossover strategy
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
   
}
