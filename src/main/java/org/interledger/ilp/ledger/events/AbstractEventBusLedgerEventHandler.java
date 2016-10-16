package org.interledger.ilp.ledger.events;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;

import java.util.logging.Logger;

/**
 * An extension of {@link AbstractLedgerEventHandler} that implements {@link LedgerEventHandler} and uses Guava's
 * {@link EventBus} to route all ILP ledger events.
 */
public abstract class AbstractEventBusLedgerEventHandler extends AbstractLedgerEventHandler implements LedgerEventHandler<LedgerEvent> {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    private final EventBus eventBus;

    /**
     * No-args Constructor.  Provides a default implementation of all dependencies.
     */
    public AbstractEventBusLedgerEventHandler() {
        this(new EventBus());
    }

    /**
     * Required-args Constructor.
     *
     * @param eventBus An instance of {@link EventBus} that can be custom-configured by the creator of this class.
     */
    public AbstractEventBusLedgerEventHandler(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    /**
     * Handles a {@link LedgerEvent} in a type-safe fashion and accounts for unhandled events.
     *
     * @param ledgerEvent An instance of {@link LedgerEvent}.
     */
    @Override
    protected final void handleInternal(final LedgerEvent ledgerEvent) {
        Preconditions.checkNotNull(ledgerEvent);
        eventBus.post(ledgerEvent);
    }

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerConnectedEvent ledgerConnectedEvent);

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerDisonnectedEvent ledgerDisonnectedEvent);

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerTransferPreparedEvent ledgerTransferPreparedEvent);

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerTransferExecutedEvent ledgerTransferExecutedEvent);

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerDirectTransferEvent ledgerDirectTransferEvent);

    // This override is necessary to wire-up to the EventBus.
    @Override
    @Subscribe
    protected abstract boolean handleEvent(final LedgerTransferRejectedEvent ledgerTransferRejectedEvent);

    @Subscribe
    protected boolean deadEvent(final DeadEvent deadEvent) {
        throw new RuntimeException("Unhandled Event: " + deadEvent);
    }
}
