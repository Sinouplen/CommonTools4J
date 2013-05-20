/**
 * 
 */
package org.sinouplen.tools.string;

/**
 * @author Sinouplen
 * 
 */
public final class RegexpUtils {

	private static final String PATTERNE_NUMBERS = "^[0-9]+$";

	private RegexpUtils() {

	}

	public static boolean isNumbers(String stringToCheck) {
		return stringToCheck.matches(PATTERNE_NUMBERS);
	}

}
