/**
 * 
 */
package nl.icscards.springintegration.filter;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import nl.icscards.xmldsigning.constants.AppConstants;

/**
 * @author speddyre
 *
 */
@Component
public class TransactionFilter {
	public static final Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

	/**
	 * This method is invoked when the transaction is successful and committed the process. 
	 * @param file
	 */
	public void transactionAfterCommit(File file){
		LOG.info("In TransactionFilter executing afterCommit method");
		//LOG.info("Source file="+file.getPath());
		try{
			String fileName = file.getName();
			String filePath = file.getParent();
			
			File successDir = new File(filePath+AppConstants.PATH_DELIMITER+AppConstants.SUCCESS+AppConstants.PATH_DELIMITER+fileName);
 
			FileUtils.copyFile(file, successDir, true);
			LOG.info("Processing is done and copied "+fileName+" to the path "+successDir);

			FileUtils.forceDeleteOnExit(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is invoked when the transaction is failed and rollback is triggered.
	 * @param file
	 */
	public void transactionRollback(File file){
		LOG.info("In TransactionFilter executing afterCommit method");
		try{
			String fileName = file.getName();
			String filePath = file.getParent();
			
			File failedDir = new File(filePath+AppConstants.PATH_DELIMITER+AppConstants.FAILED+AppConstants.PATH_DELIMITER+fileName);
			
			FileUtils.copyFile(file, failedDir, true);
			LOG.info("Processing is failed and copied "+fileName+" to the path "+failedDir);
			FileUtils.forceDeleteOnExit(file);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
