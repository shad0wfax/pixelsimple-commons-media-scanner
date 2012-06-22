/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.Resource;
import com.pixelsimple.appcore.Resource.RESOURCE_TYPE;
import com.pixelsimple.appcore.registry.RegistryService;
import com.pixelsimple.commons.mediascanner.config.MediaScannerConfig;
import com.pixelsimple.commons.mediascanner.config.MediaScannerRegistryKeys;

/**
 * @author Akshay Sharma
 *
 * Jun 18, 2012
 */
public class MediaScanner {
	
	/**
	 * Factory method that will create a correct scanner based on the resource type.
	 * @param baseDir
	 * @return
	 */
	public Scanner create(Resource resource) throws MediaScannerException {
		if (!resource.isValid()) {
			throw new MediaScannerException("Looks like the passed in resource is not valid, " +
				"or does not have the correct read permissions. - " + resource);
		}

		ApiConfig apiConfig = RegistryService.getRegisteredApiConfig();
		MediaScannerConfig config = RegistryService.getGenericRegistryEntry().getEntry(
				MediaScannerRegistryKeys.MEDIA_SCANNER_CONFIG);
			
		if (resource.getResourceType() == RESOURCE_TYPE.DIRECTORY) {
			return new DirectoryScanner(resource, apiConfig, config);
			// Add other scanner types supported for future. 
		} else {
			// No scanner exists for the resource type passed. Flag erro?
			throw new MediaScannerException("Looks like the there is no scanner available for the resource type in" + resource);
		}
		
	}
}
