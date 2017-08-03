package nl.icscards.springintegration.activator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import nl.icscards.xmldsigning.args.KeyFileFormat;
import nl.icscards.xmldsigning.constants.AppConstants;
import nl.icscards.xmldsigning.hash.HashUtil;
import nl.icscards.xmldsigning.key.KeyUtil;
import nl.icscards.xmldsigning.signer.SignerUtil;
import nl.icscards.xmldsigning.utility.Utils;

@Component
public class InputFileService {
	
	public static final Logger LOG = LoggerFactory.getLogger(InputFileService.class);
	
	private String inputFilePath;
	
	private String outputFilePath;
	
	@Value("${spring.icscards.destination.directory}")
	private String outputPath;
	
	@Value("${spring.icscards.pvtkey.directory}")
	private String pvtKeyfilePath;

	@Value("${spring.icscards.pubkey.directory}")
	private String pubKeyFilePath;
	
	@ServiceActivator
	public File processInputFile(Message<File> message)throws Exception{
		LOG.info("In InputFileService executing business logic of hashing and signing");
		inputFilePath = ((File)message.getPayload()).getAbsolutePath();
		outputFilePath = outputPath + AppConstants.PATH_DELIMITER + message.getHeaders().get(AppConstants.OUTPUT_FILE).toString();
		processInputFile();
		
/*		String oldFilePath = ((File)message.getPayload()).getPath();
		String newFilePath = oldFilePath.replaceAll(".xml", "-success.xml");
		
		File oldFile = new File(oldFilePath);
		File newFile = new File(oldFilePath);
		LOG.info("rename flag = "+oldFile.renameTo(newFile));
		LOG.info("delete flag = "+oldFile.delete());
		*/
		/*return value modifies the payload of the message which effects the functionality of some attributes of outbound-file-adapter*/
		return getOutputFilePath();
		//return message.getPayload();
	}
	
	
	/**
	 * @return the inputFilePath
	 */
	public File getInputFilePath() {
		return new File(inputFilePath);
	}
	
	/**
	 * @return the outputFilePath
	 */
	public File getOutputFilePath() {
		return new File(outputFilePath);
	}

	/**
	 * @return the pvtKeyFilePath
	 */
	public File getPvtKeyFilePath() {
		return new File(pvtKeyfilePath);
	}

	/**
	 * @return the pubKeyFilePath
	 */
	public File getPubKeyFilePath() {
		return new File(pubKeyFilePath);
	}

	private void processInputFile() throws Exception
	{
		validateInput();

		// INPUT File -> BASE64
		String base64 = Base64.encodeBase64String(HashUtil.sha256(getInputFilePath()));
		//LOG.info("Base64 inputFile: " + base64);

		// READ PRIVATE / PUBLIC KEY
		PrivateKey readPrivateKey = null;
		PublicKey readPublicKey = null;
		if (KeyFileFormat.PEM.toString() == getKeyFileFormat())
		{
			readPrivateKey = KeyUtil.readPrivateKeyPEM(getPvtKeyFilePath());
			readPublicKey = KeyUtil.readPublicKeyPEM(getPubKeyFilePath());
		}
		else
		{
			readPrivateKey = KeyUtil.readPrivateKeyDER(getPvtKeyFilePath());
			readPublicKey = KeyUtil.readPublicKeyDER(getPubKeyFilePath());
		}

		// OUTPUT File
		OutputStream outputStream = new FileOutputStream(getOutputFilePath());

		// SIGN
		SignerUtil.sign(readPrivateKey, readPublicKey, base64, outputStream);

		//LOG.info("About to append Base64 encoded input to output file...");

		String base64Input = Base64.encodeBase64String(FileUtils.readFileToByteArray(getInputFilePath()));
		IOUtils.write(base64Input, outputStream, StandardCharsets.UTF_8);
		IOUtils.closeQuietly(outputStream);
		
	}
	

	/**
	 * Method for validating the input
	 * @return
	 */
	private void validateInput()
	{
		// Validate Private Key File
		File privateKeyFile = getPvtKeyFilePath();
		Utils.checkFile(privateKeyFile, AppConstants.PVT_KEY, true);

		// Validate Public Key
		File publicKeyFile = getPubKeyFilePath();
		Utils.checkFile(publicKeyFile, AppConstants.PUB_KEY, true);

		// Validate input file
		File inputFile = getInputFilePath();
		Utils.checkFile(inputFile, AppConstants.INPUT, true);

		// Validate writability of output file
		File outputFile = getOutputFilePath();
		Utils.checkIsNotDirectory(outputFile, AppConstants.OUTPUT);		
	}
	
	/**
	 * Method to return the key file format of the private key
	 * @param inputFile
	 * @return
	 */
	private String getKeyFileFormat(){
		String fileName = getPvtKeyFilePath().getName();
		return fileName.substring(fileName.lastIndexOf(".")).toUpperCase();
	}
}
