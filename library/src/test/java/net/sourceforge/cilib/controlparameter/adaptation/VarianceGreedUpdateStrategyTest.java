/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.controlparameter.adaptation;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kris
 */
public class VarianceGreedUpdateStrategyTest {
   
    /**
     * Test of change method, of class VarianceGreedUpdateStrategy.
     */
    @Test
    public void testChangeMinValue() {
        System.out.println("change");
        double variance = 5.2;
        int totalIndividuals = 5;
        double crossoverProbability = 6.1;
        VarianceGreedUpdateStrategy instance = new VarianceGreedUpdateStrategy();
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
        VarianceGreedUpdateStrategy instance = new VarianceGreedUpdateStrategy();
        instance.setUpdateParameters(variance, totalIndividuals, crossoverProbability);
        instance.setMinimalParameterValue(2.0);
        SettableControlParameter parameter = new StandardUpdatableControlParameter();
        parameter.setParameter(5.0);
        instance.change(parameter);
        Assert.assertEquals(Math.round(-1.085665361461421 * 5) / 5, Math.round(parameter.getParameter()));
    }

}
