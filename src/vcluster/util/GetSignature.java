package vcluster.util;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.axis.encoding.Base64;

public class GetSignature {
	
	/**
	 * Computes RFC 2104-compliant HMAC signature.
	 * 
	 * @param data
	 *            The data to be signed.
	 * @param key
	 *            The signing key.
	 * @return The base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */

	public static String calculateRFC2104HMAC(String data, String key, String sigMethod) 
	throws NoSuchAlgorithmException, InvalidKeyException {
		String result;
		// get an hmac_sha1 key from the raw key bytes
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), sigMethod);

		// get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance(sigMethod);
		mac.init(signingKey);
	
		
		// compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(data.getBytes());
		result = Base64.encode(rawHmac);

		return result;
	}
		
}