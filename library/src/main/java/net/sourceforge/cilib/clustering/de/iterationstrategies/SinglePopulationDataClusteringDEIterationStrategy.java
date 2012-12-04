/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * This class holds the methods that are shared by certain clustering EC iteration strategies
 */
public abstract class SinglePopulationDataClusteringDEIterationStrategy extends AbstractIterationStrategy<DataClusteringEC>{
    protected DataTable dataset;
    protected EuclideanDistanceMeasure distanceMeasure;
    protected SlidingWindow window;
    protected int reinitialisationInterval;
    protected int dimensions;
    protected boolean reinitialized;
    
    /*
     * Default cosntructor for SinglePopulationDataClusteringDEIterationStrategy
     */
    public SinglePopulationDataClusteringDEIterationStrategy() {
        dataset = new StandardPatternDataTable();
        reinitialized = false;
        distanceMeasure = new EuclideanDistanceMeasure();
        boundaryConstraint = new CentroidBoundaryConstraint();
        window = new SlidingWindow();
        reinitialisationInterval = 1;
        dimensions = 0;
        
    }
    
    /*
     * Copy constructor for SinglePopulationDataClusteringDEIterationStrategy
     * @param copy The SinglePopulationDataClusteringDEIterationStrategy to be copied
     */
    public SinglePopulationDataClusteringDEIterationStrategy(SinglePopulationDataClusteringDEIterationStrategy copy) {
        dataset = copy.dataset;
        distanceMeasure = copy.distanceMeasure;
        boundaryConstraint = copy.boundaryConstraint;
        window = copy.window;
        reinitialisationInterval = copy.reinitialisationInterval;
        dimensions = copy.dimensions;
        reinitialized = copy.reinitialized;
    }
    
    /*
     * Clone method for SinglePopulationDataClusteringDEIterationStrategy
     * @return A new instance of this SinglePopulationDataClusteringDEIterationStrategy
     */
    @Override
    public abstract SinglePopulationDataClusteringDEIterationStrategy getClone();

    /*
     * Performs one iteration of a SinglePopulationDataClusteringDEIterationStrategy
     */
    @Override
    public abstract void performIteration(DataClusteringEC algorithm);
    
   /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToIndividual(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure aDistanceMeasure = new EuclideanDistanceMeasure();
        Vector pattern;
        
        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                pattern = ((StandardPattern) dataset.getRow(i)).getVector();
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
    
    /*
     * Removes all previously assigned patterns from the centroids that they were assigned to
     * @param topology The topology to be cleaned up
     */
    protected void clearCentroidDistanceValues(Topology<ClusterIndividual> topology) {
        CentroidHolder candidateSolution;
        for(ClusterIndividual particle : topology) {
            candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            
            for(ClusterCentroid centroid : candidateSolution) {
                centroid.clearDataItems();
            }
        }
    }

    /*
     * Gets the dataset being clustered
     * @return The dataset being clustered
     */
    public DataTable getDataset() {
        return dataset;
    }

    /*
     * Sets the dataset to be clustered
     * @param dataset The new dataset to be clustered
     */
    public void setDataset(DataTable dataset) {
        this.dataset = dataset;
    }

    /*
     * Gets the distance measure to be used to determine the proximity
     * of a cluster centroid to a data pattern
     * @return The distance measure
     */
    public EuclideanDistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /*
     * Sets the distance measure to be used to determine the proximity
     * of a cluster centroid to a data pattern
     * @param distanceMeasure The new distance measure
     */
    public void setDistanceMeasure(EuclideanDistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /*
     * Gets the current window
     * @return The current window
     */
    public SlidingWindow getWindow() {
        return window;
    }

    /*
     * Sets the window to the one received as a parameter and
     * the dataset to the new window's current dataset
     * @param window The new window
     */
    public void setWindow(SlidingWindow window) {
        this.window = window;
        dataset = window.getCurrentDataset();
    }

    /*
     * Gets the value of the reinitialisation interval
     * @return The reinitialisation interval
     */
    public int getReinitialisationInterval() {
        return reinitialisationInterval;
    }

    /*
     * Sets the itnerval at which the individuals will be re-initialized if reinitialization 
     * due to change in environment is required. In other words, every how-many particles
     * must be initialized? To initialize all, the interval is 1
     * @param reinitialisationInterval the new interval
     */
    public void setReinitialisationInterval(int reinitialisationInterval) {
        this.reinitialisationInterval = reinitialisationInterval;
    }

    /*
     * Gets the dimensions of the centroids (determined by the dataset)
     * @return The dimensions of the centroids
     */
    public int getDimensions() {
        return dimensions;
    }

    /*
     * Sets the dimensions of the centroids
     * @param dimensions The new dimensions
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    /*
     * Gets the value of reinitialized, which indicates whether the population has been
     * reinitialised.
     * @return The value of reinitialized
     */
    public boolean isReinitialized() {
        return reinitialized;
    }

    /*
     * Sets the  value of reinitialized, which indicates whether the population has been
     * reinitialised.
     * @param reinitialized The new value of reinitialized
     */
    public void setReinitialized(boolean reinitialized) {
        this.reinitialized = reinitialized;
    }
    
}
