<<<<<<< HEAD
package edu.pnu.process;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.jdbc.JDBCDataStore;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;

import edu.pnu.indoor.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.dual.State;
import edu.pnu.model.primal.CellSpace;





public class SubSpacing {
	ArrayList<Stack<Coordinate>> sites = new ArrayList<Stack<Coordinate>>();
	public SpaceLayer sl;
	public void makeSpaceLayer() {
		SimpleIndoorGMLImporter importer;

			try {
				importer = new SimpleIndoorGMLImporter("src/main/resources/removeDoorState.gml");
				sl = importer.getSpaceLayer();
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
	public void makeModifiedSpaceLayer() {
		
	}
	public ArrayList<Geometry> getVoronoi() {
		VoronoiDiagramBuilder vd = new VoronoiDiagramBuilder();
		ArrayList<Geometry> result = new ArrayList<Geometry>();
		
		for(Iterator<CellSpace> i = sl.getCells().iterator();i.hasNext();){
			CellSpace cell = i.next();
			String usage = (String) cell.getUserData().get("USAGE");
			if(usage.equalsIgnoreCase("Corridor")) {
				//List<Geometry> site = new ArrayList<Geometry>();
				
				Stack<Coordinate> stack = new Stack<Coordinate>();
				State duality = cell.getDuality();
				List<State> interlayer = duality.getInterLayerConnection();
				for (int j = 0; j < interlayer.size(); j++) {
					State s = interlayer.get(j);
					Point p = s.getPoint();
					//site.add(p);
					stack.push(p.getCoordinate());
					//Site site = new Site(p.getX(), p.getY());
					
					//sites.add(site);
				}
				vd.setSites(stack);
				sites.add(stack);
				Geometry geom = vd.getDiagram(new GeometryFactory());
				Geometry clipPoly = cell.getGeometry2D();
				List<Geometry> clipped = new ArrayList<Geometry>();
				for (int j = 0; j < geom.getNumGeometries(); j++) {
					Geometry g = geom.getGeometryN(j);
					Object userData = new HashMap<Object, Object>();
					userData = g.getUserData();
					
					Geometry clip = null;
					// don't clip unless necessary
					if (clipPoly.contains(g))
						clip = g;
					else if (clipPoly.intersects(clipPoly)) {
						clip = clipPoly.intersection(g);
						// keep vertex key info
						clip.setUserData(g.getUserData());
					}

					if (clip != null && ! clip.isEmpty()) {
						clipped.add(clip);
					}
				}
				result.add(geom.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(clipped)));
				/*for(int j=0;j<geo.getNumGeometries();j++)
			    {
			        Polygon poly=(Polygon)geo.getGeometryN(j);
			        Coordinate[] c = poly.getCoordinates();
			        for(int k = 0;k < c.length;k++) {
			        	System.out.println(c[k].toString());
			        }
			    }*/
				break;

				}
				
			}
			
			return result;
	}
	public ArrayList<Stack<Coordinate>> getSites() {
		return sites;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
			
		
            
		
		// create a root polygon which limits the voronoi diagram.
		// here it is just a rectangle.

		

		// create 100 points (sites) and set random positions in the rectangle defined above.
		
	}

}