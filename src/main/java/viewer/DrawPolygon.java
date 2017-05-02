package viewer;
import com.vividsolutions.jts.geom.Coordinate; 
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

import edu.pnu.process.SubSpacing;

import java.awt.Graphics; 
import java.awt.Point; 
import java.awt.Rectangle; 
import java.awt.geom.AffineTransform; 
import java.awt.geom.Point2D; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame; 
import javax.swing.JPanel; 
public class DrawPolygon extends JPanel { 
    private static final int MARGIN = 5; 

    private List<Polygon> polygons = new ArrayList<Polygon>(); 
    private static List<Stack<Coordinate>> points = new ArrayList<Stack<Coordinate>>();
    private AffineTransform geomToScreen; 

    public void addPolygon(Polygon polygon) { 
    	polygons.add(polygon); 
    } 

    @Override 
    protected void paintComponent(Graphics g) { 
        super.paintComponent(g); 

        if (!polygons.isEmpty()) { 
            setTransform(); 

            for (Polygon polygon : polygons) { 
                Point[] screenCoords = getScreenCoords(polygon); 
                
                //int length = screenCoords.length;
                //int[] x = new int[length],y = new int[length];int index = 0;
                for (int i = 1; i < screenCoords.length; i++) { 
                    g.drawLine(screenCoords[i-1].x, screenCoords[i-1].y, 
                               screenCoords[i].x, screenCoords[i].y); 
                    //x[index] = screenCoords[i].x;
                    //y[index] = screenCoords[i].y;
                    //index++;
                } 
                //g.drawPolygon(x,y,length);
            } 
            for(Stack<Coordinate> p : points) {
            	while(!p.empty()) {
            		Point screencoord = getScreeanCoord(p.pop());
                	char[] c = {'p'};
                	g.drawChars(c, 0, 1, screencoord.x, screencoord.y);
            	}
            	
            }
        } 
    } 

    private void setTransform() { 
        Envelope env = getGeometryBounds(); 
        Rectangle visRect = getVisibleRect(); 
        Rectangle drawingRect = new Rectangle( 
                visRect.x + MARGIN, visRect.y + MARGIN, visRect.width 
- 2*MARGIN, visRect.height - 2*MARGIN); 

        double scale = Math.min(drawingRect.getWidth() / 
env.getWidth(), drawingRect.getHeight() / env.getHeight()); 
        double xoff = MARGIN - scale * env.getMinX(); 
        double yoff = MARGIN + env.getMaxY() * scale; 
        geomToScreen = new AffineTransform(scale, 0, 0, -scale, xoff, yoff); 
    } 

    private Envelope getGeometryBounds() { 
        Envelope env = new Envelope(); 
        for (Polygon polygon : polygons) { 
            Envelope lineEnv = polygon.getEnvelopeInternal(); 
            env.expandToInclude(lineEnv); 
        } 

        return env; 
    } 

    private Point[] getScreenCoords(Polygon polygon) { 
        Coordinate[] coords = polygon.getCoordinates(); 
        Point[] screenCoords = new Point[coords.length]; 
        Point2D p = new Point2D.Double(); 

        for (int i = 0; i < coords.length; i++) { 
            p.setLocation(coords[i].x, coords[i].y); 
            geomToScreen.transform(p, p); 
            screenCoords[i] = new Point((int)p.getX(), (int)p.getY()); 
            System.out.println(screenCoords[i]); 
        } 

        return screenCoords; 
    } 
    private Point getScreeanCoord(Coordinate coords) {
        Point screenCoords = new Point(); 
        Point2D p = new Point2D.Double(); 

        p.setLocation(coords.x, coords.y); 
        geomToScreen.transform(p, p); 
        screenCoords = new Point((int)p.getX(), (int)p.getY()); 
        
        return screenCoords; 
    }
    public static void main(String[] args) throws Exception { 
        DrawPolygon panel = new DrawPolygon(); 
        JFrame frame = new JFrame("Draw polygons"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.add(panel); 
        frame.setSize(500, 500); 

        SubSpacing sub = new SubSpacing();
        sub.makeSpaceLayer();
        ArrayList<Geometry> collection = sub.getVoronoi();
        //for(int i = 0;i < collection.size();i++) {
        	Geometry geom = collection.get(0);
        	for (int j = 0; j < geom.getNumGeometries(); j++) {
    			Geometry g = geom.getGeometryN(j);
    			panel.addPolygon((Polygon) g);
    		}
        //}
         points = sub.getSites();
        
        

        frame.setVisible(true); 
    } 
} 
