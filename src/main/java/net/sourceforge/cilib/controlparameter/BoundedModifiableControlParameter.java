/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 * @author Kristina
 */
public class BoundedModifiableControlParameter implements BoundedControlParameter{
    private Real parameter;

    /**
     * Create an instance of {@code LinearDecreasingControlParameter}.
     */
    public BoundedModifiableControlParameter() {
        parameter = Real.valueOf(0.0);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BoundedModifiableControlParameter(BoundedModifiableControlParameter copy) {
        this.parameter = copy.parameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundedModifiableControlParameter getClone() {
        return new BoundedModifiableControlParameter(this);
    }

    /**
     * Update the parameter linearly based on the current percentage complete of the running
     * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
     * The update is done in an increasing manner.
     */
    @Override
    public void updateParameter() {
       
    }
    
    public void updateParameter(double value) {
        parameter = Real.valueOf(value, parameter.getBounds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLowerBound(double lower) {
        Bounds current = parameter.getBounds();
        this.parameter = Real.valueOf(lower, new Bounds(lower, current.getUpperBound()));
    }

    @Override
    public double getParameter() {
        return parameter.doubleValue();
    }

    @Override
    public double getParameter(double min, double max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParameter(double value) {
        this.parameter = Real.valueOf(value, parameter.getBounds());
    }

    @Override
    public double getLowerBound() {
        return parameter.getBounds().getLowerBound();
    }

    @Override
    public double getUpperBound() {
        return parameter.getBounds().getUpperBound();
    }

    @Override
    public void setUpperBound(double value) {
        this.parameter = Real.valueOf(value, new Bounds(parameter.getBounds().getLowerBound(), value));
    }

    @Override
    public void setRange(String range) {
        Vector v = (Vector) DomainParser.parse(range);

        if (v.size() != 1) {
            throw new RuntimeException("Range incorrect in BoundedUpdateStrategy! Please correct");
        } else {
            this.parameter = (Real) v.get(0);
        }
    }
}
