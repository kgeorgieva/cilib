/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation.CooperativeDynDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation.KIndependentCooperativeDynDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 *
 * @author Kris
 */
public class CooperativeMultipopulationAlgorithm extends StandardMultipopulationAlgorithm {   
    @Override
    public OptimisationSolution getBestSolution() {
        ClusterIndividual context;
        
        if(multiSwarmsIterationStrategy instanceof CooperativeDynDEIterationStrategy) {
            context = ((CooperativeDynDEIterationStrategy) multiSwarmsIterationStrategy).createContext(this.getPopulations());
        } else {
            context = ((KIndependentCooperativeDynDEIterationStrategy) multiSwarmsIterationStrategy).createContext(this.getPopulations());
        }
        
        //System.out.println("Total clusters: " + context.getCandidateSolution().size());
        return new OptimisationSolution(context.getCandidateSolution(), context.getFitness());
    }
    
    /**
     * initialises every population.
     *
     */
    @Override
    public void algorithmInitialisation()    {
        ClusteringProblem problem = (ClusteringProblem) getOptimisationProblem();
        problem.setNumberOfClusters(subPopulationsAlgorithms.size());
        
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.setOptimisationProblem(problem);
            currentAlgorithm.performInitialisation();
        }//for
    }
}
