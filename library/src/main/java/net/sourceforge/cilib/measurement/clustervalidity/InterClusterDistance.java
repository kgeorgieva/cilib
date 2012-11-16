/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

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
public class InterClusterDistance extends ValidityIndex{
    
    public InterClusterDistance() {
        super();
    }

    public InterClusterDistance(InterClusterDistance copy) {
        super(copy);
    }
    
    @Override
    public Measurement<Real> getClone() {
        return new InterClusterDistance(this);
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder position = (CentroidHolder) algorithm.getBestSolution().getPosition();
        return Real.valueOf(calculateInterClusterDistance(position));
    }
    
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
