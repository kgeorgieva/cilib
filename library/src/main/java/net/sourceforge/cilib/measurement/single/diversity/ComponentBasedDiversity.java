/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class ComponentBasedDiversity implements Measurement<Vector>{
    Diversity delegate;
    
    public ComponentBasedDiversity() {
        delegate = new AverageDiversityAroundAllEntities();
    }
    
   public ComponentBasedDiversity(ComponentBasedDiversity copy) {
       delegate = copy.delegate;
   }
   
   @Override
   public ComponentBasedDiversity getClone() {
       return new ComponentBasedDiversity(this);
   }

    public Vector getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm.getClone();
        Topology<Entity> topology = (Topology<Entity>) populationBasedAlgorithm.getTopology().getClone();
        Topology<Entity> populationBasedAlgorithmTopology = (Topology<Entity>) populationBasedAlgorithm.getTopology();
        int totalDimensions = topology.get(0).getCandidateSolution().size();
        
        Vector.Builder diversity = Vector.newBuilder();
        for(int d = 0; d < totalDimensions; d++) {
            ArrayList<Entity> oneDimensionalList = new ArrayList<Entity>();
            for(Entity entity : topology) {
                Entity oneDimEntity = entity.getClone();
                oneDimEntity.setCandidateSolution(Vector.of(((Vector) entity.getCandidateSolution()).get(d)));
                oneDimensionalList.add(oneDimEntity);
            }
            
            populationBasedAlgorithmTopology.clear();
            populationBasedAlgorithmTopology.addAll(oneDimensionalList);
            diversity.add(delegate.getValue(populationBasedAlgorithm));
        }
        
        return diversity.build();
    }
   
}
