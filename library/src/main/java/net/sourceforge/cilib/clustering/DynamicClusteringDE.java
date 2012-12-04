/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.BrownianClusteringUpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Dynamic Differential Evolution algorithm described in 
 * Mendes and Mohais' 2005 IEEE paper "DynDE: a Differential Evolution for Dynamic
 * Optimization Problems".
 */
public class DynamicClusteringDE extends DataClusteringEC {
    private ProbabilityDistributionFunction random;
    private UpdateStrategy firstUpdateStrategy;
    private UpdateStrategy secondUpdateStrategy;
    private double updateStrategyProbability;
    
    /*
     * Default constructor for the DynamicClusteringDE
     */
    public DynamicClusteringDE() {
        random = new UniformDistribution();
        firstUpdateStrategy = new StandardClusteringDEUpdateStrategy();
        secondUpdateStrategy = new BrownianClusteringUpdateStrategy();
        super.iterationStrategy = new StandardClusteringDEIterationStrategy();
        updateStrategyProbability = 0.5;
    }
    
    /*
     * Copy constructor for the DynamicClusteringDE
     * @param copy The DynamicClusteringDE to be copied
     */
    public DynamicClusteringDE(DynamicClusteringDE copy) {
        random = copy.random;
        firstUpdateStrategy = copy.firstUpdateStrategy;
        secondUpdateStrategy = copy.secondUpdateStrategy;
        updateStrategyProbability = copy.updateStrategyProbability;
    }
    
    /*
     * Clone method for the DynamicClusteringDE
     * @return A new instance of this DynamicClusteringDE
     */
    @Override
    public DynamicClusteringDE getClone() {
        return new DynamicClusteringDE(this);
    }
    
    /*
     * Initialises the algorithm according to Mendes and Mohais' paper.
     * It uses an initialisation strategy to initialise individuals
     * and then randomly assigns an update strategy to eaach individual.
     */
    @Override
    public void algorithmInitialisation() {
        DataTable dataset = window.initializeWindow();

        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        ((ClusteringProblem) super.problem).setDimension((int) pattern.size());

        ((DataDependantPopulationInitializationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterIndividual> individuals = (Iterable<ClusterIndividual>) this.initialisationStrategy.initialise(this.getOptimisationProblem());

        ClusterIndividual individual;
        for (Entity clusterEntity : individuals) {
            individual = (ClusterIndividual) clusterEntity;
            if(random.getRandomNumber(0,1) < updateStrategyProbability) {
                individual.setUpdateStrategy(firstUpdateStrategy.getClone());
            } else {
                individual.setUpdateStrategy(secondUpdateStrategy.getClone());
            }
        }
        
        super.topology.clear();
        super.topology.addAll(Lists.<ClusterIndividual>newLinkedList(individuals));

        ((SinglePopulationDataClusteringDEIterationStrategy) iterationStrategy).setWindow(window);
        
    }

    /*
     * Gets the probability distribution function used to select an update
     * strategy during initialisation of the individuals.
     * @return The probability distribution fi=unction used during initialisation
     */
    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    /*
     * Set the probability distribution function to be used to select an
     * update strategy during the initialisation of individuals
     * @param random The new probability distribution function
     */
    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    /*
     * Gets one of the update strategies to be assigned to each individual
     * @return The update strategy
     */
    public UpdateStrategy getFirstUpdateStrategy() {
        return firstUpdateStrategy;
    }

    /*
     * Sets one of the update strategies to be assigned to each individual
     * @param firstUpdateStrategy The new update strategy
     */
    public void setFirstUpdateStrategy(UpdateStrategy firstUpdateStrategy) {
        this.firstUpdateStrategy = firstUpdateStrategy;
    }

    /*
     * Gets the other update strategy to be assigned to each individual
     * @return The update strategy
     */
    public UpdateStrategy getSecondUpdateStrategy() {
        return secondUpdateStrategy;
    }

    /*
     * Sets the other update strategy to be assigned to each individual
     * @param secondUpdateStrategy The new update strategy
     */
    public void setSecondUpdateStrategy(UpdateStrategy secondUpdateStrategy) {
        this.secondUpdateStrategy = secondUpdateStrategy;
    }

    /*
     * Gets the probability that an update strategy will be selected and 
     * assigned to an individual
     * @return The update strategy selection probability
     */
    public double getUpdateStrategyProbability() {
        return updateStrategyProbability;
    }

    /*
     * Sets the probability that an update strategy will be selected and 
     * assigned to an individual.
     * @param The new update strategy selection probability
     */
    public void setUpdateStrategyProbability(double updateStrategyProbability) {
        this.updateStrategyProbability = updateStrategyProbability;
    }
    
}
