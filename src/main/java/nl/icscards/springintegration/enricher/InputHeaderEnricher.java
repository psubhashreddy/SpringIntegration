/**
 * 
 */
package nl.icscards.springintegration.enricher;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import nl.icscards.xmldsigning.utility.Utils;

/**
 * @author speddyre
 *
 */
public class InputHeaderEnricher {
	
	public static final Logger LOG = LoggerFactory.getLogger(InputHeaderEnricher.class);
		
	/**
	 * Method used to add the input file name to the header of the message.
	 * @param message
	 * @throws IOException
	 */
	public String updateHeaderWithInputFileName(Message<?> message){
		/*LOG.info("*********Adding input file name to the header*********");*/
		return ((File)message.getPayload()).getName();
	}
	
	/**
	 * Method used to add the output file name to the header of the message.
	 * @param message
	 * @throws IOException
	 */
	public String updateHeaderWithOutputFileName(Message<?> message){
		/*LOG.info("*********Adding output file name into Header*********");*/		
		return Utils.generateNewName(((File)message.getPayload()).getName());
	}
	
}
