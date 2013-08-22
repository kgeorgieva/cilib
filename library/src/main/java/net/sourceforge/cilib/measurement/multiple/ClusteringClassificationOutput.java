/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.multiple;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.CooperativeMultipopulationAlgorithm;
import net.sourceforge.cilib.clustering.DataClusteringEC;
import net.sourceforge.cilib.clustering.de.iterationstrategies.SinglePopulationDataClusteringDEIterationStrategy;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kris
 */
public class ClusteringClassificationOutput implements Measurement<TypeList>{

    public Measurement<TypeList> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TypeList getValue(Algorithm algorithm) {
        TypeList resultList = new TypeList();
        
            CentroidHolder entity = (CentroidHolder) algorithm.getBestSolution().getPosition();
            StandardPatternDataTable table = (StandardPatternDataTable) ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) ((CooperativeMultipopulationAlgorithm) algorithm).getPopulations().get(0)).getIterationStrategy()).getWindow().getCurrentDataset();
            ArrayList<StandardPattern> listOfPatterns = new ArrayList<StandardPattern>();
            ArrayList<Vector> listOfPatternVectors = new ArrayList<Vector>();

            for(int i = 0; i < table.size(); i++) {
                listOfPatterns.add(table.getRow(i));
                listOfPatternVectors.add(table.getRow(i).getVector());
            }

            int centroidIndex = 0;
            for(ClusterCentroid centroid : entity) {
                for(Vector pattern : centroid.getDataItems()) {
                    int index = getIndexOfPattern(listOfPatternVectors, pattern);
                    Vector.Builder vectorBuilder = Vector.newBuilder();
                    for(Numeric num : pattern) {
                        vectorBuilder.add(num);
                    }
                    vectorBuilder.add(((Numeric) listOfPatterns.get(index).getTarget()).intValue());
                    vectorBuilder.add(centroidIndex);
                    resultList.add(vectorBuilder.build());
                }
                centroidIndex++;
            }
        
        return resultList;
    }
    
    private int getIndexOfPattern(ArrayList<Vector> list, Vector pattern) {
        int index = 0;
        for(Vector vector : list) {
            if(vector.containsAll(pattern)) {
                return index;
            }
            index++;
        }
        
        return -1;
    }
    
}
