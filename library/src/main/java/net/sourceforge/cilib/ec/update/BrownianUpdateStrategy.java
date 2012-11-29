/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec.update;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class BrownianUpdateStrategy implements UpdateStrategy{
    GaussianDistribution random;
    
    public BrownianUpdateStrategy() {
        random = new GaussianDistribution();
        random.setMean(0);
        random.setDeviation(0.2);
    }
    
     public BrownianUpdateStrategy(BrownianUpdateStrategy copy) {
         random = copy.random;
     }
    
    public UpdateStrategy getClone() {
        return new BrownianUpdateStrategy(this);
    }

    public Entity update(Entity currentEntity, Topology topology) {
        Vector solution = (Vector) currentEntity.getCandidateSolution();
        Vector.Builder builder = Vector.newBuilder();
        for(Numeric number : solution) {
            builder.add(number.doubleValue() + random.getRandomNumber());
        }
        
        Entity newEntity = currentEntity.getClone();
        newEntity.setCandidateSolution(builder.build());
        
        return newEntity;
    }
    
}
