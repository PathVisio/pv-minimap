package org.pathvisio.minimap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.pathvisio.desktop.PvDesktop;
import org.pathvisio.desktop.plugin.Plugin;

/**
 * 
 * @author mkutmon
 * Plugin class registering the Navigation Panel 
 *
 */
public class MiniMapPlugin implements Plugin {

	private PvDesktop desktop;
	private JPanel navPanel;
	
	@Override
	public void init(PvDesktop desktop) {
		this.desktop = desktop;
		
		JScrollPane pathwayScrollPane = new JScrollPane();		
		navPanel = new MiniMapPanel(desktop.getSwingEngine(), pathwayScrollPane);
		
		JTabbedPane sidebarTabbedPane = desktop.getSideBarTabbedPane();
		sidebarTabbedPane.addTab("MiniMap", navPanel);
	}

	@Override
	public void done() {
		desktop.getSideBarTabbedPane().remove(navPanel);
	}

}
