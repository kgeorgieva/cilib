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

public class StandardClusteringDEUpdateStrategyTest {
    
    /**
     * Test of update method, of class StandardClusteringDEUpdateStrategy.
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
        ClusterIndividual individual2 = new ClusterIndividual();
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(6,7,8,9));
        holder2.add(ClusterCentroid.of(1,2,5,4));
        individual2.setCandidateSolution(holder2.getClone());
        ClusterIndividual individual3 = new ClusterIndividual();
        CentroidHolder holder3 = new CentroidHolder();
        holder3.add(ClusterCentroid.of(1,4,7,8));
        holder3.add(ClusterCentroid.of(9,6,5,2));
        individual3.setCandidateSolution(holder3.getClone());
        ClusterIndividual individual4 = new ClusterIndividual();
        CentroidHolder holder4 = new CentroidHolder();
        holder4.add(ClusterCentroid.of(1,2,3,4));
        holder4.add(ClusterCentroid.of(5,5,5,5));
        individual4.setCandidateSolution(holder4.getClone());
        topology.add(individual);
        topology.add(individual2);
        topology.add(individual3);
        topology.add(individual4);
        StandardClusteringDEUpdateStrategy instance = new StandardClusteringDEUpdateStrategy();
        
        Individual newIndividual = (Individual) instance.update(individual, topology);
        
        Assert.assertFalse(newIndividual.getCandidateSolution().containsAll(holder));
    }

    /**
     * Test of getTrialEntity method, of class StandardClusteringDEUpdateStrategy.
     */
    @Test
    public void testGetTrialEntity() {
       
    }

    /**
     * Test of getOffspring method, of class StandardClusteringDEUpdateStrategy.
     */
    @Test
    public void testGetOffspring() {
        
    }
}
