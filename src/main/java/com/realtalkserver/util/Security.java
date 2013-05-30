package com.realtalkserver.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Provides security measures.
 * @author Jory Rice
 */
public class Security {

	/**
	 * Hash the given string using SHA, for secure storage in the database.
	 * @param st a string to hash
	 * @return the hashed version of the string
	 */
	public static String hash(String st) {
		return DigestUtils.sha256Hex(st);
		//return st;
	}

}
