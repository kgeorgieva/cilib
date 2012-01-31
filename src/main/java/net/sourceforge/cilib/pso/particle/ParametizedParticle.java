/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class ParametizedParticle extends AbstractParticle {
 
    private static final long serialVersionUID = 2610843008637279845L;
    protected Particle neighbourhoodBest;
    
    public ParametizedParticle() {
        super();
        this.getProperties().put(EntityType.Particle.BEST_POSITION, new Vector());
        this.getProperties().put(EntityType.Particle.VELOCITY, new Vector());
    }
    
    public ParametizedParticle(ParametizedParticle copy) {
        super(copy);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ParametizedParticle getClone() {
           return new ParametizedParticle(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        ParametizedParticle other = (ParametizedParticle) object;
        return super.equals(object) &&
            (this.neighbourhoodBest == null ? true : this.neighbourhoodBest.equals(other.neighbourhoodBest));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return (Fitness) this.getProperties().get(EntityType.Particle.BEST_FITNESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getBestPosition() {
        /*TODO: Kristina Modify*/
        return (Vector) this.getProperties().get(EntityType.Particle.BEST_POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return getPosition().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle getNeighbourhoodBest() {
        return this.neighbourhoodBest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getPosition() {
        /*TODO: Kristina Modify*/
        return (Vector) getCandidateSolution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return (Vector) this.getProperties().get(EntityType.Particle.VELOCITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(OptimisationProblem problem) {
        /*TODO: Kristina Modify*/
        
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresenation().getClone());
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getPosition().getClone());
        this.getProperties().put(EntityType.Particle.VELOCITY, getPosition().getClone());

        this.positionInitialisationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialize(EntityType.Particle.BEST_POSITION, this);
        this.velocityInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        /*TODO: Kristina Modify*/
        getProperties().put(EntityType.CANDIDATE_SOLUTION, this.behavior.getPositionProvider().get(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        /*TODO: Kristina Modify*/
        Fitness fitness = getFitnessCalculator().getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);

        this.personalBestUpdateStrategy.updatePersonalBest(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = particle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVelocity() {
        /*TODO: Kristina Modify*/
        getProperties().put(EntityType.Particle.VELOCITY, this.behavior.getVelocityProvider().get(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControlParameters() {
        /*TODO: Kristina Modify*/
        this.behavior.getVelocityProvider().updateControlParameters(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        /*TODO: Kristina Modify*/
        this.positionInitialisationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialize(EntityType.Particle.BEST_POSITION, this);
        this.velocityInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);
    }
}
