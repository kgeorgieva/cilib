/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.controlparameter.adaptation.ParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceBasedUpdateStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceCrossoverProbabilityUpdateStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceGreedUpdateStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.VarianceScalingFactorUpdateStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.ControlParameterInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.RandomParameterInitialisationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the individual described by Zaharie in her 2003 paper "Control 
 * of Population Diversity and Adaptation in Differential Evolution Algorithms"
 * published in the Proceedings of Mendel 2003, 9th International Conference on
 * Soft Computing.
 * 
 * It holds a list for each parameter, namely scaling factor, crossover 
 * probability and greed parameter, which is as long as the candidate solution.
 * Each index of the list holds the parameter used to update that dimension of 
 * the candidate solution.  
 * 
 * There is an update method which allows for each of these parameters to be updated
 * using a control parameter update strategy.
 */
public class ComponentBasedIndividual extends Individual{
    private ArrayList<SettableControlParameter> crossoverProbabilityPerComponent;
    private ArrayList<SettableControlParameter> scalingFactorPerComponent;
    private ArrayList<SettableControlParameter> greedPerComponent;
    private double parameterChangeCount;
    private VarianceBasedUpdateStrategy greedParameterAdaptationStrategy;
    private VarianceBasedUpdateStrategy scalingFactorParameterAdaptationStrategy;
    private VarianceBasedUpdateStrategy crossoverProbabilityParameterAdaptationStrategy;
    private ControlParameterInitialisationStrategy scalingFactorInitialisationStrategy;
    private ControlParameterInitialisationStrategy  crossoverProbabilityInitialisationStrategy;
    
    private ControlParameterInitialisationStrategy greedInitialisationStrategy;
    
    /*
     * Default Constructor for ComponentBasedIndividual
     */
    public ComponentBasedIndividual() {
        super();
        crossoverProbabilityPerComponent = new ArrayList<SettableControlParameter>();
        scalingFactorPerComponent = new ArrayList<SettableControlParameter>();
        greedPerComponent = new ArrayList<SettableControlParameter>();
        greedInitialisationStrategy = new RandomParameterInitialisationStrategy();
        greedParameterAdaptationStrategy = new VarianceGreedUpdateStrategy();
        scalingFactorParameterAdaptationStrategy = new VarianceScalingFactorUpdateStrategy();
        crossoverProbabilityParameterAdaptationStrategy = new VarianceCrossoverProbabilityUpdateStrategy();
        scalingFactorInitialisationStrategy = new RandomParameterInitialisationStrategy();
        crossoverProbabilityInitialisationStrategy = new RandomParameterInitialisationStrategy();
        parameterChangeCount = 0;
    }
    
    /*
     * Copy constructor for ComponentBasedIndividual
     * @param copy The ComponentBasedIndividual to be copied
     */
    public ComponentBasedIndividual(ComponentBasedIndividual copy) {
        super(copy);
        crossoverProbabilityPerComponent = copy.crossoverProbabilityPerComponent;
        scalingFactorPerComponent = copy.scalingFactorPerComponent;
        greedPerComponent = copy.greedPerComponent;
        greedInitialisationStrategy = copy.greedInitialisationStrategy;
        greedParameterAdaptationStrategy = copy.greedParameterAdaptationStrategy;
        parameterChangeCount = copy.parameterChangeCount;
        scalingFactorParameterAdaptationStrategy = copy.scalingFactorParameterAdaptationStrategy;
        crossoverProbabilityParameterAdaptationStrategy = copy.crossoverProbabilityParameterAdaptationStrategy;
        scalingFactorInitialisationStrategy = copy.scalingFactorInitialisationStrategy.getClone();
        crossoverProbabilityInitialisationStrategy = copy.crossoverProbabilityInitialisationStrategy.getClone();
    }
    
    /*
     * Clone method for ComponentBasedIndividual
     * @return A new instance of this ComponentBasedIndividual
     */
    @Override
    public ComponentBasedIndividual getClone() {
        return new ComponentBasedIndividual(this);
    }
    
    /*
     * Initialises the ComponentBasedIndividual's candidate solution and fitness using an 
     * entity initialisation strategy.
     * It also initialises the parameters using Control Parameter Initialisation Strategies
     * @param problem The problem to be soved
     */
    @Override
     public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).buildRandom());
        
        this.initialisationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        
        Vector strategy = Vector.fill(0.0, this.getCandidateSolution().size());
        
        this.getProperties().put(EntityType.STRATEGY_PARAMETERS, strategy);
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        scalingFactorPerComponent = new ArrayList<SettableControlParameter>();
        crossoverProbabilityPerComponent = new ArrayList<SettableControlParameter>();
        greedPerComponent = new ArrayList<SettableControlParameter>();
        
        for(int i =0; i < getCandidateSolution().size(); i++) {
            StandardUpdatableControlParameter sParameter = new StandardUpdatableControlParameter();
            scalingFactorInitialisationStrategy.initialize(sParameter);
            scalingFactorPerComponent.add(sParameter);
            
            StandardUpdatableControlParameter cParameter = new StandardUpdatableControlParameter();
            crossoverProbabilityInitialisationStrategy.initialize(cParameter);
            crossoverProbabilityPerComponent.add(cParameter);
            
            StandardUpdatableControlParameter gParameter = new StandardUpdatableControlParameter();
            greedInitialisationStrategy.initialize(gParameter);
            greedPerComponent.add(gParameter);
        }
     }
    
    /*
     * Updates each parameter in the parameter lists using some variance update strategy
     * @param variance The variance to be used in order to update the parameters
     * @param totalIndividuals The total number of individuals in the topology
     */
     public void updateParameters(Vector variance, int totalIndividuals) {
         if(parameterChangeCount == 0) {
             int index = 0;
             for(SettableControlParameter parameter : scalingFactorPerComponent) {
                 //set parameters needed
                 scalingFactorParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, crossoverProbabilityPerComponent.get(index).getParameter());
                 scalingFactorParameterAdaptationStrategy.change(parameter);
                 index++;
             }
         } else if(parameterChangeCount == 1) {
             int index = 0;
             for(SettableControlParameter parameter : crossoverProbabilityPerComponent) {
                 //set parameters needed
                 crossoverProbabilityParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, scalingFactorPerComponent.get(index).getParameter());
                 crossoverProbabilityParameterAdaptationStrategy.change(parameter);
                 index++;
             }
         } else if(parameterChangeCount == 2) {
             int index = 0;
             for(SettableControlParameter parameter : greedPerComponent) {
                 //set parameters needed
                 greedParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, crossoverProbabilityPerComponent.get(index).getParameter());
                 greedParameterAdaptationStrategy.change(parameter);
                 index++;
             }
         }
         
         if(parameterChangeCount == 2) {
             parameterChangeCount = 0;
         } else {
             parameterChangeCount++;
         }
     }

    /*
     * Gets the crossover probability list
     * @return The list of crossover probability parameters
     */
    public ArrayList<SettableControlParameter> getCrossoverProbabilityPerComponent() {
        return crossoverProbabilityPerComponent;
    }

    /*
    * Sets the crossover probability list
    * @param crossoverProbabilityPerComponent The new list of crossover probability parameters
    */
    public void setCrossoverProbabilityPerComponent(ArrayList<SettableControlParameter> crossoverProbabilityPerComponent) {
        this.crossoverProbabilityPerComponent = crossoverProbabilityPerComponent;
    }

    /*
    * Gets the scaling factor list
    * @return The list of scaling factor parameters
    */
    public ArrayList<SettableControlParameter> getScalingFactorPerComponent() {
        return scalingFactorPerComponent;
    }

    /*
    * Sets the scaling factor list
    * @param scalingFactorPerComponent The new list of scaling factor parameters
    */
    public void setScalingFactorPerComponent(ArrayList<SettableControlParameter> scalingFactorPerComponent) {
        this.scalingFactorPerComponent = scalingFactorPerComponent;
    }

    /*
    * Gets the greed list
    * @return The list of greed parameters
    */
    public ArrayList<SettableControlParameter> getGreedPerComponent() {
        return greedPerComponent;
    }

    /*
    * Sets the greed list
    * @param greedPerComponent The new list of greed parameters
    */
    public void setGreedPerComponent(ArrayList<SettableControlParameter> greedPerComponent) {
        this.greedPerComponent = greedPerComponent;
    }

    /*
    * Gets the current count of which parameter will change next
    * @return The current count of which parameter will change next
    */
    public double getParameterChangeCount() {
        return parameterChangeCount;
    }

    /*
    * Sets the count of which parameter will change next
    * @param parameterChangeCount The new count of which parameter will change next
    */
    public void setParameterChangeCount(double parameterChangeCount) {
        this.parameterChangeCount = parameterChangeCount;
    }

    /*
    * Gets the parameter adaptation strategy for the greed parameter
    * @return The parameter adaptation strategy for the greed parameter
    */
    public ParameterAdaptationStrategy getGreedParameterAdaptationStrategy() {
        return greedParameterAdaptationStrategy;
    }

    /*
    * Sets the parameter adaptation strategy for the greed parameter
    * @param greedParameterAdaptationStrategy The new parameter adaptation strategy for the greed parameter
    */
    public void setGreedParameterAdaptationStrategy(VarianceBasedUpdateStrategy greedParameterAdaptationStrategy) {
        this.greedParameterAdaptationStrategy = greedParameterAdaptationStrategy;
    }

    /*
    * Gets the parameter initialisation strategy for the greed parameter
    * @return The parameter initialisation strategy for the greed parameter
    */
    public ControlParameterInitialisationStrategy getGreedInitialisationStrategy() {
        return greedInitialisationStrategy;
    }

    /*
    * Sets the parameter initialisation strategy for the greed parameter
    * @param greedInitialisationStrategy The new parameter initialisation strategy for the greed parameter
    */
    public void setGreedInitialisationStrategy(ControlParameterInitialisationStrategy greedInitialisationStrategy) {
        this.greedInitialisationStrategy = greedInitialisationStrategy;
    }

    /*
    * Gets the parameter adaptation strategy for the scaling factor parameter
    * @return The parameter adaptation strategy for the scaling factor parameter
    */
    public VarianceBasedUpdateStrategy getScalingFactorParameterAdaptationStrategy() {
        return scalingFactorParameterAdaptationStrategy;
    }

    /*
    * Sets the parameter adaptation strategy for the scaling factor parameter
    * @param scalingFactorParameterAdaptationStrategy The new parameter adaptation strategy for the scaling factor parameter
    */
    public void setScalingFactorParameterAdaptationStrategy(VarianceBasedUpdateStrategy scalingFactorParameterAdaptationStrategy) {
        this.scalingFactorParameterAdaptationStrategy = scalingFactorParameterAdaptationStrategy;
    }

    /*
    * Gets the parameter adaptation strategy for the crossover probability parameter
    * @return The parameter adaptation strategy for the crossover probability parameter
    */
    public VarianceBasedUpdateStrategy getCrossoverProbabilityParameterAdaptationStrategy() {
        return crossoverProbabilityParameterAdaptationStrategy;
    }

    /*
    * Sets the parameter adaptation strategy for the crossover probability parameter
    * @param crossoverProbabilityParameterAdaptationStrategy The new parameter adaptation strategy for the crossover probability parameter
    */
    public void setCrossoverProbabilityParameterAdaptationStrategy(VarianceBasedUpdateStrategy crossoverProbabilityParameterAdaptationStrategy) {
        this.crossoverProbabilityParameterAdaptationStrategy = crossoverProbabilityParameterAdaptationStrategy;
    }

    /*
    * Gets the parameter initialisation strategy for the scaling factor parameter
    * @return The parameter initialisation strategy for the scaling factor parameter
    */
    public ControlParameterInitialisationStrategy getScalingFactorInitialisationStrategy() {
        return scalingFactorInitialisationStrategy;
    }

    /*
    * Sets the parameter initialisation strategy for the scaling factor parameter
    * @param scalingFactorInitialisationStrategy The new parameter initialisation strategy for the scaling factor parameter
    */
    public void setScalingFactorInitialisationStrategy(ControlParameterInitialisationStrategy scalingFactorInitialisationStrategy) {
        this.scalingFactorInitialisationStrategy = scalingFactorInitialisationStrategy;
    }

    /*
    * Gets the parameter initialisation strategy for the crossover probability parameter
    * @return The parameter initialisation strategy for the crossover probability parameter
    */
    public ControlParameterInitialisationStrategy getCrossoverProbabilityInitialisationStrategy() {
        return crossoverProbabilityInitialisationStrategy;
    }

    /*
    * Sets the parameter initialisation strategy for the crossover probability parameter
    * @param crossoverProbabilityInitialisationStrategy The new parameter initialisation strategy for the crossover probability parameter
    */
    public void setCrossoverProbabilityInitialisationStrategy(ControlParameterInitialisationStrategy crossoverProbabilityInitialisationStrategy) {
        this.crossoverProbabilityInitialisationStrategy = crossoverProbabilityInitialisationStrategy;
    }

    /*
    * Gets the initialisation strategy for the candidate solution
    * @return The initialisation strategy for the candidate solution
    */
    @Override
    public InitializationStrategy<Individual> getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /*
    * Sets the initialisation strategy for the candidate solution
    * @param initialisationStrategy The new initialisation strategy for the candidate solution
    */
    public void setInitialisationStrategy(InitializationStrategy<Individual> initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }
     
}
