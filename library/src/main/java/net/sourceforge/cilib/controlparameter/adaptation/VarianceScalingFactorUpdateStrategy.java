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
 * Variance Based update strategy for the Scaling Factor parameter described by 
 * Zaharie in her 2003 paper "Control of Population Diversity and Adaptation 
 * in Differential Evolution Algorithms" published in the Proceedings of Mendel
 * 2003, 9th International Conference on Soft Computing.
 * 
 * It can be applied to any parameter.
 */
public class VarianceScalingFactorUpdateStrategy implements VarianceBasedUpdateStrategy{
    private double variance;
    private double totalIndividuals;
    private double minimalParameterValue;
    private double crossoverProbability;
    
    /*
     * Default constructor for VarianceScalingFactorUpdateStrategy
     */
    public VarianceScalingFactorUpdateStrategy() {
        variance = 0.0;
        totalIndividuals = 50;
        minimalParameterValue = 1 / Math.sqrt(totalIndividuals);
        crossoverProbability = 0.0;
    }
    
    /*
     * Copy constructor for VarianceScalingFactorUpdateStrategy
     * @param copy The VarianceScalingFactorUpdateStrategy to be copied
     */
    public VarianceScalingFactorUpdateStrategy(VarianceScalingFactorUpdateStrategy copy) {
        variance = copy.variance;
        totalIndividuals = copy.totalIndividuals;
        minimalParameterValue = copy.minimalParameterValue;
        crossoverProbability = copy.crossoverProbability;
    }

    /*
     * Clone method for VarianceScalingFactorUpdateStrategy
     * @return A new instance of this VarianceScalingFactorUpdateStrategy
     */
    public VarianceScalingFactorUpdateStrategy getClone() {
        return new VarianceScalingFactorUpdateStrategy(this);
    }

    /*
     * Changes the parameter sent using Zaharie's scaling factor parameter update strategy
     * @param parameter The parameter to be changed
     */
    public void change(SettableControlParameter parameter) {
        double newParameter = 0;
        double probability = totalIndividuals * (variance - 1) + crossoverProbability * (2 - crossoverProbability);
        
        if(probability >= 0) {
            newParameter = Math.sqrt((totalIndividuals * (variance - 1) + crossoverProbability * (2 - crossoverProbability)) / (2 * totalIndividuals * crossoverProbability));
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
    public void setUpdateParameters(double variance, int totalIndividuals,  double crossoverProbability) {
        this.variance = variance;
        this.totalIndividuals = totalIndividuals;
        this.crossoverProbability = crossoverProbability;
    }

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
    }

    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
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
