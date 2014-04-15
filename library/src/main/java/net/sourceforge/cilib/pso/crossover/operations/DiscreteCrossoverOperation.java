/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.CrossoverReplaceFunction;
import net.sourceforge.cilib.pso.crossover.parentupdate.ParentReplacementStrategy;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;

public class DiscreteCrossoverOperation extends PSOCrossoverOperation {

    private CrossoverReplaceFunction function;

    public DiscreteCrossoverOperation() {
        this.function = new CrossoverReplaceFunction();
    }

    @Override
    public Topology<Particle> f(PSO pso) {
        Topology<Particle> newTopology = pso.getTopology().getClone();
        newTopology.clear();

        for (Particle p : pso.getTopology()) {
            newTopology.add(function.f(p));
        }

        return newTopology;
    }

    @Override
    public PSOCrossoverOperation getClone() {
        return this;
    }

    public void setParentProvider(GuideProvider parentProvider) {
        this.function.setParentProvider(parentProvider);
    }

    public GuideProvider getParentProvider() {
        return function.getParentProvider();
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.function.setCrossoverStrategy(crossoverStrategy);
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return function.getCrossoverStrategy();
    }

    public void setParentReplacementStrategy(ParentReplacementStrategy parentReplacementStrategy) {
        this.function.setParentReplacementStrategy(parentReplacementStrategy);
    }

    public ParentReplacementStrategy getParentReplacementStrategy() {
        return function.getParentReplacementStrategy();
    }

    public void setCrossoverProbability(SettableControlParameter crossoverProbability) {
        this.function.setCrossoverProbability(crossoverProbability);
    }

    public ControlParameter getCrossoverProbability() {
        return function.getCrossoverProbability();
    }

}
