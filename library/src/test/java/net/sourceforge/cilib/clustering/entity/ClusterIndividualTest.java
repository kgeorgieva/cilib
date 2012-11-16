/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.entity;

import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.entity.initialization.StandardCentroidInitializationStrategy;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
/**
 *
 * @author Kris
 */
public class ClusterIndividualTest {
    
    @Test
    public void initialiseTest() {
        ClusterIndividual instance = new ClusterIndividual();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setNumberOfClusters(3);
        
        instance.initialise(problem);
        
        Assert.assertTrue(instance.getCandidateSolution().size() > 0);
    }

    @Test
    public void setCandidateSolutionTest() {
        ClusterIndividual instance = new ClusterIndividual();
        CentroidHolder holder = new CentroidHolder();
        instance.setCandidateSolution(holder);
        Assert.assertEquals(holder, instance.getCandidateSolution());
    }

    @Test
    public void getDimensionTest() {
        ClusterIndividual instance = new ClusterIndividual();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setNumberOfClusters(3);
        
        instance.initialise(problem);
        
        Assert.assertEquals(3, instance.getDimension());
    }

    @Test
    public void getNumberOfClustersTest() {
        ClusterIndividual instance = new ClusterIndividual();
        instance.setNumberOfClusters(3);
        
        Assert.assertEquals(3, instance.getNumberOfClusters());
    }

    @Test
    public void setNumberOfClustersTest() {
        ClusterIndividual instance = new ClusterIndividual();
        instance.setNumberOfClusters(3);
        
        Assert.assertEquals(3, instance.getNumberOfClusters());
    }

    @Test
    public void getCentroidInitialisationStrategyTest() {
        ClusterIndividual instance = new ClusterIndividual();
        StandardCentroidInitializationStrategy strategy = new StandardCentroidInitializationStrategy();
        instance.setCentroidInitialisationStrategy(strategy);
        Assert.assertEquals(strategy, instance.getCentroidInitialisationStrategy());
    }

    @Test
    public void setCentroidInitialisationStrategyTest() {
        ClusterIndividual instance = new ClusterIndividual();
        StandardCentroidInitializationStrategy strategy = new StandardCentroidInitializationStrategy();
        instance.setCentroidInitialisationStrategy(strategy);
        Assert.assertEquals(strategy, instance.getCentroidInitialisationStrategy());
    }
}
