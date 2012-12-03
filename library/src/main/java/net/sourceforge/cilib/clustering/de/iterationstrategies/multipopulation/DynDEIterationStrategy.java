/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kris
 */
public class DynDEIterationStrategy extends AbstractIterationStrategy<StandardMultipopulationAlgorithm>{
    private double exclusionRadius;
    private DistanceMeasure measure;
    
    public DynDEIterationStrategy() {
        exclusionRadius = 1.0;
        measure = new EuclideanDistanceMeasure();
    }
    
    public DynDEIterationStrategy(DynDEIterationStrategy copy) {
        exclusionRadius = copy.exclusionRadius;
        measure = copy.measure;
    }
    
    @Override
    public AbstractIterationStrategy<StandardMultipopulationAlgorithm> getClone() {
        return new DynDEIterationStrategy(this);
    }

    @Override
    public void performIteration(StandardMultipopulationAlgorithm algorithm) {
        evaluatePopulations(algorithm);
        processPopulations(algorithm.getPopulations());
    }
    
    protected void evaluatePopulations(MultiPopulationBasedAlgorithm algorithm) {
        for(PopulationBasedAlgorithm alg : algorithm.getPopulations()) {
            for(Entity individual : alg.getTopology()) {
                assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) alg).getIterationStrategy()).getWindow().getCurrentDataset());
                individual.calculateFitness();
            }
        }
    }
    
    protected void processPopulations(List<PopulationBasedAlgorithm> populations) {
        boolean distanceIsSmaller;
        PopulationBasedAlgorithm weakest;
        for(PopulationBasedAlgorithm algorithm : populations) {
            distanceIsSmaller = false;
            weakest = algorithm;
            for(PopulationBasedAlgorithm otherAlgorithm : populations) {
                if(!algorithm.equals(otherAlgorithm)) {
                    if(aDistanceIsSmallerThanRadius((CentroidHolder) algorithm.getBestSolution().getPosition(), (CentroidHolder) otherAlgorithm.getBestSolution().getPosition())) {
                        if (weakest.getBestSolution().compareTo(otherAlgorithm.getBestSolution()) > 0) {
                            weakest = otherAlgorithm;
                        }
                        distanceIsSmaller = true;
                    }
                }
            }
            
            if(distanceIsSmaller) {
                reinitialisePopulation((DataClusteringEC) weakest);
            }
            
            if(!algorithm.getTopology().get(0).getFitness().equals(InferiorFitness.instance())) {
                algorithm.performIteration();
            }
        }
        
    }
    
    protected void reinitialisePopulation(DataClusteringEC algorithm) {
        for(Entity individual : algorithm.getTopology()) {
            individual.reinitialise();
            assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), 
                    ((SinglePopulationDataClusteringDEIterationStrategy) algorithm.getIterationStrategy()).getWindow().getCurrentDataset());
        }
    }
    
    
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
    
    protected boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, CentroidHolder otherPosition) {
        for(int i = 0; i < currentPosition.size(); i++) {
            if(measure.distance(currentPosition.get(i).toVector(), otherPosition.get(i).toVector()) < exclusionRadius) {
                return true;
            }
        }
        return false;
    }

    public double getExclusionRadius() {
        return exclusionRadius;
    }

    public void setExclusionRadius(double exclusionRadius) {
        this.exclusionRadius = exclusionRadius;
    }

    public DistanceMeasure getMeasure() {
        return measure;
    }

    public void setMeasure(DistanceMeasure measure) {
        this.measure = measure;
    }
    
    
}
