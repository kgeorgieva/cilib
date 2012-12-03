/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update.clustering;

import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 *
 * @author Kris
 */
public class BrownianClusteringUpdateStrategy implements UpdateStrategy{
    GaussianDistribution random;
    
    public BrownianClusteringUpdateStrategy() {
        random = new GaussianDistribution();
        random.setMean(0);
        random.setDeviation(0.2);
    }
    
     public BrownianClusteringUpdateStrategy(BrownianClusteringUpdateStrategy copy) {
         random = copy.random;
     }
    
    public UpdateStrategy getClone() {
        return new BrownianClusteringUpdateStrategy(this);
    }

    public Entity update(Entity currentEntity, Topology topology) {
        CentroidHolder solution = (CentroidHolder) currentEntity.getCandidateSolution();
        CentroidHolder newSolution = new CentroidHolder();
        ClusterCentroid newCentroid;
        
        for(ClusterCentroid centroid : solution) {
            newCentroid = new ClusterCentroid();
            for(Numeric number : centroid) {
                newCentroid.add( Real.valueOf(number.doubleValue() + random.getRandomNumber(), number.getBounds()));
            }
            newSolution.add(newCentroid);
        }
        
        Entity newEntity = currentEntity.getClone();
        newEntity.setCandidateSolution(newSolution);
        
        return newEntity;
    }
}
