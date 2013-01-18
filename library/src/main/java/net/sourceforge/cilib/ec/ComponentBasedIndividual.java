/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author Kris
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
    
    @Override
    public ComponentBasedIndividual getClone() {
        return new ComponentBasedIndividual(this);
    }
    
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
    
     public void updateParameters(Vector variance, int totalIndividuals) {
         
         if(parameterChangeCount == 0) {
             int index = 0;
             for(SettableControlParameter parameter : scalingFactorPerComponent) {
                 //set parameters needed
                 scalingFactorParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, crossoverProbabilityPerComponent.get(0).getParameter());
                 scalingFactorParameterAdaptationStrategy.change(parameter);
                 index++;
             }
         } else if(parameterChangeCount == 1) {
             int index = 0;
             for(SettableControlParameter parameter : crossoverProbabilityPerComponent) {
                 //set parameters needed
                 crossoverProbabilityParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, scalingFactorPerComponent.get(0).getParameter());
                 crossoverProbabilityParameterAdaptationStrategy.change(parameter);
                 index++;
             }
         } else if(parameterChangeCount == 2) {
             int index = 0;
             for(SettableControlParameter parameter : greedPerComponent) {
                 //set parameters needed
                 greedParameterAdaptationStrategy.setUpdateParameters(variance.get(index).doubleValue(), totalIndividuals, crossoverProbabilityPerComponent.get(0).getParameter());
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

    public ArrayList<SettableControlParameter> getCrossoverProbabilityPerComponent() {
        return crossoverProbabilityPerComponent;
    }

    public void setCrossoverProbabilityPerComponent(ArrayList<SettableControlParameter> crossoverProbabilityPerComponent) {
        this.crossoverProbabilityPerComponent = crossoverProbabilityPerComponent;
    }

    public ArrayList<SettableControlParameter> getScalingFactorPerComponent() {
        return scalingFactorPerComponent;
    }

    public void setScalingFactorPerComponent(ArrayList<SettableControlParameter> scalingFactorPerComponent) {
        this.scalingFactorPerComponent = scalingFactorPerComponent;
    }

    public ArrayList<SettableControlParameter> getGreedPerComponent() {
        return greedPerComponent;
    }

    public void setGreedPerComponent(ArrayList<SettableControlParameter> greedPerComponent) {
        this.greedPerComponent = greedPerComponent;
    }

    public double getParameterChangeCount() {
        return parameterChangeCount;
    }

    public void setParameterChangeCount(double parameterChangeCount) {
        this.parameterChangeCount = parameterChangeCount;
    }

    public ParameterAdaptationStrategy getGreedParameterAdaptationStrategy() {
        return greedParameterAdaptationStrategy;
    }

    public void setGreedParameterAdaptationStrategy(VarianceBasedUpdateStrategy greedParameterAdaptationStrategy) {
        this.greedParameterAdaptationStrategy = greedParameterAdaptationStrategy;
    }

    public ControlParameterInitialisationStrategy getGreedInitialisationStrategy() {
        return greedInitialisationStrategy;
    }

    public void setGreedInitialisationStrategy(ControlParameterInitialisationStrategy greedInitialisationStrategy) {
        this.greedInitialisationStrategy = greedInitialisationStrategy;
    }

    public VarianceBasedUpdateStrategy getScalingFactorParameterAdaptationStrategy() {
        return scalingFactorParameterAdaptationStrategy;
    }

    public void setScalingFactorParameterAdaptationStrategy(VarianceBasedUpdateStrategy scalingFactorParameterAdaptationStrategy) {
        this.scalingFactorParameterAdaptationStrategy = scalingFactorParameterAdaptationStrategy;
    }

    public VarianceBasedUpdateStrategy getCrossoverProbabilityParameterAdaptationStrategy() {
        return crossoverProbabilityParameterAdaptationStrategy;
    }

    public void setCrossoverProbabilityParameterAdaptationStrategy(VarianceBasedUpdateStrategy crossoverProbabilityParameterAdaptationStrategy) {
        this.crossoverProbabilityParameterAdaptationStrategy = crossoverProbabilityParameterAdaptationStrategy;
    }

    public ControlParameterInitialisationStrategy getScalingFactorInitialisationStrategy() {
        return scalingFactorInitialisationStrategy;
    }

    public void setScalingFactorInitialisationStrategy(ControlParameterInitialisationStrategy scalingFactorInitialisationStrategy) {
        this.scalingFactorInitialisationStrategy = scalingFactorInitialisationStrategy;
    }

    public ControlParameterInitialisationStrategy getCrossoverProbabilityInitialisationStrategy() {
        return crossoverProbabilityInitialisationStrategy;
    }

    public void setCrossoverProbabilityInitialisationStrategy(ControlParameterInitialisationStrategy crossoverProbabilityInitialisationStrategy) {
        this.crossoverProbabilityInitialisationStrategy = crossoverProbabilityInitialisationStrategy;
    }

    public InitializationStrategy<Individual> getInitialisationStrategy() {
        return initialisationStrategy;
    }

    public void setInitialisationStrategy(InitializationStrategy<Individual> initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }
     
}
