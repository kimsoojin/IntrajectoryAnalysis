package edu.pnu.process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.pnu.indoor.io.IndoorTrajectoryImporter;
import edu.pnu.indoor.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.trajectory.IndoorMovingObject;
import edu.pnu.model.trajectory.IndoorTrajectory;
import edu.pnu.util.StateDijkstraPathFinder;

public class Similarity {
	
    static ArrayList<SpaceLayer> l;
    public Similarity() throws ParserConfigurationException, SAXException, IOException{
    	SimpleIndoorGMLImporter importer = new SimpleIndoorGMLImporter("src/main/resources/shopping2.gml");
        
    	l = importer.getSpaceLayer();
    }
    
	public double calSimilarity(IndoorTrajectory t1, IndoorTrajectory t2) {
		double similarity = 0;
		int[] LCSS = getLCSS(t1.getCellSequence(), t2.getCellSequence());
		double st1 = t1.getintime(LCSS[0]);
		double st2 = t2.getintime(LCSS[1]);
		if(st1 > st2) {
			t2.shiftTime(st1 - st2);
		}else {
			t1.shiftTime(st2 - st1);
		}
		similarity = LCSS[2];
		return similarity;
	}
	public double calIndoorDistance(String cellid_1, String cellid_2){
		double distance = 0;
		StateDijkstraPathFinder finder = new StateDijkstraPathFinder(l.get(0));
		State s1 = l.get(0).getCellSpace(cellid_1).getDuality();
		State s2 = l.get(0).getCellSpace(cellid_2).getDuality();
		distance = finder.getShortestPath(s1, s2);
		return distance;
	}
	public int[] getLCSS(List<String> t1, List<String> t2){
		int Start1 = 0;
		int Start2 = 0;
	    int Max = 0;
	    for (int i = 0; i < t1.size(); i++)
	    {
	        for (int j = 0; j < t2.size(); j++)
	        {
	            int x = 0;
	            while (t1.get(i + x).equalsIgnoreCase(t2.get(j + x)))
	            {
	                x++;
	                if (((i + x) >= t1.size()) || ((j + x) >= t2.size())) break;
	            }
	            if (x > Max)
	            {
	                Max = x;
	                Start1 = i;
	                Start2 = j;
	            }
	         }
	    }
	    int[] result = {Start1, Start2, Max};
	    return result;
	}
	public static void main(String[] args) throws Exception { 
        IndoorTrajectoryImporter iti = new IndoorTrajectoryImporter();
        iti.getData();
        ArrayList<IndoorMovingObject> imos = iti.getIMVs();
        Similarity sm = new Similarity();
        try {
        
            BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
            for(int i = 1;i < imos.size();i++){
            	IndoorMovingObject first = imos.get(i - 1);
            	IndoorMovingObject second = imos.get(i);
            	IndoorTrajectory ft = first.getTrajectory(0);
            	IndoorTrajectory st = second.getTrajectory(0);
            
            	out.write(ft.toString()); out.newLine();
            	out.write(st.toString()); out.newLine();
            	
            	out.write(String.valueOf(sm.calSimilarity(ft, st))); out.newLine();
            }
            out.close();
           
          } catch (IOException e) {
              System.err.println(e); 
              System.exit(1);
          }
    } 
}
class CSS{
	String cellid;
	int firstPosition;
	int secondPosition;
}
