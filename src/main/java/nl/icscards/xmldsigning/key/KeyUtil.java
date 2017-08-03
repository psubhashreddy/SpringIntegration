package nl.icscards.xmldsigning.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import nl.icscards.xmldsigning.constants.AppConstants;

public class KeyUtil {
	

	public static PrivateKey readPrivateKeyDER(final File privateKeyDERFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		byte[] keyBytes = Files.readAllBytes(privateKeyDERFile.toPath());

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance(AppConstants.ALGORITHM);
		return kf.generatePrivate(spec);
	}

	public static PrivateKey readPrivateKeyPEM(final File privateKeyPEMFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		byte[] content = readPEMKey(privateKeyPEMFile);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
		KeyFactory kf = KeyFactory.getInstance(AppConstants.ALGORITHM);
		return kf.generatePrivate(spec);
	}

	public static PublicKey readPublicKeyDER(final File publicKeyDERFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		byte[] keyBytes = Files.readAllBytes(publicKeyDERFile.toPath());

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance(AppConstants.ALGORITHM);
		return kf.generatePublic(spec);
	}

	public static PublicKey readPublicKeyPEM(final File publicKeyPEMFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		byte[] binKey = readPEMKey(publicKeyPEMFile);

		X509EncodedKeySpec spec = new X509EncodedKeySpec(binKey);
		KeyFactory kf = KeyFactory.getInstance(AppConstants.ALGORITHM);
		return kf.generatePublic(spec);
	}

	private static byte[] readPEMKey(final File privateKeyPEMFile) throws FileNotFoundException, IOException
	{
		Security.addProvider(new BouncyCastleProvider());

		PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(privateKeyPEMFile)));
		PemObject readPemObject = null;
		try
		{
			readPemObject = pemReader.readPemObject();
		}
		finally
		{
			pemReader.close();
		}
		byte[] content = readPemObject.getContent();
		return content;
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static String readPEMKey_OLD(final File publicKeyDERFile) throws IOException
	{
		StringBuilder base64KeyBuilder = new StringBuilder();

		boolean firstLineMarkerSeen = false;
		boolean lastLineMarkerSeen = false;
		List<String> readAllLines = Files.readAllLines(publicKeyDERFile.toPath(), StandardCharsets.UTF_8);
		for (String line : readAllLines)
		{
			if (isLastLine(line))
			{
				lastLineMarkerSeen = true;
			}

			if (firstLineMarkerSeen && !lastLineMarkerSeen)
			{
				base64KeyBuilder.append(StringUtils.trim(line));
			}

			if (isFirstLine(line))
			{
				firstLineMarkerSeen = true;
			}
		}

		if (!firstLineMarkerSeen || !lastLineMarkerSeen)
		{
			throw new RuntimeException(
					"Wrong PEM file format. Missing markers: No '--BEGIN PUBLIC/RSA PRIVATE KEY--' or '--END PUBLIC/RSA PRIVATE KEY--' seen.");
		}

		System.out.println("About to read key: " + base64KeyBuilder.toString());

		return base64KeyBuilder.toString();
	}

	private static boolean isFirstLine(final String line)
	{
		return StringUtils.contains(line, "--BEGIN PUBLIC KEY--") || StringUtils.contains(line, "--BEGIN RSA PRIVATE KEY--");
	}

	private static boolean isLastLine(final String line)
	{
		return StringUtils.contains(line, "--END PUBLIC KEY--") || StringUtils.contains(line, "--END RSA PRIVATE KEY--");
	}
}
