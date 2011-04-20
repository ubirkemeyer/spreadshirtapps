package com.socialnetworkshirts.twittershirts.dataaccess.twitter.exceptions;

/**
 * @author mbs
 * @version $version$
 */
public class RetrievalException extends Exception {
    public RetrievalException() {
    }

    public RetrievalException(String message) {
        super(message);
    }

    public RetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
