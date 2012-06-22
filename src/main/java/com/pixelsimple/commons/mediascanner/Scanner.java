/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner;

import java.util.List;

/**
 * @author Akshay Sharma
 *
 * Jun 18, 2012
 */
public interface Scanner {
	List<String> scan() throws MediaScannerException;
	Scanner recursive(boolean recursive);
	Scanner filterOnFileExtensions(String[] fileNameExtensions);
}
