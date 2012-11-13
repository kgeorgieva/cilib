/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.initialization.DataDependantInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
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
 *
 * @author Kris
 */
public class ClusterIndividual extends Individual implements ClusterEntity{
    private int numberOfClusters;
    private DataDependantInitializationStrategy centroidInitialisationStrategy;
    
    /**
     * Create an instance of {@linkplain Individual}.
     */
    public ClusterIndividual() {
        super();
        numberOfClusters = 1;
        centroidInitialisationStrategy = new StandardCentroidInitializationStrategy();
    }

    /**
     * Copy constructor. Creates a copy of the given {@linkplain Individual}.
     * @param copy The {@linkplain Individual} to copy.
     */
    public ClusterIndividual(ClusterIndividual copy) {
        super(copy);
        numberOfClusters = copy.numberOfClusters;
        centroidInitialisationStrategy = copy.centroidInitialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusterIndividual getClone() {
        return new ClusterIndividual(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        return hash;
    }

    /**
     * Resets the fitness to <code>InferiorFitness</code>.
     */
    public void resetFitness() {
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
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
    public int compareTo(Entity o) {
        return this.getFitness().compareTo(o.getFitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCandidateSolution(StructuredType type) {
        super.setCandidateSolution(type);
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
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return getCandidateSolution().size();
    }

    /**
     * Create a textual representation of the current {@linkplain Individual}. The
     * returned {@linkplain String} will contain both the genotypes and penotypes for
     * the current {@linkplain Individual}.
     * @return The textual representation of this {@linkplain Individual}.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getCandidateSolution().toString());
        str.append(getProperties().get(EntityType.STRATEGY_PARAMETERS));
        return str.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        throw new UnsupportedOperationException("Implementation is required for this method");
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    public DataDependantInitializationStrategy getCentroidInitialisationStrategy() {
        return centroidInitialisationStrategy;
    }

    public void setCentroidInitialisationStrategy(DataDependantInitializationStrategy centroidInitialisationStrategy) {
        this.centroidInitialisationStrategy = centroidInitialisationStrategy;
    }
    
    
}
