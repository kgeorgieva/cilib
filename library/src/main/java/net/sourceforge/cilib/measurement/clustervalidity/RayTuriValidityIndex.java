/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import net.sourceforge.cilib.clustering.SlidingWindow;


/**
 * This class calculates the Ray Tury Validity Index that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */
public class RayTuriValidityIndex extends ValidityIndex {
    /*
     * Default constructor for RayTuriValidityIndex
     */
    public RayTuriValidityIndex() {
        super();
    }
    
    /*
     * Copy constructor for RayTuriValidityIndex
     */
    public RayTuriValidityIndex(RayTuriValidityIndex copy) {
        super(copy);
    }
    
    /*
     * Calculates the Ray Turi Validity Index
     * @param algorithm The algorithm for which the validity index is being calculated
     * @return result The validity index value
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        assignDataPatternsToCentroid(holder);
        double inter = getInterClusterDistance(holder);
        
        if(Double.isInfinite(inter)) {
            return Real.valueOf(Double.POSITIVE_INFINITY);
        } else {
            double result = getaverageClusterDistance(holder) / inter;
            //System.out.println("Ray: " + result + " inter: " + getInterClusterDistance(holder));
            return Real.valueOf(result);
        }
    }
    
    /*
     * Calculates the average distance between all centroid and their patterns
     * @param centroidHolder The set of centroids
     * @return distace The average distance
     */
    protected double getaverageClusterDistance(CentroidHolder centroidHolder) {
        double sum = 0;
        double numberOfPatterns = 0;
        for(ClusterCentroid centroid :centroidHolder) {
            for(Vector pattern : centroid.getDataItems()) {
                sum += distanceMeasure.distance(pattern, centroid.toVector());
                numberOfPatterns++;
            }
            
        }
        
        sum /= (double) numberOfPatterns;
        
        //System.out.println("Intra min: " + sum);
        return sum;
    }
    
    /*
     * Calculates the minimum distance between all centroids
     * @param centoidHolder The set of centroids to be checked
     * @return minimumDistance the smallest distance between clusters
     */
    protected double getInterClusterDistance(CentroidHolder centroidHolder) {
        double minimum = Double.POSITIVE_INFINITY;
        double distance;
        for(int k = 0; k < centroidHolder.size() - 1; k++) {
            for(int j = k + 1; j < centroidHolder.size(); j++) {
                distance = distanceMeasure.distance(centroidHolder.get(k).toVector(), centroidHolder.get(j).toVector());
                
                if(distance < minimum) {
                    minimum = distance;
                }
            }
        }
        //System.out.println("Inter min: " + minimum);
        return minimum;
    }
    
    public void assignDataPatternsToCentroid(CentroidHolder candidateSolution) {
        EuclideanDistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        for(ClusterCentroid centroid : candidateSolution) {
            centroid.clearDataItems();
        }
        
        //DataTable dataset = ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow().getCurrentDataset();
        SlidingWindow window= ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow();
        DataTable dataset;
        if(window.windowHasSlid()) {
            dataset = window.getPreviousDataset();
        } else {
           dataset = window.getCurrentDataset(); 
        }
        
        double euclideanDistance;
        Vector addedPattern;
        Vector pattern;
        
            for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
            
    }

    /*
     * Clone method for RayTuriValidityIndex
     */
    @Override
    public Measurement<Real> getClone() {
        return new RayTuriValidityIndex(this);
    }
    
}
