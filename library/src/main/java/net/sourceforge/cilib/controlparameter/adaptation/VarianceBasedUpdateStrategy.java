/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

/**
 * Interface for all Variance Based methods described by 
 * Zaharie in her 2003 paper "Control of Population Diversity and Adaptation 
 * in Differential Evolution Algorithms" published in the Proceedings of Mendel
 * 2003, 9th International Conference on Soft Computing.
 */
public interface VarianceBasedUpdateStrategy extends ParameterAdaptationStrategy{
    
    public void setUpdateParameters(double variance, int totalIndividuals, double otherParameter);
    
}
