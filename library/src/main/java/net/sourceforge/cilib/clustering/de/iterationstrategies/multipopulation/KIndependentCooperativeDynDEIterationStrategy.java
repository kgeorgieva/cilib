/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.BrownianClusteringUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.measurement.clustervalidity.InterClusterDistance;
import net.sourceforge.cilib.measurement.clustervalidity.IntraClusterDistance;
import net.sourceforge.cilib.measurement.clustervalidity.SilhouetteCoefficient;
import net.sourceforge.cilib.measurement.clustervalidity.ValidityIndex;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kris
 */
public class KIndependentCooperativeDynDEIterationStrategy extends AbstractIterationStrategy<StandardMultipopulationAlgorithm> {
    private ClusterIndividual context;
    protected double exclusionRadius;
    protected DistanceMeasure measure;
    protected int totalReplaceableIndividuals;
    protected UpdateStrategy updateStrategyForWeakestIndividuals;
    
    private double previousInterCluster;
    private double previousIntraCluster;
    
    private double previousRayTuri;
    private boolean populationHasConverged;
    
    private double silhouetteThreshold;
    private double convergenceRadius;
    
    /*
     * Default constructor for CooperativeDynDEIterationStrategy
     */
    public KIndependentCooperativeDynDEIterationStrategy() {
        super();
        exclusionRadius = 5.0;
        context = new ClusterIndividual();
        exclusionRadius = 3.0;
        measure = new EuclideanDistanceMeasure();
        updateStrategyForWeakestIndividuals = new BrownianClusteringUpdateStrategy();
        totalReplaceableIndividuals = 5;
        previousInterCluster = 0;
        previousRayTuri = Double.POSITIVE_INFINITY;
        previousIntraCluster = Double.POSITIVE_INFINITY;
        populationHasConverged = true;
        silhouetteThreshold = 0.7;
        convergenceRadius = 2.0;
    }
    
    /*
     * Copy constructor for CooperativeDynDEIterationStrategy
     * @param copy The CooperativeDynDEIterationStrategy to be copied
     */
    public KIndependentCooperativeDynDEIterationStrategy(KIndependentCooperativeDynDEIterationStrategy copy) {
        this.context = copy.context;
        exclusionRadius = copy.exclusionRadius;
        measure = copy.measure;
        updateStrategyForWeakestIndividuals = copy.updateStrategyForWeakestIndividuals;
        totalReplaceableIndividuals = copy.totalReplaceableIndividuals;
        previousInterCluster = copy.previousInterCluster;
        previousIntraCluster = copy.previousIntraCluster;
        previousRayTuri = copy.previousRayTuri;
        populationHasConverged = copy.populationHasConverged;
        silhouetteThreshold = copy.silhouetteThreshold;
        convergenceRadius = copy.convergenceRadius;
    }
    
    /*
     * Clone method for CooperativeDynDEIterationStrategy
     * @return A new instance of this CooperativeDynDEIterationStrategy
     */
    @Override
    public KIndependentCooperativeDynDEIterationStrategy getClone() {
        return new KIndependentCooperativeDynDEIterationStrategy(this);
    }
    
    /*
     * Performs the iteration of a cooperative dynamic DE. First the context is updated, then the populations
     * are given the context and then the populations are processed.
     * @param algorithm The multi-population algorithm currently running
     */
    @Override
    public void performIteration(StandardMultipopulationAlgorithm algorithm) {
        evaluatePopulations(algorithm.getPopulations());
        createContext(algorithm.getPopulations());
        contextualisePopulations(algorithm.getPopulations());
        evaluatePopulations(algorithm.getPopulations());
        processPopulations(algorithm);
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
    
    protected void updatePopulations(PopulationBasedAlgorithm algorithm, int algIndx, List<PopulationBasedAlgorithm> populationList) {   
        performIteration((SinglePopulationBasedAlgorithm) algorithm, algIndx);
        
        createContext(populationList);
       
        //with new context
        contextualisePopulation(algorithm, algIndx);
        evaluatePopulation(algorithm);

        updateWeakest((SinglePopulationBasedAlgorithm) algorithm);

        //brownian individuals changed whole individual
        contextualisePopulation(algorithm, algIndx);
        
    }
    
    private void evaluatePopulation(PopulationBasedAlgorithm algorithm) {
        
        for(Entity individual : algorithm.getTopology()) {
            assignDataPatternsToEntity((CentroidHolder) individual.getCandidateSolution(), ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) algorithm).getIterationStrategy()).getWindow().getCurrentDataset());
            individual.calculateFitness();
        }
    }
    
    protected boolean aDistanceIsSmallerThanRadius(CentroidHolder currentPosition, int currentIndex, CentroidHolder otherPosition, int otherIndex) {
            if(measure.distance(currentPosition.get(currentIndex).toVector(), otherPosition.get(otherIndex).toVector()) < convergenceRadius) {
                return true;
            }
            
            return false;
    }
    
    private void performIteration(SinglePopulationBasedAlgorithm algorithm, int algIndex) {
        Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) algorithm.getTopology();
        List<ClusterIndividual> newTopology = Lists.newArrayList();
        int index = 0;
        SinglePopulationDataClusteringDEIterationStrategy iterationStrategy = (SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) algorithm).getIterationStrategy();
        
        for(ClusterIndividual individual : topology) {
            CentroidHolder candidateSolution = (CentroidHolder) individual.getCandidateSolution();
            
            iterationStrategy.assignDataPatternsToIndividual(candidateSolution, iterationStrategy.getDataset());
            individual.setCandidateSolution(candidateSolution);
            individual.calculateFitness();
          
            ClusterIndividual offspring;

            offspring = (ClusterIndividual) individual.getUpdateStrategy().update(individual, algorithm);

            boundaryConstraint.enforce(offspring);
            contextualiseIndividual(offspring, algIndex);//
            
            iterationStrategy.assignDataPatternsToIndividual((CentroidHolder) offspring.getCandidateSolution(), iterationStrategy.getDataset());
            offspring.calculateFitness();
            
            if (offspring.getFitness().compareTo(individual.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(index, offspring);
            }
            
            index++;
        }
        
        iterationStrategy.setDataset(iterationStrategy.getWindow().slideWindow());
    }
    
    protected PopulationBasedAlgorithm addPopulation(StandardMultipopulationAlgorithm mpAlgorithm) {
        
        List<PopulationBasedAlgorithm> populationList = mpAlgorithm.getPopulations();
        PopulationBasedAlgorithm newAlgorithm = populationList.get(0).getClone();
        populationList.add(newAlgorithm);
        
        int indexToReplace = getIndex(newAlgorithm, populationList);
        int dimension = ((CentroidHolder) newAlgorithm.getTopology().get(0).getCandidateSolution()).get(0).size();
        
        for(ClusterIndividual individual : (Topology<ClusterIndividual>) newAlgorithm.getTopology()) {
             while(individual.getCandidateSolution().size() < populationList.size()) {
                
                individual.getCandidateSolution().add(new ClusterCentroid(dimension));
                individual.reinitialise();
                contextualiseIndividual(individual, indexToReplace);
             }
        }
        
        evaluatePopulation(newAlgorithm);
        
        ClusterIndividual newAlgorithmsBest = (ClusterIndividual) Topologies.getBestEntity(newAlgorithm.getTopology());
        
        for(PopulationBasedAlgorithm alg : populationList) {
            if(!alg.equals(newAlgorithm)) {
            //add new dimension to solution which this population will be optimising
                for(ClusterIndividual individual : (Topology<ClusterIndividual>) alg.getTopology()) {
                    if (individual.getCandidateSolution().size() < populationList.size()) {
                        individual.getCandidateSolution().add(new ClusterCentroid(dimension));
                        ((CentroidHolder) individual.getCandidateSolution()).set(indexToReplace, ((CentroidHolder) newAlgorithmsBest.getCandidateSolution()).get(indexToReplace));
                    }

                    if(((CentroidHolder) alg.getBestSolution().getPosition()).size() < populationList.size()) {
                        Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) alg.getTopology();
                        ClusterIndividual best = (ClusterIndividual) Topologies.getBestEntity(topology);

                        best.getCandidateSolution().add(new ClusterCentroid(dimension));
                        ((CentroidHolder) best.getCandidateSolution()).set(indexToReplace, ((CentroidHolder) newAlgorithmsBest.getCandidateSolution()).get(indexToReplace));
                    }
                }
            }
        }
        
        ClusteringProblem clusteringProblem = (ClusteringProblem) mpAlgorithm.getOptimisationProblem();
        clusteringProblem.setNumberOfClusters(clusteringProblem.getNumberOfClusters() + 1);
        
        context.getCandidateSolution().add(new ClusterCentroid(dimension));
        ((CentroidHolder) context.getCandidateSolution()).set(indexToReplace, ((CentroidHolder) newAlgorithmsBest.getCandidateSolution()).get(indexToReplace));
        
        return populationList.get(indexToReplace);
    }
    
    protected void mergePopulations(PopulationBasedAlgorithm algorithm1, PopulationBasedAlgorithm algorithm2, StandardMultipopulationAlgorithm mpAlgorithm) {
            
            List<PopulationBasedAlgorithm> populationList = mpAlgorithm.getPopulations();
            PopulationBasedAlgorithm keptAlgorithm;
            List<ClusterIndividual> halfAlgorithm1 = getPartOfPopulation((Topology<ClusterIndividual>) algorithm1.getTopology());
            List<ClusterIndividual> halfAlgorithm2 = getPartOfPopulation((Topology<ClusterIndividual>) algorithm2.getTopology());
            //topology1.addAll(topology2);
            
            int index;
            if(algorithm1.getBestSolution().getFitness().compareTo(algorithm2.getBestSolution().getFitness()) > 0) {
                index = getIndex(algorithm2, populationList);
                populationList.remove(algorithm2);
                keptAlgorithm = algorithm1;
            } else {
                index = getIndex(algorithm1, populationList);
                populationList.remove(algorithm1);
                keptAlgorithm = algorithm2;
            }
            
            keptAlgorithm.getTopology().clear();
            halfAlgorithm1.addAll(halfAlgorithm2);
            ((Topology<ClusterIndividual>) keptAlgorithm.getTopology()).addAll(halfAlgorithm1);
            
            for(PopulationBasedAlgorithm alg : populationList) {
                //add new dimension to solution which this population will be optimising
                for(ClusterIndividual individual : (Topology<ClusterIndividual>) alg.getTopology()) {
                    if(individual.getCandidateSolution().size() > populationList.size()) {
                        ClusterCentroid centroid = ((CentroidHolder) individual.getCandidateSolution()).get(index);
                        individual.getCandidateSolution().remove(centroid);
                    }
                }
                
                Topology<ClusterIndividual> topology = (Topology<ClusterIndividual>) alg.getTopology();

                if(((CentroidHolder) alg.getBestSolution().getPosition()).size() > populationList.size()) {
                    ClusterCentroid centroid = ((CentroidHolder) ((ClusterIndividual) Topologies.getBestEntity(topology)).getCandidateSolution()).get(index);
                    ((ClusterIndividual) Topologies.getBestEntity(topology)).getCandidateSolution().remove(centroid);
                }
            }
                       
            evaluatePopulation(keptAlgorithm);
            
            ClusteringProblem clusteringProblem = (ClusteringProblem) mpAlgorithm.getOptimisationProblem();
            clusteringProblem.setNumberOfClusters(clusteringProblem.getNumberOfClusters() - 1);
                    
            createContext(mpAlgorithm.getPopulations());
            contextualisePopulations(mpAlgorithm.getPopulations());
            evaluatePopulations(mpAlgorithm.getPopulations());
    }
    
    /*
     * Assigns data patterns to all centroids of each population and determines the
     * fitness of each individual using these.
     * @param The multipopulation algorithm holding all the populations to be
     * evaluated
     */
    protected void evaluatePopulations(List<PopulationBasedAlgorithm> populations) {
        for(PopulationBasedAlgorithm alg : populations) {
            evaluatePopulation(alg);
        }
        
    }
        
    protected int getIndex(PopulationBasedAlgorithm algorithm, List<PopulationBasedAlgorithm> list) {
        int index = 0;
        for(PopulationBasedAlgorithm alg: list) {
            if(algorithm.equals(alg)) {
                return index;
            }
            index++;
        }
        
        return -1;
    } 
    
     protected List<ClusterIndividual> getPartOfPopulation(Topology<ClusterIndividual> topology) {
        MersenneTwister random = new MersenneTwister();
        List<ClusterIndividual> list = Lists.newArrayList();
        Topology<ClusterIndividual> remainder = topology.getClone();
        
        while(list.size() < topology.size() / 2) {
            int index = random.nextInt(remainder.size());
            list.add(remainder.get(index));
            remainder.remove(index);
        }
        
        return list;
    }
    
    /*
     * Determines which populations must be reinitialised and for which
     * populations an iteration of the clustering algorithm must take place.
     * @param populations The populations being dealt with
     */
    protected void processPopulations(StandardMultipopulationAlgorithm mpAlgorithm) {
        List<PopulationBasedAlgorithm> populationList = mpAlgorithm.getPopulations();
        //ArrayList<ClusterOperation> listOfOperations = new ArrayList<ClusterOperation>();
                
        InterClusterDistance inter = new InterClusterDistance();
        double interClusterDistance = inter.getValue(mpAlgorithm).doubleValue();
        
        IntraClusterDistance intra = new IntraClusterDistance();
        double intraClusterDistance = intra.getValue(mpAlgorithm).doubleValue();
        
        double silhouette = 0;
        
        boolean converging = populationsAreStagnating(mpAlgorithm);
        
        if(converging) {
           ValidityIndex validityIndex = new SilhouetteCoefficient();
           silhouette = validityIndex.getValue(mpAlgorithm).doubleValue();
           
        }
        
        boolean goodSilhouette = (silhouette > silhouetteThreshold) ? true: false;
        
        boolean intraWorse = intraClusterDistance > previousIntraCluster && interClusterDistance > previousInterCluster && converging && !goodSilhouette;
        boolean allBetter = interClusterDistance > previousInterCluster && intraClusterDistance < previousIntraCluster && converging && !goodSilhouette;
        boolean allWorse = interClusterDistance < previousInterCluster && intraClusterDistance > previousIntraCluster && converging && !goodSilhouette;
                
        if(populationList.size() >= 2) {
            PopulationBasedAlgorithm weakestPopulation1 = populationList.get(0);
            PopulationBasedAlgorithm weakestPopulation2 = populationList.get(1);
            double smallestDistance = getDistance((CentroidHolder) weakestPopulation1.getBestSolution().getPosition(), 0, (CentroidHolder) weakestPopulation2.getBestSolution().getPosition(), 1);
            int index = 0;
            
                for(PopulationBasedAlgorithm algorithm : populationList) {  
                    int populationIndex = 0;
                        for(PopulationBasedAlgorithm otherAlgorithm : populationList) {
                                if(!algorithm.equals(otherAlgorithm)) {
                                   double newDistance = getDistance((CentroidHolder) algorithm.getBestSolution().getPosition(), index, (CentroidHolder) otherAlgorithm.getBestSolution().getPosition(), populationIndex);
                                   if(newDistance < smallestDistance) {
                                       smallestDistance = newDistance;
                                       weakestPopulation1 = algorithm;
                                       weakestPopulation2 = otherAlgorithm;
                                   }
                                 
                                }

                                populationIndex++;
                        }

                        index++;
                }
                
                if(smallestDistance < exclusionRadius) {
                    mergePopulations(weakestPopulation1, weakestPopulation2, mpAlgorithm);
                }
                    
        }
        
        if(allBetter || allWorse || intraWorse) {
            addPopulation(mpAlgorithm);
        } 
        
        if(converging) {
            previousInterCluster = interClusterDistance;
            previousIntraCluster = intraClusterDistance;
        }
        
        int index = 0;
        for(PopulationBasedAlgorithm algorithm : populationList) {
            updatePopulations(algorithm, index, populationList);
            index++;
        }
        
    }
    
    boolean isConverged(PopulationBasedAlgorithm algorithm, MultiPopulationBasedAlgorithm ca) {
        int populationIndex = getIndex(algorithm, ca.getPopulations());
        int convergedCounter = 0;
        
        DistanceMeasure dm = new EuclideanDistanceMeasure();
        CentroidHolder best = (CentroidHolder) algorithm.getBestSolution().getPosition();
        
        for(ClusterIndividual individual : (Topology<ClusterIndividual>) algorithm.getTopology()) {
            if(aDistanceIsSmallerThanRadius(best, populationIndex, (CentroidHolder) individual.getCandidateSolution(), populationIndex)) {
                convergedCounter++;
            }
        }
        
        if(convergedCounter == algorithm.getTopology().size()) {
            return true;
        }
        
        return false;
    }
    
    private boolean populationsAreStagnating(StandardMultipopulationAlgorithm mpAlgorithm) {
        int converged = 0;
        for (PopulationBasedAlgorithm current : mpAlgorithm.getPopulations()) {
            if (isConverged(current, mpAlgorithm)) {
                converged++;
            }
        }
        
        if(converged == mpAlgorithm.getPopulations().size()) {
            populationHasConverged = true;
            return true;
        }
        
        return false;
    }
    
    /*
     * Updates the weakest individuals to become Brownian Individuals
     * @param algorithm The algorithm whose individuals must be updated
     */
    protected void updateWeakest(SinglePopulationBasedAlgorithm algorithm) {
        Topology<Individual> topology = (Topology<Individual>) algorithm.getTopology();
        int weakest;
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
    
    /*
     * Checks if an array contains a value
     * @param array The array to be checked
     * @param value The value yo ve checked for
     */
    protected boolean contains(int[] array, int value) {
        for(int val : array) {
            if(val == value) {
                return true;
            }
        }
        return false;
    }
        
    /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    protected void assignDataPatternsToEntity(CentroidHolder candidateSolution, DataTable dataset) {
        for(ClusterCentroid centroid : candidateSolution) {
            centroid.clearDataItems();
        }
        
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
   
    protected double getDistance(CentroidHolder currentPosition, int currentIndex, CentroidHolder otherPosition, int otherIndex) {
        return measure.distance(currentPosition.get(currentIndex).toVector(), otherPosition.get(otherIndex).toVector());
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

    public double getSilhouetteThreshold() {
        return silhouetteThreshold;
    }

    public void setSilhouetteThreshold(double silhouetteThreshold) {
        this.silhouetteThreshold = silhouetteThreshold;
    }

    public double getConvergenceRadius() {
        return convergenceRadius;
    }

    public void setConvergenceRadius(double convergenceRadius) {
        this.convergenceRadius = convergenceRadius;
    }
    
}
