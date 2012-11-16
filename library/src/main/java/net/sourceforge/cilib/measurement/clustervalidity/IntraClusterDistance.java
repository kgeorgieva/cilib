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
 *
 * @author Kris
 */
public class IntraClusterDistance extends ValidityIndex{
    
    public IntraClusterDistance() {
        super();
    }

    public IntraClusterDistance(IntraClusterDistance copy) {
        super(copy);
    }
    
    @Override
    public Measurement<Real> getClone() {
        return new IntraClusterDistance(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder position = (CentroidHolder) algorithm.getBestSolution().getPosition();
        return Real.valueOf(calculateIntraClusterDistance(position));
    }
    
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
