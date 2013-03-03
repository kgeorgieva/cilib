/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover.de;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

public class ComponentBasedBinomialCrossoverTest {
    
     /**
     * Test of crossover method, of class ComponentBasedBinomialCrossover.
     */
    @Test
    public void testCrossover() {
        System.out.println("crossover");
        
        Rand.setSeed(0);
        ComponentBasedIndividual i1 = new ComponentBasedIndividual();
        ComponentBasedIndividual i2 = new ComponentBasedIndividual();

        i1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0, 1.0));
        i2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(5.0, 6.0));
        i1.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        i2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.2));

        ArrayList<SettableControlParameter> crossoverParams = new ArrayList<SettableControlParameter>();
        StandardUpdatableControlParameter par1 = new StandardUpdatableControlParameter();
        par1.setParameter(0.0);
        StandardUpdatableControlParameter par2 = new StandardUpdatableControlParameter();
        par2.setParameter(1.0);
        crossoverParams.add(par1);
        crossoverParams.add(par2);

        i1.setCrossoverProbabilityPerComponent(crossoverParams);
        i2.setCrossoverProbabilityPerComponent(crossoverParams);

        List<Entity> parents = new ArrayList<Entity>();
        parents.add(i1);
        parents.add(i2);

        ComponentBasedBinomialCrossover crossoverStrategy = new ComponentBasedBinomialCrossover();

        List<Entity> children = (List<Entity>) crossoverStrategy.crossover(parents);
        Vector child1 = (Vector) children.get(0).getCandidateSolution();
        Vector parent1 = (Vector) i1.getCandidateSolution();
        Vector parent2 = (Vector) i2.getCandidateSolution();

        Assert.assertEquals(1, children.size());

        Assert.assertSame(child1.get(0), parent1.get(0));
        Assert.assertNotSame(child1.get(1), parent1.get(1));
        Assert.assertSame(child1.get(1), parent2.get(1));
        Assert.assertNotSame(child1.get(0), parent2.get(0));
        
    }

    /**
     * Test of setRandom method, of class ComponentBasedBinomialCrossover.
     */
    @Test
    public void testSetRandom() {
        System.out.println("setRandom");
        ProbabilityDistributionFunction random = new CauchyDistribution();
        ComponentBasedBinomialCrossover instance = new ComponentBasedBinomialCrossover();
        instance.setRandom(random);
        
        Assert.assertEquals(random, instance.getRandom());
    }

    /**
     * Test of getRandom method, of class ComponentBasedBinomialCrossover.
     */
    @Test
    public void testGetRandom() {
        System.out.println("getRandom");
        ProbabilityDistributionFunction random = new CauchyDistribution();
        ComponentBasedBinomialCrossover instance = new ComponentBasedBinomialCrossover();
        instance.setRandom(random);
        
        Assert.assertEquals(random, instance.getRandom());
    }

}
