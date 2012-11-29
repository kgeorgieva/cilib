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
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;

/**
 *
 * @author Kris
 */
public class StandardClusteringDEIterationStrategyTest {
    
    @Test
    public void performIterationTest() {
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
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
        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
   
}
