/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.TopologyBestContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.clustering.SlidingWindow;
/**
 *
 * @author Kris
 */
public class DataClusteringECTest {
    
    @Test
    public void algorithmInitialisationTest() {
        DataClusteringEC instance = new DataClusteringEC();
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
        instance.algorithmInitialisation();
        
        Assert.assertTrue(((SinglePopulationDataClusteringDEIterationStrategy) instance.getIterationStrategy()).getDataset().size() > 0);
        Assert.assertTrue(!instance.getTopology().isEmpty());
        
    }
    
    @Test
    public void algorithmIterationTest() {
        DataClusteringEC instance = new DataClusteringEC();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
        RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
        ClusterIndividual indiv = new ClusterIndividual();
        indiv.setInitialisationStrategy(initStrategy);
        init.setEntityType(indiv);
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        instance.performInitialisation();
        
        ClusterIndividual individualBefore = (ClusterIndividual) instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterIndividual individualAfter = (ClusterIndividual) instance.getTopology().get(0).getClone();
       
        Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution()));
    }

    @Test
    public void getWindowTest() {
       SlidingWindow window = new SlidingWindow();
       DataClusteringEC instance = new DataClusteringEC();
       instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
       instance.setWindow(window);
       
       Assert.assertEquals(window, instance.getWindow());
    }

    @Test
    public void setIsExplorerTest() {
       DataClusteringEC instance = new DataClusteringEC();
       instance.setIsExplorer(true);
       
       Assert.assertEquals(true, instance.isExplorer());
    }

    @Test
    public void getIterationStrategyTest() {
       StandardClusteringDEIterationStrategy strategy = new StandardClusteringDEIterationStrategy();
       DataClusteringEC instance = new DataClusteringEC();
       instance.setIterationStrategy(strategy);
       
       Assert.assertEquals(strategy, instance.getIterationStrategy());
    }

    @Test
    public void setIterationStrategyTest() {
       StandardClusteringDEIterationStrategy strategy = new StandardClusteringDEIterationStrategy();
       DataClusteringEC instance = new DataClusteringEC();
       instance.setIterationStrategy(strategy);
       
       Assert.assertEquals(strategy, instance.getIterationStrategy());
    }
    
    @Test
    public void setSourceURLTest() {
       DataClusteringEC instance = new DataClusteringEC();
       instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
       
       Assert.assertTrue(instance.getWindow().getSourceURL().contains("library/src/test/resources/datasets/iris2.arff")
               || instance.getWindow().getSourceURL().contains("library\\src\\test\\resources\\datasets\\iris2.arff"));
    }

    @Test
    public void setWindowTest() {
       SlidingWindow window = new SlidingWindow();
       DataClusteringEC instance = new DataClusteringEC();
       instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
       instance.setWindow(window);
       
       Assert.assertEquals(window, instance.getWindow());
    }

    @Test
    public void isExplorerTest() {
       DataClusteringEC instance = new DataClusteringEC();
       instance.setIsExplorer(true);
       
       Assert.assertEquals(true, instance.isExplorer());
    }
    
    @Test
    public void getBestSolutionTest() {
        ClusterIndividual indiv1 = new ClusterIndividual();
        ClusterIndividual indiv2 = new ClusterIndividual();
        ClusterIndividual indivBest = new ClusterIndividual();
        
        indiv1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        indiv2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        indivBest.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.5));
        
        GBestTopology topology = new GBestTopology();
        topology.add(indiv1);
        topology.add(indiv2);
        topology.add(indivBest);
        
        DataClusteringEC instance = new DataClusteringEC();
        instance.setTopology(topology);
        
        Assert.assertEquals(indivBest.getFitness().getValue(), instance.getBestSolution().getFitness().getValue());
    }

}
