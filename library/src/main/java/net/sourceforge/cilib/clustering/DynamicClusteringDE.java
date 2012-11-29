/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.update.UpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.BrownianClusteringUpdateStrategy;
import net.sourceforge.cilib.ec.update.clustering.StandardClusteringDEUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class DynamicClusteringDE extends DataClusteringEC {
    private ProbabilityDistributionFunction random;
    private UpdateStrategy firstUpdateStrategy;
    private UpdateStrategy secondUpdateStrategy;
    private double updateStrategyProbability;
    
    public DynamicClusteringDE() {
        random = new UniformDistribution();
        firstUpdateStrategy = new StandardClusteringDEUpdateStrategy();
        secondUpdateStrategy = new BrownianClusteringUpdateStrategy();
        super.iterationStrategy = new StandardClusteringDEIterationStrategy();
        updateStrategyProbability = 0.5;
    }
    
    public DynamicClusteringDE(DynamicClusteringDE copy) {
        random = copy.random;
        firstUpdateStrategy = copy.firstUpdateStrategy;
        secondUpdateStrategy = copy.secondUpdateStrategy;
        updateStrategyProbability = copy.updateStrategyProbability;
    }
    
    @Override
    public DynamicClusteringDE getClone() {
        return new DynamicClusteringDE(this);
    }
    
    @Override
    public void algorithmInitialisation() {
        DataTable dataset = window.initializeWindow();

        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        ((ClusteringProblem) super.problem).setDimension((int) pattern.size());

        ((DataDependantPopulationInitializationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterIndividual> individuals = (Iterable<ClusterIndividual>) this.initialisationStrategy.initialise(this.getOptimisationProblem());

        ClusterIndividual individual;
        for (Entity clusterEntity : individuals) {
            individual = (ClusterIndividual) clusterEntity;
            if(random.getRandomNumber(0,1) < updateStrategyProbability) {
                individual.setUpdateStrategy(firstUpdateStrategy.getClone());
            } else {
                individual.setUpdateStrategy(secondUpdateStrategy.getClone());
            }
        }
        
        super.topology.clear();
        super.topology.addAll(Lists.<ClusterIndividual>newLinkedList(individuals));

        ((SinglePopulationDataClusteringDEIterationStrategy) iterationStrategy).setWindow(window);
        
    }

    public ProbabilityDistributionFunction getRandom() {
        return random;
    }

    public void setRandom(ProbabilityDistributionFunction random) {
        this.random = random;
    }

    public UpdateStrategy getFirstUpdateStrategy() {
        return firstUpdateStrategy;
    }

    public void setFirstUpdateStrategy(UpdateStrategy firstUpdateStrategy) {
        this.firstUpdateStrategy = firstUpdateStrategy;
    }

    public UpdateStrategy getSecondUpdateStrategy() {
        return secondUpdateStrategy;
    }

    public void setSecondUpdateStrategy(UpdateStrategy secondUpdateStrategy) {
        this.secondUpdateStrategy = secondUpdateStrategy;
    }

    public double getUpdateStrategyProbability() {
        return updateStrategyProbability;
    }

    public void setUpdateStrategyProbability(double updateStrategyProbability) {
        this.updateStrategyProbability = updateStrategyProbability;
    }
    
}
