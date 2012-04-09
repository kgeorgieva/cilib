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
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.HashMap;
import java.util.Hashtable;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ControlParameters;
import net.sourceforge.cilib.util.RandomProviders;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation.
 *
 */
public final class StandardVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;
    
    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected RandomProvider r1;
    protected RandomProvider r2;

    /** Creates a new instance of StandardVelocityUpdate. */
    public StandardVelocityProvider() {
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
        this.r1 = new MersenneTwister();
        this.r2 = new MersenneTwister();
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public StandardVelocityProvider(StandardVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.r1 = copy.r1;
        this.r2 = copy.r2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardVelocityProvider getClone() {
        return new StandardVelocityProvider(this);
    }

    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(ControlParameters.supplierOf(this.inertiaWeight));
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(ControlParameters.supplierOf(this.cognitiveAcceleration)).multiply(RandomProviders.supplierOf(this.r1));
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(ControlParameters.supplierOf(this.socialAcceleration)).multiply(RandomProviders.supplierOf(this.r2));
        return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent);
    }

    /**
     * Get the <code>ControlParameter</code> representing the inertia weight of
     * the VelocityProvider.
     * @return Returns the inertia component <tt>ControlParameter</tt>.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the inertia weight of the velocity
     * update equation.
     * @param inertiaComponent The inertiaComponent to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the cognitive component within this
     * <code>VelocityProvider</code>.
     * @return Returns the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the cognitive component <code>ControlParameter</code>.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * Get the <tt>ControlParameter</tt> representing the social component of
     * the velocity update equation.
     * @return Returns the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }

    public RandomProvider getR1() {
        return r1;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public RandomProvider getR2() {
        return r2;
    }

    public void setR2(RandomProvider r2) {
        this.r2 = r2;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public void setControlParameters(ParametizedParticle particle) {
        inertiaWeight  = particle.getInertia();
        socialAcceleration = particle.getSocialAcceleration();
        cognitiveAcceleration = particle.getCognitiveAcceleration();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParametizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double> ();
        
        double velocity = particle.getInertia().getVelocity();
        double position = particle.getInertia().getParameter();
        ControlParameter localGuide = particle.getLocalGuideInertia();
        ControlParameter globalGuide = particle.getGlobalGuideInertia();

        double dampenedVelocity = velocity * this.inertiaWeight.getParameter();
        double cognitiveComponent = localGuide.getParameter() - position * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble();
        double socialComponent = globalGuide.getParameter() - position * this.socialAcceleration.getParameter() * this.r2.nextDouble();
        double newVelocity = dampenedVelocity + cognitiveComponent + socialComponent;
        parameterVelocity.put("InertiaVelocity", newVelocity);
        
        velocity = particle.getSocialAcceleration().getVelocity();
        position = particle.getSocialAcceleration().getParameter();
        localGuide = particle.getLocalGuideSocial();
        globalGuide = particle.getGlobalGuideSocial();

        dampenedVelocity = velocity * this.inertiaWeight.getParameter();
        cognitiveComponent = localGuide.getParameter() - position * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble();
        socialComponent = globalGuide.getParameter() - position * this.socialAcceleration.getParameter() * this.r2.nextDouble();
        newVelocity = dampenedVelocity + cognitiveComponent + socialComponent;
        parameterVelocity.put("SocialAccelerationVelocity", newVelocity);
        
        velocity = particle.getCognitiveAcceleration().getVelocity();
        position = particle.getCognitiveAcceleration().getParameter();
        localGuide = particle.getLocalGuidePersonal();
        globalGuide = particle.getGlobalGuidePersonal();

        dampenedVelocity = velocity * this.inertiaWeight.getParameter();
        cognitiveComponent = localGuide.getParameter() - position * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble();
        socialComponent = globalGuide.getParameter() - position * this.socialAcceleration.getParameter() * this.r2.nextDouble();
        newVelocity = dampenedVelocity + cognitiveComponent + socialComponent;
        parameterVelocity.put("CognitiveAccelerationVelocity", newVelocity);
        
        velocity = particle.getVmax().getVelocity();
        position = particle.getVmax().getParameter();
        localGuide = particle.getLocalGuidePersonal();
        globalGuide = particle.getGlobalGuidePersonal();

        dampenedVelocity = velocity * this.inertiaWeight.getParameter();
        cognitiveComponent = localGuide.getParameter() - position * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble();
        socialComponent = globalGuide.getParameter() - position * this.socialAcceleration.getParameter() * this.r2.nextDouble();
        newVelocity = dampenedVelocity + cognitiveComponent + socialComponent;
        parameterVelocity.put("VmaxVelocity", newVelocity);
        
        return parameterVelocity;
    }
}
