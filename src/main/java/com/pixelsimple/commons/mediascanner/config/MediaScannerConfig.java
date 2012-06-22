/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner.config;

import java.io.File;

import com.pixelsimple.commons.util.Assert;

/**
 * @author Akshay Sharma
 *
 * Jun 19, 2012
 */
public class MediaScannerConfig {
	private String mediaScannerPath;
	

	/**
	 * @return the mediaScannerPath
	 */
	public String getMediaScannerPath() {
		return this.mediaScannerPath;
	}

	/**
	 * @param mediaScannerPath the mediaScannerPath to set
	 */
	public void setMediaScannerPath(String mediaScannerPath) {
		this.validateFile(mediaScannerPath);
		this.mediaScannerPath = mediaScannerPath;
	}


	private void validateFile(String fullExecutablePath) {
		File file = new File(fullExecutablePath);
		Assert.isTrue(file.isFile(), "Looks like the the file provided in path is not valid::" + fullExecutablePath);

		file = null; // gc it hopefully
	}

}
