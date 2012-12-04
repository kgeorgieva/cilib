/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.update;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Interface holding the structure of Update strategies
 */
public interface UpdateStrategy {
    
    /*
     * Clone method for an UpdateStrategy
     * @return A new instance of this UpdateStrategy
     */
    UpdateStrategy getClone();

    /*
     * Updates the parameter accordingly
     */
    Entity update(Entity currentEntity, Topology topology);
}
