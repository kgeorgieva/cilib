/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.util.selection.recipes.FeasibilitySelector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/*
 * This is the Diversity Differential Evolution iteration strategy described by Mezura-Montes 
 * and Palomeque-Ortiz in their 2009 paper "Self-adaptive and Deterministic Parameter
 * Control in Differential Evolution for Constrained Optimization".
 *
 * For firther details find:
 * 
 * @incollection{Mezura09a,
 * author = {Efr\'{e}n Mezura-Montes and Ana Gabriela Palomeque-Ortiz},
 * title = {{Self-adaptive and Deterministic Parameter Control in Differential
 *	Evolution for Constrained Optimization}},
 * booktitle = {Constraint-Handling in Evolutionary Computation},
 * editor = {Efr\'{e}n Mezura-Montes},
 * pages = {95--120},
 * chapter = {5},
 * publisher = {Springer. Studies in Computational Intelligence, Volume 198},
 * address = {Berlin},
 * year = {2009},
 * note = {ISBN 978-3-642-00618-0}
 */
public class DDEIterationStrategy  extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = 8019668923312811974L;
    protected Selector targetVectorSelectionStrategy; // x
    protected CreationStrategy trialVectorCreationStrategy; // y
    protected CrossoverStrategy crossoverStrategy; // z
    private double selectorParameter; //Parameter intorduced for DDE
    private int totalOffspring; //Parameter intorduced for DDE
    private ProbabilityDistributionFunction scalingFactorRandom;
    private ProbabilityDistributionFunction selectorRandom;
    private Selector offspringSelectionStrategy;
    private Selector nextGenerationSelectionStrategy;

    /**
     * Create an instance of the {@linkplain DDEIterationStrategy}.
     */
    public DDEIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
        this.selectorParameter = 0.5;
        this.totalOffspring = 2;
        this.scalingFactorRandom = new UniformDistribution();
        ((UniformDistribution) scalingFactorRandom).setLowerBound(ConstantControlParameter.of(0.3));
        ((UniformDistribution) scalingFactorRandom).setUpperBound(ConstantControlParameter.of(0.9));
        
        this.selectorRandom = new UniformDistribution();
        ((UniformDistribution) selectorRandom).setLowerBound(ConstantControlParameter.of(0));
        ((UniformDistribution) selectorRandom).setUpperBound(ConstantControlParameter.of(1));
        
        offspringSelectionStrategy = new FeasibilitySelector<Individual>();
        nextGenerationSelectionStrategy = new FeasibilitySelector<Individual>();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public DDEIterationStrategy(DDEIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.selectorParameter = copy.selectorParameter;
        this.totalOffspring = copy.totalOffspring;
        this.scalingFactorRandom = copy.scalingFactorRandom;
        this.selectorRandom = copy.selectorRandom;
        this.offspringSelectionStrategy = copy.offspringSelectionStrategy;
        this.nextGenerationSelectionStrategy = copy.offspringSelectionStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DDEIterationStrategy getClone() {
        return new DDEIterationStrategy(this);
    }

    /**
     * Perform an iteration of the DDE algorithm
     * @param ec The {@linkplain EC} on which to perform this iteration.
     */
    @Override
    public void performIteration(EC ec) {
        Topology<Entity> topology = (Topology<Entity>) ec.getTopology();
        List<Entity> newTopology = Lists.newArrayList();
        ArrayList selectorList = new ArrayList();
        Entity current;
        Entity offspringEntity;
        Entity targetEntity;
        Entity trialEntity;
        List<Entity> offspring;
        Entity tempOffspringEntity;

        //generate the scaling factor randomly each iteration
        trialVectorCreationStrategy.setScaleParameter(scalingFactorRandom.getRandomNumber());
        
        for (int i = 0; i < topology.size(); i++) {
            current = topology.get(i);
            current.calculateFitness();
            offspringEntity = current.getClone();
            
            //take the best offspring from a set of offsprings created with the same trial vector
            for(int o = 0; o < totalOffspring; o++) {
               
                // Create the trial vector by applying mutation
                
                targetEntity = (Entity) targetVectorSelectionStrategy.on(topology).exclude(current).select();

                // Create the trial vector / entity
                trialEntity = trialVectorCreationStrategy.create(targetEntity.getClone(), current.getClone(), topology.getClone());

                // Create the offspring by applying cross-over
                offspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!
                tempOffspringEntity = offspring.get(0);
                tempOffspringEntity.calculateFitness();
                
                selectorList.clear();
                selectorList.add(offspringEntity);
                selectorList.add(tempOffspringEntity);
                
                //Select the best offspring so far
                if(totalOffspring > 1) {
                    offspringEntity = ((Entity) offspringSelectionStrategy.on(selectorList).select()).getClone();
                } else {
                    offspringEntity = tempOffspringEntity.getClone();
                }
                
            }
            
            //select the best between the current entity and the offspring entity
            if(selectorRandom.getRandomNumber() > selectorParameter) {
                if(offspringEntity.getFitness().compareTo(current.getFitness()) > 0 ){
                    newTopology.add(offspringEntity);
                } else {
                    newTopology.add(current);
                }
            } else {
                selectorList.clear();
                selectorList.add(offspringEntity);
                selectorList.add(current);
                offspringEntity = (Entity) nextGenerationSelectionStrategy.on(selectorList).select();
                newTopology.add(offspringEntity);
            }
        }
        
        topology.clear();
        topology.addAll(newTopology);
    }

    /**
     * Obtain the {@linkplain SelectionStrategy} used to select the target vector.
     * @return The {@linkplain SelectionStrategy} of the target vector.
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain SelectionStrategy} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain SelectionStrategy} to use for the
     *        selection of the target vector.
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    /**
     * Get the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @return The {@linkplain CrossoverStrategy} used to create offspring.
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /**
     * Set the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to create entities.
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /**
     * Get the current strategy for creation of the trial vector.
     * @return The {@linkplain CreationStrategy}.
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    /**
     * Set the strategy to create trial vectors.
     * @param trialVectorCreationStrategy The value to set.
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }

    /*
     * Gets the current value of the selector parameter
     * @return selectorParamerer The value of the selector parameter
     */
    public double getSelectorParameter() {
        return selectorParameter;
    }

    /*
     * Sets the current value of the selector parameter to the one provided as a parameter
     * @param selectorParamerer The new value of the selector parameter
     */
    public void setSelectorParameter(double selectorParameter) {
        this.selectorParameter = selectorParameter;
    }

    /*
     * Gets the current value of the total number of offspring to be generated
     * @return totalOffspring The total number of offspring to be generated
     */
    public int getTotalOffspring() {
        return totalOffspring;
    }

    /*
     * sets the current value of the total number of offspring to be generated
     * @param totalOffspring The total number of offspring to be generated
     */
    public void setTotalOffspring(int totalOffspring) {
        this.totalOffspring = totalOffspring;
    }

    /**
     * Get the probability distribution that will be used for the scaling factor.
     * @return The {@linkplain ProbabilityDistributionFunction}.
     */
    public ProbabilityDistributionFunction getScalingFactorRandom() {
        return scalingFactorRandom;
    }

    /**
     * Set the probability distribution that will be used for the scaling factor.
     * @return The {@linkplain ProbabilityDistributionFunction}.
     */
    public void setScalingFactorRandom(ProbabilityDistributionFunction random) {
        this.scalingFactorRandom = random;
    }

    /**
     * Get the selection strategy that will be used to select between offsprings
     * @return The {@linkplain Selector}.
     */
    public Selector getOffspringSelectionStrategy() {
        return offspringSelectionStrategy;
    }
    
    /**
     * Set the selection strategy that will be used to select between offsprings
     * @return The {@linkplain Selector}.
     */
    public void setOffspringSelectionStrategy(Selector offspringSelectionStrategy) {
        this.offspringSelectionStrategy = offspringSelectionStrategy;
    }

    /**
     * Get the selection strategy that will be used to select survivors for the next generation
     * @return The {@linkplain Selector}.
     */
    public Selector getNextGenerationSelectionStrategy() {
        return nextGenerationSelectionStrategy;
    }

     /**
     * Sets the selection strategy that will be used to select survivors for the next generation
     * @return The {@linkplain Selector}.
     */
    public void setNextGenerationSelectionStrategy(Selector nextGenerationSelectionStrategy) {
        this.nextGenerationSelectionStrategy = nextGenerationSelectionStrategy;
    }

     /**
     * Get the probability distribution function that will be used for the decision between the selector to be used
     * @return The {@linkplain ProbabilityDistributionFunction}.
     */
    public ProbabilityDistributionFunction getSelectorRandom() {
        return selectorRandom;
    }

     /**
     * Sets the probability distribution function that will be used for the decision between the selector to be used
     * @return The {@linkplain ProbabilityDistributionFunction}.
     */
    public void setSelectorRandom(ProbabilityDistributionFunction selectorRandom) {
        this.selectorRandom = selectorRandom;
    }
    
    
}
