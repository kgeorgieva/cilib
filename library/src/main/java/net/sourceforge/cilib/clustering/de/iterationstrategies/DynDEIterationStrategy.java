/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.pso.iterationstrategies.SinglePopulationDataClusteringPSOIterationStrategy;
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
public class DynDEIterationStrategy extends AbstractIterationStrategy<MultiPopulationBasedAlgorithm>{
    private double exclusionRadius;
    private IterationStrategy iterationStrategy;
    private DistanceMeasure measure;
    
    public DynDEIterationStrategy() {
        exclusionRadius = 1.0;
        iterationStrategy = new StandardClusteringDEIterationStrategy();
        measure = new EuclideanDistanceMeasure();
    }
    
    public DynDEIterationStrategy(DynDEIterationStrategy copy) {
        exclusionRadius = copy.exclusionRadius;
        iterationStrategy = copy.iterationStrategy.getClone();
        measure = copy.measure;
    }
    
    @Override
    public AbstractIterationStrategy<MultiPopulationBasedAlgorithm> getClone() {
        return new DynDEIterationStrategy(this);
    }

    @Override
    public void performIteration(MultiPopulationBasedAlgorithm algorithm) {
        
    }
    
    protected void evaluatePopulations(List<PopulationBasedAlgorithm> populations) {
        for(PopulationBasedAlgorithm algorithm : populations) {
            for(Entity individual : algorithm.getTopology()) {
                individual.calculateFitness();
            }
        }
    }
    
    protected boolean processPopulations(List<PopulationBasedAlgorithm> populations) {
        boolean distanceIsSmaller;
        PopulationBasedAlgorithm weakest;
        for(PopulationBasedAlgorithm algorithm : populations) {
            distanceIsSmaller = false;
            weakest = algorithm;
            for(PopulationBasedAlgorithm otherAlgorithm : populations) {
                if(!algorithm.equals(otherAlgorithm)) {
                    if(measure.distance((Vector) algorithm.getBestSolution().getPosition(), (Vector) otherAlgorithm.getBestSolution().getPosition()) < exclusionRadius) {
                        if (weakest.getBestSolution().compareTo(otherAlgorithm.getBestSolution()) > 0) {
                            weakest = otherAlgorithm;
                        }
                        distanceIsSmaller = true;
                    }
                }
            }
            
            if(distanceIsSmaller) {
                reinitialisePopulation((DataClusteringEC) weakest);
                return true;
            }
            
            if(algorithm.getTopology().get(0).getFitness().equals(InferiorFitness.instance())) {
                // perform algorithm
            }
        }
        
        return false;
    }
    
    protected void reinitialisePopulation(DataClusteringEC algorithm) {
        for(Entity individual : algorithm.getTopology()) {
            individual.reinitialise();
            assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), 
                    ((SinglePopulationDataClusteringPSOIterationStrategy) algorithm.getIterationStrategy()).getWindow().getCurrentDataset());
        }
    }
    
    
    public void assignDataPatternsToEntity(CentroidHolder candidateSolution, DataTable dataset) {
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
}
