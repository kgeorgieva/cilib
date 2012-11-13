/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterIndividual;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class DataClusteringEC extends EC{
    protected SlidingWindow window;
    protected boolean isExplorer;
    protected IterationStrategy<DataClusteringEC> iterationStrategy;
    
    public DataClusteringEC() {
        super();
        contributionSelection = new ZeroContributionSelectionStrategy();
        initialisationStrategy = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
        window = new SlidingWindow();
        isExplorer = false;
    }
    
    public DataClusteringEC(DataClusteringEC copy) {
        super(copy);
        initialisationStrategy = copy.initialisationStrategy;
        window = copy.window;
        isExplorer = copy.isExplorer;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DataClusteringEC getClone() {
        return new DataClusteringEC(this);
    }
    
    @Override
    public void algorithmInitialisation() {
        DataTable dataset = window.initializeWindow();

        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        ((ClusteringProblem) super.problem).setDimension((int) pattern.size());

        ((DataDependantPopulationInitializationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterIndividual> individuals = (Iterable<ClusterIndividual>) this.initialisationStrategy.initialise(this.getOptimisationProblem());

        super.topology.clear();
        super.topology.addAll(Lists.<ClusterIndividual>newLinkedList(individuals));

        ((SinglePopulationDataClusteringDEIterationStrategy) iterationStrategy).setWindow(window);
    }
    
    @Override
    public void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    public SlidingWindow getWindow() {
        return window;
    }

    public boolean isIsExplorer() {
        return isExplorer;
    }

    public void setIsExplorer(boolean isExplorer) {
        this.isExplorer = isExplorer;
    }

    @Override
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    @Override
    public void setIterationStrategy(IterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }
    
    /*
     * Sets the window's source URL. This source URL is the path to the file containing
     * the dataset to be clsutered.
     * @param sourceURL The path to the dataset
     */
    public void setSourceURL(String sourceURL) {
        window.setSourceURL(sourceURL);
    }

    /*
     * Sets the SlidingWindow to the one received as a parameter
     * @param slidingWindow The new sliding window
     */
    public void setWindow(SlidingWindow slidingWindow) {
        String url = window.getSourceURL();
        window = slidingWindow;
        window.setSourceURL(url);
    }

    /*
     * Returns the value of isExplorer, i.e. it checks if the algorithm is currently an explorer
     * @return isExplorer The value of isExplorer
     */
    public boolean isExplorer() {
        return isExplorer;
    }
    
     /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Entity bestEntity = Topologies.getBestEntity(topology);
        OptimisationSolution solution = new OptimisationSolution(bestEntity.getCandidateSolution().getClone(), bestEntity.getFitness());

        return solution;
    }

    
}
