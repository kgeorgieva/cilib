/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import org.junit.Test;

public class VarianceScalingFactorUpdateStrategyTest {
    
    /**
     * Test of change method, of class VarianceGreedUpdateStrategy.
     */
    @Test
    public void testChangeMinValue() {
        System.out.println("change");
        double variance = 5.2;
        int totalIndividuals = 5;
        double crossoverProbability = 6.1;
        VarianceScalingFactorUpdateStrategy instance = new VarianceScalingFactorUpdateStrategy();
        instance.setUpdateParameters(variance, totalIndividuals, crossoverProbability);
        instance.setMinimalParameterValue(2.0);
        SettableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        instance.change(parameter);
        Assert.assertEquals(2.0, parameter.getParameter());
    }
    
    @Test
    public void testChange() {
        System.out.println("change");
        double variance = 5.2;
        int totalIndividuals = 5;
        double crossoverProbability = 0.9;
        VarianceScalingFactorUpdateStrategy instance = new VarianceScalingFactorUpdateStrategy();
        instance.setUpdateParameters(variance, totalIndividuals, crossoverProbability);
        SettableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        instance.change(parameter);
        Assert.assertEquals(Math.round(1.563116545025781 * 5) / 5, Math.round(parameter.getParameter() * 5) / 5);
    }
}