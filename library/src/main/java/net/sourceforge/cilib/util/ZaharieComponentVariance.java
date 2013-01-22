/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class calculates the standard variance as well as the Zaharie variance 
 * described by Zaharie in her 2003 paper "Control of Population Diversity and
 * Adaptation in Differential Evolution Algorithms" published in the Proceedings
 * of Mendel 2003, 9th International Conference on Soft Computing. It does this in
 * a component based manner.
 */
public class ZaharieComponentVariance {
    private ControlParameter varianceInfluence;
    
    /*
     * Default constructor for ZaharieComponentVariance
     */
    public ZaharieComponentVariance() {
        varianceInfluence = ConstantControlParameter.of(0.9);
    }
    
    /*
     * Copy constructor for ZaharieComponentVariance
     * @copy The ZaharieComponentVariance to be copied
     */
    public ZaharieComponentVariance(ZaharieComponentVariance copy) {
        varianceInfluence = copy.varianceInfluence;
    }
    
    /*
     * Calculates the Zaharie variance using the current variance, previous
     * variance and a variance influence parameter.
     * @param currentVariance The current variance
     * @param previousVariance The previousVariance
     * @return The Zaharie Variance
     */
    public Vector getZaharieMeasure(Vector currentVariance, Vector previousVariance) {
        Vector variance;
        variance = previousVariance.multiply(varianceInfluence.getParameter()).divide(currentVariance);
        
        return variance;
    }
    
    
    /*
     * Gets the  mean of the topology
     * @param topology The topology on which the mean is calculated
     * @return The vector of mean per component
     */
    private Vector getMean(Topology<Entity> topology) {
        Vector resultingMean = Vector.fill(0, topology.get(0).getCandidateSolution().size());
        for(Entity entity : topology) {
            //System.out.println("Entity:" + entity.getCandidateSolution());
            resultingMean = resultingMean.plus((Vector) entity.getCandidateSolution());
            //System.out.println("Resulting Mean:" + resultingMean);
        }
        
        resultingMean = resultingMean.divide(topology.size());
        return resultingMean;
    }
    
    /*
     * Gets the Mean of square of the topology
     * @param topology The topology on which the mean is calculated
     * @return The vector of mean of square per component
     */
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
    
    /*
     * Calculates the variance = mean od square - square of mean
     * @param topology The topology for which the variance is being calculated
     * @return The vector of variance values per component
     */
    public Vector calculateVariance(Topology<Entity> topology) {
        Vector variance = getMeanSquared(topology).subtract(getMean(topology).pow(2));
        
        return variance;
    }
}
