package edu.pnu.indoor.io;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.geotools.jdbc.JDBCDataStore;


import edu.pnu.model.trajectory.IndoorMovingObject;
import edu.pnu.model.trajectory.IndoorTrajectory;
import edu.pnu.process.NaviGraph;

public class IndoorTrajectoryImporter {
	private ArrayList<IndoorMovingObject> imos;
	public IndoorTrajectoryImporter() {
		imos = new ArrayList<IndoorMovingObject>();
	}
	public ArrayList<IndoorMovingObject> getIMVs() {
        return  imos;
    }
	public void getData() throws IOException {
		 Statement st = null;
		 Connection cx = null; 
		 ResultSet rs = null;
		 JDBCDataStore closer = new JDBCDataStore();
         try {
             // connect to template1 instead
             String url = "jdbc:postgresql" + "://" + "localhost" + ":" + "5432" + "/zone";
             cx = NaviGraph.getConnection("postgres", "gis", url);

             String sql = "select user_no, cellid, intime, outtime from lotte order by user_no, intime;";
             st = cx.createStatement();
          
             rs = st.executeQuery(sql);
             String zone = "";
        	 String user_no = "";
        	 String preuser = "pre";
        	 double intime = 0;
        	 double outtime = 0;
        	 IndoorTrajectory t = null;
        	 IndoorMovingObject imo = null;
             while(rs.next()) {
            	 user_no = rs.getString(1);
            
            	 if(!user_no.equalsIgnoreCase(preuser)) { 
                	 if(intime != 0)imos.add(imo);
            		 imo = new IndoorMovingObject();
            		 t = new IndoorTrajectory();// after receive time and check inteval, create instance of this
            		 imo.addTrajectory(t);
            		 imo.setId(user_no);
            		 preuser = user_no;
                	 
            	 }
            	
        		 zone = rs.getString(2);
            	
        		 intime = rs.getDouble(3);
            	 outtime = rs.getDouble(4);
            	 
        		 t.addTrajectory(zone, intime, outtime);
        		
            	 	 
            	 
             }
             rs.close();
         } catch (SQLException e) {
             throw new IOException("Failed to create the target database", e);
         } finally {
        	 
             closer.closeSafe(st);
             closer.closeSafe(cx);
         }
	}
}
