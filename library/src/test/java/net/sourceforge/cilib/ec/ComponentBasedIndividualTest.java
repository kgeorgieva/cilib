/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec;

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.controlparameter.adaptation.ParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceBasedUpdateStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceCrossoverProbabilityUpdateStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.ControlParameterInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.RandomBoundedParameterInitialisationStrategy;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomInitializationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.type.types.container.Vector;
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
public class ComponentBasedIndividualTest {
    
    /**
     * Test of initialise method, of class ComponentBasedIndividual.
     */
    @Test
    public void testInitialise() {
        System.out.println("initialise");
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-5.12:5.12)^30");
        problem.setFunction(new Spherical());
        
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        individual.initialise(problem);
        Assert.assertTrue(!individual.getCandidateSolution().isEmpty());
        Assert.assertTrue(!individual.getScalingFactorPerComponent().isEmpty());
        Assert.assertTrue(!individual.getCrossoverProbabilityPerComponent().isEmpty());
        Assert.assertTrue(!individual.getGreedPerComponent().isEmpty());
    }

    /**
     * Test of updateParameters method, of class ComponentBasedIndividual.
     */
    @Test
    public void testUpdateParameters() {
        System.out.println("updateParameters");
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        
        RandCreationStrategy creationStrategy = new RandCreationStrategy();
        DifferentialEvolutionBinomialCrossover crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
        StandardUpdatableControlParameter param = new StandardUpdatableControlParameter();
        param.setParameter(5.0);
        creationStrategy.setScaleParameter(param);
        crossoverStrategy.setCrossoverPointProbability(param.getClone());
        ArrayList<SettableControlParameter> crossoverParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        crossoverParams.add(par);
        crossoverParams.add(par2);
        
        ArrayList<SettableControlParameter> scalingParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par3 = new StandardUpdatableControlParameter();
        par3.setParameter(2.0);
        StandardUpdatableControlParameter par4 = new StandardUpdatableControlParameter();
        par4.setParameter(0.1);
        scalingParams.add(par3);
        scalingParams.add(par4);
        
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
            problem.setDomain("R(-5.12:5.12)^30");
            problem.setFunction(new Spherical());
        individual.initialise(problem);
        
        individual.setCrossoverProbabilityPerComponent(crossoverParams);
        individual.setScalingFactorPerComponent(scalingParams);
        individual.setParameterChangeCount(1);
        
        individual.updateParameters(Vector.of(5.2,0.1), 5);
        
        Assert.assertTrue(Math.round(individual.getCrossoverProbabilityPerComponent().get(0).getParameter() * 5) / 5 == Math.round(0.0567327246634755 * 5) / 5);
    }

    /**
     * Test of getCrossoverProbabilityPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetCrossoverProbabilityPerComponent() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> crossoverParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        crossoverParams.add(par);
        crossoverParams.add(par2);
        individual.setCrossoverProbabilityPerComponent(crossoverParams);
        
        Assert.assertEquals(crossoverParams, individual.getCrossoverProbabilityPerComponent());
    }

    /**
     * Test of setCrossoverProbabilityPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetCrossoverProbabilityPerComponent() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> crossoverParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        crossoverParams.add(par);
        crossoverParams.add(par2);
        individual.setCrossoverProbabilityPerComponent(crossoverParams);
        
        Assert.assertEquals(crossoverParams, individual.getCrossoverProbabilityPerComponent());
    }

    /**
     * Test of getScalingFactorPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetScalingFactorPerComponent() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> scalingFactorParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        scalingFactorParams.add(par);
        scalingFactorParams.add(par2);
        individual.setScalingFactorPerComponent(scalingFactorParams);
        
        Assert.assertEquals(scalingFactorParams, individual.getScalingFactorPerComponent());
    }

    /**
     * Test of setScalingFactorPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetScalingFactorPerComponent() {
       ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> scalingFactorParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        scalingFactorParams.add(par);
        scalingFactorParams.add(par2);
        individual.setScalingFactorPerComponent(scalingFactorParams);
        
        Assert.assertEquals(scalingFactorParams, individual.getScalingFactorPerComponent());
    }

    /**
     * Test of getGreedPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetGreedPerComponent() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> greedParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        greedParams.add(par);
        greedParams.add(par2);
        individual.setGreedPerComponent(greedParams);
        
        Assert.assertEquals(greedParams, individual.getGreedPerComponent());
    }

    /**
     * Test of setGreedPerComponent method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetGreedPerComponent() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ArrayList<SettableControlParameter> greedParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par = new StandardUpdatableControlParameter();
        par.setParameter(2.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(0.1);
        greedParams.add(par);
        greedParams.add(par2);
        individual.setGreedPerComponent(greedParams);
        
        Assert.assertEquals(greedParams, individual.getGreedPerComponent());
    }

    /**
     * Test of getParameterChangeCount method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetParameterChangeCount() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        individual.setParameterChangeCount(2.0);
        
        Assert.assertEquals(2.0, individual.getParameterChangeCount());
    }

    /**
     * Test of setParameterChangeCount method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetParameterChangeCount() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        individual.setParameterChangeCount(2.0);
        
        Assert.assertEquals(2.0, individual.getParameterChangeCount());
    }

    /**
     * Test of getGreedParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetGreedParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setGreedParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getGreedParameterAdaptationStrategy());
    }

    /**
     * Test of setGreedParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetGreedParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setGreedParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getGreedParameterAdaptationStrategy());
    }

    /**
     * Test of getGreedInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetGreedInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setGreedInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getGreedInitialisationStrategy());
    }

    /**
     * Test of setGreedInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetGreedInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setGreedInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getGreedInitialisationStrategy());
    }

    /**
     * Test of getScalingFactorParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetScalingFactorParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setScalingFactorParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getScalingFactorParameterAdaptationStrategy());
    }

    /**
     * Test of setScalingFactorParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetScalingFactorParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setScalingFactorParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getScalingFactorParameterAdaptationStrategy());
    }

    /**
     * Test of getCrossoverProbabilityParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetCrossoverProbabilityParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setCrossoverProbabilityParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getCrossoverProbabilityParameterAdaptationStrategy());
    }

    /**
     * Test of setCrossoverProbabilityParameterAdaptationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetCrossoverProbabilityParameterAdaptationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        VarianceBasedUpdateStrategy strategy = new VarianceCrossoverProbabilityUpdateStrategy();
        individual.setCrossoverProbabilityParameterAdaptationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getCrossoverProbabilityParameterAdaptationStrategy());
    }

    /**
     * Test of getScalingFactorInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetScalingFactorInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setScalingFactorInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getScalingFactorInitialisationStrategy());
    }

    /**
     * Test of setScalingFactorInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetScalingFactorInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setScalingFactorInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getScalingFactorInitialisationStrategy());
    }

    /**
     * Test of getCrossoverProbabilityInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetCrossoverProbabilityInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setCrossoverProbabilityInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getCrossoverProbabilityInitialisationStrategy());
    }

    /**
     * Test of setCrossoverProbabilityInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetCrossoverProbabilityInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        ControlParameterInitialisationStrategy strategy = new RandomBoundedParameterInitialisationStrategy();
        individual.setCrossoverProbabilityInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getCrossoverProbabilityInitialisationStrategy());
    }

    /**
     * Test of getInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testGetInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        InitializationStrategy strategy = new RandomInitializationStrategy();
        individual.setInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getInitialisationStrategy());
    }

    /**
     * Test of setInitialisationStrategy method, of class ComponentBasedIndividual.
     */
    @Test
    public void testSetInitialisationStrategy() {
        ComponentBasedIndividual individual = new ComponentBasedIndividual();
        InitializationStrategy strategy = new RandomInitializationStrategy();
        individual.setInitialisationStrategy(strategy);
        
        Assert.assertEquals(strategy, individual.getInitialisationStrategy());
    }
}
