/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.entity.initialization;

import java.lang.reflect.Type;
import java.util.Vector;
import javax.swing.text.html.parser.Entity;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;

/**
 *
 * @author Kristina
 */
public class ParameterInclusiveInitializationStrategy implements InitializationStrategy<Particle> {
    private static final long serialVersionUID = -7926839076670354209L;
    private ControlParameter lowerBoundEntity;
    private ControlParameter upperBoundEntity;
    private ProbabilityDistributionFuction random;

    public ParameterInclusiveInitializationStrategy() {
        this.lowerBoundEntity = new ConstantControlParameter(0.1);
        this.upperBoundEntity = new ConstantControlParameter(0.1);
        this.random = new UniformDistribution();
    }

    public ParameterInclusiveInitializationStrategy(ParameterInclusiveInitializationStrategy copy) {
        this.lowerBoundEntity = copy.lowerBoundEntity;
        this.upperBoundEntity = copy.upperBoundEntity;
        this.random = copy.random;
    }

    @Override
    public ParameterInclusiveInitializationStrategy getClone() {
        return new ParameterInclusiveInitializationStrategy(this);
    }

    @Override
    public void initialize(Enum<?> key, Particle particle) {
       /* TODO: Kris Implement */
    }

    public ControlParameter getLowerBoundEntity() {
        return lowerBoundEntity;
    }

    public void setLowerBoundEntity(ControlParameter lowerBound) {
        this.lowerBoundEntity = lowerBound;
    }

    public ControlParameter getUpperBoundEntity() {
        return upperBoundEntity;
    }

    public void setUpperBoundEntity(ControlParameter upperBound) {
        this.upperBoundEntity = upperBound;
    }
    
}
