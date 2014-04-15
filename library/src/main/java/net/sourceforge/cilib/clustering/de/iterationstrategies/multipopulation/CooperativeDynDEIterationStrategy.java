/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 *
 * @author Kris
 */
public class CooperativeDynDEIterationStrategy extends DynDEIterationStrategy {
    private ClusterIndividual context;
    
    
    /*
     * Default constructor for CooperativeDynDEIterationStrategy
     */
    public CooperativeDynDEIterationStrategy() {
        super();
        exclusionRadius = new StandardUpdatableControlParameter(5.0);
        context = new ClusterIndividual();
    }
    
    /*
     * Copy constructor for CooperativeDynDEIterationStrategy
     * @param copy The CooperativeDynDEIterationStrategy to be copied
     */
    public CooperativeDynDEIterationStrategy(CooperativeDynDEIterationStrategy copy) {
        super(copy);
        this.context = copy.context;
    }
    
    /*
     * Clone method for CooperativeDynDEIterationStrategy
     * @return A new instance of this CooperativeDynDEIterationStrategy
     */
    @Override
    public CooperativeDynDEIterationStrategy getClone() {
        return new CooperativeDynDEIterationStrategy(this);
    }
    
    /*
     * Performs the iteration of a cooperative dynamic DE. First the context is updated, then the populations
     * are given the context and then the populations are processed.
     * @param algorithm The multi-population algorithm currently running
     */
    @Override
    public void performIteration(StandardMultipopulationAlgorithm algorithm) {
        createContext(algorithm.getPopulations());
        contextualisePopulations(algorithm.getPopulations());
        evaluatePopulations(algorithm.getPopulations());
        processPopulations(algorithm.getPopulations());
    }
    
    private void contextualisePopulations(List<PopulationBasedAlgorithm> populations) {
        int index = 0;
        for(PopulationBasedAlgorithm population : populations) {
            contextualisePopulation(population, index);
            index++;
        }
    }
    
    /*
     * Updates the popualations to contain the context
     * @param multipopAlgorithm The multi-population algorithm currently running
     */
    public void contextualisePopulation(PopulationBasedAlgorithm algorithm, int indx) {
        
        for(ClusterIndividual indiv : (Topology<ClusterIndividual>) algorithm.getTopology()) {
            int centroidIndex = 0;
            for(ClusterCentroid centroid : ((CentroidHolder) context.getCandidateSolution())) {
                if(centroidIndex != indx) {
                    ((CentroidHolder) indiv.getCandidateSolution()).set(centroidIndex, centroid.getClone());
                } 

                centroidIndex++;
            }
        } 

    }
    
    public void contextualiseIndividual(ClusterIndividual individual, int indx) {
        int centroidIndex = 0;
        for(ClusterCentroid centroid : ((CentroidHolder) context.getCandidateSolution())) {
            if(centroidIndex != indx) {
                ((CentroidHolder) individual.getCandidateSolution()).set(centroidIndex, centroid.getClone());
            } 

            centroidIndex++;
        }
    }
    
    /*
     * Updates the context individual using the best solutions from each populstion.
     * @param populations The populations to be used to determine the context
     * @return The context particle
     */
    public ClusterIndividual createContext(List<PopulationBasedAlgorithm> populations) {
        context = (ClusterIndividual) populations.get(0).getTopology().get(0).getClone();
        int index = 0;
        
        for(PopulationBasedAlgorithm algorithm : (List<PopulationBasedAlgorithm>) populations) {
               ((CentroidHolder) context.getCandidateSolution()).set(index, (ClusterCentroid) ((CentroidHolder) algorithm.getBestSolution().getPosition()).get(index).getClone());
                index++;
        }
        
        for(ClusterCentroid centroid : (CentroidHolder) context.getCandidateSolution()) {
            centroid.clearDataItems();
        }
        
        assignDataPatternsToEntity((CentroidHolder) context.getCandidateSolution(), ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) populations.get(0)).getIterationStrategy()).getWindow().getCurrentDataset());
        context.calculateFitness();
         
        return context;
    }
    
    @Override
    protected void updatePopulations(boolean distanceIsSmaller, PopulationBasedAlgorithm weakestPopulation, int weakestIndex, PopulationBasedAlgorithm algorithm, int algIndx, List<PopulationBasedAlgorithm> populationList) {   
        performIteration((SinglePopulationBasedAlgorithm) algorithm, algIndx);
        
        //contextualisePopulations(populationList);
        //evaluatePopulations(populationList);
        
        createContext(populationList);
        if(distanceIsSmaller) {
            contextualisePopulation(weakestPopulation, weakestIndex); //
            evaluatePopulation(weakestPopulation);
        }

        //with new context
        contextualisePopulation(algorithm, algIndx);
        evaluatePopulation(algorithm);

        updateWeakest((SinglePopulationBasedAlgorithm) algorithm);

        //brownian individuals changed whole individual
        contextualisePopulation(algorithm, algIndx);
        evaluatePopulation(algorithm);
        
        createContext(populationList); //
        contextualisePopulation(algorithm, algIndx);
        evaluatePopulation(algorithm);
    }
    
    private void evaluatePopulation(PopulationBasedAlgorithm algorithm) {
        clearCentroidDistanceValues((Topology<ClusterIndividual>) algorithm.getTopology());//

        for(Entity individual : algorithm.getTopology()) {
            assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) algorithm).getIterationStrategy()).getWindow().getCurrentDataset());
            individual.calculateFitness();
        }
    }
    
    @Override
    protected boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, int currentIndex, CentroidHolder otherPosition, int otherIndex) {
//        int count = 0;
//        for(int i = 0; i < currentPosition.size(); i++) {
            if(measure.distance(currentPosition.get(currentIndex).toVector(), otherPosition.get(otherIndex).toVector()) < exclusionRadius.getParameter()) {
                return true;
            }
            
            return false;
//        }
//        
//        if(count == currentPosition.size()) {
//            return true;
//        }
//        return false;
    }
    
    private void performIteration(SinglePopulationBasedAlgorithm algorithm, int algIndex) {
        Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) algorithm.getTopology();
        List<ClusterIndividual> newTopology = Lists.newArrayList();
        clearCentroidDistanceValues(topology);
        int index = 0;
        SinglePopulationDataClusteringDEIterationStrategy iterationStrategy = (SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) algorithm).getIterationStrategy();
        int totalRandoms = 5;
//        int[] randomIndexes = new int[totalRandoms];
//        ClusterIndividual[] randomVectors = new ClusterIndividual[totalRandoms];
//        int indexesChanged = 0;
//        
//        for(int i = 0; i < totalRandoms; i++) {
//            boolean unique = false;
//            
//            while(!unique) {
//                int newIndex = (int) new UniformDistribution().getRandomNumber(0,topology.size());
//                if(!contains(randomIndexes, newIndex)) {
//                    randomIndexes[i] = newIndex;
//                    unique = true;
//                }
//            }
//             
//        }
//        
//        for(int k = 0; k < totalRandoms; k++) {
//            ClusterIndividual newIndividual = topology.get(0).getClone();
//            newIndividual.reinitialise();
//            randomVectors[k] = newIndividual;
//        }
        
        for(ClusterIndividual individual : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) individual.getCandidateSolution();
            
            iterationStrategy.assignDataPatternsToIndividual(candidateSolution, iterationStrategy.getDataset());
            individual.setCandidateSolution(candidateSolution);
            individual.calculateFitness();
          
            ClusterIndividual offspring;
//            if(contains(randomIndexes, index)) {
//                offspring = (ClusterIndividual) individual.getUpdateStrategy().update(randomVectors[indexesChanged], algorithm);
//                indexesChanged++;
//            } else {
                offspring = (ClusterIndividual) individual.getUpdateStrategy().update(individual, algorithm);
//            }
            
            boundaryConstraint.enforce(offspring);
            contextualiseIndividual(offspring, algIndex);//
            
            iterationStrategy.assignDataPatternsToIndividual((CentroidHolder) offspring.getCandidateSolution(), iterationStrategy.getDataset());
            offspring.calculateFitness();
            
            if (offspring.getFitness().compareTo(individual.getFitness()) > 0) { // the trial vector is better than the parent
            /*    newTopology.add(offspring); // Replace the parent with the offspring individual
            } else {
                newTopology.add(individual);
            }*/
                topology.set(index, offspring);
            }
            
            index++;
        }
        
        //topology.clear();
        //topology.addAll(newTopology);
        iterationStrategy.setDataset(iterationStrategy.getWindow().slideWindow());
    }
    
}
