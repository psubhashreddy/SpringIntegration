/**
 * 
 */
package nl.icscards.springintegration.transformer;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author speddyre
 *
 */
@Component
public class CustomTransformer {	
	public static final Logger LOG = LoggerFactory.getLogger(CustomTransformer.class);
	
	public File transform(File file) throws Exception{
		/*LOG.info("In CustomTransformer doing nothing");*/	
		return file;
	}
	

	
}
