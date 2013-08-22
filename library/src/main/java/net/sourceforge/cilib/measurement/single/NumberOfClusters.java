/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 * @author Kris
 */
public class NumberOfClusters implements Measurement<Real> {

    public Measurement<Real> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Real getValue(Algorithm algorithm) {
        ClusteringProblem problem = (ClusteringProblem) algorithm.getOptimisationProblem();
        return Real.valueOf(problem.getNumberOfClusters());
    }
    
}
