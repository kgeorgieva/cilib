/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Brownian update strategy described in Mendes and Mohais' 
 * 2005 IEEE paper "DynDE: a Differential Evolution for Dynamic Optimization Problems".
 */
public class BrownianUpdateStrategy implements UpdateStrategy{
    GaussianDistribution random;
    
    /*
     * Default constructor for BrownianUpdateStrategy
     */
    public BrownianUpdateStrategy() {
        random = new GaussianDistribution();
        random.setMean(0);
        random.setDeviation(0.2);
    }
    
    /*
     * Copy Constructor for BrownianUpdateStrategy
     * @param copy The BrownianUpdateStrategy to be copied
     */
     public BrownianUpdateStrategy(BrownianUpdateStrategy copy) {
         random = copy.random;
     }
    
     /*
      * Clone method for BrownianUpdateStrategy
      * @return A new instance of this BrownianUpdateStrategy
      */
    public UpdateStrategy getClone() {
        return new BrownianUpdateStrategy(this);
    }

    /*
     * Creates a brownian individual by generating a point around the best 
     * individual by adding a random variable sampled form a normal distribution.
     */
    public Entity update(Entity currentEntity, SinglePopulationBasedAlgorithm algorithm) {
        Entity bestEntity = currentEntity.getClone();
        bestEntity.setCandidateSolution((StructuredType) algorithm.getBestSolution().getPosition().getClone());
        bestEntity.getProperties().put(EntityType.FITNESS, algorithm.getBestSolution().getFitness().getClone());
        
        Vector solution = (Vector) bestEntity.getCandidateSolution();
        Vector.Builder builder = Vector.newBuilder();
        for(Numeric number : solution) {
            builder.add(number.doubleValue() + random.getRandomNumber());
        }
        
        bestEntity.setCandidateSolution(builder.build());
        
        return bestEntity;
    }
    
}
