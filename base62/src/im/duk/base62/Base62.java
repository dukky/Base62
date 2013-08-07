/*******************************************************************************
 * Copyright 2013 Andreas Holley
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package im.duk.base62;

/**
 * A class to convert numbers to Base62. Default charset: 0..9a..zA..Z Alternate
 * character sets can be specified when constructing the object.
 * 
 * @author Andreas Holley
 */
public class Base62 {
	private String characters;

	/**
	 * Constructs a Base62 object with the default charset (0..9a..zA..Z).
	 */
	public Base62() {
		this("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * Constructs a Base62 object with a custom charset.
	 * 
	 * @param characters
	 *            the charset to use. Must be 62 characters.
	 * @throws <code>IllegalArgumentException<code> if the supplied charset is not 62 characters long.
	 */
	public Base62(String characters) {
		if (!(characters.length() == 62)) {
			throw new IllegalArgumentException(
					"Invalid string length, must be 62.");
		}
		this.characters = characters;
	}

	/**
	 * Encodes a decimal value to a Base62 <code>String</code>.
	 * 
	 * @param b10
	 *            the decimal value to encode, must be nonnegative.
	 * @return the number encoded as a Base62 <code>String</code>.
	 */
	public String encodeBase10(long b10) {
		if (b10 < 0) {
			throw new IllegalArgumentException("b10 must be nonnegative");
		}
		String ret = "";
		while (b10 > 0) {
			ret = characters.charAt((int) (b10 % 62)) + ret;
			b10 /= 62;
		}
		return ret;

	}

	/**
	 * Decodes a Base62 <code>String</code> returning a <code>long</code>.
	 * 
	 * @param b62
	 *            the Base62 <code>String</code> to decode.
	 * @return the decoded number as a <code>long</code>.
	 * @throws IllegalArgumentException
	 *             if the given <code>String</code> contains characters not
	 *             specified in the constructor.
	 */
	public long decodeBase62(String b62) {
		for (char character : b62.toCharArray()) {
			if (!characters.contains(String.valueOf(character))) {
				throw new IllegalArgumentException(
						"Invalid character(s) in string: " + character);
			}
		}
		long ret = 0;
		b62 = new StringBuffer(b62).reverse().toString();
		long count = 1;
		for (char character : b62.toCharArray()) {
			ret += characters.indexOf(character) * count;
			count *= 62;
		}
		return ret;
	}

	// Examples
	public static void main(String[] args) throws InterruptedException {

		// Create a Base62 object using the default charset.
		Base62 base62 = new Base62();

		// Create a Base62 object with an alternate charset.
		Base62 base62alt = new Base62(
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

		// Convert 1673 to Base62 (qZ).
		System.out.println("1673 encoded to Base62: "
				+ base62.encodeBase10(1673));

		// Convert 1673 to Base62 with the alternate character set (A9).
		System.out.println("1673 encoded with alternate charset: "
				+ base62alt.encodeBase10(1673));

		// Convert nHkl3S4B to decimal (83,458,179,955,437).
		System.out.println("nHkl3S4B decoded from Base62: "
				+ base62.decodeBase62("nHkl3S4B"));

		// Encoding and decoding a number returns the original result.
		System.out.println("32442342 encoded to Base62 and back again: "
				+ base62.decodeBase62(base62.encodeBase10(32442342)));

		// Using invalid characters throws a runtime exception.
		// Output was out of order with ant.
		
		Thread.sleep(100);
		try {
			// Doesn't work
			System.out.println(base62.decodeBase62("_j+j%"));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
