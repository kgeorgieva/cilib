/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.DataDependantInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.changeDetection.ChangeDetectionStrategy;
import net.sourceforge.cilib.util.changeDetection.IterationBasedChangeDetectionStrategy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReinitialisingClusteringDEIterationStrategyTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
   
    /**
     * Test of performIteration method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            DataClusteringEC instance = new DataClusteringEC();
             QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
             problem.setDomain("R(-5.12:5.12)");
             ReinitialisingClusteringDEIterationStrategy strategy = new ReinitialisingClusteringDEIterationStrategy();
             SinglePopulationDataClusteringDEIterationStrategy delegate = new StandardClusteringDEIterationStrategy();
             strategy.setDelegate(delegate);
             instance.setIterationStrategy(strategy);
             instance.setOptimisationProblem(problem);
             instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 5));
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

             junit.framework.Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution())); 
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }

    /**
     * Test of getDelegate method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testGetDelegate() {
        System.out.println("getDelegate");
        ReinitialisingClusteringDEIterationStrategy strategy = new ReinitialisingClusteringDEIterationStrategy();
        StandardClusteringDEIterationStrategy delegate = new StandardClusteringDEIterationStrategy();
        strategy.setDelegate(delegate);
        
        Assert.assertEquals(delegate, strategy.getDelegate());
    }

    /**
     * Test of setDelegate method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        ReinitialisingClusteringDEIterationStrategy strategy = new ReinitialisingClusteringDEIterationStrategy();
        StandardClusteringDEIterationStrategy delegate = new StandardClusteringDEIterationStrategy();
        strategy.setDelegate(delegate);
        
        Assert.assertEquals(delegate, strategy.getDelegate());
    }

    /**
     * Test of setBoundaryConstraint method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testSetBoundaryConstraint() {
        System.out.println("setBoundaryConstraint");
        BoundaryConstraint boundaryConstraint = new ClampingBoundaryConstraint();
        ReinitialisingClusteringDEIterationStrategy instance = new ReinitialisingClusteringDEIterationStrategy();
        instance.setBoundaryConstraint(boundaryConstraint);
        
        Assert.assertEquals(boundaryConstraint, instance.getBoundaryConstraint());
    }

    /**
     * Test of setChangeDetectionStrategy method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testSetChangeDetectionStrategy() {
        System.out.println("setChangeDetectionStrategy");
        ChangeDetectionStrategy changeStrategy = new IterationBasedChangeDetectionStrategy();
        ReinitialisingClusteringDEIterationStrategy instance = new ReinitialisingClusteringDEIterationStrategy();
        instance.setChangeDetectionStrategy(changeStrategy);
        
        Assert.assertEquals(changeStrategy, instance.getChangeDetectionStrategy());
    }

    /**
     * Test of getChangeDetectionStrategy method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testGetChangeDetectionStrategy() {
        System.out.println("getChangeDetectionStrategy");
        ChangeDetectionStrategy changeStrategy = new IterationBasedChangeDetectionStrategy();
        ReinitialisingClusteringDEIterationStrategy instance = new ReinitialisingClusteringDEIterationStrategy();
        instance.setChangeDetectionStrategy(changeStrategy);
        
        Assert.assertEquals(changeStrategy, instance.getChangeDetectionStrategy());
    }

    /**
     * Test of reinitializePosition method, of class ReinitialisingClusteringDEIterationStrategy.
     */
    @Test
    public void testReinitializePosition() {
        System.out.println("reinitializePosition");
        Topology<ClusterIndividual> topology = new GBestTopology<ClusterIndividual>();
        RandomBoundedInitializationStrategy initStrategy = new RandomBoundedInitializationStrategy();
        DataDependantInitializationStrategy centroidInitStrategy = new StandardCentroidInitializationStrategy<ClusterIndividual>();
        centroidInitStrategy.setInitialisationStrategy(initStrategy);
        initStrategy.setLowerBound(ConstantControlParameter.of(0));
        initStrategy.setUpperBound(ConstantControlParameter.of(12));
        
        CentroidHolder holder1 = new CentroidHolder();
        holder1.add(ClusterCentroid.of(1,2,3,4,5));
        holder1.add(ClusterCentroid.of(5,4,3,2,1));
        
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(6,7,8,9,10));
        holder2.add(ClusterCentroid.of(10,9,8,7,6));
        
        CentroidHolder holder3 = new CentroidHolder();
        holder3.add(ClusterCentroid.of(1,5,9,7,8));
        holder3.add(ClusterCentroid.of(7,5,3,2,1));
        
        CentroidHolder holder4 = new CentroidHolder();
        holder4.add(ClusterCentroid.of(8,5,2,9,6));
        holder4.add(ClusterCentroid.of(7,4,1,8,5));
        
        ClusterIndividual individual1 = new ClusterIndividual();
        individual1.setCandidateSolution(holder1);
        individual1.setCentroidInitialisationStrategy(centroidInitStrategy);
        
        ClusterIndividual individual2 = new ClusterIndividual();
        individual2.setCandidateSolution(holder2);
        individual2.setCentroidInitialisationStrategy(centroidInitStrategy);
        
        ClusterIndividual individual3 = new ClusterIndividual();
        individual3.setCandidateSolution(holder3);
        individual3.setCentroidInitialisationStrategy(centroidInitStrategy);
        
        ClusterIndividual individual4 = new ClusterIndividual();
        individual4.setCandidateSolution(holder4);
        individual4.setCentroidInitialisationStrategy(centroidInitStrategy);
        
        topology.add(individual1);
        topology.add(individual2);
        topology.add(individual3);
        topology.add(individual4);
        
        Topology<ClusterIndividual> oldTopology = topology.getClone();
        
        ReinitialisingClusteringDEIterationStrategy instance = new ReinitialisingClusteringDEIterationStrategy();
        instance.setReinitialisationPercentage(50);
        instance.reinitializePosition(topology);
        
        int totalChanged = 0;
        
        for(int i = 0; i < topology.size(); i++) {
            if(!((CentroidHolder) topology.get(i).getCandidateSolution()).containsAll(((CentroidHolder) oldTopology.get(i).getCandidateSolution()))){
                totalChanged++;
            }
        }
        
        Assert.assertTrue(totalChanged == 2);
    }
}
