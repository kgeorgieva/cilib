/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *Measures the intra-cluster distance of a clustering problem
 */
public class IntraClusterDistance extends ValidityIndex{
    
    /*
     * Default constructor for IntraClusterDistance
     */
    public IntraClusterDistance() {
        super();
    }

    /*
     * Copy constructor for IntraClusterDistance
     * @param copy The IntraClusterDistance to be copied
     */
    public IntraClusterDistance(IntraClusterDistance copy) {
        super(copy);
    }
    
    /*
     * Clone method for IntraClusterDistance
     * @return A new instance of this IntraClusterDistance
     */
    @Override
    public Measurement<Real> getClone() {
        return new IntraClusterDistance(this);
    }

    /*
     * Gets the intra-cluster distance value
     * @param algorithm The algorithm being measured
     * @return The intra-cluster distance
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder position = (CentroidHolder) algorithm.getBestSolution().getPosition();
        return Real.valueOf(calculateIntraClusterDistance(position));
    }
    
    /*
     * Calculates the intra-cluster distance using the patterns assigned
     * to each centroid.
     * @param position The CentroidHolder holding the cluster centroids being 
     * compared
     * @return The intra-cluster distance
     */
    protected double calculateIntraClusterDistance(CentroidHolder position) {
        double sum = 0;
        int totalDataPatterns = 0;
        for(ClusterCentroid centroid : position) {
            for(Vector pattern : centroid.getDataItems()) {
                sum += distanceMeasure.distance(centroid.toVector(), pattern);
                totalDataPatterns ++;
            }
        }
        
        sum /= (double) totalDataPatterns;
        
        return sum;
    }
    
}
