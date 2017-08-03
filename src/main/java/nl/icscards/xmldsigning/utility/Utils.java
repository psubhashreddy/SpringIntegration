/**
 * 
 */
package nl.icscards.xmldsigning.utility;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.icscards.xmldsigning.constants.AppConstants;

/**
 * @author speddyre
 *
 */
public class Utils {
	
	public static final Logger LOG = LoggerFactory.getLogger(Utils.class);
		
	/**
	 * Method to check the file is a directory or not
	 * @param outputFile
	 * @param description
	 */
	public static void checkIsNotDirectory(final File outputFile, final String description)
	{
		if (outputFile.isDirectory())
		{
			LOG.info("ERROR: Specified " + description + " file is not a file (" + outputFile.getAbsolutePath() + ")");
			System.exit(1);
		}
	}

	/**
	 * Method to check the file existence
	 * @param file
	 * @param description
	 * @param checkReadable
	 */
	public static void checkFile(final File file, final String description, final boolean checkReadable)
	{
		if (!file.exists())
		{
			LOG.info("ERROR: " + description + " file not found (" + file.getAbsolutePath() + ")");
			System.exit(1);
		}

		if (checkReadable && !file.canRead())
		{
			LOG.info("ERROR: " + description + " file not readable (" + file.getAbsolutePath() + ")");
			System.exit(1);
		}
	}
	
	/**
	 * Adding a done string to the input file name
	 * @param fileName
	 * @return
	 */
	public static String generateNewName(String fileName){
		StringBuilder sb = new StringBuilder();
		sb.append(fileName.replace(fileName.substring(0,fileName.indexOf(AppConstants.HYPHEN)), AppConstants.OUTPUT));	
		return sb.toString();
	}
}
