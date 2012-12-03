/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;

/**
 *
 * @author Kris
 */
public class DynamicClusteringDETest {
    
   @Test
   public void algorithmInitialisationTest() {
        DynamicClusteringDE instance = new DynamicClusteringDE();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
        RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
        ClusterIndividual indiv = new ClusterIndividual();
        indiv.setInitialisationStrategy(initStrategy);
        init.setEntityType(indiv);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        instance.performInitialisation();
        
        junit.framework.Assert.assertTrue(((SinglePopulationDataClusteringDEIterationStrategy) instance.getIterationStrategy()).getDataset().size() > 0);
        junit.framework.Assert.assertTrue(!instance.getTopology().isEmpty());
   }

   @Test
    public void getRandomTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        UniformDistribution strategy = new UniformDistribution();
        de.setRandom(strategy);
        
        Assert.assertEquals(strategy, de.getRandom());
    }

   @Test
    public void setRandomTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        UniformDistribution strategy = new UniformDistribution();
        de.setRandom(strategy);
        
        Assert.assertEquals(strategy, de.getRandom());
    }

   @Test
    public void getFirstUpdateStrategyTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        StandardClusteringDEUpdateStrategy strategy = new StandardClusteringDEUpdateStrategy();
        de.setFirstUpdateStrategy(strategy);
        
        Assert.assertEquals(strategy, de.getFirstUpdateStrategy());  
    }

   @Test
    public void setFirstUpdateStrategyTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        StandardClusteringDEUpdateStrategy strategy = new StandardClusteringDEUpdateStrategy();
        de.setFirstUpdateStrategy(strategy);
        
        Assert.assertEquals(strategy, de.getFirstUpdateStrategy());
    }

   @Test
    public void getSecondUpdateStrategyTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        StandardClusteringDEUpdateStrategy strategy = new StandardClusteringDEUpdateStrategy();
        de.setSecondUpdateStrategy(strategy);
        
        Assert.assertEquals(strategy, de.getSecondUpdateStrategy());
    }

   @Test
    public void setSecondUpdateStrategyTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        StandardClusteringDEUpdateStrategy strategy = new StandardClusteringDEUpdateStrategy();
        de.setSecondUpdateStrategy(strategy);
        
        Assert.assertEquals(strategy, de.getSecondUpdateStrategy());
    }

   @Test
    public void getUpdateStrategyProbabilityTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        de.setUpdateStrategyProbability(0.2);
        
        Assert.assertTrue(0.2 == de.getUpdateStrategyProbability());
    }

   @Test
    public void setUpdateStrategyProbabilityTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        de.setUpdateStrategyProbability(0.2);
        
        Assert.assertTrue(0.2 == de.getUpdateStrategyProbability());
    }
    
}
