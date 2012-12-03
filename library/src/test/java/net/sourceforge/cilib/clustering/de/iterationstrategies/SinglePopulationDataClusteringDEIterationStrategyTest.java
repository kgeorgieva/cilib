/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;

public class SinglePopulationDataClusteringDEIterationStrategyTest {
    
    @Test
    public void testPerformIteration() {
        DataClusteringEC instance = new DataClusteringEC();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 10));
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
        RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
        ClusterIndividual indiv = new ClusterIndividual();
        indiv.setInitialisationStrategy(initStrategy);
        init.setEntityType(indiv);
        init.setEntityNumber(3);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        instance.performInitialisation();
        
        ClusterIndividual individualBefore = (ClusterIndividual) instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterIndividual individualAfter = (ClusterIndividual) instance.getTopology().get(0).getClone();
       
        Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution()));
    }

    /**
     * Test of getDistanceMeasure method, of class SinglePopulationDataClusteringDEIterationStrategy.
     */
    @Test
    public void testGetDistanceMeasure() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        Assert.assertTrue(instance.getDistanceMeasure() instanceof EuclideanDistanceMeasure);
    }

    /**
     * Test of getDataset method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetDataset() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        window.initializeWindow();
        instance.setWindow(window);
        
        Assert.assertEquals(instance.getDataset().size(), 1);
        
        Vector beforeSlide =  ((StandardPattern) instance.getDataset().getRow(0)).getVector();
        Vector expectedBeforeSlide = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(beforeSlide.containsAll(expectedBeforeSlide));
    }

    /**
     * Test of setReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetReinitialisationInterval() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        instance.setReinitialisationInterval(2);
        
        assertEquals(instance.getReinitialisationInterval(), 2);
    }
    /**
     * Test of getReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetReinitialisationInterval() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        instance.setReinitialisationInterval(2);
        
        assertEquals(instance.getReinitialisationInterval(), 2);
    }

    /**
     * Test of setDimensions method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetDimensions() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        instance.setDimensions(2);
        
        assertEquals(instance.dimensions, 2);
    }

    /**
     * Test of setWindow method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetWindow() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        instance.setWindow(window);
        
        Assert.assertEquals(window, instance.getWindow());
    }

    /**
     * Test of getWindow method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetWindow() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        instance.setWindow(window);
        
        Assert.assertEquals(window, instance.getWindow());
    }

    /**
     * Test of assignDataPatternsToIndividual method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testAssignDataPatternsToIndividualTest() {
        SinglePopulationDataClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        CentroidHolder candidateSolution = new CentroidHolder();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(3);
        instance.setWindow(window);
        instance.getWindow().initializeWindow();
        
        candidateSolution.add(ClusterCentroid.of(1.25,1.1,1.3,1.9));
        candidateSolution.add(ClusterCentroid.of(1.92,2.6,3.1,1.8));
        candidateSolution.add(ClusterCentroid.of(0.9,1.1,0.85,0.79));
        
        DataTable dataset = instance.getWindow().getCurrentDataset();
        
        instance.assignDataPatternsToIndividual(candidateSolution, dataset);
        Assert.assertTrue(candidateSolution.get(0).getDataItems().contains(Vector.of(1.0,1.0,1.0,2.0)));
        Assert.assertTrue(candidateSolution.get(1).getDataItems().contains(Vector.of(2.0,3.0,4.0,2.0)));
        Assert.assertTrue(candidateSolution.get(2).getDataItems().contains(Vector.of(1.0,1.0,1.0,1.0)));
    }
}
