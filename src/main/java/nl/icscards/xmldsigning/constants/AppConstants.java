/**
 * 
 */
package nl.icscards.xmldsigning.constants;

/**
 * @author speddyre
 *
 */
public class AppConstants {
	public static final String ALGORITHM = "RSA";
	public static final String HASH_ALGORITHM = "SHA-256";

	public static final String XML_SIGNATURE_MECHANISM_DOM = "DOM";
	
	public static final String ID_RESOURCE_1 = "Resource1";
	public static final String ID_KEYINFO_001 = "KeyInfo001";
	public static final String REFERENCE_URI_KEYINFO_001 = "#KeyInfo001";
	public static final String REFERENCE_URI_RESOURCE1 = "#Resource1";
	public static final String DEFAULT_NAMESPACE = "dsig";
	public static final String SIGNATURE_METHOD_RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
	
			
	public static final String INPUT = "INPUT";
	public static final String OUTPUT = "OUTPUT";
	public static final String PVT_KEY = "PVTKEY";
	public static final String PUB_KEY = "PUBKEY";
	public static final String KEY_FILE_FORMAT = "KEYFILEFORMAT";
	public static final String INPUT_FILE ="input-file";
	public static final String OUTPUT_FILE ="output-file";
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";

	public static final String PATH_DELIMITER = "\\";
	public static final String HYPHEN = "-";
	public static final String STATUS = "PASSED";
}
