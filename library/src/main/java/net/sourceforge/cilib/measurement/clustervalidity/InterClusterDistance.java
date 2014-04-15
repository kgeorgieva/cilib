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

/**
 * Measures the InterClusterDistane of a Clustering Problem
 */
public class InterClusterDistance extends ValidityIndex{
    
    /*
     * Default constructor for InterClusterDistance
     */
    public InterClusterDistance() {
        super();
    }

    /* 
     * Copy constructor for 
     * @param copy The InterClusterDistance to be copied
     */
    public InterClusterDistance(InterClusterDistance copy) {
        super(copy);
    }
    
    /*
     * Clone method for InterClusterDistance
     * @return A new instance of this InterClusterDistance
     */
    @Override
    public Measurement<Real> getClone() {
        return new InterClusterDistance(this);
    }

    /*
     * Gets the final inter-cluster distance value
     * @param algorithm The algorithm being measured
     * @return The inter-cluster distance
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder position = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = calculateInterClusterDistance(position);
        
        if(Double.isNaN(result)) {
            result = 0.1;
        }
        
        return Real.valueOf(result);
    }
    
    /*
     * Calculates the inter cluster distance using the distance between all
     * cluster centroids
     * @param position The centroig holder that contains the centroids to be compared
     * @return The inter-cluster distance
     */
    protected double calculateInterClusterDistance(CentroidHolder position) {
        double sum = 0;
        for(int k = 0; k < position.size() - 1; k++) {
            for(int j = k + 1; j < position.size(); j++) {
                if(!position.get(k).equals(position.get(j))) {
                    sum += distanceMeasure.distance(position.get(k).toVector(), position.get(j).toVector());
                }
            }
        }
        
        return sum * (2 / (double)(position.size() * (position.size() - 1)));
    }
    
}
