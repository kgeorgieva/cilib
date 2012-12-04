/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
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
        SinglePopulationBasedAlgorithm algorithm = new DataClusteringEC();
        algorithm.setTopology(topology);
        
        Individual newIndividual = (Individual) instance.update(individual, algorithm);
        
        Assert.assertFalse(newIndividual.getCandidateSolution().containsAll(holder));
    }

    /**
     * Test of getTrialEntity method, of class StandardClusteringDEUpdateStrategy.
     */
    @Test
    public void testGetTrialEntity() {
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
        holder3.add(ClusterCentroid.of(1,2));
        holder3.add(ClusterCentroid.of(3,4));
        indiv3.setCandidateSolution(holder3);
        indiv3.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        ClusterIndividual indiv4 = new ClusterIndividual();
        CentroidHolder holder4 = new CentroidHolder();
        holder4.add(ClusterCentroid.of(1,2));
        holder4.add(ClusterCentroid.of(3,4));
        indiv4.setCandidateSolution(holder4);
        indiv4.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        
        GBestTopology topology = new GBestTopology();
        topology.add(indiv1);
        topology.add(indiv2);
        topology.add(indiv3);
        topology.add(indiv4);
        
        StandardClusteringDEUpdateStrategy instance = new StandardClusteringDEUpdateStrategy();
        ClusterIndividual result = instance.getTrialEntity(indiv1, indiv2, topology);
        
        Assert.assertTrue(result.getCandidateSolution().containsAll(indiv1.getCandidateSolution()));
    }

    /**
     * Test of getOffspring method, of class StandardClusteringDEUpdateStrategy.
     */
    @Test
    public void testGetOffspring() {
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
        
        StandardClusteringDEUpdateStrategy instance = new StandardClusteringDEUpdateStrategy();
        ClusterIndividual result = instance.getOffspring(indiv1, indiv2);
        
        boolean resultToCheck = (((CentroidHolder) result.getCandidateSolution()).get(1).get(0).doubleValue() == 3.0 ||
                ((CentroidHolder) result.getCandidateSolution()).get(1).get(0).doubleValue() == 4.0) &&
                (((CentroidHolder) result.getCandidateSolution()).get(1).get(1).doubleValue() == 3.0 ||
                ((CentroidHolder) result.getCandidateSolution()).get(1).get(1).doubleValue() == 4.0);
        Assert.assertTrue(resultToCheck);
    }
}
