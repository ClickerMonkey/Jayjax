/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.jayjax.io;

/**
 * A set of characters. This is used to quickly determine if a character is in a
 * given set where sets are defined by the user and is used by the
 * {@link CharacterReader}
 * 
 * @author Philip Diffenderfer
 * 
 */
public class CharacterSet
{

	private static final int RANGE_SIZE = 2;

	private final char min;
	private final char max;
	private final boolean[] map;

	/**
	 * Instantiates a new CharacterSet.
	 * 
	 * @param charArray
	 * 	The set of all characters.
	 */
	public CharacterSet( char... charArray )
	{
		char charMin = Character.MAX_VALUE;
		char charMax = Character.MIN_VALUE;

		for (int i = 0; i < charArray.length; i++)
		{
			char c = charArray[i];

			if (c < charMin)
			{
				charMin = c;
			}
			if (c > charMax)
			{
				charMax = c;
			}
		}

		map = new boolean[charMax - charMin + 1];

		for (int i = 0; i < charArray.length; i++)
		{
			map[charArray[i] - charMin] = true;
		}

		min = charMin;
		max = charMax;
	}

	/**
	 * Instantiates a new CharacterSet.
	 * 
	 * @param charRanges
	 * 	Ranges of characters in this set. If a 2-element array is given all
	 * 	characters between those two are added, if it's a 1-element array
	 * 	or > 2 element array they are added to the set as is.
	 */
	public CharacterSet( char[][] charRanges )
	{
		char charMin = Character.MAX_VALUE;
		char charMax = Character.MIN_VALUE;

		for (int i = 0; i < charRanges.length; i++)
		{
			int gridLength = charRanges[i].length;

			for (int k = 0; k < gridLength; k++)
			{
				char c = charRanges[i][k];

				if (c < charMin)
				{
					charMin = c;
				}
				if (c > charMax)
				{
					charMax = c;
				}
			}
		}

		map = new boolean[charMax - charMin + 1];

		for (int i = 0; i < charRanges.length; i++)
		{
			int gridLength = charRanges[i].length;

			if (gridLength == RANGE_SIZE)
			{
				for (int k = charRanges[i][0]; k <= charRanges[i][1]; k++)
				{
					map[k - charMin] = true;
				}
			}
			else
			{
				for (int k = 0; k < gridLength; k++)
				{
					map[charRanges[i][k] - charMin] = true;
				}
			}
		}

		min = charMin;
		max = charMax;
	}

	/**
	 * Returns whether this set has the given character.
	 * 
	 * @param c
	 * 		The character to test for existence in this set.
	 * @return
	 * 		True if the character is in this set, otherwise false.
	 */
	public boolean has( char c )
	{
		return (c >= min && c <= max && map[c - min]);
	}

	/**
	 * Returns whether this set has the given character.
	 * 
	 * @param c
	 * 		The character to test for existence in this set.
	 * @return
	 * 		True if the character is in this set, otherwise false.
	 */
	public boolean has( int c )
	{
		return (c >= min && c <= max && map[c - min]);
	}

}