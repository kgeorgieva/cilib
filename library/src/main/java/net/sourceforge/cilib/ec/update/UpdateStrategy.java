/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.ec.update;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;

/**
 *
 * @author Kris
 */
public interface UpdateStrategy {
    
    UpdateStrategy getClone();

    Entity update(Entity currentEntity, Topology topology);
}
