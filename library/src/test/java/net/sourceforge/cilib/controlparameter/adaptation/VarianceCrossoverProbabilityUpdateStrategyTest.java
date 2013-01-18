/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import org.junit.Test;

/**
 *
 * @author Kris
 */
public class VarianceCrossoverProbabilityUpdateStrategyTest {
    
    /**
     * Test of change method, of class VarianceCrossoverProbabilityUpdateStrategy.
     */
    @Test
    public void testChange() {
        System.out.println("change");
        double variance = 5.2;
        int totalIndividuals = 5;
        double scalingFactor = 6.1;
        VarianceCrossoverProbabilityUpdateStrategy instance = new VarianceCrossoverProbabilityUpdateStrategy();
        instance.setUpdateParameters(variance, totalIndividuals, scalingFactor);
        SettableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        instance.change(parameter);
        Assert.assertEquals(Math.round(0.0567327246634755 * 5) / 5, Math.round(parameter.getParameter() * 5) / 5);
    }
}
