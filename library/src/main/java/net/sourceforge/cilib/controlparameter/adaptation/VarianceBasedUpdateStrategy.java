/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.controlparameter.adaptation;

/**
 *
 * @author Kris
 */
public interface VarianceBasedUpdateStrategy extends ParameterAdaptationStrategy{
    
    public void setUpdateParameters(double variance, int totalIndividuals, double otherParameter);
    
}
