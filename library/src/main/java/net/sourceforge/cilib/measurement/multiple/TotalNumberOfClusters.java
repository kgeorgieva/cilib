/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.CentroidHolder;

/**
 *
 * @author Kris
 */
public class TotalNumberOfClusters implements Measurement<Int> {

    public Measurement<Int> getClone() {
        return this;
    }

    public Int getValue(Algorithm algorithm) {
        int totalClusters = ((CentroidHolder) algorithm.getBestSolution().getPosition()).size();
        return Int.valueOf(totalClusters);
    }

  
    
}
