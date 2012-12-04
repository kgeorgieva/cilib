/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.clustering.DynamicClusteringDE;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;

public class DynDEIterationStrategyTest {
    
    @Test
    public void performIterationTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
             StandardMultipopulationAlgorithm instance = new StandardMultipopulationAlgorithm();
             DataClusteringEC alg = new DataClusteringEC();
             QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
             problem.setDomain("R(-5.12:5.12)");
             instance.setOptimisationProblem(problem);
             instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 5));
             DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
             RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
             ClusterIndividual indiv = new ClusterIndividual();
             indiv.setInitialisationStrategy(initStrategy);
             init.setEntityType(indiv);
             init.setEntityNumber(3);
             alg.setInitialisationStrategy(init);
             alg.setSourceURL("library/src/test/resources/datasets/iris2.arff");
             instance.addPopulationBasedAlgorithm(alg);
             DynDEIterationStrategy strategy = new DynDEIterationStrategy();
             instance.setMultiSwarmIterationStrategy(strategy);
             instance.performInitialisation();
             
             ClusterIndividual individualBefore = (ClusterIndividual) instance.getPopulations().get(0).getTopology().get(0).getClone();

             instance.run();

             ClusterIndividual individualAfter = (ClusterIndividual) instance.getPopulations().get(0).getTopology().get(0).getClone();
                
             Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution())); 
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
    
    @Test
    public void processPopulationsTest() {
        performIterationTest();
    }
    
    @Test
    public void reinitialisePopulationTest() {
        DynamicClusteringDE de = new DynamicClusteringDE();
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        GBestTopology topology = new GBestTopology();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        de.setOptimisationProblem(problem);
        de.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterIndividual>();
        RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
        ClusterIndividual indiv = new ClusterIndividual();
        indiv.setInitialisationStrategy(initStrategy);
        init.setEntityType(indiv);
        init.setEntityNumber(2);
        de.setInitialisationStrategy(init);
        de.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        de.algorithmInitialisation();
        
        ClusterIndividual topologyBefore = (ClusterIndividual) de.getTopology().get(0).getClone();
        
        instance.reinitialisePopulation(de);
        
        ClusterIndividual topologyAfter = (ClusterIndividual) de.getTopology().get(0).getClone();
        
        Assert.assertFalse(topologyBefore.getCandidateSolution().containsAll(topologyAfter.getCandidateSolution()));
    }
    
    @Test
    public void assignDataPatternsToEntityTest() {
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        StandardClusteringDEIterationStrategy standardStrategy = new StandardClusteringDEIterationStrategy();
        
        CentroidHolder candidateSolution = new CentroidHolder();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(3);
        standardStrategy.setWindow(window);
        standardStrategy.getWindow().initializeWindow();
        
        candidateSolution.add(ClusterCentroid.of(1.25,1.1,1.3,1.9));
        candidateSolution.add(ClusterCentroid.of(1.92,2.6,3.1,1.8));
        candidateSolution.add(ClusterCentroid.of(0.9,1.1,0.85,0.79));
        
        DataTable dataset = standardStrategy.getWindow().getCurrentDataset();
        
        instance.assignDataPatternsToEntity(candidateSolution, dataset);
        junit.framework.Assert.assertTrue(candidateSolution.get(0).getDataItems().contains(Vector.of(1.0,1.0,1.0,2.0)));
        junit.framework.Assert.assertTrue(candidateSolution.get(1).getDataItems().contains(Vector.of(2.0,3.0,4.0,2.0)));
        junit.framework.Assert.assertTrue(candidateSolution.get(2).getDataItems().contains(Vector.of(1.0,1.0,1.0,1.0)));
    }
    
    @Test
    public void aDistanceIsSmallerThanRadiusTest() {
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        CentroidHolder holder1 = new CentroidHolder();
        CentroidHolder holder2 = new CentroidHolder();
        
        holder1.add(ClusterCentroid.of(1,2,3));
        holder1.add(ClusterCentroid.of(3,4,5));
        holder1.add(ClusterCentroid.of(1,5,2));
        
        holder2.add(ClusterCentroid.of(9,2,7));
        holder2.add(ClusterCentroid.of(10,10,10));
        holder2.add(ClusterCentroid.of(1,1,1));
        
        Assert.assertFalse(instance.aDistanceIsSmallerThanRadius(holder1,holder2));
        
        holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(1,2,3));
        holder2.add(ClusterCentroid.of(3,4,5));
        holder2.add(ClusterCentroid.of(1,5,2));
        
        Assert.assertTrue(instance.aDistanceIsSmallerThanRadius(holder1,holder2));
        
    }
    
    @Test
    public void getExclusionRadiusTest() {
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        instance.setExclusionRadius(2.3);
        
        Assert.assertTrue(2.3 == instance.getExclusionRadius());
    }

    @Test
    public void setExclusionRadiusTest() {
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        instance.setExclusionRadius(2.3);
        
        Assert.assertTrue(2.3 == instance.getExclusionRadius());
    }

    @Test
    public void getMeasureTest() {
       DynDEIterationStrategy instance = new DynDEIterationStrategy();
        EuclideanDistanceMeasure measure = new EuclideanDistanceMeasure();
        instance.setMeasure(measure);
        
        Assert.assertEquals(measure, instance.getMeasure());
    }

    @Test
    public void setMeasureTest() {
        DynDEIterationStrategy instance = new DynDEIterationStrategy();
        EuclideanDistanceMeasure measure = new EuclideanDistanceMeasure();
        instance.setMeasure(measure);
        
        Assert.assertEquals(measure, instance.getMeasure());
    }
}
