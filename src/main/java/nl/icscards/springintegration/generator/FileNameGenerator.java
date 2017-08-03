package nl.icscards.springintegration.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.messaging.Message;

import nl.icscards.xmldsigning.constants.AppConstants;

public class FileNameGenerator extends DefaultFileNameGenerator{
	
	public static final Logger LOG = LoggerFactory.getLogger(FileNameGenerator.class);

	public FileNameGenerator() {
		super();
	}
	
	@Override
    public String generateFileName(Message<?> message) {
		/*LOG.info("In File OutBound Channel Adaptor generating output filename");	*/
		return message.getHeaders().get(AppConstants.OUTPUT_FILE).toString();
    }	

}
