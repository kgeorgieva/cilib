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
public class VarianceScalingFactorUpdateStrategy implements ParameterAdaptationStrategy{
    private double variance;
    private double totalIndividuals;
    private double minimalParameterValue;
    private double crossoverProbability;
    
    public VarianceScalingFactorUpdateStrategy() {
        variance = 0.0;
        totalIndividuals = 50;
        minimalParameterValue = 1 / Math.sqrt(totalIndividuals);
        crossoverProbability = 0.0;
    }
    
    public VarianceScalingFactorUpdateStrategy(VarianceScalingFactorUpdateStrategy copy) {
        variance = copy.variance;
        totalIndividuals = copy.totalIndividuals;
        minimalParameterValue = copy.minimalParameterValue;
        crossoverProbability = copy.crossoverProbability;
    }

    public VarianceScalingFactorUpdateStrategy getClone() {
        return new VarianceScalingFactorUpdateStrategy(this);
    }

    public void change(SettableControlParameter parameter) {
        double newParameter = 0;
        double probability = totalIndividuals * (variance - 1) + crossoverProbability * (2 - crossoverProbability);
        
        if(probability >= 0) {
            newParameter = (totalIndividuals * (variance - 1) + crossoverProbability * (2 - crossoverProbability)) / (2 * totalIndividuals * crossoverProbability);
        } else {
            newParameter = minimalParameterValue;
        }
        
        parameter.update(newParameter);
    }
    
    public void setUpdateParameters(double variance, int totalIndividuals, double minimalParameterValue, double crossoverProbability) {
        this.variance = variance;
        this.totalIndividuals = totalIndividuals;
        this.minimalParameterValue = minimalParameterValue;
        this.crossoverProbability = crossoverProbability;
    }

    public void accepted(SettableControlParameter parameter, Entity entity, boolean accepted) {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
    }

    public double recalculateAdaptiveVariables() {
        throw new UnsupportedOperationException("Not applicable to this update trategy");
    }
    
}
