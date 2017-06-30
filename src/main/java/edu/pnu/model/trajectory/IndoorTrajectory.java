package edu.pnu.model.trajectory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IndoorTrajectory {
	List<String> cellsequence;
	List<Double> intime;
	List<Double> outtime;
	public IndoorTrajectory() {
		cellsequence = new ArrayList<String>();
		intime = new ArrayList<Double>();
		outtime = new ArrayList<Double>();
	}
	public void addTrajectory(String zone, double in, double out) {
		cellsequence.add(zone);
		intime.add(in);
		outtime.add(out);
	}
	public void shiftTime(double offset) {
		for(int i = 0;i < intime.size();i++) {
			intime.set(i, intime.get(i) + offset);
			outtime.set(i, outtime.get(i) + offset);
		}
	}
	public String toString() {
		String sequence = "";
		for(int i = 0;i < cellsequence.size();i++) {
			sequence += cellsequence.get(i) + "[";
			sequence += intime.get(i) + ",";
			sequence += outtime.get(i) + "] ";
		}
		sequence += "\n";
		return sequence;
	}
	public int getSize() {
		return cellsequence.size();
	}
	public List<String> getCellSequence() {
		return cellsequence;
	}
	public double getintime(int i) {
		return intime.get(i);
	}
	public String findCellbyTimeInterval(double itime, double otime) {
		double max = 0;
		String cell = "";
    	for(int i = 0;i<intime.size();i++) {
    		if(itime < outtime.get(i)) {
    			if(max == 0 && otime <= outtime.get(i)) {
    				max = otime - itime;
    				cell = cellsequence.get(i);
    				break;
    			}else {
    				double interval = outtime.get(i) - Math.max(itime, intime.get(i));
    				if(max < interval) {
    					max = interval;
    					cell = cellsequence.get(i);
    				}
    			}
    		
    		}
    	}
    	return cell;
    }
}
