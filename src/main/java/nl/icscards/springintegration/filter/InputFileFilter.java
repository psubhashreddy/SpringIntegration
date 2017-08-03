package nl.icscards.springintegration.filter;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

import nl.icscards.xmldsigning.constants.AppConstants;

public class InputFileFilter implements MessageSelector {
	
	public static final Logger LOG = LoggerFactory.getLogger(InputFileFilter.class);

	/* (non-Javadoc)
	 * @see org.springframework.integration.core.MessageSelector#accept(org.springframework.messaging.Message)
	 */
	@Override
	public boolean accept(Message<?> message) {
		LOG.info("In InputFileFilter validating the file name for further processing");		
		String fileName = ((File)message.getPayload()).getName();				
		if(validateFileName(fileName)){
			/*LOG.info("rejecting file for processing");*/
			return false;
		}else{
			/*LOG.info("accepting file for processing");*/
			return true;
		}
	}
	

	
	/**
	 * Checking for success string in fileName
	 * @param fileName
	 * @return
	 */
	private boolean validateFileName(String fileName){
		/*LOG.info("validating fileName");*/
		return fileName.contains(AppConstants.STATUS);
	}
}
