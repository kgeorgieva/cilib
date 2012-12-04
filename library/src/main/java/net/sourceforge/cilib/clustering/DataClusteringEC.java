/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.clustering.de.iterationstrategies.StandardClusteringDEIterationStrategy;
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
 * Generic EC skeleton algorithm for data clustering problems. The algorithm is altered by defining the
 * appropriate clustering iteration strategy.
 */
public class DataClusteringEC extends EC{
    protected SlidingWindow window;
    protected boolean isExplorer;
    protected IterationStrategy<DataClusteringEC> iterationStrategy;
    
    /*
     * Default constructor for the DataClusteringEC
     */
    public DataClusteringEC() {
        super();
        contributionSelection = new ZeroContributionSelectionStrategy();
        initialisationStrategy = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
        window = new SlidingWindow();
        isExplorer = false;
        iterationStrategy = new StandardClusteringDEIterationStrategy();
    }
    
    /*
     * Copy constructor for the DataClusteringEC
     * @param copy The DataClusteringEC to be copied
     */
    public DataClusteringEC(DataClusteringEC copy) {
        super(copy);
        initialisationStrategy = copy.initialisationStrategy;
        window = copy.window;
        isExplorer = copy.isExplorer;
        iterationStrategy = copy.iterationStrategy;
    }
    
    /**
     * Clone method for DataClusteringEC
     * @return A new instance of this DataClusteringEC
     */
    @Override
    public DataClusteringEC getClone() {
        return new DataClusteringEC(this);
    }
    
    /*
     * Initialises the population appropriately using the initialisation strategy
     * selected and the dataset. It also initialises the window.
     */
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
    
    /*
     * Calls the iteration strategy to perform one iteration of the algorithm.
     */
    @Override
    public void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /*
     * Gets the current window
     * @return The current window
     */
    public SlidingWindow getWindow() {
        return window;
    }

    /*
     * Sets the algorithm to be an explorer or not
     * @param isExplorer The boolean indicating whether the algorithm must be an
     * explorer or not
     */
    public void setIsExplorer(boolean isExplorer) {
        this.isExplorer = isExplorer;
    }

    /*
     * Gets the clustering iteration strategy
     * @return The clustering iteration strategy
     */
    @Override
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /*
     * Sets the iteration straetgy to the one received as a parameter
     * @param iterationStrategy The new iteration strategy
     */
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
