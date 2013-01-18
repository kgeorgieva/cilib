/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.util;

import fj.P1;
import java.lang.Number;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class ZaharieComponentVariance {
    private ControlParameter varianceInfluence;
    
    public ZaharieComponentVariance() {
        varianceInfluence = ConstantControlParameter.of(0.9);
    }
    
    public ZaharieComponentVariance(ZaharieComponentVariance copy) {
        varianceInfluence = copy.varianceInfluence;
    }
    
    public Vector getZaharieMeasure(Vector currentVariance, Vector previousVariance) {
        Vector variance = previousVariance.multiply(varianceInfluence.getParameter()).divide(currentVariance);
                
        return variance;
    }
    
    private Vector getMean(Topology<Entity> topology) {
        Vector resultingMean = Vector.fill(0, topology.get(0).getCandidateSolution().size());;
        for(Entity entity : topology) {
            resultingMean = resultingMean.plus((Vector) entity.getCandidateSolution());
        }
        
        resultingMean = resultingMean.divide(topology.size());
        
        return resultingMean;
    }
    
    private Vector getMeanSquared(Topology<Entity> topology) {
        Vector resultingMean = Vector.fill(0, topology.get(0).getCandidateSolution().size());
        for(Entity entity : topology) {
            Vector squaredResult = ((Vector) entity.getCandidateSolution());
            squaredResult = squaredResult.pow(2);
            resultingMean = resultingMean.plus(squaredResult);
        }
        
        resultingMean = resultingMean.divide(topology.size());
        return resultingMean;
    }
    
    public Vector calculateVariance(Topology<Entity> topology) {
        Vector variance = getMeanSquared(topology).subtract(getMean(topology).pow(2));
        return variance;
    }
}
