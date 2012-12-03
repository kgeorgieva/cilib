/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class StandardClusteringDEIterationStrategy extends SinglePopulationDataClusteringDEIterationStrategy{
    
    public StandardClusteringDEIterationStrategy() {
        super();
    }
    
    public StandardClusteringDEIterationStrategy(StandardClusteringDEIterationStrategy copy) {
        super(copy);
    }
    
    @Override
    public StandardClusteringDEIterationStrategy getClone() {
        return new StandardClusteringDEIterationStrategy(this);
    }
    
    @Override
    public void performIteration(DataClusteringEC algorithm) {
        Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) algorithm.getTopology();
        double euclideanDistance;
        Vector addedPattern;
        clearCentroidDistanceValues(topology);
        reinitialized = false;
        Vector pattern;
        int index = 0;
        
        for(ClusterIndividual individual : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) individual.getCandidateSolution();
            
            assignDataPatternsToIndividual(candidateSolution, dataset);
            individual.setCandidateSolution(candidateSolution);
            individual.calculateFitness();
          
            ClusterIndividual offspring = (ClusterIndividual) individual.getUpdateStrategy().update(individual, topology);
            
            boundaryConstraint.enforce(offspring);
            assignDataPatternsToIndividual((CentroidHolder) offspring.getCandidateSolution(), dataset);
            offspring.calculateFitness();
            
            if (offspring.getFitness().compareTo(individual.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(index, offspring); // Replace the parent with the offspring individual
            }
            
            index++;
        }
        
        dataset = window.slideWindow();
        
    }
}

