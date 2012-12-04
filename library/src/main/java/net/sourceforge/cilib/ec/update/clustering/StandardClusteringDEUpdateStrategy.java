/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update.clustering;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Standard DE update strategy where the individual receied is updated using the 
 * normal DE operators.
 * This one is specific to clustering problems as it deals with CentroidHolders
 */
public class StandardClusteringDEUpdateStrategy implements UpdateStrategy{
    private Selector targetVectorSelectionStrategy;
    private CreationStrategy trialVectorCreationStrategy;
    private CrossoverStrategy crossoverStrategy;
    
    /*
     * Default constructor for StandardClusteringDEUpdateStrategy
     */
    public StandardClusteringDEUpdateStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }
    
    /*
     * Copy constructor for StandardClusteringDEUpdateStrategy
     * @param copy The StandardClusteringDEUpdateStrategy to be copied
     */
    public StandardClusteringDEUpdateStrategy(StandardClusteringDEUpdateStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }
    
    /*
     * Clone method for StandardClusteringDEUpdateStrategy
     * @return A new instance of this StandardClusteringDEUpdateStrategy
     */
    public StandardClusteringDEUpdateStrategy getClone() {
        return new StandardClusteringDEUpdateStrategy(this);
    }
    
    /*
     * Updates the parameter using the standard DE operators
     */
    public Entity update(Entity entity, Topology topology) {
        ClusterIndividual currentEntity = (ClusterIndividual) entity;
        ClusterIndividual targetEntity = (ClusterIndividual) targetVectorSelectionStrategy.on(topology).exclude(currentEntity).select();
            
        ClusterIndividual trialEntity = getTrialEntity(targetEntity, currentEntity, topology);
            
        ClusterIndividual offspring = getOffspring(currentEntity, trialEntity);
        
        return offspring;
    }
    
    protected ClusterIndividual getTrialEntity(ClusterIndividual targetEntity, ClusterIndividual current, Topology<ClusterIndividual> topology) {
        Entity trialEntity;
        CentroidHolder holder = new CentroidHolder();
        Individual tempTarget;
        Individual tempCurrent;
        Topology tempTopology;
        int index = 0;
        Individual tempIndiv;
        
        for(ClusterCentroid centroid : (CentroidHolder) current.getCandidateSolution()) {
            tempTopology = new GBestTopology<Individual>();
            for(ClusterIndividual cindiv : topology) {
                tempIndiv = new Individual();
                tempIndiv.setCandidateSolution(((CentroidHolder) cindiv.getCandidateSolution()).get(index).toVector());
                tempIndiv.getProperties().put(EntityType.FITNESS, cindiv.getFitness());
                tempTopology.add(tempIndiv.getClone());
            }
            
            tempCurrent = new Individual();
            tempCurrent.setCandidateSolution(centroid.toVector());
            
            tempTarget = new Individual();
            tempTarget.setCandidateSolution(((CentroidHolder) targetEntity.getCandidateSolution()).get(index).toVector());
            
            trialEntity = trialVectorCreationStrategy.create(tempTarget, tempCurrent, tempTopology);
            ClusterCentroid c = new ClusterCentroid();
            c.addAll(trialEntity.getCandidateSolution());
            holder.add(c);
            index++;
        }
        ClusterIndividual trialIndividual = current.getClone();
        trialIndividual.setCandidateSolution(holder);
        
        return trialIndividual;
    }
    
    protected ClusterIndividual getOffspring(ClusterIndividual current, ClusterIndividual trialEntity) {
        CentroidHolder holder = new CentroidHolder();
        List<Entity> centroidOffspring;
        Entity currentCentroid;
        Entity trialCentroid;
        ClusterIndividual offspring = current.getClone();
        
        for(int i = 0; i < current.getCandidateSolution().size(); i++) {
            currentCentroid = new Individual();
            currentCentroid.setCandidateSolution(((CentroidHolder) current.getCandidateSolution()).get(i).toVector());
            
            trialCentroid = new Individual();
            trialCentroid.setCandidateSolution(((CentroidHolder) trialEntity.getCandidateSolution()).get(i).toVector());
            
            centroidOffspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(currentCentroid, trialCentroid));
            
            ClusterCentroid c = new ClusterCentroid();
            c.addAll(centroidOffspring.get(0).getCandidateSolution());
            holder.add(c);
        }
        
        offspring.setCandidateSolution(holder);
        
        return offspring;
    }
    
}
