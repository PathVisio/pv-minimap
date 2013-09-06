package org.pathvisio.minimap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.pathvisio.core.ApplicationEvent;
import org.pathvisio.core.Engine.ApplicationEventListener;
import org.pathvisio.core.view.VPathway;
import org.pathvisio.core.view.VPathwayEvent;
import org.pathvisio.core.view.VPathwayListener;
import org.pathvisio.gui.SwingEngine;

/**
 * navigation panel for larger pathways
 * @author jakefried
 *
 */
public class MiniMapPanel extends JPanel implements AdjustmentListener, VPathwayListener, ApplicationEventListener {

	private SwingEngine eng;
		
	public MiniMapPanel(SwingEngine eng, JScrollPane pathwayScrollPane) {
		this.eng = eng;
		pathwayScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
		pathwayScrollPane.getVerticalScrollBar().addAdjustmentListener(this);
		MapMouseAdapter mouseAdapter = new MapMouseAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		eng.getEngine().addApplicationEventListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(eng.getEngine().getActivePathway() == null) {
			return;
		}
		drawMap((Graphics2D) g);
		drawRect((Graphics2D) g);
		g.dispose();
	}
	
	/**
	 * draw the pathway in the minimap
	 * @param g
	 */
	public void drawMap(Graphics2D g) {
		VPathway path = eng.getEngine().getActiveVPathway();
		double zoom = getFitZoomFactor();
		g.scale(zoom, zoom);
		path.draw(g);		
	}
	
	/**
	 * draw the rect representing your current view of the whole pathway in the minimap
	 * @param g
	 */
	public void drawRect(Graphics2D g) {
		JViewport v = eng.getApplicationPanel().getScrollPane().getViewport();
		if(v ==null) {
			return;
		}
		g.setColor(Color.RED);
		g.drawRect(v.getViewPosition().x, v.getViewPosition().y, v.getWidth(), v.getHeight());
	}
	
	/**
	 * Calculate the zoom factor that would
	 * make the pathway fit in the sidebar.
	 */
	public double getFitZoomFactor() {
		VPathway path = eng.getEngine().getActiveVPathway();
		if(path == null) {
			return 0;
		}
		Dimension panelSize = getSize();
		double xScale = panelSize.getWidth()/path.getVWidth(); 
		double yScale = panelSize.getHeight()/path.getVHeight(); 
		return Math.min(xScale, yScale);
	}
	
		/**
	 * red rect moves when scrolling through pathway
	 */
	@Override
	public void adjustmentValueChanged(AdjustmentEvent arg0) {
		repaint();
	}
	/**
	 * when pathway element is modified redraw minimap
	 */
	@Override
	public void vPathwayEvent(VPathwayEvent e) {
		repaint();
	}
	/**
	 * add vpathway listener when vpathway is opened/created/new
	 */
	@Override
	public void applicationEvent(ApplicationEvent e) {
		if(e.getType().equals(ApplicationEvent.Type.PATHWAY_OPENED) ||
				e.getType().equals(ApplicationEvent.Type.PATHWAY_NEW) ||
				e.getType().equals(ApplicationEvent.Type.APPLICATION_CLOSE)) {
			eng.getEngine().getActiveVPathway().addVPathwayListener(this);	
		}
	}
	/**
	 * MouseAdapter for clicks and drags to the minimap
	 *
	 */
	class MapMouseAdapter extends MouseAdapter {
		public void mousePressed (MouseEvent e) {
			scrollTo(e.getPoint());
		}
		public void mouseDragged(MouseEvent e) {
			scrollTo(e.getPoint());
		}
		/**
		 * called when either the mouse is clicked or dragged
		 * @param p
		 */
		public void scrollTo(Point p) {
			double zoom = getFitZoomFactor();
			p.setLocation((int) (p.x * (1/zoom)),(int) (p.y * (1/zoom))); //convert minimap point to pathway point
			eng.getEngine().getActiveVPathway().getWrapper().scrollCenterTo(p.x, p.y);
		}
	}
}
