/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.io.ARFFFileReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.PatternConversionOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;

/**
 * This class is in charge of sliding a window across a dataset.
 * By default, if a frequency of change and a window size are set
 * the window will slide from one time-step to another, i.e. by the 
 * window's size. If a slideSize is set, then the window will slide
 * by this amount instead of the amount of windowSize.
 * If no window size is set, the dataset is assumed to be static and 
 * the window size becomes the size of the dataset and the value of
 * isTemporal becomes false.
 */
public class SlidingWindow {
    private DataTable completeDataset;
    private DataTable currentDataset;
    private DataTable previousDataset;
    private DataTableBuilder tableBuilder;
    private int windowSize;
    private int[] windowSizes = new int[0];
    private int slideSize;
    private DataOperator patternConverstionOperator;
    private int currentIndex;
    private int previousIndex;
    private int slidingTime;
    private boolean isTemporal;
    private int slideFrequency;
    private int iterationModulus;
    private int timeStep;
    private int windowSizeCount;
    private int nextChangeTS;
    private int totalTimeSteps;
    private int tsToChange;
    private boolean windowSlid;
    /*
     * Default constructor for the SlidingWindow
     */
    public SlidingWindow() {
        completeDataset = new StandardPatternDataTable();
        currentDataset = new StandardPatternDataTable();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        windowSize = 0;
        patternConverstionOperator = new PatternConversionOperator();
        currentIndex = 0;
        slidingTime = 0;
        isTemporal = true;
        slideFrequency = 0;
        slideSize = 0;
        timeStep = 1;
        windowSizeCount = 0;
        totalTimeSteps = 1000; //iterations
        windowSlid = false;
         
    }
    
    /*
     * Copy constructor for the SlidingWindow
     * @param copy The SlidingWindow to be copied
     */
    public SlidingWindow(SlidingWindow copy) {
        completeDataset = copy.completeDataset;
        currentDataset = copy.currentDataset;
        previousDataset = copy.previousDataset;
        tableBuilder = copy.tableBuilder;
        windowSize = copy.windowSize;
        patternConverstionOperator = copy.patternConverstionOperator;
        currentIndex = copy.currentIndex;
        slidingTime = copy.slidingTime;
        isTemporal = copy.isTemporal;
        slideFrequency = copy.slideFrequency;
        slideSize = copy.slideSize;
        timeStep = copy.timeStep;
        windowSizeCount = copy.windowSizeCount;
        windowSizes = copy.windowSizes;
        previousIndex = copy.previousIndex;
        iterationModulus = copy.iterationModulus;
        nextChangeTS = copy.nextChangeTS;
        totalTimeSteps = copy.totalTimeSteps;
        tsToChange = copy.tsToChange;
        windowSlid = copy.windowSlid;
    }
    
    public SlidingWindow getCleanSlidingWindow() {
        SlidingWindow cleanWindow = new SlidingWindow();
        cleanWindow.windowSize = windowSize;
        cleanWindow.patternConverstionOperator = patternConverstionOperator;
        cleanWindow.isTemporal = isTemporal;
        cleanWindow.slideFrequency = slideFrequency;
        cleanWindow.slideSize = slideSize;
        cleanWindow.tableBuilder.setSourceURL(tableBuilder.getSourceURL());
        
        return cleanWindow;
    }
    
    /*
     * Clone method for the SlidingWindow
     */
    public SlidingWindow getClone() {
        return new SlidingWindow(this);
    }
    
    /*
     * Checks if the SlidingWindow has finished sliding (has reached the end of the dataset)
     * @return true if it has not finished, false otherwise
     */
    private boolean hasNotFinished() {
            return currentIndex < completeDataset.size();
    }
    
    /*
     * Slides the window to the next position
     * @return currentDataset The current set of data patterns over which the 
     * window is currently placed
     */
    public DataTable slideWindow() {
        windowSlid = false;
        if(hasNotFinished()) {
            //System.out.println(windowSize + " - " + AbstractAlgorithm.get().getIterations() + "-" + currentIndex);
            //System.out.println("\nIteration to change: " + slidingTime + ", iteration: " + AbstractAlgorithm.get().getIterations());
            if(slidingTime == getIterationToChange()) {
                
                previousDataset = new StandardPatternDataTable();
                for(int i = 0; i < currentDataset.size(); i++) {
                    previousDataset.addRow((StandardPattern) currentDataset.getRow(i));
                }
               
                currentDataset = new StandardPatternDataTable();
                
//                System.out.println("\nIterations: " + AbstractAlgorithm.get().getIterations());
//                if(currentIndex == (windowSize * timeStep) && timeStep != 1) {
//                    changeWindowTS = timeStep;
//                    nextChangeTS = changeWindowTS;
//                }
                
                //if((timeStep == nextChangeTS) && windowSizes.length > 0) {
                //System.out.println(AbstractAlgorithm.get().getIterations() + " - " + (nextChangeTS - 1) + "-" + windowSizes.length);
                if((AbstractAlgorithm.get().getIterations() == (nextChangeTS - 1)) && windowSizes.length > 0) {
                    //System.out.println(windowSize + " - " + AbstractAlgorithm.get().getIterations() + "-" + currentIndex);
                    nextChangeTS += tsToChange;
                    //System.out.println("timeStep: " + timeStep + " ---- " + "nextChangeTS: " + nextChangeTS + " --- iterations: " + AbstractAlgorithm.get().getIterations());
                    windowSize = windowSizes[windowSizeCount];
                    slideSize = windowSize;
                    windowSizeCount++;
                }
                
                previousIndex = currentIndex;
                
                int upTo = currentIndex + slideSize;
                if(currentIndex + slideSize > completeDataset.size()) {
                    upTo = completeDataset.size();
                }
                 
                for(int i = currentIndex; i < upTo; i++) {
                    currentDataset.addRow((StandardPattern) completeDataset.getRow(i));
                }
                
                currentIndex = upTo;
                
                //System.out.println("\nTS: " + timeStep + ", windowsize: " + windowSize + " current index: " + currentIndex);
                slidingTime = 1;
                //timeStep++;
                windowSlid = true;
                //System.out.println("Complete DS: " + completeDataset.size());
            } else {
                slidingTime++;
            }
            
           
        }
        
        
        return currentDataset;
    }
    
    /*
     * Initializes the SlidingWindow to hold the appropriate poriton of the dataset.
     * Initializes the widow to be the size of the datset if it was never set.
     * Initializes the slideSize to be the size of the window if it was never set.
     * Sets the boolean isTemporal.
     * Sets the counts.
     */
    public DataTable initializeWindow() {
        //System.out.println("first iteration: " + AbstractAlgorithm.get().getIterations());
        tableBuilder.addDataOperator(new TypeConversionOperator());
        tableBuilder.addDataOperator(patternConverstionOperator);
        try {
            tableBuilder.buildDataTable();
                        
        } catch (CIlibIOException ex) {
            Logger.getLogger(DataClusteringPSO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        completeDataset = tableBuilder.getDataTable();
        
        for(int i = 0; i < completeDataset.size(); i++) {
            ((StandardPattern) completeDataset.getRow(i)).setId(i);
        }
                
        if(windowSizes.length > 0) {
            windowSize = windowSizes[windowSizeCount];
            tsToChange = totalTimeSteps / windowSizes.length;
            windowSizeCount++;
            //System.out.println("tsToChange: " + tsToChange + " --- " + "totalTimeSteps: " + totalTimeSteps + " --- " + "windowSizes.length: " + windowSizes.length);
        }
                
        if((windowSize == 0) || windowSize == completeDataset.size()) {
            windowSize = completeDataset.size();
            isTemporal = false;
        } else if(windowSize > completeDataset.size()) {
            throw new UnsupportedOperationException("The window size provided is larger than the size of the dataset");
        }
        
        if(slideSize == 0) {
            slideSize = windowSize;
        }
        
        for(int i = 0; i < currentIndex + slideSize; i++) {
            currentDataset.addRow((StandardPattern) completeDataset.getRow(i));
        }
        
        nextChangeTS = tsToChange;
        currentIndex+= slideSize;
        slidingTime++;
        timeStep++;
        
        return currentDataset;
    }
    
    /*
     * Sets the slideSize, i.e. by how much the window will slide
     * @param size The new size of the slide
     */
    public void setSlideSize(int size) {
        slideSize = size;
    }
    
    /*
     * Returns the slideSize, i.e. by how much the window will slide
     * @return slideSize The size of the slide
     */
    public int getSlideSize() {
        return slideSize;
    }
    
    /*
     * Sets the size of the window, i.e. how man data patterns are being dealt 
     * with by the algorithm at once.
     * @param size The new window size
     */
    public void setWindowSize(int size) {
        windowSize = size;
    }
    
    /*
     * Returns the size of the window, i.e. how man data patterns are being dealt 
     * with by the algorithm at once
     * @return windowSize The size of the window
     */
    public int getWindowSize() {
        return windowSize;
    }
    
    /**
     * Gets the datatable builder.
     * @return the datatable builder.
     */
    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }

    /**
     * Sets the data table builder.
     * @param dataTableBuilder the new data table builder.
     */
    public void setDataTableBuilder(DataTableBuilder dataTableBuilder) {
        this.tableBuilder = dataTableBuilder;
    }

    /**
     * Gets the source URL of the the data table builder.
     * @return the source URL of the the data table builder.
     */
    public String getSourceURL() {
        return tableBuilder.getSourceURL();
    }

    /**
     * Sets the source URL of the the data table builder.
     * @param sourceURL the new source URL of the the data table builder.
     */
    public void setSourceURL(String sourceURL) {
        tableBuilder.setSourceURL(sourceURL);
    }

    /**
     * Get the { @link PatternConversionOperator}
     * @return the pattern conversion operator
     */
    public DataOperator getPatternConversionOperator() {
        return patternConverstionOperator;
    }

    /**
     * Set the { @link PatternConversionOperator}
     * @param patternConverstionOperator the new pattern conversion operator
     */
    public void setPatternConversionOperator(DataOperator patternConverstionOperator) {
        this.patternConverstionOperator = patternConverstionOperator;
    }
    
    /**
     * Gets the current dataset.
     * @return the current dataset builder.
     */
    public DataTable getCurrentDataset() {
        return currentDataset;
    }
    
    /**
     * Gets the current dataset.
     * @return the current dataset builder.
     */
    public DataTable getCompleteDataset() {
        return completeDataset;
    }
    
    /*
     * Gets the iteration at which the window should slide
     * @return The iteration during which the window should slide
     */
    private int getIterationToChange() {
        return slideFrequency;
    }
    
    /*
     * Returns the fequency of change.
     * Note: currently this is the same as getIterationToChange, but the representation 
     * of frequencies may be altered in future.
     * @return The frequency of change
     */
    public int getSlideFrequency() {
        return slideFrequency;
    }
    
    /*
     * Sets the frequency at which the window slides
     * @param frequency The new frequency of change
     */
    public void setSlideFrequency(int newFrequency) {
        slideFrequency = newFrequency;
    }
    
    /*
     * Checks if the window has slid to the next portion of the dataset
     * @return true if it has slid, false otherwise
     */
    public boolean hasSlid() {
        return isTemporal && slidingTime == 0;
    }

    public int getIterationModulus() {
        return iterationModulus;
    }

    public void setIterationModulus(int iterationModulus) {
        this.iterationModulus = iterationModulus;
    }
    
    public void clearTables() {
        tableBuilder = new DataTableBuilder();
    }
    
    public int[] getWindowSizes() {
        return windowSizes;
    }
    
    public void setWindowSizes(int[] windowSizes) {
        this.windowSizes = windowSizes;
    }
    
    public void setExtraWindowSize(int newWindowSize) {
        int[] tempWindowSizes = new int[windowSizes.length + 1];
        int count = 0;
        for(int value : windowSizes) {
            tempWindowSizes[count] = value;
            count++;
        }
        tempWindowSizes[tempWindowSizes.length - 1] = newWindowSize;
        windowSizes = tempWindowSizes;
    }

    public int getPreviousIndex() {
        return previousIndex;
    }

    public void setPreviousIndex(int previousIndex) {
        this.previousIndex = previousIndex;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public DataTable getPreviousDataset() {
        return previousDataset;
    }

    public boolean windowHasSlid() {
        return windowSlid;
    }
    
}
