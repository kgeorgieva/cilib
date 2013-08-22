/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.CooperativeMultipopulationAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.StandardMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.de.iterationstrategies.multipopulation.KIndependentCooperativeDynDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.measurement.clustervalidity.RayTuriValidityIndex;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class KIndependentParameterOptimisation extends AbstractProblem {
    CooperativeMultipopulationAlgorithm mmАlgorithm;
    int iterations;
    Problem problem;

    public StandardMultipopulationAlgorithm getAlgorithm() {
        return mmАlgorithm;
    }

    public void setAlgorithm(CooperativeMultipopulationAlgorithm algorithm) {
        this.mmАlgorithm = algorithm;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public AbstractProblem getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        System.out.println("number of populations: " + mmАlgorithm.getPopulations().size());
        AbstractAlgorithm abstractAlg = (AbstractAlgorithm) mmАlgorithm.getClone();
        abstractAlg.setOptimisationProblem(problem);
        abstractAlg.performInitialisation();

        Vector parameters = (Vector) solution;
        for(PopulationBasedAlgorithm alg : mmАlgorithm.getPopulations()) {
            for(ClusterIndividual individual : (Topology<ClusterIndividual>) alg.getTopology()) {
                System.out.println("individual: " + individual.getCandidateSolution().size());
                if(individual.getUpdateStrategy() instanceof StandardClusteringDEUpdateStrategy) {
                    StandardClusteringDEUpdateStrategy updateStrategy = (StandardClusteringDEUpdateStrategy) individual.getUpdateStrategy();
                    updateStrategy.getTrialVectorCreationStrategy().setScaleParameter(parameters.get(0).doubleValue());
                    updateStrategy.getCrossoverStrategy().setCrossoverPointProbability(parameters.get(1).doubleValue());
                }
            }
        }
        
       ((KIndependentCooperativeDynDEIterationStrategy) mmАlgorithm.getMultiSwarmIterationStrategy()).setExclusionRadius(parameters.get(2).doubleValue());
       ((KIndependentCooperativeDynDEIterationStrategy) mmАlgorithm.getMultiSwarmIterationStrategy()).setConvergenceRadius(parameters.get(3).doubleValue());
       
       while (!abstractAlg.isFinished()) {
                abstractAlg.performIteration();
       }
       
       RayTuriValidityIndex indx = new RayTuriValidityIndex();
       return objective.evaluate(indx.getValue(mmАlgorithm).doubleValue());
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
    
}
