/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
/**
 *
 * @author Kris
 */
public class InterClusterDistanceTest {
    
   @Test
   public void calculateInterClusterDistanceTest() {
       InterClusterDistance measure = new InterClusterDistance();
       CentroidHolder holder = new CentroidHolder();
       holder.add(ClusterCentroid.of(1,5));
       holder.add(ClusterCentroid.of(7,2));
       holder.add(ClusterCentroid.of(9,1));
       double result = measure.calculateInterClusterDistance(holder);
       
       Assert.assertTrue(Math.round(5.96284793999944 * 5) / 5 == Math.round(result * 5) / 5);
   }
}
