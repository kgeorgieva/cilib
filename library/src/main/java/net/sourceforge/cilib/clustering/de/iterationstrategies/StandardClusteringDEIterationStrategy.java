/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 * Standard data clustering differential evolution iteration strategy described in 
 * Sai Hanuman A., Dr Vinaya Babu A., Dr Govardhan A., Dr S C Satapathy's 2010 paper
 * "Data Clustering using almost parameter free Differential Evolution".
 */
public class StandardClusteringDEIterationStrategy extends SinglePopulationDataClusteringDEIterationStrategy{
    
    /*
     * Default constructor for StandardClusteringDEIterationStrategy
     */
    public StandardClusteringDEIterationStrategy() {
        super();
        
    }
    
    /*
     * Copy constructor for StandardClusteringDEIterationStrategy
     * @param copy The StandardClusteringDEIterationStrategy to be copied
     */
    public StandardClusteringDEIterationStrategy(StandardClusteringDEIterationStrategy copy) {
        super(copy);
        
    }
    
    /*
     * Clone method for StandardClusteringDEIterationStrategy
     * @return A new instance of StandardClusteringDEIterationStrategy
     */
    @Override
    public StandardClusteringDEIterationStrategy getClone() {
        return new StandardClusteringDEIterationStrategy(this);
    }
    
    /*
     * Performs one iteration of the standard clustering DE algorithm where 
     * patterns are assigned to individuals, DE is performed and the best 
     * individual between the offspring and parent is selected to survive to
     * the next generation.
     */
    @Override
    public void performIteration(DataClusteringEC algorithm) {
        Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) algorithm.getTopology();
        List<ClusterIndividual> newTopology = Lists.newArrayList();
        clearCentroidDistanceValues(topology);
        reinitialized = false;
        int index = 0;
        
        for(ClusterIndividual individual : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) individual.getCandidateSolution();
            
            assignDataPatternsToIndividual(candidateSolution, dataset);
            individual.setCandidateSolution(candidateSolution);
            individual.calculateFitness();
          
            ClusterIndividual offspring = (ClusterIndividual) individual.getUpdateStrategy().update(individual, algorithm);
            
            boundaryConstraint.enforce(offspring);
            
            assignDataPatternsToIndividual((CentroidHolder) offspring.getCandidateSolution(), dataset);
            offspring.calculateFitness();
            
            if (offspring.getFitness().compareTo(individual.getFitness()) > 0) { // the trial vector is better than the parent
                newTopology.add(offspring); // Replace the parent with the offspring individual
            } else {
                newTopology.add(individual);
            }
                //topology.set(index, offspring);
            
            index++;
        }
        
        topology.clear();
        topology.addAll(newTopology);
        dataset = window.slideWindow();
        
    }
   
}

