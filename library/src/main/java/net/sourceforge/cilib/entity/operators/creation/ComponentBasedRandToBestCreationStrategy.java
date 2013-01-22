/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.List;
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
 * Component based version of the RandToBest creation strategy. Described by 
 * Zaharie in her 2003 paper "Control of Population Diversity and Adaptation 
 * in Differential Evolution Algorithms" published in the Proceedings of Mendel
 * 2003, 9th International Conference on Soft Computing.
 * 
 * It performs the same task as the RandToBest creation strategy but uses the 
 * parameters held by the individual instead of by the strategy. A different 
 * parameter is used for each different dimension (component).
 */
public class ComponentBasedRandToBestCreationStrategy extends RandCreationStrategy {
    /*
     * Default constructor for ComponentBasedRandToBestCreationStrategy
     */
    public ComponentBasedRandToBestCreationStrategy() {
        super();
    }

    /*
     * Copy constructor for ComponentBasedRandToBestCreationStrategy
     * @param copy The ComponentBasedRandToBestCreationStrategy to be copied
     */
    public ComponentBasedRandToBestCreationStrategy(ComponentBasedRandToBestCreationStrategy other) {
        super(other);
    }

    /*
     * Creates a trial vector using the list of parameters held by the current individual
     * @param targetEntity The selected target entity
     * @param current The current entity
     * @param topology The entire topology
     * @return The generated trial entity
     */
    @Override
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

    /*
     * The clone methid for ComponentBasedRandToBestCreationStrategy
     * @return A new instance of this ComponentBasedRandToBestCreationStrategy
     */
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