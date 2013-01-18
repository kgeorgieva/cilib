/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

/**
 *
 * @author Kris
 */
public class ComponentBasedRandToBestCreationStrategy extends RandCreationStrategy {
    public ComponentBasedRandToBestCreationStrategy() {
        super();
    }

    public ComponentBasedRandToBestCreationStrategy(ComponentBasedRandToBestCreationStrategy other) {
        super(other);
    }

    public Entity create(Entity targetEntity, Entity current,
            Topology<? extends Entity> topology) {
        Entity bestEntity = Topologies.getBestEntity(topology);
        RandomProvider random = new MersenneTwister();
        List<Entity> participants = (List<Entity>) Selection.copyOf(topology)
                .exclude(targetEntity, bestEntity, current)
                .orderBy(new RandomArrangement(random))
                .select(Samples.first((int) numberOfDifferenceVectors.getParameter()).unique());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = ((Vector) targetEntity.getCandidateSolution());
        //multiply by appropriate greedyness parameter
        int index = 0;
        for(Numeric value : targetVector) {
            double newValue = value.doubleValue() * (1 - ((ComponentBasedIndividual) current).getGreedPerComponent().get(index).getParameter());
            targetVector.setReal(index, newValue);
            index++;  
        }
        
        Vector bestVector = ((Vector) bestEntity.getCandidateSolution());
        //multiply by appropriate greedyness parameter
        index = 0;
        for(Numeric value : bestVector) {
            double newValue = value.doubleValue() * ((ComponentBasedIndividual) current).getGreedPerComponent().get(index).getParameter();
            bestVector.setReal(index, newValue);
            index++;  
        }
        
        //multiply by appropriate scaling factor
        index = 0;
        for(Numeric value :differenceVector) {
            double newValue = value.doubleValue() * ((ComponentBasedIndividual) current).getScalingFactorPerComponent().get(index).getParameter();
            differenceVector.setReal(index, newValue);
            index++;  
        }
        
        Vector trialVector = bestVector.plus(targetVector.plus(differenceVector));

        Entity trialEntity = current.getClone();
        trialEntity.setCandidateSolution(trialVector);

        return trialEntity;
    }

    @Override
    public ComponentBasedRandToBestCreationStrategy getClone() {
        return new ComponentBasedRandToBestCreationStrategy(this);
    }

    public void setGreedynessParameter(ControlParameter greedynessParameter) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }
    
    public SettableControlParameter getScaleParameter() {
        throw new UnsupportedOperationException("Can not get. Component specific parameter held by parent entity. Each dimension has its own.");
    }

    public void setScaleParameter(SettableControlParameter scaleParameter) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }
    
    public void setScaleParameter(double scaleParameterValue) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }
    
    
}