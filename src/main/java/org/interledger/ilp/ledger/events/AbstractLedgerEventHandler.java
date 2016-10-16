package org.interledger.ilp.ledger.events;

import com.google.common.base.Preconditions;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;

/**
 * An abstract implementation of {@link LedgerEventHandler} that handles all variants of {@link LedgerEvent} in a
 * type-safe manner, and provides a mechanism to detect unhandled event-types at runtime.
 */
public abstract class AbstractLedgerEventHandler implements LedgerEventHandler<LedgerEvent> {

    @Override
    public final void onLedgerEvent(final LedgerEvent ledgerEvent) {
        this.handleInternal(ledgerEvent);
    }

    /**
     * Handles a {@link LedgerEvent} in a type-safe fashion and accounts for unhandled events.
     *
     * @param ledgerEvent An instance of {@link LedgerEvent}.
     */
    protected void handleInternal(final LedgerEvent ledgerEvent) {
        Preconditions.checkNotNull(ledgerEvent);

        // If this were a more recent version of Java, this could be a Switch statement...
        if (ledgerEvent.getClass().equals(LedgerConnectedEvent.class)) {
            this.handleEvent((LedgerConnectedEvent) ledgerEvent);
        } else if (ledgerEvent.getClass().equals(LedgerDisonnectedEvent.class)) {
            this.handleEvent((LedgerDisonnectedEvent) ledgerEvent);
        } else if (ledgerEvent.getClass().equals(LedgerTransferPreparedEvent.class)) {
            this.handleEvent((LedgerTransferPreparedEvent) ledgerEvent);
        } else if (ledgerEvent.getClass().equals(LedgerTransferExecutedEvent.class)) {
            this.handleEvent((LedgerTransferExecutedEvent) ledgerEvent);
        } else if (ledgerEvent.getClass().equals(LedgerDirectTransferEvent.class)) {
            this.handleEvent((LedgerDirectTransferEvent) ledgerEvent);
        } else if (ledgerEvent.getClass().equals(LedgerTransferRejectedEvent.class)) {
            this.handleEvent((LedgerTransferRejectedEvent) ledgerEvent);
        } else {
            this.handleUnhandledEvent(ledgerEvent);
        }
    }

    /**
     * Handle an instance of {@link LedgerConnectedEvent}.
     *
     * @param ledgerConnectedEvent An instance of {@link LedgerConnectedEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerConnectedEvent ledgerConnectedEvent);

    /**
     * Handle an instance of {@link LedgerDisonnectedEvent}.
     *
     * @param ledgerDisonnectedEvent An instance of {@link LedgerDisonnectedEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerDisonnectedEvent ledgerDisonnectedEvent);

    /**
     * Handle an instance of {@link LedgerTransferPreparedEvent}.
     *
     * @param ledgerTransferPreparedEvent An instance of {@link LedgerTransferPreparedEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerTransferPreparedEvent ledgerTransferPreparedEvent);

    /**
     * Handle an instance of {@link LedgerTransferExecutedEvent}.
     *
     * @param ledgerTransferExecutedEvent An instance of {@link LedgerTransferExecutedEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerTransferExecutedEvent ledgerTransferExecutedEvent);

    /**
     * Handle an instance of {@link LedgerDirectTransferEvent}.
     *
     * @param ledgerDirectTransferEvent An instance of {@link LedgerDirectTransferEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerDirectTransferEvent ledgerDirectTransferEvent);

    /**
     * Handle an instance of {@link LedgerTransferRejectedEvent}.
     *
     * @param ledgerTransferRejectedEvent An instance of {@link LedgerTransferRejectedEvent}.
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected abstract boolean handleEvent(final LedgerTransferRejectedEvent ledgerTransferRejectedEvent);

    /**
     * Throws a {@link RuntimeException} if an un-handled event is encountered.  Protected so that sub-classes can
     * override this behavior, if desired.
     *
     * @param ledgerEvent An instance of {@link LedgerEvent} that is unhandled by this
     * @return {@code true} if the event was handled; {@code false} otherwise.
     */
    protected boolean handleUnhandledEvent(final LedgerEvent ledgerEvent) {
        throw new RuntimeException("Unhandled Event: " + ledgerEvent);
    }
}
