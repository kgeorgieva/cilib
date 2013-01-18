/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.entity.operators.crossover.de;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.ec.ComponentBasedIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class ComponentBasedBinomialCrossover implements CrossoverStrategy {

    private ProbabilityDistributionFunction random;
    
    public ComponentBasedBinomialCrossover() {
        this.random = new UniformDistribution();
    }
    
    public ComponentBasedBinomialCrossover(ComponentBasedBinomialCrossover copy) {
        this.random = copy.random;
    }

    @Override
    public ComponentBasedBinomialCrossover getClone() {
        return new ComponentBasedBinomialCrossover(this);
    }
    
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionBinomialCrossover requires 2 parents.");

        ComponentBasedIndividual current = (ComponentBasedIndividual) parentCollection.get(0);
        Vector parentVector = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector trialVector = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector.Builder offspringVector = Vector.newBuilder();

        //this is the index of the dimension that will always be included
        int i = Double.valueOf(random.getRandomNumber(0, parentVector.size())).intValue();

        for (int j = 0; j < parentVector.size(); j++) {
            ControlParameter crossoverPointProbability = current.getCrossoverProbabilityPerComponent().get(i);
            if (random.getRandomNumber() < crossoverPointProbability.getParameter() || j == i) {
                offspringVector.add(trialVector.get(j));
            } else {
                offspringVector.add(parentVector.get(j));
            }
        }

        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setCandidateSolution(offspringVector.build());
        
        return Arrays.asList(offspring);
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setCrossoverPointProbability(SettableControlParameter crossoverPointProbability) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }

    public SettableControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Can not get. Component specific parameter held by parent entity. Each dimension has its own.");
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
    
    public void setCrossoverPointProbability(double crossoverPointProbability) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }
    
    public void setCrossoverProbabilityParameter(SettableControlParameter crossoverPointProbability) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }
    
}
