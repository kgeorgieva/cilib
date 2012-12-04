/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import org.junit.Assert;
import org.junit.Test;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

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
