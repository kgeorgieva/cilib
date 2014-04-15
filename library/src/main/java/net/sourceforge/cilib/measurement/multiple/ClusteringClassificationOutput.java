/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.multiple;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import net.sourceforge.cilib.clustering.SlidingWindow;

/*
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
            assignDataPatternsToCentroid(entity);
            
            //StandardPatternDataTable table = (StandardPatternDataTable) ((SinglePopulationDataClusteringDEIterationStrategy) ((DataClusteringEC) ((CooperativeMultipopulationAlgorithm) algorithm).getPopulations().get(0)).getIterationStrategy()).getWindow().getCurrentDataset();
            StandardPatternDataTable table = (StandardPatternDataTable) ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow().getCompleteDataset();
//            ArrayList<StandardPattern> listOfPatterns = new ArrayList<StandardPattern>();
//            ArrayList<Vector> listOfPatternVectors = new ArrayList<Vector>();
//
//            for(int i = 0; i < table.size(); i++) {
//                listOfPatterns.add(table.getRow(i));
//                listOfPatternVectors.add(table.getRow(i).getVector());
//            }

            int centroidIndex = 0;
            int indx = 0;
            for(ClusterCentroid centroid : entity) {
                //System.out.println("Index: " + centroid.getDataItems().size());
                for(Vector pattern : centroid.getDataItems()) {
                    int index = pattern.getId(); //getIndexOfPattern(listOfPatternVectors, pattern);
                    //System.out.println("Index: " + index);
                    
                    //Vector.Builder vectorBuilder = Vector.newBuilder();
//                    for(Numeric num : pattern) {
//                        vectorBuilder.add(num);
//                    }
//                    vectorBuilder.add(((Numeric) listOfPatterns.get(index).getTarget()).intValue());
                    
                    //vectorBuilder.add(index);
                    //vectorBuilder.add(centroidIndex);
                    //resultList.add(vectorBuilder.build());
                                        
                    resultList.append(new StringType(index + "_" + centroidIndex));
                    indx++;
                }
                centroidIndex++;
            }
        
//        if(AbstractAlgorithm.get().getIterations() == 10) {
//            for(Type val : resultList) {
//                System.out.println(val.toString());
//            }
//        }
        
        return resultList;
    }
//    
//    private int getIndexOfPattern(ArrayList<Vector> list, Vector pattern) {
//        int previousIndex = ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow().getPreviousIndex();
//        int currentIndex = ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow().getCurrentIndex();
//        
//        int index = 0;
//        for(Vector vector : list) {
//            if(previousIndex == currentIndex || (index >= previousIndex)) {
//                if(vector.containsAll(pattern)) {
//                    return index;
//                }
//            }
//            index++;
//        }
//        
//        return -1;
//    }
    
     public void assignDataPatternsToCentroid(CentroidHolder candidateSolution) {
        EuclideanDistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        for(ClusterCentroid centroid : candidateSolution) {
            centroid.clearDataItems();
        }
        
        //DataTable dataset = ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow().getCurrentDataset();
        SlidingWindow window= ((ClusteringProblem) AbstractAlgorithm.get().getOptimisationProblem()).getWindow();
        DataTable dataset;
        if(window.windowHasSlid()) {
            dataset = window.getPreviousDataset();
        } else {
           dataset = window.getCurrentDataset(); 
        }
        
        double euclideanDistance;
        Vector addedPattern;
        Vector pattern;
        
        //System.out.println("Dataset size: " + dataset.size());
            for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }
                
                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
            
    }
    
    
}
