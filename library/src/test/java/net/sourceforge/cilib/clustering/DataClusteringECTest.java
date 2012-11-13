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
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
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
        init.setEntityType(new ClusterIndividual());
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        instance.performInitialisation();
        
        Assert.assertTrue(((SinglePopulationDataClusteringDEIterationStrategy) instance.getIterationStrategy()).getDataset().size() > 0);
        Assert.assertTrue(!instance.getTopology().isEmpty());
        
    }
    
    @Test
    public void algorithmIterationTest() {
       DataClusteringEC instance = new DataClusteringEC();
        
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        StandardClusteringDEIterationStrategy strategy = new StandardClusteringDEIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
      
        init.setEntityType(new ClusterIndividual());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());
        
        instance.algorithmInitialisation();
        
        ClusterIndividual individualBefore = (ClusterIndividual) instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterIndividual individualAfter = (ClusterIndividual) instance.getTopology().get(0).getClone();
        
        Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution()));
    }

    @Test
    public void getWindowTest() {
       
    }

    @Test
    public void isIsExplorerTest() {
        
    }

    @Test
    public void setIsExplorerTest() {
       
    }

    @Test
    public void getIterationStrategyTest() {
       
    }

    @Test
    public void setIterationStrategyTest() {
        
    }
    
    @Test
    public void setSourceURLTest() {
       
    }

    @Test
    public void setWindowTest() {
        
    }

    @Test
    public void isExplorerTest() {
       
    }
    
    @Test
    public void getBestSolutionTest() {
        
    }

}
