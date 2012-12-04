/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.initialization.DataDependantInitializationStrategy;

/**
 * Interface for entities used for clustering problems
 */
public interface ClusterEntity extends Entity{
    /*
     * Gets the total number of clusters
     * @return The number of clusters
     */
    public int getNumberOfClusters();

    /*
     * Sets the total number of clusters 
     * @param numberOfClusters The new number of clsuters
     */
    public void setNumberOfClusters(int numberOfClusters);
    
    /*
     * Gets the initialisation strategy used to initialise the centroids
     * @return The centroid initialisation strategy
     */
    public DataDependantInitializationStrategy getCentroidInitialisationStrategy();

    /*
     * Sets the initialisation strategy used to initialise the centroids
     * @param The new centroid initialisation strategy
     */
    public void setCentroidInitialisationStrategy(DataDependantInitializationStrategy centroidInitialisationStrategy);
    
}
