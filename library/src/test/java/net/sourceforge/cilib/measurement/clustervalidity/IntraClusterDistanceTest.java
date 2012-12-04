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
import net.sourceforge.cilib.type.types.container.Vector;

public class IntraClusterDistanceTest {
   @Test
   public void calculateIntraClusterDistanceTest() {
       IntraClusterDistance measure = new IntraClusterDistance();
       CentroidHolder holder = new CentroidHolder();
       ClusterCentroid centroid1 = ClusterCentroid.of(1,5);
       ClusterCentroid centroid2 = ClusterCentroid.of(7,2);
       centroid1.addDataItem(2.0, Vector.of(1.5,4.2));
       centroid1.addDataItem(2.0, Vector.of(2.0,5.1));
       centroid2.addDataItem(3.0, Vector.of(6.8,1.2));
       centroid2.addDataItem(3.0, Vector.of(8.1,2.5));
       
       holder.add(centroid1);
       holder.add(centroid2);
       
       double result = measure.calculateIntraClusterDistance(holder);
       
       Assert.assertTrue(Math.round(0.9953278494501844 * 5) / 5 == Math.round(result * 5) / 5);
   }

}
