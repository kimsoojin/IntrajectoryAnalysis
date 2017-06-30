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
	public String findCellbyTime(double time) {
    	for(int i = 0;i<intime.size();i++) {
    		if(time > intime.get(i)) {
    			return cellsequence.get(i);
    		}
    	}
    	return "";
    }
}
