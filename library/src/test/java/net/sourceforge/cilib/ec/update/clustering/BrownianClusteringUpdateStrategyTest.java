/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class BrownianClusteringUpdateStrategyTest {

    /**
     * Test of update method, of class BrownianClusteringUpdateStrategy.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        GBestTopology topology = new GBestTopology();
        ClusterIndividual individual = new ClusterIndividual();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4));
        holder.add(ClusterCentroid.of(3,4,5,6));
        individual.setCandidateSolution(holder.getClone());
        BrownianClusteringUpdateStrategy instance = new BrownianClusteringUpdateStrategy();
        
        Individual newIndividual = (Individual) instance.update(individual, topology);
        
        Assert.assertFalse(newIndividual.getCandidateSolution().containsAll(holder));
    }
}
