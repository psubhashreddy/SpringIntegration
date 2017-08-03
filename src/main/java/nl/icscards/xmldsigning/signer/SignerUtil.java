package nl.icscards.xmldsigning.signer;

import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import lombok.Data;
import nl.icscards.xmldsigning.constants.AppConstants;

@Data
public class SignerUtil {
	
	public static void sign(final PrivateKey privateKey, final PublicKey publicKey, final String input, final OutputStream os) throws Exception
	{
		// First, create the DOM XMLSignatureFactory that will be used to
		// generate the XMLSignature
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance(AppConstants.XML_SIGNATURE_MECHANISM_DOM);
		
		// Next, create a Reference to a same-document URI that is an Object
		// element and specify the SHA2 digest algorithm
		Reference ref1 = fac.newReference(AppConstants.REFERENCE_URI_RESOURCE1, fac.newDigestMethod(DigestMethod.SHA256, null));
		Reference ref2 = fac.newReference(AppConstants.REFERENCE_URI_KEYINFO_001, fac.newDigestMethod(DigestMethod.SHA256, null));

		// Next, create the referenced Object
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		Node text = doc.createTextNode(input);
		XMLStructure content = new DOMStructure(text);
		XMLObject obj = fac.newXMLObject(Collections.singletonList(content), AppConstants.ID_RESOURCE_1, null, null);

		//Below newSignatureMethod argument will be the signature method you wanted to use as per the java api
		//Refer the page for using the signature method algorithm not supported by api - https://blogs.oracle.com/mullan/using-stronger-xml-signature-algorithms-in-jdk-7
		//References - https://www.w3.org/TR/xmldsig-core1/#sec-AlgID
		//Previous value was SignatureMethod.RSA_SHA1
		//Create the SignedInfo
		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null),
				fac.newSignatureMethod(AppConstants.SIGNATURE_METHOD_RSA_SHA256, null), Arrays.asList(ref1, ref2));

		// Create a DSA KeyPair
		// KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM);
		// kpg.initialize(2048);
		// KeyPair kp = kpg.generateKeyPair();

		// Create a KeyValue containing the DSA PublicKey that was generated
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		KeyValue kv = kif.newKeyValue(publicKey);

		// Create a KeyInfo and add the KeyValue to it
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv), AppConstants.ID_KEYINFO_001);

		// Create the XMLSignature (but don't sign it yet)
		XMLSignature signature = fac.newXMLSignature(si, ki, Collections.singletonList(obj), null, null);

		// Create a DOMSignContext and specify the DSA PrivateKey for signing
		// and the document location of the XMLSignature
		DOMSignContext dsc = new DOMSignContext(privateKey, doc);
		// Adding the default namespace for the signed xml file
		dsc.setDefaultNamespacePrefix(AppConstants.DEFAULT_NAMESPACE);

		// Lastly, generate the enveloping signature using the PrivateKey
		signature.sign(dsc);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(os));
	}
}
