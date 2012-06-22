/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner.init;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.config.Environment;
import com.pixelsimple.appcore.init.Initializable;
import com.pixelsimple.appcore.registry.GenericRegistryEntry;
import com.pixelsimple.commons.mediascanner.config.MediaScannerConfig;
import com.pixelsimple.commons.mediascanner.config.MediaScannerRegistryKeys;

/**
 * @author Akshay Sharma
 *
 * Jun 18, 2012
 */
public class MediaScannerInitializer implements Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(MediaScannerInitializer.class);
	private static final String APP_CONFIG_MEDIA_SCANNER_PATH = "mediaScannerPath";


	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.init.Initializable#initialize(com.pixelsimple.appcore.ApiConfig)
	 */
	@Override
	public void initialize(ApiConfig apiConfig) throws Exception {
		LOG.debug("initialize Initing media scanner");
		
		Environment env = apiConfig.getEnvironment();
		Map<String, String> configMap = env.getImmutableApplicationConfiguratations();

		MediaScannerConfig config = new MediaScannerConfig();
		config.setMediaScannerPath(configMap.get(APP_CONFIG_MEDIA_SCANNER_PATH));
		
		GenericRegistryEntry genericRegistryEntry = apiConfig.getGenericRegistryEntry();
		genericRegistryEntry.addEntry(MediaScannerRegistryKeys.MEDIA_SCANNER_CONFIG, config);
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.init.Initializable#deinitialize(com.pixelsimple.appcore.ApiConfig)
	 */
	@Override
	public void deinitialize(ApiConfig apiConfig) throws Exception {
		GenericRegistryEntry genericRegistryEntry = apiConfig.getGenericRegistryEntry();
		genericRegistryEntry.removeEntry(MediaScannerRegistryKeys.MEDIA_SCANNER_CONFIG);
	}

}
