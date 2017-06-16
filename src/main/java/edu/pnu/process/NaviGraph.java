package edu.pnu.process;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.jdbc.JDBCDataStore;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import edu.pnu.indoor.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.dual.Transition;
import edu.pnu.model.primal.CellSpace;

public class NaviGraph {
	private static ArrayList<SpaceLayer> layer;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleIndoorGMLImporter importer;
		try {
			importer = new SimpleIndoorGMLImporter("src/main/resources/SAMPLE_DATA_LWM_2D.gml");
			layer = importer.getSpaceLayer();
			CellSpace cs = layer.get(0).getCellSpace("C931");
			Coordinate[] c = cs.getGeometry2D().getCoordinates();
			String sql = "SELECT st_astext(ST_ApproximateMedialAxis(ST_GeomFromText('POLYGON ((";
			sql += c[0].x + " " + c[0].y;
			for(int i = 1;i < c.length;i++) {
				Coordinate cd = c[i];
				sql += "," + cd.x + " " + cd.y;
			}
			sql += "))')));";
			 Statement st = null;
			 Connection cx = null; 
			 ResultSet rs = null;
			 JDBCDataStore closer = new JDBCDataStore();
             try {
                 // connect to template1 instead
                 String url = "jdbc:postgresql" + "://" + "localhost" + ":" + "5432" + "/zone";
                 cx = getConnection("postgres", "gis", url);

                 // create the database
                 
                 //String createParams = (String) CREATE_PARAMS.lookUp(params);
                 //String sql = "CREATE DATABASE \"" + db + "\" " + (createParams == null ? "" : createParams);
                 st = cx.createStatement();
              
                 rs = st.executeQuery(sql);
                 
                 rs.close();
             } catch (SQLException e) {
                 throw new IOException("Failed to create the target database", e);
             } finally {
            	 
                 closer.closeSafe(st);
                 closer.closeSafe(cx);
             }
             /* SpaceLayer result = new SpaceLayer();
			for(Iterator<CellSpace> i = layer.getCells().iterator();i.hasNext();){
				CellSpace cell = i.next();
				String usage = (String) cell.getUserData().get("Usage");
				if(usage.equalsIgnoreCase("Door") || usage.equalsIgnoreCase("Stair")) {
					for(Iterator<State> j = cell.getDuality().getNeighbors().iterator();i.hasNext();){
						State state = j.next();
						if(state.getNumberofConnects() > 2){
							cell.getDuality().removeTransition(state.getId());
						}else{
							Transition t = cell.getDuality().getConnectWith(state);
							if(result.getTransition(t.getId()) != null){
								result.addTransition(cell.getDuality().getConnectWith(state));
							}
							
						}
					}
					result.addCellSpace(cell);
					result.addState(cell.getDuality());
					
				}else if(cell.getDuality().getNumberofConnects() < 2){
					
				}
				else{
					Coordinate[] c = cs.getGeometry2D().getCoordinates();
					String sql = "SELECT st_astext(ST_ApproximateMedialAxis(ST_GeomFromText('POLYGON ((";
					sql += c[0].x + " " + c[0].y;
					for(int i = 1;i < c.length;i++) {
						Coordinate cd = c[i];
						sql += "," + cd.x + " " + cd.y;
					}
					sql += "))')));";
				}
				
				
				
			}*/
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
	public static Connection getConnection(String user, String password, String url) throws SQLException {
        Connection cx;
        if(user != null) {
            cx = DriverManager.getConnection(url, user, password);
        } else {
            cx = DriverManager.getConnection(url);
        }
        return cx;
    }
}
