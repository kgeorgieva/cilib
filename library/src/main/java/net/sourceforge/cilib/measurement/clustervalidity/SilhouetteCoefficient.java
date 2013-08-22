/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author Kris
 */
public class SilhouetteCoefficient extends ValidityIndex{

    DistanceMeasure distanceMeasure;
    
    public SilhouetteCoefficient() {
         distanceMeasure = new EuclideanDistanceMeasure();
    }
    
    @Override
    public Measurement<Real> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder bestSolution = (CentroidHolder) algorithm.getBestSolution().getPosition();
        
        double totalAverageSilhouette = 0;
        int numberOfPatterns = 0;
        
        for(ClusterCentroid centroid : bestSolution) {         
            
            for(Vector pattern : centroid.getDataItems()) {
                double localDissimilarity = getAverageDissimilarity(centroid, pattern, true);
                double minExternalDisimilarity = Double.POSITIVE_INFINITY;
                
                for(ClusterCentroid otherCentroid : bestSolution) {
                    if(centroid != otherCentroid) {
                        double externalDisimilarity = getAverageDissimilarity(otherCentroid, pattern, false);
                        
                        if(externalDisimilarity < minExternalDisimilarity) {
                            minExternalDisimilarity = externalDisimilarity;
                        }
                    }
                }
                
                totalAverageSilhouette += calculateSilhouette(localDissimilarity, minExternalDisimilarity);
                numberOfPatterns++;
            }
            
            /*if(centroid.getDataItems().size() > 0) {
                totalAverageSilhouette += averagePatternSilhouette / (double) centroid.getDataItems().size();
            }*/
        }
        
        return Real.valueOf(totalAverageSilhouette / (double) numberOfPatterns);
        
        
    }
    
    private double getAverageDissimilarity(ClusterCentroid centroid, Vector comparedPattern, boolean sameCluster) {
        double total = 0;
        
        for(Vector pattern : centroid.getDataItems()) {
            if(!pattern.equals(comparedPattern)) {
                total += distanceMeasure.distance(pattern, comparedPattern);
            }
            
        }
        
        double denominator;
        
        if(sameCluster) {
            denominator = (double) (centroid.getDataItems().size() - 1);
        } else {
            denominator = (double) (centroid.getDataItems().size());
        }
        
        if(denominator != 0) {
            return total / denominator;
        }
        
        return denominator;
    }
    
    private double calculateSilhouette(double localDissimilarity, double minExternalDisimilarity) {
        double denominator = minExternalDisimilarity;
        if(localDissimilarity > minExternalDisimilarity) {
            denominator = localDissimilarity;
        }
        
        if(denominator == 0) {
            return 0;
        }
        
        return (minExternalDisimilarity - localDissimilarity) / denominator;
        
    }
    
}
