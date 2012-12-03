/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update;

import junit.framework.Assert;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class StandardDEUpdateStrategyTest {
    /**
     * Test of update method, of class StandardDEUpdateStrategy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        GBestTopology topology = new GBestTopology();
        Individual individual = new Individual();
        individual.setCandidateSolution(Vector.of(1,2,3,4));
        Individual individual2 = new Individual();
        individual2.setCandidateSolution(Vector.of(6,7,8,9));
        Individual individual3 = new Individual();
        individual3.setCandidateSolution(Vector.of(8,5,4,1));
        Individual individual4 = new Individual();
        individual4.setCandidateSolution(Vector.of(3,6,4,7));
        topology.add(individual);
        topology.add(individual2);
        topology.add(individual3);
        topology.add(individual4);
        StandardDEUpdateStrategy instance = new StandardDEUpdateStrategy();
        
        Individual newIndividual = (Individual) instance.update(individual, topology);
        
        Assert.assertFalse(newIndividual.getCandidateSolution().containsAll(Vector.of(1,2,3,4)));
    }
}
