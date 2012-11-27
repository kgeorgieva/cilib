/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.de.iterationstrategies;

import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 *
 * @author Kris
 */
public class ClusteringDynDEIterationStrategy extends SinglePopulationDataClusteringDEIterationStrategy{
    private Selector targetVectorSelectionStrategy;
    private CreationStrategy standardTrialVectorCreationStrategy;
    private CrossoverStrategy standardCrossoverStrategy;
    private CreationStrategy differentTrialVectorCreationStrategy;
    private CrossoverStrategy differentCrossoverStrategy;
    
    public ClusteringDynDEIterationStrategy() {
        super();
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.standardTrialVectorCreationStrategy = new RandCreationStrategy();
        this.standardCrossoverStrategy = new DifferentialEvolutionBinomialCrossover();
        this.differentTrialVectorCreationStrategy = new RandCreationStrategy();
        this.differentCrossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }
    
    public ClusteringDynDEIterationStrategy(ClusteringDynDEIterationStrategy copy) {
        super();
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.standardTrialVectorCreationStrategy = copy.standardTrialVectorCreationStrategy.getClone();
        this.standardCrossoverStrategy = copy.standardCrossoverStrategy.getClone();
        this.differentTrialVectorCreationStrategy = copy.differentTrialVectorCreationStrategy.getClone();
        this.differentCrossoverStrategy = copy.differentCrossoverStrategy.getClone();
    }
    
    @Override
    public SinglePopulationDataClusteringDEIterationStrategy getClone() {
        return new ClusteringDynDEIterationStrategy(this);
    }

    @Override
    public void performIteration(DataClusteringEC algorithm) {
        Topology t = algorithm.getTopology();
        t.add(new Individual());
        t.add(new StandardParticle());
    }
    
}
