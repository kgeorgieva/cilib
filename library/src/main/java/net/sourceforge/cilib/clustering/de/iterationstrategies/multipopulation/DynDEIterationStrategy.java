/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.BrownianClusteringUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * Dynamic Differential Evolution iteration strategy described in 
 * Mendes and Mohais' 2005 IEEE paper "DynDE: a Differential Evolution
 * for Dynamic Optimization Problems".
 */
public class DynDEIterationStrategy extends AbstractIterationStrategy<StandardMultipopulationAlgorithm>{
    private double exclusionRadius;
    private DistanceMeasure measure;
    private int totalReplaceableIndividuals;
    private UpdateStrategy updateStrategyForWeakestIndividuals;
    
    /*
     * Default constructor for DynDEIterationStrategy
     */
    public DynDEIterationStrategy() {
        exclusionRadius = 1.0;
        measure = new EuclideanDistanceMeasure();
        updateStrategyForWeakestIndividuals = new BrownianClusteringUpdateStrategy();
        totalReplaceableIndividuals = 5;
    }
    
    /*
     * Copy constructor for DynDEIterationStrategy
     * @param copy The DynDEIterationStrategy to be copied
     */
    public DynDEIterationStrategy(DynDEIterationStrategy copy) {
        exclusionRadius = copy.exclusionRadius;
        measure = copy.measure;
        updateStrategyForWeakestIndividuals = copy.updateStrategyForWeakestIndividuals;
        totalReplaceableIndividuals = copy.totalReplaceableIndividuals;
    }
    
    /*
     * Clone method for DynDEIterationStrategy
     * @return A new instance of this DynDEIterationStrategy
     */
    @Override
    public AbstractIterationStrategy<StandardMultipopulationAlgorithm> getClone() {
        return new DynDEIterationStrategy(this);
    }

    /*
     * Performs one iteration of the Dynamic DE algorithm.
     * Populations are first evalueated and then processed
     * @param algorithm The multipopulation algorithm being used
     */
    @Override
    public void performIteration(StandardMultipopulationAlgorithm algorithm) {
        evaluatePopulations(algorithm);
        processPopulations(algorithm.getPopulations());
    }
    
    /*
     * Assigns data patterns to all centroids of each population and determines the
     * fitness of each individual using these.
     * @param The multipopulation algorithm holding all the populations to be
     * evaluated
     */
    protected void evaluatePopulations(MultiPopulationBasedAlgorithm algorithm) {
        for(PopulationBasedAlgorithm alg : algorithm.getPopulations()) {
            for(Entity individual : alg.getTopology()) {
                assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) alg).getIterationStrategy()).getWindow().getCurrentDataset());
                individual.calculateFitness();
            }
        }
    }
    
    /*
     * Determines which populations must be reinitialised and for which
     * populations an iteration of the clustering algorithm must take place.
     * @param populations The populations being dealt with
     */
    protected void processPopulations(List<PopulationBasedAlgorithm> populations) {
        boolean distanceIsSmaller;
        PopulationBasedAlgorithm weakestPopulation;
        
        for(PopulationBasedAlgorithm algorithm : populations) {
            distanceIsSmaller = false;
            weakestPopulation = algorithm;
            for(PopulationBasedAlgorithm otherAlgorithm : populations) {
                if(!algorithm.equals(otherAlgorithm)) {
                    if(aDistanceIsSmallerThanRadius((CentroidHolder) algorithm.getBestSolution().getPosition(), (CentroidHolder) otherAlgorithm.getBestSolution().getPosition())) {
                        if (weakestPopulation.getBestSolution().compareTo(otherAlgorithm.getBestSolution()) > 0) {
                            weakestPopulation = otherAlgorithm;
                        }
                        distanceIsSmaller = true;
                    }
                }
            }
            
            if(distanceIsSmaller) {
                reinitialisePopulation((DataClusteringEC) weakestPopulation);
            }
            
            if(!algorithm.getTopology().get(0).getFitness().equals(InferiorFitness.instance())) {
                algorithm.performIteration();
            }
            
            updateWeakest((SinglePopulationBasedAlgorithm) algorithm);
        }
        
    }
    
    protected void updateWeakest(SinglePopulationBasedAlgorithm algorithm) {
        Topology<Individual> topology = (Topology<Individual>) algorithm.getTopology();
        int weakest = 0;
        int individualCount;
        int[] weakestIndividuals = new int[totalReplaceableIndividuals];
        
        for(int i = 0; i < totalReplaceableIndividuals; i++) {
            individualCount = 0;
            weakest = 0;
            for(Individual individual : topology) {
                if(!contains(weakestIndividuals, individualCount) && topology.get(weakest).getFitness().compareTo(individual.getFitness()) > 0) {
                    weakest = individualCount;
                }
                individualCount++;
            }

            weakestIndividuals[i] = weakest;
        }
                
        for(int individualID : weakestIndividuals) {
            topology.set(individualID, (Individual) updateStrategyForWeakestIndividuals.update(topology.get(individualID), algorithm));
        }
        
    }
    
    private boolean contains(int[] array, int value) {
        for(int val : array) {
            if(val == value) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * Reinitialises the population held by the algorithm sent as a parameter
     * @param algorithm The algorithm whose population must be reinitialised
     */
    protected void reinitialisePopulation(DataClusteringEC algorithm) {
        for(Entity individual : algorithm.getTopology()) {
            individual.reinitialise();
            assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), 
                    ((SinglePopulationDataClusteringDEIterationStrategy) algorithm.getIterationStrategy()).getWindow().getCurrentDataset());
        }
    }
    
    /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    protected void assignDataPatternsToEntity(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure aDistanceMeasure = new EuclideanDistanceMeasure();
        
        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(aDistanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = aDistanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
    }
    
    /*
     * Checks whether populations are within radius distance of each other
     * @param currentPosition The CentoridHolder being compared
     * @param otherPosition The CentroidHolder being compared against
     * @return True if the populations are close enough, false if they are still far appart
     */
    protected boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, CentroidHolder otherPosition) {
        for(int i = 0; i < currentPosition.size(); i++) {
            if(measure.distance(currentPosition.get(i).toVector(), otherPosition.get(i).toVector()) < exclusionRadius) {
                return true;
            }
        }
        return false;
    }

    /*
     * Gets the value of the exclusion radius
     * @return The value of the exclusion radius
     */
    public double getExclusionRadius() {
        return exclusionRadius;
    }

    /*
     * Sets the value of the exclusion radius
     * @param exclusionRadius The new value for the exclusion radius
     */
    public void setExclusionRadius(double exclusionRadius) {
        this.exclusionRadius = exclusionRadius;
    }

    /*
     * Gets the measure used to determine the proximity of two populations
     * @return The measure used to determine the proximity of two populations
     */
    public DistanceMeasure getMeasure() {
        return measure;
    }

    /*
     * Sets the measure used to determine the proximity of two populations
     * @param measure The new measure
     */
    public void setMeasure(DistanceMeasure measure) {
        this.measure = measure;
    }
    
    
}
