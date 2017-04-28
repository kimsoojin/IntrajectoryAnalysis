package edu.pnu.process;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.jdbc.JDBCDataStore;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;

import edu.pnu.indoor.io.SimpleIndoorGMLImporter;
import edu.pnu.model.SpaceLayer;
import edu.pnu.model.primal.CellSpace;
import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;




public class SubSpacing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PowerDiagram diagram = new PowerDiagram();

		// normal list based on an array
		OpenList sites = new OpenList();

		Random rand = new Random(100);
		
		SimpleIndoorGMLImporter importer;
			try {
				importer = new SimpleIndoorGMLImporter("src/main/resources/SAMPLE_DATA_LWM_2D.gml");
				SpaceLayer layer = importer.getSpaceLayer();
				for(Iterator<CellSpace> i = layer.getCells().iterator();i.hasNext();){
					CellSpace cell = i.next();
					String usage = (String) cell.getUserData().get("Usage");
					if(usage.equalsIgnoreCase("Corridor")) {
						PolygonSimple rootPolygon = new PolygonSimple();
						Coordinate[] points = cell.getGeometry2D().getCoordinates();
						for(int j = 0;j < points.length; j++) {
							rootPolygon.add(points[j].x, points[j].y);
						}
						
						for (int j = 0; j < 100; j++) {
							Site site = new Site(rand.nextInt(j), rand.nextInt(j));
							// we could also set a different weighting to some sites
							// site.setWeight(30)
							sites.add(site);
						}

						// set the list of points (sites), necessary for the power diagram
						diagram.setSites(sites);
						// set the clipping polygon, which limits the power voronoi diagram
						diagram.setClipPoly(rootPolygon);

						// do the computation
						diagram.computeDiagram();

						// for each site we can no get the resulting polygon of its cell. 
						// note that the cell can also be empty, in this case there is no polygon for the corresponding site.
						for (int j=0;j<sites.size;j++){
							Site site=sites.array[j];
							PolygonSimple polygon=site.getPolygon();
						}
					}
					
				}
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
			
		
            
		
		// create a root polygon which limits the voronoi diagram.
		// here it is just a rectangle.

		

		// create 100 points (sites) and set random positions in the rectangle defined above.
		
	}

}
