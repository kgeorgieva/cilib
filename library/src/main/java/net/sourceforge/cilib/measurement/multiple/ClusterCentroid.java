/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;

/**
 *
 * @author Kris
 */
public class ClusterCentroid implements Measurement<Type> {

    public Measurement<Type> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Type getValue(Algorithm algorithm) {
        return algorithm.getBestSolution().getPosition();
    }
    
}
