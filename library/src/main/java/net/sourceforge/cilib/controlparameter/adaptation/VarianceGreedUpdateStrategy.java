/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;

/**
 *
 * @author Kris
 */
public class VarianceGreedUpdateStrategy implements VarianceBasedUpdateStrategy{
    private double variance;
    private double totalIndividuals;
    private double minimalParameterValue;
    private double scalingFactor;
    
    public VarianceGreedUpdateStrategy() {
        variance = 0.0;
        totalIndividuals = 50;
        minimalParameterValue = 1;
        scalingFactor = 0.0;
    }
    
    public VarianceGreedUpdateStrategy(VarianceGreedUpdateStrategy copy) {
        variance = copy.variance;
        totalIndividuals = copy.totalIndividuals;
        minimalParameterValue = copy.minimalParameterValue;
        scalingFactor = copy.scalingFactor;
    }

    public ParameterAdaptationStrategy getClone() {
        return new VarianceGreedUpdateStrategy(this);
    }

    public void change(SettableControlParameter parameter) {
        double newParameter = 0;
        
        if(variance >= 2 * Math.pow(scalingFactor, 2)) {
            newParameter = 1 - Math.sqrt((totalIndividuals / (totalIndividuals - 1)) * (variance - 2 * Math.pow(scalingFactor, 2)));
        } else {
            newParameter = minimalParameterValue;
        }
        
        parameter.update(newParameter);
    }
    
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

    public double getMinimalParameterValue() {
        return minimalParameterValue;
    }

    public void setMinimalParameterValue(double minimalParameterValue) {
        this.minimalParameterValue = minimalParameterValue;
    }
    
}
