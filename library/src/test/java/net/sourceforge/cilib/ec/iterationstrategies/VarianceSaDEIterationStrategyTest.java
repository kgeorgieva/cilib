/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.util.selection.recipes.BoltzmannSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import org.junit.Assert;
import org.junit.Test;

public class VarianceSaDEIterationStrategyTest {
   
    /**
     * Test of getTargetVectorSelectionStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testGetTargetVectorSelectionStrategy() {
        System.out.println("testGetTargetVectorSelectionStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        Selector expResult = new BoltzmannSelector();
        instance.setTargetVectorSelectionStrategy(expResult);
        Selector result = instance.getTargetVectorSelectionStrategy();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setTargetVectorSelectionStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testSetTargetVectorSelectionStrategy() {
        System.out.println("testSetTargetVectorSelectionStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        Selector expResult = new BoltzmannSelector();
        instance.setTargetVectorSelectionStrategy(expResult);
        Selector result = instance.getTargetVectorSelectionStrategy();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getTrialVectorCreationStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testGetTrialVectorCreationStrategy() {
        System.out.println("testGetTrialVectorCreationStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        CreationStrategy expResult = new RandCreationStrategy();
        instance.setTrialVectorCreationStrategy(expResult);
        CreationStrategy result = instance.getTrialVectorCreationStrategy();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setTrialVectorCreationStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testSetTrialVectorCreationStrategy() {
        System.out.println("testSetTrialVectorCreationStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        CreationStrategy expResult = new RandCreationStrategy();
        instance.setTrialVectorCreationStrategy(expResult);
        CreationStrategy result = instance.getTrialVectorCreationStrategy();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getCrossoverStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testGetCrossoverStrategy() {
        System.out.println("testGetCrossoverStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        CrossoverStrategy expResult = new DifferentialEvolutionBinomialCrossover();
        instance.setCrossoverStrategy(expResult);
        CrossoverStrategy result = instance.getCrossoverStrategy();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setCrossoverStrategy method, of class VarianceSaDEIterationStrategy.
     */
    @Test
    public void testSetCrossoverStrategy() {
        System.out.println("testSetCrossoverStrategy");
        VarianceSaDEIterationStrategy instance = new VarianceSaDEIterationStrategy();
        CrossoverStrategy expResult = new DifferentialEvolutionBinomialCrossover();
        instance.setCrossoverStrategy(expResult);
        CrossoverStrategy result = instance.getCrossoverStrategy();
        Assert.assertEquals(expResult, result);
    }
}
