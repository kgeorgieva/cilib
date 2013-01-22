/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
 * Component based version of the binomial crossover strategy. Described by 
 * Zaharie in her 2003 paper "Control of Population Diversity and Adaptation 
 * in Differential Evolution Algorithms" published in the Proceedings of Mendel
 * 2003, 9th International Conference on Soft Computing.
 * 
 * It performs the same task as the DifferentialEvolutionBinomialCrossover creation
 * strategy but uses the  * parameters held by the individual instead of by the 
 * strategy. A different parameter is used for each different dimension (component).
 */
public class ComponentBasedBinomialCrossover implements CrossoverStrategy {

    private ProbabilityDistributionFunction random;
    
    /*
     * Default constructor for ComponentBasedBinomialCrossover
     */
    public ComponentBasedBinomialCrossover() {
        this.random = new UniformDistribution();
    }
    
    /*
     * Copy constructor for ComponentBasedBinomialCrossover
     * @param copy The ComponentBasedBinomialCrossover to be copied
     */
    public ComponentBasedBinomialCrossover(ComponentBasedBinomialCrossover copy) {
        this.random = copy.random;
    }

    /*
     * Clone method for ComponentBasedBinomialCrossover
     * @return A new instance of the ComponentBasedBinomialCrossover
     */
    @Override
    public ComponentBasedBinomialCrossover getClone() {
        return new ComponentBasedBinomialCrossover(this);
    }
    
    /*
     * Combines the parents to generate an offspring entity using the list
     * of parameters held by the individual.
     * @param parentCollection The list of parents to be crossed over
     * @return A list of offspring entities
     */
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
            ControlParameter crossoverPointProbability = current.getCrossoverProbabilityPerComponent().get(j);
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

    /*
     * Set the probability distribution function to be used to determine the index of the dimension
     * of parent 1 which will always be included.
     * @param random The probability districution function
     */
    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    /*
     * Gets the probability distribution function to be used to determine the index of the dimension
     * of parent 1 which will always be included.
     * @return The probability districution function
     */
    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setCrossoverPointProbability(SettableControlParameter crossoverPointProbability) {
        throw new UnsupportedOperationException("Can not set. Component specific parameter held by parent entity. Each dimension has its own.");
    }

    public SettableControlParameter getCrossoverPointProbability() {
        throw new UnsupportedOperationException("Can not get. Component specific parameter held by parent entity. Each dimension has its own.");
    }

    /*
     * Gets the total number of parents required to perform this crossover
     * @return The total number of parents required to perform this crossover
     */
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
