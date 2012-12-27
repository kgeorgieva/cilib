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
public class VarianceCrossoverProbabilityUpdateStrategy implements ParameterAdaptationStrategy{
    private double variance;
    private double totalIndividuals;
    private double minimalParameterValue;
    private double scalingFactor;
    
    public VarianceCrossoverProbabilityUpdateStrategy() {
        variance = 0.0;
        totalIndividuals = 50;
        minimalParameterValue = 0.01;
        scalingFactor = 0.0;
    }
    
    public VarianceCrossoverProbabilityUpdateStrategy(VarianceCrossoverProbabilityUpdateStrategy copy) {
        variance = copy.variance;
        totalIndividuals = copy.totalIndividuals;
        minimalParameterValue = copy.minimalParameterValue;
        scalingFactor = copy.scalingFactor;
    }

    public VarianceCrossoverProbabilityUpdateStrategy getClone() {
        return new VarianceCrossoverProbabilityUpdateStrategy(this);
    }

    public void change(SettableControlParameter parameter) {
        double newParameter = 0;
        
        if(variance >= 1) {
            newParameter = -(totalIndividuals * Math.pow(scalingFactor, 2) - 1) + Math.sqrt((Math.pow(totalIndividuals * Math.pow(scalingFactor, 2) - 1, 2) -
                    (totalIndividuals * (1 - variance))));
        } else {
            newParameter = minimalParameterValue;
        }
        
        parameter.update(newParameter);
    }
    
    public void setUpdateParameters(double variance, int totalIndividuals, double minimalParameterValue, double scalingFactor) {
        this.variance = variance;
        this.totalIndividuals = totalIndividuals;
        this.minimalParameterValue = minimalParameterValue;
        this.scalingFactor = scalingFactor;
    }

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
    }

    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
    }
}
