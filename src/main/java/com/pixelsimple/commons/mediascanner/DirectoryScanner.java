/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.mediascanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.Resource;
import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.command.CommandRunner;
import com.pixelsimple.commons.command.CommandRunnerFactory;
import com.pixelsimple.commons.mediascanner.config.MediaScannerConfig;
import com.pixelsimple.commons.util.OSUtils;
import com.pixelsimple.commons.util.StringUtils;

/**
 * @author Akshay Sharma
 *
 * Jun 18, 2012
 */
public class DirectoryScanner implements Scanner {
	private Resource baseDir;
	private boolean recursive = true;
	private String[] fileNameExtensions; 
	private ApiConfig apiConfig;
	private MediaScannerConfig mediaScannerConfig;
	
	public DirectoryScanner(Resource baseDir, ApiConfig apiConfig, MediaScannerConfig mediaScannerConfig) {
		this.baseDir = baseDir;
		this.apiConfig = apiConfig;
		this.mediaScannerConfig = mediaScannerConfig;
	}
	

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.mediascanner.Scanner#scan()
	 */
	@Override
	public List<String> scan() throws MediaScannerException {
		// Call the listing scripts/batch file based on the configuration. Using the system level api calls for performance
		// Pass the arguments needed to the script file as needed.
		// Script will write a a file to the temp folder with a given UUID as name (passed by the app here)
		// Once the command returns, read the file to generate the list of media file and then cleanup the temp (UUID) file.
		UUID uuid = UUID.randomUUID();
		String outputFileName = this.apiConfig.getEnvironment().getTempDirectory() + uuid.toString() + ".txt";
		String baseDirPath = this.getFormattedBaseDir();
		String filterString = this.createFilterString();
		
		CommandRequest req = new CommandRequest();
		req.addCommand(mediaScannerConfig.getMediaScannerPath(), 0).addArgument(baseDirPath)
			.addArgument(outputFileName).addArgument("" + this.recursive)
			.addArgument(filterString);
		
		CommandRunner runner = CommandRunnerFactory.newBlockingCommandRunner();
		CommandResponse res = new CommandResponse();
		// Call in a blocking mode.
		runner.runCommand(req, res);
		File outputFile = new File(outputFileName);
		
		if (res.hasCommandFailed()) {
			if (outputFile.exists())
				outputFile.delete();

			throw new MediaScannerException("The scanning of directory/resource - " + baseDir + " failed.", 
				res.getFailureCause());
		}
		
		if (!outputFile.exists() || !outputFile.isFile()) {
			throw new MediaScannerException("The scanning of directory/resource - " + baseDir + " failed. The output file " 
				+ outputFileName + " seems to be missing/invalid.");
		}
		List<String> scanResults = loadFile(outputFile);
		outputFile.delete();
		return scanResults;
	}

	/**
	 * @return
	 */
	private String getFormattedBaseDir() {
		if (OSUtils.isWindows() || !this.recursive) {
			return OSUtils.appendFolderSeparator(baseDir.getResourceAsString());
		}
		
		// For mac/linux remove the trailing slash if it is recursive (tail command messes up paths :()
		String dir = baseDir.getResourceAsString();
		return dir.endsWith("/") ? dir.substring(0, dir.length() - 1) : dir;
	}


	/**
	 * @param outputFileName
	 * @return
	 */
	private List<String> loadFile(File outputFile) throws MediaScannerException {
		List<String> scannedOutput = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(outputFile));
			scannedOutput = new ArrayList<String>();
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (!StringUtils.isNullOrEmpty(line))
					scannedOutput.add(line);
			}
		} catch (Exception e) {
			throw new MediaScannerException("Error occurred reading the scanner file - " + outputFile, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// What a piece of crap java is. Ignore this.
				}
			}
		}
		return scannedOutput;
	}


	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.mediascanner.Scanner#recursive(boolean)
	 */
	@Override
	public Scanner recursive(boolean recursive) {
		this.recursive = recursive;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.mediascanner.Scanner#filterOnFileExtensions(java.lang.String)
	 */
	@Override
	public Scanner filterOnFileExtensions(String[] fileNameExtensions) {
		this.fileNameExtensions = fileNameExtensions;
		return this;
	}

	private String createFilterString() {
		// Match any if null
		if (this.fileNameExtensions == null)
			return "\\.";
		String filterSeparator;
		if (OSUtils.isWindows()) {
			filterSeparator = " ";
		} else {
			filterSeparator = "|";
		}
		
		// For mac and linux send it in the following format:"\.mov$|\.mp4$|\.m4v$" or "\.mov$ \.mp4$ \.m4v$" for Windows 
		StringBuilder builder = new StringBuilder();
		
		for (String extn : this.fileNameExtensions) {
			builder.append("\\.").append(extn).append("$").append(filterSeparator);			
		}
		String output;
		if (builder.length() > 0)  {
			// Remove the last '|' for the correct command.
			output = builder.substring(0, builder.length() - 1);
		} else {
			// Match any
			output = "\\.";
		}
//		return "\"" + output +  "\"";
		// Don't quote it as it seems to run into issues.
		return output;
	}
}
