/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.ClampingBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;

/**
 *
 * @author Kris
 */
public class StandardClusteringDEIterationStrategyTest {
    
    @Test
    public void performIterationTest() {
       DataClusteringEC instance = new DataClusteringEC();
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
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        instance.performInitialisation();
        
        ClusterIndividual individualBefore = (ClusterIndividual) instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterIndividual individualAfter = (ClusterIndividual) instance.getTopology().get(0).getClone();
       
        Assert.assertFalse(individualAfter.getCandidateSolution().containsAll(individualBefore.getCandidateSolution())); 
    }
    
    @Test
    public void getTrialEntityTest() {
        ClusterIndividual indiv1 = new ClusterIndividual();
        CentroidHolder holder1 = new CentroidHolder();
        holder1.add(ClusterCentroid.of(1,2));
        holder1.add(ClusterCentroid.of(3,4));
        indiv1.setCandidateSolution(holder1);
        indiv1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        ClusterIndividual indiv2 = new ClusterIndividual();
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(1,2));
        holder2.add(ClusterCentroid.of(3,4));
        indiv2.setCandidateSolution(holder2);
        indiv2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        ClusterIndividual indiv3 = new ClusterIndividual();
        CentroidHolder holder3 = new CentroidHolder();
        holder3.add(ClusterCentroid.of(2,3));
        holder3.add(ClusterCentroid.of(4,5));
        indiv3.setCandidateSolution(holder3);
        indiv3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        ClusterIndividual indiv4 = new ClusterIndividual();
        CentroidHolder holder4 = new CentroidHolder();
        holder4.add(ClusterCentroid.of(2,3));
        holder4.add(ClusterCentroid.of(4,5));
        indiv4.setCandidateSolution(holder4);
        indiv4.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        GBestTopology topology = new GBestTopology();
        topology.add(indiv1);
        topology.add(indiv2);
        topology.add(indiv3);
        topology.add(indiv4);
        
        StandardClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        ClusterIndividual result = instance.getTrialEntity(indiv1, indiv2, topology);
        
        Assert.assertTrue(result.getCandidateSolution().containsAll(indiv1.getCandidateSolution()));
        
    }
    
    @Test
    public void getOffspringTest() {
        ClusterIndividual indiv1 = new ClusterIndividual();
        CentroidHolder holder1 = new CentroidHolder();
        holder1.add(ClusterCentroid.of(1,2));
        holder1.add(ClusterCentroid.of(3,4));
        indiv1.setCandidateSolution(holder1);
        indiv1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        ClusterIndividual indiv2 = new ClusterIndividual();
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(1,2));
        holder2.add(ClusterCentroid.of(4,3));
        indiv2.setCandidateSolution(holder2);
        indiv2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        StandardClusteringDEIterationStrategy instance = new StandardClusteringDEIterationStrategy();
        ClusterIndividual result = instance.getOffspring(indiv1, indiv2);
        
        boolean resultToCheck = (((CentroidHolder) result.getCandidateSolution()).get(1).get(0).doubleValue() == 3.0 ||
                ((CentroidHolder) result.getCandidateSolution()).get(1).get(0).doubleValue() == 4.0) &&
                (((CentroidHolder) result.getCandidateSolution()).get(1).get(1).doubleValue() == 3.0 ||
                ((CentroidHolder) result.getCandidateSolution()).get(1).get(1).doubleValue() == 4.0);
        Assert.assertTrue(resultToCheck);
    }
           
}
