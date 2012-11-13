/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.clustering.entity;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.initialization.DataDependantInitializationStrategy;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;

/**
 *
 * @author Kris
 */
public interface ClusterEntity extends Entity{
    public int getNumberOfClusters();

    public void setNumberOfClusters(int numberOfClusters);
    
    public DataDependantInitializationStrategy getCentroidInitialisationStrategy();

    public void setCentroidInitialisationStrategy(DataDependantInitializationStrategy centroidInitialisationStrategy);
    
}
