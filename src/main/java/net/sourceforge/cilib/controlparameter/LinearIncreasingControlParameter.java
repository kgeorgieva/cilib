/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.controlparameter;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.parser.DomainParser;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}
 * that is defined to update itself in an linearly increasing manner. The rate of change
 * within the parameter is based on the percentage complete of the running
 * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
 *
 */
public class LinearIncreasingControlParameter implements BoundedControlParameter {

    private static final long serialVersionUID = -6813625954992761973L;
    private Real parameter;

    /**
     * Create an instance of {@code LinearDecreasingControlParameter}.
     */
    public LinearIncreasingControlParameter() {
        parameter = Real.valueOf(0.0);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public LinearIncreasingControlParameter(LinearIncreasingControlParameter copy) {
        this.parameter = copy.parameter.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinearIncreasingControlParameter getClone() {
        return new LinearIncreasingControlParameter(this);
    }

    /**
     * Update the parameter linearly based on the current percentage complete of the running
     * {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}.
     * The update is done in an increasing manner.
     */
    @Override
    public void updateParameter() {
        double result = getLowerBound() + (getUpperBound() - getLowerBound()) * AbstractAlgorithm.get().getPercentageComplete();
        parameter = Real.valueOf(result, parameter.getBounds());

        if (this.parameter.doubleValue() < this.parameter.getBounds().getLowerBound()) {
            this.parameter = Real.valueOf(this.parameter.getBounds().getLowerBound(), parameter.getBounds());
        } else if (this.parameter.doubleValue() > this.parameter.getBounds().getUpperBound()) {
            this.parameter = Real.valueOf(this.parameter.getBounds().getUpperBound(), parameter.getBounds());
        }
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
    
    public void updateParameter(double value) {
        
    }
    
    /*
     * Get the current velocity of the parameter
     * @return The current velocity of the parameter
     */
    @Override
    public double getVelocity(){
        return 0.0;
    }
    
    /*
     * Set the current velocity of the parameter
     */
    @Override
    public void setVelocity(double value){
        
    }
    
    /*
     * Not currently applicable
     */
    public boolean wasSetByUser() {
        return false;
    }
}
