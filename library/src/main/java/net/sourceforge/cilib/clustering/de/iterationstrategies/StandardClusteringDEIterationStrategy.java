/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 *
 * @author Kris
 */
public class StandardClusteringDEIterationStrategy extends SinglePopulationDataClusteringDEIterationStrategy{
    private Selector targetVectorSelectionStrategy; // x
    private CreationStrategy trialVectorCreationStrategy; // y
    private CrossoverStrategy crossoverStrategy; // z
    
    public StandardClusteringDEIterationStrategy() {
        super();
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }
    
    public StandardClusteringDEIterationStrategy(StandardClusteringDEIterationStrategy copy) {
        super(copy);
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
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
            
            ClusterIndividual targetEntity = (ClusterIndividual) targetVectorSelectionStrategy.on(topology).exclude(individual).select();
            
            ClusterIndividual trialEntity = getTrialEntity(targetEntity, individual, topology);
            
            ClusterIndividual offspring = getOffspring(individual, trialEntity);
            
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

