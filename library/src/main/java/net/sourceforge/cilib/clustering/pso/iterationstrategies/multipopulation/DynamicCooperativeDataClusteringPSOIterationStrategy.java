/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.pso.iterationstrategies.multipopulation;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.CooperativePSO;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.util.changeDetection.ChangeDetectionStrategy;
import net.sourceforge.cilib.util.changeDetection.IterationBasedChangeDetectionStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * This class an iteration of the cooperative data clustering iteration strategy.
 * If there is a change in the environment, this class re-initializes the context 
 * particle as well as part of or the whole population.
 */
public class DynamicCooperativeDataClusteringPSOIterationStrategy extends CooperativeDataClusteringPSOIterationStrategy{
    int reinitialisationPercentage;
    ChangeDetectionStrategy changeDetectionStrategy;
    
    /*
     * Default constructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy() {
        super();
        reinitialisationPercentage = 1;
        changeDetectionStrategy = new IterationBasedChangeDetectionStrategy();
    }
    
    /*
     * Copy cosntructor for DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    public DynamicCooperativeDataClusteringPSOIterationStrategy(DynamicCooperativeDataClusteringPSOIterationStrategy copy) {
        super(copy);
        reinitialisationPercentage = copy.reinitialisationPercentage;
        changeDetectionStrategy = copy.changeDetectionStrategy;
    }
    
    /*
     * Clone method for DynamicCooperativeDataClusteringPSOIterationStrategy
     * @return new instance of the DynamicCooperativeDataClusteringPSOIterationStrategy
     */
    @Override
    public DynamicCooperativeDataClusteringPSOIterationStrategy getClone() {
        return new DynamicCooperativeDataClusteringPSOIterationStrategy(this);
    }

    /*
     * Performs an iteration of DynamicCooperativeDataClusteringPSOIterationStrategy
     * First it performs an iteration of the cooperative data clustering iteration strategy,
     * followed by a check for change which leads to a re-initialization process if it is true.
     * @param algorithm The algorithm for which the iteration is veing performed
     */
    @Override
    public void performIteration(CooperativePSO algorithm) {
        Topology topology;
        if(changeDetectionStrategy.detectChange()) {
               this.reinitializeContext(algorithm);
               for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                    topology = currentAlgorithm.getTopology();

                    int index = (int) new UniformDistribution().getRandomNumber(0, topology.size());
                    int totalEntities = topology.size() * reinitialisationPercentage / 100;
                    int[] alreadyChangedIndexes = new int[totalEntities];

                    for(int j = 0; j < alreadyChangedIndexes.length; j++) {
                        alreadyChangedIndexes[j] = -1;
                    }

                    for(int i = 0; i < totalEntities; i++) {
                        while(contains(alreadyChangedIndexes, index)) {
                            index = (int) new UniformDistribution().getRandomNumber(0, topology.size());
                        }

                        ((ClusterParticle) topology.get(i)).reinitialise();
                        assignDataPatternsToParticle(((CentroidHolder)((ClusterParticle) topology.get(i)).getCandidateSolution()), table);
                        alreadyChangedIndexes[i] = index;
                    }
             }
               
        }
        
        super.performIteration(algorithm);
    }
    
    /*
     * Checks if an array contains a value
     * @param array The array to be checked
     * @param value The value to be checked for
     * @return True if the value is contained in the array, false otherwise
     */
    private boolean contains(int[] array, int value) {
        for(int val : array) {
            if(val == value) {
                return true;
            }
        }
        
        return false;
    }
    
    /*
     * Re-initializes the context particle. It is used by the Dynamic co-operative data clustering 
     * @param currentAlgorithm The algorithm for wich the context must be re-initialied
     */
    public void reinitializeContext(CooperativePSO currentAlgorithm) {  
        contextParticle = ((DataClusteringPSO) currentAlgorithm.getPopulations().get(0)).getTopology().get(0).getClone();
        contextParticle.reinitialise();
        clearDataPatterns(contextParticle);
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
        contextParticle.calculateFitness();
    }
    
    /*
     * Returns the percentage of entities to be re-initialized
     * @return reinitializationPercentage The percentage at which entities are re-initialized
     */
    public int getReinitialisationPercentage() {
        return reinitialisationPercentage;
    }
    
    /*
     * Sets the percentage of entities to be re-initialized
     * @param percentage The percentage of entities to be reinitialised
     */
    public void setReinitialisationPercentage(int percentage) {
        reinitialisationPercentage = percentage;
    }
    
    /*
     * Sets the change detection strategy to be used
     * @param changeStrategy The new changeDetectionStrategy
     */
    public void setChangeDetectionStrategy(ChangeDetectionStrategy changeStrategy) {
        changeDetectionStrategy = changeStrategy;
    }
    
    /*
     * Returns the change detection strategy being used
     * @return cahngeDetectionStrategy The current change detection strategy
     */
    public ChangeDetectionStrategy getChangeDetectionStrategy() {
        return changeDetectionStrategy;
    }
    
}
