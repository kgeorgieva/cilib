/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.controlparameter.StandardUpdatableControlParameter;
import net.sourceforge.cilib.controlparameter.adaptation.ParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.adaptation.SaDEParameterAdaptationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.ControlParameterInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.initialisation.RandomParameterInitialisationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class ComponentBasedIndividual extends SaDEIndividual{
    private ArrayList<SettableControlParameter> crossoverProbabilityPerComponent;
    private ArrayList<SettableControlParameter> scalingFactorPerComponent;
    private ArrayList<SettableControlParameter> greedPerComponent;
    private double parameterChangeCount;
    private ParameterAdaptationStrategy greedParameterAdaptationStrategy;
    
    private ControlParameterInitialisationStrategy greedInitialisationStrategy;
    
    public ComponentBasedIndividual() {
        super();
        crossoverProbabilityPerComponent = new ArrayList<SettableControlParameter>();
        scalingFactorPerComponent = new ArrayList<SettableControlParameter>();
        greedPerComponent = new ArrayList<SettableControlParameter>();
        greedInitialisationStrategy = new RandomParameterInitialisationStrategy();
        greedParameterAdaptationStrategy = new SaDEParameterAdaptationStrategy();
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
    
     public void updateParameters(double varianceOfOldPopulation, double varianceOfNewPopulation) {
         if(parameterChangeCount == 0) {
             for(SettableControlParameter parameter : scalingFactorPerComponent) {
                 //set parameters needed
                 scalingFactorParameterAdaptationStrategy.change(parameter);
             }
         } else if(parameterChangeCount == 1) {
             for(SettableControlParameter parameter : crossoverProbabilityPerComponent) {
                 //set parameters needed
                 crossoverProbabilityParameterAdaptationStrategy.change(parameter);
             }
         } else if(parameterChangeCount == 2) {
             for(SettableControlParameter parameter : greedPerComponent) {
                 //set parameters needed
                 greedParameterAdaptationStrategy.change(parameter);
             }
         }
     }
     
}
