/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.pso.iterationstrategies.SinglePopulationDataClusteringPSOIterationStrategy;
import net.sourceforge.cilib.clustering.pso.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class holds the functionality of the Standard Data Clustering PSO described in:
 * <pre>
 * {@literal @}article{vanDerMerwe03,
 *  title={{Data Clustering using Particle Swarm Optimization }},
 *  author={van der Merwe, D.W.; Engelhrecht, A.P.},
 *  year={2003},
 *  journal={Congress on Evolutionary Computation},
 *  volume={1},
 *  pages={215-220}
 * }
 * </pre>
 *
 * This is so if the StandardDataClusteringIterationStrategy is used. Variations of the algorithm
 * in the article above can then be tested by using other iteration strategies, such as the
 * ReinitializingDataClusteringIterationStrategy
 */
public class DataClusteringPSO extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    protected Topology<ClusterParticle> topology;
    protected IterationStrategy<DataClusteringPSO> iterationStrategy;
    protected ContributionSelectionStrategy contributionSelection;
    protected boolean isExplorer;

    /*
     * Default Constructor for DataClusteringPSO
     */
    public DataClusteringPSO() {
        super();
        contributionSelection = new ZeroContributionSelectionStrategy();
        topology = new GBestTopology<ClusterParticle>();
        initialisationStrategy = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
        iterationStrategy = new StandardDataClusteringIterationStrategy();
        isExplorer = false;
    }

    /*
     * copy constructor for DataClusteringPSO
     * @param copy Th DataClusteringPSO to be copied
     */
    public DataClusteringPSO(DataClusteringPSO copy) {
        super(copy);
        topology = copy.topology;
        initialisationStrategy = copy.initialisationStrategy;
        iterationStrategy = copy.iterationStrategy;
        contributionSelection = copy.contributionSelection;
        isExplorer = copy.isExplorer;
    }

    /*
     * Clone method for the DataClusteringPSO
     * @return new instance of the DataClusteringPSO
     */
    @Override
    public DataClusteringPSO getClone() {
        return new DataClusteringPSO(this);
    }

    /*
     * Calls the IterationStrategy's performIteration method
     */
    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);

    }

    /*
     * Returns the current topology of the algorithm
     * @return topology (population and arrangement of population) The topology of the algorithm
     */
    @Override
    public Topology<ClusterParticle> getTopology() {
        return topology;
    }

    /*
     * Sets the topology of the algorithm
     * @param topology The new topology
     */
    @Override
    public void setTopology(Topology<? extends Entity> receivedTopology) {
        topology = (Topology<ClusterParticle>) receivedTopology;
    }

    /*
     * Initializes the algorithm. This includes the SlidingWindow and topology
     */
    @Override
    public void algorithmInitialisation() {
        SlidingWindow window = ((ClusteringProblem) this.getOptimisationProblem()).getWindow();
        DataTable dataset;
        
        if(window.getCurrentDataset().size() == 0) {
            dataset = window.initializeWindow();
        } else {
            dataset = window.getCurrentDataset();
        }
        
        Vector pattern = ((StandardPattern) dataset.getRow(0)).getVector();
        
        ((ClusteringProblem) super.optimisationProblem).setDimension((int) pattern.size());

        ((DataDependantPopulationInitializationStrategy) initialisationStrategy).setDataset(window.getCompleteDataset());
        Iterable<ClusterParticle> particles = (Iterable<ClusterParticle>) this.initialisationStrategy.initialise(this.getOptimisationProblem());

        topology.clear();
        topology.addAll(Lists.<ClusterParticle>newLinkedList(particles));

        ((SinglePopulationDataClusteringPSOIterationStrategy) iterationStrategy).setWindow(window);
        
    }

    /*
     * Returns the global best solution found by the algorithm so far
     * @return solution The global best solution
     */
    @Override
    public OptimisationSolution getBestSolution() {
        ClusterParticle bestEntity = Topologies.getBestEntity(topology, new SocialBestFitnessComparator<ClusterParticle>());
        return new OptimisationSolution(bestEntity.getBestPosition(), bestEntity.getBestFitness());
        
    }

    /*
     * Returns a list with each particle's best solution
     * @return solutions The list of solutions held by the Topology
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = Lists.newLinkedList();
        for (ClusterParticle e : Topologies.getNeighbourhoodBestEntities(topology, new SocialBestFitnessComparator<ClusterParticle>())) {
            solutions.add(new OptimisationSolution(e.getBestPosition(), e.getBestFitness()));
        }
        return solutions;

    }

    /*
     * Returns the contribution selection strategy
     * @return strategy The conribution selection strategy
     */
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    /*
     * Sets teh contribution selection strategy to the one received as a parameter
     * @param strategy The new contribution selection strategy
     */
    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }

    /*
     * Sets the iteration strategy to the one provided as a parameter
     * @param strategy The new iterations strategy
     */
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }

    /*
     * Returns the current iteration strategy
     * @return iterationStrategy The current iteration strategy
     */
    public IterationStrategy getIterationStrategy() {
        return iterationStrategy;
    }

    /*
     * Sets the boolean value of isExplorer to the one provided as a parameter.
     * This is used in the co-operative multi-swarm as one of the swarms must be
     * an explorer.
     * @param value The new boolean value of isExplorer
     */
    public void setIsExplorer(boolean value) {
        isExplorer = value;
    }

    /*
     * Returns the value of isExplorer, i.e. it checks if the algorithm is currently an explorer
     * @return isExplorer The value of isExplorer
     */
    public boolean isExplorer() {
        return isExplorer;
    }

}
