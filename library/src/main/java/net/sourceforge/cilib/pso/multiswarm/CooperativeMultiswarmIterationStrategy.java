/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.multiswarm;

import java.util.List;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.pso.iterationstrategies.SinglePopulationDataClusteringPSOIterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.entity.Topology;

/**
 *
 */
public class CooperativeMultiswarmIterationStrategy extends AbstractCooperativeIterationStrategy<MultiPopulationBasedAlgorithm>{
    StandardClusteringMultiSwarmIterationStrategy delegate;
    private ControlParameter exclusionRadius = new StandardUpdatableControlParameter(2.0);
    
    /*
     * Default constructor for CooperativeMultiswarmIterationStrategy
     */
    public CooperativeMultiswarmIterationStrategy() {
        super();
        delegate = new StandardClusteringMultiSwarmIterationStrategy();
    }
    
    /*
     * Copy constructor for CooperativeMultiswarmIterationStrategy
     * @param copy The CooperativeMultiswarmIterationStrategy to be copied
     */
    public CooperativeMultiswarmIterationStrategy(CooperativeMultiswarmIterationStrategy copy) {
        super(copy);
        delegate = copy.delegate;
    }
    
    /*
     * Clone method for CooperativeMultiswarmIterationStrategy
     * @return new instance of the CooperativeMultiswarmIterationStrategy
     */
    @Override
    public CooperativeMultiswarmIterationStrategy getClone() {
        return new CooperativeMultiswarmIterationStrategy(this);
    }
    
    /*
     * Updates the popualations to contain the context
     * @param multipopAlgorithm The multi-population algorithm currently running
     */
    public void contextualisePopulation(PopulationBasedAlgorithm algorithm, int indx) {
        
        for(ClusterParticle indiv : (Topology<ClusterParticle>) algorithm.getTopology()) {
            int centroidIndex = 0;
            for(ClusterCentroid centroid : ((CentroidHolder) contextParticle.getCandidateSolution())) {
                if(centroidIndex != indx) {
                    ((CentroidHolder) indiv.getCandidateSolution()).set(centroidIndex, centroid.getClone());
                } 

                centroidIndex++;
            }
            
            assignDataPatternsToParticle((CentroidHolder) indiv.getCandidateSolution(), table);
            indiv.calculateFitness();
            
        } 

    }
    
    public void contextualiseParticle(ClusterParticle particle, int indx) {
        int centroidIndex = 0;
        for(ClusterCentroid centroid : ((CentroidHolder) contextParticle.getCandidateSolution())) {
            if(centroidIndex != indx) {
                ((CentroidHolder) particle.getCandidateSolution()).set(centroidIndex, centroid.getClone());
            } 

            centroidIndex++;
        }
    }
    
    public ClusterParticle createContext(List<PopulationBasedAlgorithm> populations) {
        ClusterParticle tempContextParticle = (ClusterParticle) populations.get(0).getTopology().get(0).getClone();
        int index = 0;
        
        for(PopulationBasedAlgorithm algorithm : (List<PopulationBasedAlgorithm>) populations) {
               ((CentroidHolder) tempContextParticle.getCandidateSolution()).set(index, (ClusterCentroid) ((CentroidHolder) algorithm.getBestSolution().getPosition()).get(index).getClone());
                index++;
        }
        
        for(ClusterCentroid centroid : (CentroidHolder) tempContextParticle.getCandidateSolution()) {
            centroid.clearDataItems();
        }
        
        
        assignDataPatternsToParticle((CentroidHolder) tempContextParticle.getCandidateSolution(), table);
        tempContextParticle.calculateFitness();
        
        if(contextParticle.getFitness() != null) {
            if(elitist) {
                if(tempContextParticle.getFitness().compareTo(contextParticle.getFitness()) > 0) {
                    //System.out.println(tempContextParticle.getFitness().getValue() + " " + contextParticle.getFitness().getValue());
                    contextParticle = tempContextParticle.getClone();
                }
            } else {
                contextParticle = tempContextParticle.getClone();
            }
        } else {
            contextParticle = tempContextParticle.getClone();
        }
        
        return contextParticle;
    }
    
    /*
     * Performs an iteration of the Cooperative Multiswarm Iteration Strategy
     * It handles the context particle.
     * It assigns the context to all particles with the appropriate dimension difference.
     * It updates personala nd global best values.
     * It uses a multi-swarm iteration strategy on the different swarms.
     * @param algorithm The multi-population algorithm whose swarms must be treated as 
     * co-operative swarms.
     */
    @Override
    public void performIteration(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;
        table = ((SinglePopulationDataClusteringPSOIterationStrategy) ((DataClusteringPSO) algorithm.getPopulations().get(0)).getIterationStrategy()).getDataset();
        createContext(algorithm.getPopulations());
                
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
                
                if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
                    contextualisePopulation(currentAlgorithm, populationIndex);
                    populationIndex++;
                }
                
                //iteration strategy does this already
                
//                for(ClusterParticle particle : currentAlgorithm) {
//                    //update personal best
//                    if(particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
//                        particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
//                        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
//                    } 
//                    
//                    //update global best for sub population
//                    if(particle.getFitness().compareTo(currentAlgorithm.getBestSolution().getBestFitness()) > 0) {
//                        particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
//                        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness().getClone());
//                    } 
//                }
                
//                if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
//                    for(ClusterParticle particle : ((DataClusteringPSO) currentAlgorithm).getTopology()) {
//                        clearDataPatterns(contextParticle);
//                        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
//                        contextParticle.calculateFitness();
//                        
//                        ClusterParticle particleWithContext = new ClusterParticle();
//                        particleWithContext.setCandidateSolution(contextParticle.getCandidateSolution().getClone());
//                        particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getBestPosition().getClone());
//                        particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getBestFitness().getClone());
//                        particleWithContext.getProperties().put(EntityType.Particle.VELOCITY, particle.getVelocity().getClone());
//                        particleWithContext.setNeighbourhoodBest(contextParticle);
//                        ((CentroidHolder) particleWithContext.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particle.getCandidateSolution()).get(populationIndex).getClone());
//                        particleWithContext.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER).getClone());
//                        particleWithContext.setCentroidInitialisationStrategy(particle.getCentroidInitializationStrategyCandidate());
//
//                        clearDataPatterns(particleWithContext);
//                        assignDataPatternsToParticle((CentroidHolder) particleWithContext.getCandidateSolution(), table);
//                        particleWithContext.calculateFitness();
//
//                        if(particleWithContext.getFitness().compareTo(particleWithContext.getBestFitness()) > 0) {
//                            particleWithContext.getProperties().put(EntityType.Particle.BEST_POSITION, particleWithContext.getPosition()).getClone();
//                            particleWithContext.getProperties().put(EntityType.Particle.BEST_FITNESS, particleWithContext.getFitness()).getClone();
//                        }
//
//                        if(particleWithContext.getBestFitness().compareTo(contextParticle.getFitness()) > 0) {
//                               ((CentroidHolder) contextParticle.getCandidateSolution()).set(populationIndex, ((CentroidHolder) particleWithContext.getCandidateSolution()).get(populationIndex).getClone());
//                        }
//                        
//                        if(contextParticle.getFitness().compareTo(contextParticle.getBestFitness()) > 0) {
//                            contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, contextParticle.getPosition()).getClone();
//                            contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, contextParticle.getFitness()).getClone();
//                        }
//
//                        particle = particleWithContext.getClone();
//
//                    }
//
//                }
                                 
        }
        
//        if(elitist) {
//            contextParticle.getProperties().put(EntityType.CANDIDATE_SOLUTION, contextParticle.getBestPosition().getClone());
//            contextParticle.getProperties().put(EntityType.FITNESS, contextParticle.getBestFitness().getClone());
//        }
                            
        StandardMultipopulationAlgorithm multiswarm = convertCooperativePSOToMultiswarm(algorithm);
        delegate.setExclusionRadius(exclusionRadius);
        delegate.processPopulations(multiswarm);
        convertMultiswarmToCooperative(multiswarm, algorithm);
        
        populationIndex = 0;
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
            if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
                contextualisePopulation(currentAlgorithm, populationIndex);
                populationIndex++;
            }
        }
        
        createContext(algorithm.getPopulations());
        
        populationIndex = 0;
        for(PopulationBasedAlgorithm currentAlgorithm : algorithm.getPopulations()) {
            if(!((DataClusteringPSO) currentAlgorithm).isExplorer()) {
                contextualisePopulation(currentAlgorithm, populationIndex);
                populationIndex++;
            }
        }
        
        multiswarm = convertCooperativePSOToMultiswarm(algorithm);
        delegate.setExclusionRadius(exclusionRadius);
        delegate.repulsePopulations(multiswarm);
        convertMultiswarmToCooperative(multiswarm, algorithm);    
            
    }
    
    /*
     * Assigns the current algorithm's topology to a multi-swarm in order to use the 
     * multi-swarm iteration strategy needed.
     * @param algorithm The algorithm to be converted
     * @return multiswarm The new multi-swarm algorithm holding the topology of algorithm
     */
    private StandardMultipopulationAlgorithm convertCooperativePSOToMultiswarm(MultiPopulationBasedAlgorithm algorithm) {
        StandardMultipopulationAlgorithm multiSwarm = new StandardMultipopulationAlgorithm();
        
        multiSwarm.setPopulations(algorithm.getPopulations());
        multiSwarm.setOptimisationProblem(algorithm.getOptimisationProblem());
        
        return multiSwarm;
    }
    
    /*
     * Sets the topology of the multi-swarm received to be held by the current algorithm
     * @param multiswarm The multi-swarm with the new topology
     * @param algorithm The current algorith to which the multi-swarm's topology will be assigned.
     */
    private void convertMultiswarmToCooperative(StandardMultipopulationAlgorithm multiswarm, MultiPopulationBasedAlgorithm algorithm) {
        algorithm.setPopulations(multiswarm.getPopulations());
    }
    
    /*
     * Sets the delegate iteration strategy to the one received as a parameter
     * @param newDelegate The new delegate iteration strategy
     */
    public void setDelegate(StandardClusteringMultiSwarmIterationStrategy newDelegate) {
        delegate = newDelegate;
    }
    
    /*
     * Returns the delegate iteration strategy
     * @return delegate The delegate iteration strategy
     */
    public IterationStrategy getDelegate() {
        return delegate;
    }

    public ControlParameter getExclusionRadius() {
        return exclusionRadius;
    }

    public void setExclusionRadius(ControlParameter exclusionRadius) {
        this.exclusionRadius = exclusionRadius;
    }
    
    
}
