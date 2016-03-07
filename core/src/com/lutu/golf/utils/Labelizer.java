
package com.lutu.golf.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class to help parse {@link String} in search of URLs.
 */
public class Labelizer {
	private static final Pattern URL_PATTERN = Pattern
			.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

	private String mText;
	private int mLastMatch;
	private boolean mIsText;
	private boolean mEndReached;
	private final Matcher mMatcher;

	/**
	 * Constructs the {@link Labelizer} instance.
	 * 
	 * @param string
	 *            initial {@link String}
	 */
	public Labelizer(String string) {
		mText = string;
		mMatcher = URL_PATTERN.matcher(string);
		mIsText = false;
		mEndReached = false;
	}

	/**
	 * Resets the {@link Labelizer} and sets a new {@link String}.
	 * 
	 * @param string
	 *            the new {@link String}
	 */
	public void reset(String string) {
		mIsText = false;
		mEndReached = false;
		mText = string;
		mMatcher.reset(string);
	}

	/**
	 * Goes to the next found token (label).
	 * 
	 * @return true if a token was found, false otherwise
	 */
	public boolean parse() {
		if (mEndReached) {
			return false;
		}
		if (mIsText) {
			mIsText = false;
			mLastMatch = mMatcher.end();
			return true;
		}

		final boolean found = mMatcher.find();

		if (found && mLastMatch != mMatcher.start()) {
			mIsText = true;
		}

		if (!found) {
			mEndReached = true;
		}

		return true;
	}

	/**
	 * Determines if the current token is a URL or not.
	 * 
	 * @return true if the current token is a URL, false otherwise
	 */
	public boolean isUrl() {
		if (mEndReached) {
			return false;
		} else {
			return !mIsText;
		}
	}

	/**
	 * Returns the current token.
	 * 
	 * @return current token
	 */
	public String getText() {
		if (mIsText) {
			return mText.substring(mLastMatch, mMatcher.start());
		} else if (mEndReached) {
			return mText.substring(mLastMatch);
		} else {
			return mText.substring(mMatcher.start(), mMatcher.end());
		}
	}
}
