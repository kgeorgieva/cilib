/*
 * RNAAccuracyMeasurement.java
 * 
 * Created on 2005/10/18
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 * 
 */
package net.sourceforge.cilib.bioinf.rnaprediction;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

public class RNAAccuracyMeasurement implements Measurement {
	
	public RNAAccuracyMeasurement(RNAAccuracyMeasurement copy) {
	}
	
	public RNAAccuracyMeasurement clone() {
		return new RNAAccuracyMeasurement(this);
	}

	public String getDomain() {
		return "?";
	}

	public Type getValue() {
		Collection<OptimisationSolution> solutions = ((RNAPSO) Algorithm.get()).getSolutions();
		StringBuilder sb = new StringBuilder();
		//Iterator i = solutions.iterator();
		for (OptimisationSolution solution : solutions) {
			int totalPairs = 0;
			int correctPairs = 0;
			totalPairs = getTotalPairs(solution);
			correctPairs = getCorrectPairs(solution);
			
			sb.append("Pairs: " + totalPairs + " Correct: " + correctPairs + "\t" );
		}
		
		StringType t = new StringType();
		t.setString(sb.toString());
		return t;
	}

	private int getTotalPairs(OptimisationSolution solution) {
		RNAConformation conf = (RNAConformation) solution.getPosition();
		return conf.getNumOfPairs();
	}
	
	private int getCorrectPairs(OptimisationSolution solution) {
		RNAConformation conf = (RNAConformation) solution.getPosition();
		int correct = 0;
		for (RNAStem stem : conf) {
			for (NucleotidePair np : stem) {
				if (NucleotideString.getInstance().knownConf.contains(np))
					correct++;
			}
		}		
		return correct;
	}
	

}