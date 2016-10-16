package org.interledger.ilp.ledger.events;

import com.google.common.eventbus.EventBus;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;

/**
 * An extension  of {@link AbstractEventBusLedgerEventHandler} that implements {@link LedgerEventHandler} by using
 * Guava's {@link EventBus} to handleInternal all subscription and listener logic related to events from an ILP leger.
 * This implementation is intended to be used by an in-memory ledger.
 */
public class SimpleEventBusLedgerEventHandler extends AbstractEventBusLedgerEventHandler implements LedgerEventHandler<LedgerEvent> {

    /**
     * Required-args Constructor.
     *
     * @param ledger   The ledger that this handler responds to events for.
     * @param eventBus An instance of {@link EventBus} that can be custom-configured by the creator of this class.
     */
    public SimpleEventBusLedgerEventHandler(final Ledger ledger, final EventBus eventBus) {
        super(ledger, eventBus);
    }

    @Override
    protected boolean handleEvent(final LedgerConnectedEvent ledgerConnectedEvent) {
        logger.info("Received Event: " + ledgerConnectedEvent);
        return true;
    }

    @Override
    protected boolean handleEvent(final LedgerDisonnectedEvent ledgerDisonnectedEvent) {
        logger.info("Received Event: " + ledgerDisonnectedEvent);
        return true;
    }

    @Override
    protected boolean handleEvent(final LedgerTransferPreparedEvent ledgerTransferPreparedEvent) {
        logger.info("Received Event: " + ledgerTransferPreparedEvent);
        return true;
    }

    @Override
    protected boolean handleEvent(final LedgerTransferExecutedEvent ledgerTransferExecutedEvent) {
        logger.info("Received Event: " + ledgerTransferExecutedEvent);
        return true;
    }

    @Override
    protected boolean handleEvent(final LedgerDirectTransferEvent ledgerDirectTransferEvent) {
        logger.info("Received Event: " + ledgerDirectTransferEvent);
        return true;
    }

    @Override
    protected boolean handleEvent(final LedgerTransferRejectedEvent ledgerTransferRejectedEvent) {
        logger.info("Received Event: " + ledgerTransferRejectedEvent);
        return true;
    }
}
