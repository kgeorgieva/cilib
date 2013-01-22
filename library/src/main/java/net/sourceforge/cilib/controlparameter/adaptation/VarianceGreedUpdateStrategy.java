/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;

/**
 * Variance Based update strategy for the Greed parameter described by 
 * Zaharie in her 2003 paper "Control of Population Diversity and Adaptation 
 * in Differential Evolution Algorithms" published in the Proceedings of Mendel
 * 2003, 9th International Conference on Soft Computing.
 * 
 * It can be applied to any parameter.
 */
public class VarianceGreedUpdateStrategy implements VarianceBasedUpdateStrategy{
    private double variance;
    private double totalIndividuals;
    private double minimalParameterValue;
    private double scalingFactor;
    
    /*
     * Default constructor for VarianceGreedUpdateStrategy
     */
    public VarianceGreedUpdateStrategy() {
        variance = 0.0;
        totalIndividuals = 50;
        minimalParameterValue = 1;
        scalingFactor = 0.0;
    }
    
    /*
     * Copy constructor for VarianceGreedUpdateStrategy
     * @param copy The VarianceGreedUpdateStrategy to be copied
     */
    public VarianceGreedUpdateStrategy(VarianceGreedUpdateStrategy copy) {
        variance = copy.variance;
        totalIndividuals = copy.totalIndividuals;
        minimalParameterValue = copy.minimalParameterValue;
        scalingFactor = copy.scalingFactor;
    }

    /*
     * Clone method for VarianceGreedUpdateStrategy
     * @return A new instance of this VarianceGreedUpdateStrategy
     */
    public ParameterAdaptationStrategy getClone() {
        return new VarianceGreedUpdateStrategy(this);
    }

    /*
     * Changes the parameter sent using Zaharie's greed parameter update strategy
     * @param parameter The parameter to be changed
     */
    public void change(SettableControlParameter parameter) {
        double newParameter = 0;
        
        if(variance >= 2 * Math.pow(scalingFactor, 2)) {
            newParameter = 1 - Math.sqrt((totalIndividuals / (totalIndividuals - 1)) * (variance - 2 * Math.pow(scalingFactor, 2)));
        } else {
            newParameter = minimalParameterValue;
        }
        
        parameter.update(newParameter);
    }
    
    /*
     * Sets the parameters to be used during the update
     * @param variance The variance value for the appropriate component
     * @param totalIndividuals The total number of individuals in the topology
     * @param crossoverProbability The value of the crossover probability for the appropriate component
     */
    public void setUpdateParameters(double variance, int totalIndividuals, double crossoverProbability) {
        this.variance = variance;
        this.totalIndividuals = totalIndividuals;
        this.scalingFactor = crossoverProbability;
    }

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Gets the minimal parameter value to be used in the update
     * @return The minimal parameter value
     */
    public double getMinimalParameterValue() {
        return minimalParameterValue;
    }

    /*
     * Sets the minimal value for the parameter
     * @param minimalParameterValue The new minimal parameter value
     */
    public void setMinimalParameterValue(double minimalParameterValue) {
        this.minimalParameterValue = minimalParameterValue;
    }
    
}
