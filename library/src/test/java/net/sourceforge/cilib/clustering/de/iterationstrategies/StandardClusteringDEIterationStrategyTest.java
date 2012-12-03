/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import org.junit.Test;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.RandomBoundedInitializationStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;

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
