package org.pathvisio.minimap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.pathvisio.desktop.plugin.Plugin;

/**
 * 
 * @author mkutmon
 * Activator class for the Navigation plugin for PathVisio
 *
 */
public class MiniMapActivator implements BundleActivator {

	private MiniMapPlugin plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = new MiniMapPlugin();
		context.registerService(Plugin.class.getName(), plugin, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin.done();
	}

}
