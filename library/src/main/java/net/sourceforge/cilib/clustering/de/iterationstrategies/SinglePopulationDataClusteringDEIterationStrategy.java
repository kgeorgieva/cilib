/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.clustering.pso.iterationstrategies.SinglePopulationDataClusteringPSOIterationStrategy;
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
 *
 * @author Kris
 */
public abstract class SinglePopulationDataClusteringDEIterationStrategy extends AbstractIterationStrategy<DataClusteringEC>{
    protected DataTable dataset;
    protected EuclideanDistanceMeasure distanceMeasure;
    protected SlidingWindow window;
    protected int reinitialisationInterval;
    protected int dimensions;
    protected boolean reinitialized;
    
    public SinglePopulationDataClusteringDEIterationStrategy() {
        dataset = new StandardPatternDataTable();
        reinitialized = false;
        distanceMeasure = new EuclideanDistanceMeasure();
        boundaryConstraint = new CentroidBoundaryConstraint();
        window = new SlidingWindow();
        reinitialisationInterval = 1;
        dimensions = 0;
        
    }
    
    public SinglePopulationDataClusteringDEIterationStrategy(SinglePopulationDataClusteringDEIterationStrategy copy) {
        dataset = copy.dataset;
        distanceMeasure = copy.distanceMeasure;
        boundaryConstraint = copy.boundaryConstraint;
        window = copy.window;
        reinitialisationInterval = copy.reinitialisationInterval;
        dimensions = copy.dimensions;
        reinitialized = copy.reinitialized;
    }
    
    @Override
    public abstract SinglePopulationDataClusteringDEIterationStrategy getClone();

    @Override
    public abstract void performIteration(DataClusteringEC algorithm);
    
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
    
    protected void clearCentroidDistanceValues(Topology<ClusterIndividual> topology) {
        CentroidHolder candidateSolution;
        for(ClusterIndividual particle : topology) {
            candidateSolution = (CentroidHolder) particle.getCandidateSolution();
            
            for(ClusterCentroid centroid : candidateSolution) {
                centroid.clearDataItems();
            }
        }
    }

    public DataTable getDataset() {
        return dataset;
    }

    public void setDataset(DataTable dataset) {
        this.dataset = dataset;
    }

    public EuclideanDistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(EuclideanDistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public SlidingWindow getWindow() {
        return window;
    }

    public void setWindow(SlidingWindow window) {
        this.window = window;
        dataset = window.getCurrentDataset();
    }

    public int getReinitialisationInterval() {
        return reinitialisationInterval;
    }

    public void setReinitialisationInterval(int reinitialisationInterval) {
        this.reinitialisationInterval = reinitialisationInterval;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public boolean isReinitialized() {
        return reinitialized;
    }

    public void setReinitialized(boolean reinitialized) {
        this.reinitialized = reinitialized;
    }
    
}
