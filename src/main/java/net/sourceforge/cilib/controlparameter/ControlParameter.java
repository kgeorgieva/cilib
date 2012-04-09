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

import net.sourceforge.cilib.util.Cloneable;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} is a
 * parameter that is used within most {@linkplain net.sourceforge.cilib.algorithm.Algorithm algorithm}
 * types. These parameters are updatable and can be changed over time, if required.
 */
public interface ControlParameter extends Cloneable {

    /**
     * Get the value of the represented parameter.
     * @return The value of the represented parameter.
     */
    double getParameter();
    
    double getParameter(double min, double max);
    
    /**
     * {@inheritDoc}
     */
    @Override
    ControlParameter getClone();
    void setParameter(double value);

    /**
     * Update the required and needed parameters contained within this
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter parameter}.
     */
    void updateParameter();
    
    /**
     * Update the parameter to the value provided. Only applicable to modifiable non-constant parameters
     * @param value The value the parameter must change to
     */
    void updateParameter(double value);
    
    /*
     * Get the current velocity of the parameter
     * @return The current velocity of the parameter
     */
    public double getVelocity();
    
    /*
     * Set the current velocity of the parameter if the parameter can change after being initialized previously.
     * @param value The new value of the parameter
     */
    public void setVelocity(double value);
          
    /*
     * States whether the parameter's initial value was set by the user or msut be initialized randomly. 
     * If false, it must be initialized randomly, if true, the initialization is not necessary.
     */
    public boolean wasSetByUser();
            
>>>>>>> O changes allowing for a parametized particle to be used containing: inertia, social acceleration, cognitive acceleration and vmax parameters
}
