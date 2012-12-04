/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.entity.initialization.DataDependantInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.EntityBasedFitnessCalculator;


/**
 * An individual that holds a ClusterHolder as a candidate solution and 
 * performs all tasks of an individual on this particular structure.
 */
public class ClusterIndividual extends Individual implements ClusterEntity{
    private int numberOfClusters;
    private DataDependantInitializationStrategy centroidInitialisationStrategy;
    private UpdateStrategy updateStrategy;
    
    /**
     * Create an instance of {@linkplain ClusterIndividual}.
     */
    public ClusterIndividual() {
        super();
        numberOfClusters = 1;
        centroidInitialisationStrategy = new StandardCentroidInitializationStrategy();
        updateStrategy = new StandardClusteringDEUpdateStrategy();
    }

    /**
     * Copy constructor. Creates a copy of the given {@linkplain ClusterIndividual}.
     * @param copy The {@linkplain ClusterIndividual} to copy.
     */
    public ClusterIndividual(ClusterIndividual copy) {
        super(copy);
        numberOfClusters = copy.numberOfClusters;
        centroidInitialisationStrategy = copy.centroidInitialisationStrategy;
        updateStrategy = copy.updateStrategy;
    }

    /**
     * Clone method for ClusterIndividual
     * @return A new instance of this ClusterIndividual
     */
    @Override
    public ClusterIndividual getClone() {
        return new ClusterIndividual(this);
    }

    /**
     * Initialises the ClusterIndividual using the centroid initialisation strategy
     * @param problem The clustering problem
     */
    @Override
    public void initialise(Problem problem) {
        
        numberOfClusters = ((ClusteringProblem) problem).getNumberOfClusters();
        
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, new CentroidHolder(numberOfClusters, problem.getDomain().getDimension()));
        
        //if(centroidInitialisationStrategy instanceof StandardCentroidInitializationStrategy)
        centroidInitialisationStrategy.setInitialisationStrategy(super.initialisationStrategy);
        
        centroidInitialisationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        
        Vector strategy = Vector.fill(0.0, this.getCandidateSolution().size());
        
        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, net.sourceforge.cilib.problem.solution.InferiorFitness.instance());
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        EntityBasedFitnessCalculator f = new EntityBasedFitnessCalculator();
        Fitness fitness = f.getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);
    }

    /**
     * Reinitialises the ClusterIndividual using the centroidInitialisationStrategy
     */
    @Override
    public void reinitialise() {
        this.centroidInitialisationStrategy.reinitialize(EntityType.CANDIDATE_SOLUTION, this);
        
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /*
     * Gets the total number of clusters
     * @return The total number of clusters
     */
    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    /*
     * Sets the total number of clusters
     * @param numberOfClusters The new number of clusters
     */
    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    /*
     * Gets the centroid initialisation strategy
     * @return The centroid initialisation strategy
     */
    public DataDependantInitializationStrategy getCentroidInitialisationStrategy() {
        return centroidInitialisationStrategy;
    }

    /*
     * Sets the centroid initialisation strategy
     * @param The new centroid initialisation strategy
     */
    public void setCentroidInitialisationStrategy(DataDependantInitializationStrategy centroidInitialisationStrategy) {
        this.centroidInitialisationStrategy = centroidInitialisationStrategy;
    }

    /*
     * Gets the update strategy that this individual is updated with
     * @return The update strategy 
     */
    public UpdateStrategy getUpdateStrategy() {
        return updateStrategy;
    }

    /*
     * Sets the update strategy to the one received asa  parameter
     * @param updateStrategy The new update strategy
     */
    public void setUpdateStrategy(UpdateStrategy updateStrategy) {
        this.updateStrategy = updateStrategy;
    }
    
}
