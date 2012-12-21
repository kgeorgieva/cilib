/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.util.changeDetection.ChangeDetectionStrategy;
import net.sourceforge.cilib.util.changeDetection.IterationBasedChangeDetectionStrategy;

/**
 *
 * @author Kris
 */
public class ReinitialisingClusteringDEIterationStrategy extends SinglePopulationDataClusteringDEIterationStrategy{
    private SinglePopulationDataClusteringDEIterationStrategy delegate;
    private ChangeDetectionStrategy changeDetectionStrategy;
    
    /*
     * Default constructor for ReinitialisingClusteringDEIterationStrategy
     */
    public ReinitialisingClusteringDEIterationStrategy() {
        super();
        delegate = new StandardClusteringDEIterationStrategy();
        changeDetectionStrategy = new IterationBasedChangeDetectionStrategy();
    }
    
    /*
     * Copy constructor for ReinitialisingClusteringDEIterationStrategy
     */
    public ReinitialisingClusteringDEIterationStrategy(ReinitialisingClusteringDEIterationStrategy copy) {
        super(copy);
        delegate = copy.delegate;
        changeDetectionStrategy = copy.changeDetectionStrategy;
    }
    
    /*
     * Clone method for ReinitializingDataClusteringDEIterationStrategy
     */
    @Override
    public ReinitialisingClusteringDEIterationStrategy getClone() {
        return new ReinitialisingClusteringDEIterationStrategy(this);
    }

    /*
     * Performs an iteration of it's delegate iteration startegy (by default the StandardClusteringDEIterationStrategy).
     * Reinitializes part of, or the whole, population if a change has taken place.
     * @param algorithm The algorithm using this iteration strategy
     */
    @Override
    public void performIteration(DataClusteringEC algorithm) {
        if(changeDetectionStrategy.detectChange()) {
            reinitializePosition((Topology<ClusterIndividual>) algorithm.getTopology());
            reinitialized = true;
        }
        
        delegate.setWindow(this.window);
        delegate.performIteration(algorithm);
        
    }
    
    /*
     * Returns the delegate iteration strategy
     * @return delegate The delegate iteration strategy
     */
    public SinglePopulationDataClusteringDEIterationStrategy getDelegate() {
        return delegate;
    }
    
    /*
     * Sets teh delegate iteration strategy to the one received as a parameter
     * @param newDelegate The new delegate iteration strategy
     */
    public void setDelegate(SinglePopulationDataClusteringDEIterationStrategy newDelegate){
        delegate = newDelegate;
    }
    
    /*
     * Reinitializes part of, or the whole, population
     * @param topology The population to be reinitialised
     */
    private void reinitializePosition(Topology<ClusterIndividual> topology) {
        int index = (int) new UniformDistribution().getRandomNumber(0, topology.size());
        int totalEntities = topology.size() * reinitialisationPercentage / 100;
        int[] alreadyChangedIndexes = new int[totalEntities];
        
        for(int j = 0; j < alreadyChangedIndexes.length; j++) {
            alreadyChangedIndexes[j] = -1;
        }
        
        for(int i = 0; i < totalEntities; i++) {
            while(contains(alreadyChangedIndexes, index)) {
                index = (int) new UniformDistribution().getRandomNumber(0, topology.size());
            }
            
            ((ClusterIndividual) topology.get(i)).reinitialise();
            assignDataPatternsToIndividual(((CentroidHolder)((ClusterIndividual) topology.get(i)).getCandidateSolution()), dataset);
            alreadyChangedIndexes[i] = index;
        }
        
    }
    
    private boolean contains(int[] array, int value) {
        for(int val : array) {
            if(val == value) {
                return true;
            }
        }
        
        return false;
    }
    
    /*
     * Sets the boundary constraint of the re-initialization strategy as well as that of its delegate
     * @param boundaryConstraint The constraint to be given to this strategy as well as its delegate
     */
    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.boundaryConstraint = boundaryConstraint;
        delegate.setBoundaryConstraint(boundaryConstraint);
    }
    
    /*
     * Sets the change detection strategy to be used
     * @param changeStrategy The new changeDetectionStrategy
     */
    public void setChangeDetectionStrategy(ChangeDetectionStrategy changeStrategy) {
        changeDetectionStrategy = changeStrategy;
    }
    
    /*
     * Returns the change detection strategy being used
     * @return cahngeDetectionStrategy The current change detection strategy
     */
    public ChangeDetectionStrategy getChangeDetectionStrategy() {
        return changeDetectionStrategy;
    }
}
