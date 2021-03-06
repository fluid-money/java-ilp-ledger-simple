package org.interledger.ilp.ledger.impl;

import org.interledger.ilp.core.exceptions.InterledgerException;

/**
 *
 * @author mrmx
 */
public class LedgerAddressParserException extends InterledgerException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of <code>LedgerAddressParserException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public LedgerAddressParserException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>LedgerAddressParserException</code> with
     * the specified detail message and <code>Throwable</code> cause.
     *
     * @param msg the detail message.
     * @param cause the <code>Throwable</code> cause.
     */
    public LedgerAddressParserException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
