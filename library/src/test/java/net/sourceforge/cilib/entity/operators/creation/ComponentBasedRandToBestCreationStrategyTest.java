/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.math.random.generator.seeder.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.seeder.Seeder;
import net.sourceforge.cilib.math.random.generator.seeder.ZeroSeederStrategy;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kris
 */
public class ComponentBasedRandToBestCreationStrategyTest {
    
    /**
     * Test of create method, of class ComponentBasedRandToBestCreationStrategy.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            ComponentBasedIndividual targetEntity = new ComponentBasedIndividual();        
            ComponentBasedIndividual entityRandom = new ComponentBasedIndividual();
            ComponentBasedIndividual otherEntity2 = new ComponentBasedIndividual();
            ComponentBasedIndividual otherEntity3 = new ComponentBasedIndividual();
            ComponentBasedIndividual current = new ComponentBasedIndividual();
            current.setCandidateSolution(Vector.of(1,2));

            ArrayList<SettableControlParameter> crossoverParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par7 = new StandardUpdatableControlParameter();
            par7.setParameter(0.5);
            StandardUpdatableControlParameter par8 = new StandardUpdatableControlParameter();
            par8.setParameter(0.5);
            crossoverParams2.add(par7);
            crossoverParams2.add(par8);

            ArrayList<SettableControlParameter> scalingParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par9 = new StandardUpdatableControlParameter();
            par9.setParameter(0.5);
            StandardUpdatableControlParameter par10 = new StandardUpdatableControlParameter();
            par10.setParameter(0.5);
            scalingParams2.add(par9);
            scalingParams2.add(par10);

            ArrayList<SettableControlParameter> greedParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par11 = new StandardUpdatableControlParameter();
            par11.setParameter(0.5);
            StandardUpdatableControlParameter par12 = new StandardUpdatableControlParameter();
            par12.setParameter(0.5);
            greedParams2.add(par11);
            greedParams2.add(par12);

            current.setCrossoverProbabilityPerComponent(crossoverParams2);
            current.setScalingFactorPerComponent(scalingParams2);
            current.setGreedPerComponent(greedParams2);

            Topology<ComponentBasedIndividual> topology = new GBestTopology<ComponentBasedIndividual>();
            topology.add(current);
            topology.add(targetEntity);
            topology.add(entityRandom);
            topology.add(otherEntity2);
            topology.add(otherEntity3);

            targetEntity.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
            targetEntity.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.1));
            entityRandom.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
            entityRandom.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.2));
            otherEntity2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
            otherEntity2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.3));
            otherEntity3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
            otherEntity3.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.4));

            ComponentBasedRandToBestCreationStrategy instance = new ComponentBasedRandToBestCreationStrategy();
            ComponentBasedIndividual resultEntity = (ComponentBasedIndividual) instance.create(entityRandom, current, topology);

            Assert.assertEquals(0.1, ((Vector) resultEntity.getCandidateSolution()).doubleValueOf(0), 0.001);

        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
    
    @Test
    public void testCreateFail() {
        System.out.println("create");
        
        SeedSelectionStrategy seedStrategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            ComponentBasedIndividual targetEntity = new ComponentBasedIndividual();        
            ComponentBasedIndividual entityRandom = new ComponentBasedIndividual();
            ComponentBasedIndividual otherEntity2 = new ComponentBasedIndividual();
            ComponentBasedIndividual otherEntity3 = new ComponentBasedIndividual();
            ComponentBasedIndividual current = new ComponentBasedIndividual();
            current.setCandidateSolution(Vector.of(1,2));

            ArrayList<SettableControlParameter> crossoverParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par7 = new StandardUpdatableControlParameter();
            par7.setParameter(0.9);
            StandardUpdatableControlParameter par8 = new StandardUpdatableControlParameter();
            par8.setParameter(0.9);
            crossoverParams2.add(par7);
            crossoverParams2.add(par8);

            ArrayList<SettableControlParameter> scalingParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par9 = new StandardUpdatableControlParameter();
            par9.setParameter(0.9);
            StandardUpdatableControlParameter par10 = new StandardUpdatableControlParameter();
            par10.setParameter(0.9);
            scalingParams2.add(par9);
            scalingParams2.add(par10);

            ArrayList<SettableControlParameter> greedParams2 = new ArrayList<SettableControlParameter>();
            StandardUpdatableControlParameter par11 = new StandardUpdatableControlParameter();
            par11.setParameter(0.5);
            StandardUpdatableControlParameter par12 = new StandardUpdatableControlParameter();
            par12.setParameter(0.5);
            greedParams2.add(par11);
            greedParams2.add(par12);

            current.setCrossoverProbabilityPerComponent(crossoverParams2);
            current.setScalingFactorPerComponent(scalingParams2);
            current.setGreedPerComponent(greedParams2);

            Topology<ComponentBasedIndividual> topology = new GBestTopology<ComponentBasedIndividual>();
            topology.add(current);
            topology.add(targetEntity);
            topology.add(entityRandom);
            topology.add(otherEntity2);
            topology.add(otherEntity3);

            targetEntity.getProperties().put(EntityType.FITNESS, new MinimisationFitness(0.0));
            targetEntity.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.1));
            entityRandom.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
            entityRandom.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.2));
            otherEntity2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
            otherEntity2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.3));
            otherEntity3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
            otherEntity3.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.4));

            ComponentBasedRandToBestCreationStrategy instance = new ComponentBasedRandToBestCreationStrategy();
            ComponentBasedIndividual resultEntity = (ComponentBasedIndividual) instance.create(entityRandom, current, topology);

            Assert.assertEquals(0.1, ((Vector) resultEntity.getCandidateSolution()).doubleValueOf(0), 0.001);

        } finally {
            Seeder.setSeederStrategy(seedStrategy);
        }
    }
    
    

}
