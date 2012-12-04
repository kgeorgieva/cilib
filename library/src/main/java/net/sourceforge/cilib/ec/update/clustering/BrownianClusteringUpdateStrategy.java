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
 * The Brownian update strategy described in Mendes and Mohais' 
 * 2005 IEEE paper "DynDE: a Differential Evolution for Dynamic Optimization Problems".
 * This one is specific to clustering problems as it deals with CentroidHolders
 */
public class BrownianClusteringUpdateStrategy implements UpdateStrategy{
    GaussianDistribution random;
    
    /*
     * Default Constructor for BrownianClusteringUpdateStrategy
     */
    public BrownianClusteringUpdateStrategy() {
        random = new GaussianDistribution();
        random.setMean(0);
        random.setDeviation(0.2);
    }
    
    /*
     * Copy constructor for BrownianClusteringUpdateStrategy
     * @param copy The BrownianClusteringUpdateStrategy to be copied
     */
     public BrownianClusteringUpdateStrategy(BrownianClusteringUpdateStrategy copy) {
         random = copy.random;
     }
    
     /*
      * Clone method for BrownianClusteringUpdateStrategy
      * @return A new instance of this BrownianClusteringUpdateStrategy
      */
    public UpdateStrategy getClone() {
        return new BrownianClusteringUpdateStrategy(this);
    }

    /*
     * Creates a brownian individual by generating a point around the best 
     * individual by adding a random variable sampled form a normal distribution.
     */
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
