/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.util;

import junit.framework.Assert;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
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

/**
 *
 * @author Kris
 */
public class ZaharieComponentVarianceTest {
   
    /**
     * Test of getZaharieMeasure method, of class ZaharieComponentVariance.
     */
    @Test
    public void testGetZaharieMeasure() {
        System.out.println("getZaharieMeasure");
        Vector currentVariance = Vector.of(1.1,2.3);
        Vector previousVariance = Vector.of(2.2,1.5);
        ZaharieComponentVariance instance = new ZaharieComponentVariance();
        Vector expResult = Vector.of(1.8, Math.round(0.5869565217391304 * 5) / 5);
        Vector result = instance.getZaharieMeasure(currentVariance, previousVariance);
        Assert.assertEquals(expResult.get(0).doubleValue(), result.get(0).doubleValue());
        Assert.assertEquals(Math.round(expResult.get(1).doubleValue() * 5) / 5, Math.round(result.get(1).doubleValue() * 5) / 5);
    }

    /**
     * Test of calculateVariance method, of class ZaharieComponentVariance.
     */
    @Test
    public void testCalculateVariance() {
        System.out.println("calculateVariance");
        Topology<Entity> topology = new GBestTopology<Entity>();
        ComponentBasedIndividual individual1 = new ComponentBasedIndividual();
        individual1.setCandidateSolution(Vector.of(1,2,3));
        ComponentBasedIndividual individual2 = new ComponentBasedIndividual();
        individual2.setCandidateSolution(Vector.of(3,2,1));
        topology.add(individual1);
        topology.add(individual2);
        
        ZaharieComponentVariance instance = new ZaharieComponentVariance();
        Vector expResult = Vector.of(1,0,1);
        Vector result = instance.calculateVariance(topology);
        Assert.assertEquals(expResult.get(0).doubleValue(), result.get(0).doubleValue());
        Assert.assertEquals(expResult.get(1).doubleValue(), result.get(1).doubleValue());
        Assert.assertEquals(expResult.get(2).doubleValue(), result.get(2).doubleValue());
    }
}
