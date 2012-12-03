/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update;

import junit.framework.Assert;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

public class BrownianUpdateStrategyTest {

    /**
     * Test of update method, of class BrownianUpdateStrategy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        GBestTopology topology = new GBestTopology();
        Individual individual = new Individual();
        individual.setCandidateSolution(Vector.of(1,2,3,4));
        BrownianUpdateStrategy instance = new BrownianUpdateStrategy();
        
        Individual newIndividual = (Individual) instance.update(individual, topology);
        
        Assert.assertFalse(newIndividual.getCandidateSolution().containsAll(Vector.of(1,2,3,4)));
    }
}
