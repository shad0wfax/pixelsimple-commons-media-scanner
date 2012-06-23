/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.init.ModuleInitializer;
import com.pixelsimple.appcore.registry.GenericRegistryEntry;
import com.pixelsimple.commons.mediascanner.config.MediaScannerConfig;
import com.pixelsimple.commons.mediascanner.config.MediaScannerRegistryKeys;
import com.pixelsimple.commons.util.OSUtils;

/**
 * @author Akshay Sharma
 *
 * Jun 18, 2012
 */
public class MediaScannerInitializer extends ModuleInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(MediaScannerInitializer.class);
	private static final String APP_CONFIG_MEDIA_SCANNER_PATH = "mediaScannerPath";
	// TODO: When linux is supported initialize it.
	private static final String CONFIG_FILE = (OSUtils.isWindows()) ? "media_scanner_config_win.properties" 
			: "media_scanner_config_mac.properties";


	public MediaScannerInitializer() {
		super(CONFIG_FILE);
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.init.Initializable#initialize(com.pixelsimple.appcore.ApiConfig)
	 */
	@Override
	public void doInitialize(ApiConfig apiConfig) throws Exception {
		LOG.debug("initialize Initing media scanner");
		
		MediaScannerConfig config = new MediaScannerConfig();
		config.setMediaScannerPath(this.moduleConfigurationMap.get(APP_CONFIG_MEDIA_SCANNER_PATH));
		
		GenericRegistryEntry genericRegistryEntry = apiConfig.getGenericRegistryEntry();
		genericRegistryEntry.addEntry(MediaScannerRegistryKeys.MEDIA_SCANNER_CONFIG, config);
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.init.Initializable#deinitialize(com.pixelsimple.appcore.ApiConfig)
	 */
	@Override
	public void doDeinitialize(ApiConfig apiConfig) throws Exception {
		GenericRegistryEntry genericRegistryEntry = apiConfig.getGenericRegistryEntry();
		genericRegistryEntry.removeEntry(MediaScannerRegistryKeys.MEDIA_SCANNER_CONFIG);
	}

}
