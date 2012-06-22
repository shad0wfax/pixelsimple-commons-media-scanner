/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner.config;

import com.pixelsimple.appcore.registry.GenericRegistryEntryKey;

/**
 *
 * @author Akshay Sharma
 * May 16, 2012
 */
public enum MediaScannerRegistryKeys implements GenericRegistryEntryKey {

	MEDIA_SCANNER_CONFIG;

	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.registry.GenericRegistryEntryKey#getUniqueModuleName()
	 */
	@Override
	public String getUniqueModuleName() {
		return "mediascanner";
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.appcore.registry.GenericRegistryEntryKey#getUniqueId()
	 */
	@Override
	public String getUniqueId() {
		return name();
	}
}
