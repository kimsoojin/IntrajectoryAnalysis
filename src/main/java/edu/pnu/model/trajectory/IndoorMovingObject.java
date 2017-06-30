package edu.pnu.model.trajectory;

import java.util.ArrayList;
import java.util.List;

public class IndoorMovingObject {
	List<IndoorTrajectory> trajectories;//one trajectory take one day
	String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public IndoorMovingObject() {
		// TODO Auto-generated constructor stub
		trajectories = new ArrayList<IndoorTrajectory>();
	}
	public void addTrajectory(IndoorTrajectory it) {
		trajectories.add(it);
	}
	public IndoorTrajectory getTrajectory(int i) {
		return trajectories.get(i);
	}
	public int getTrajectorySize() {
		return trajectories.size();
	}
}
