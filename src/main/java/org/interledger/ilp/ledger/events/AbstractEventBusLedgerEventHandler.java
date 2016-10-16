package org.interledger.ilp.ledger.events;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An extension of {@link AbstractLedgerEventHandler} that implements {@link LedgerEventHandler} and uses Guava's
 * {@link EventBus} to route all ILP ledger events.
 */
public abstract class AbstractEventBusLedgerEventHandler extends AbstractLedgerEventHandler implements LedgerEventHandler<LedgerEvent> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final Ledger ledger;
    private final EventBus eventBus;

    /**
     * No-args Constructor.  Provides a default implementation of all dependencies.
     *
     * @param ledger The ledger that this handler responds to events for.
     */
    public AbstractEventBusLedgerEventHandler(final Ledger ledger) {
        this(ledger, new EventBus());
    }

    /**
     * Required-args Constructor.
     *
     * @param ledger   The ledger that this handler responds to events for.
     * @param eventBus An instance of {@link EventBus} that can be custom-configured by the creator of this class.
     */
    public AbstractEventBusLedgerEventHandler(final Ledger ledger, final EventBus eventBus) {
        this.ledger = Preconditions.checkNotNull(ledger);
        this.eventBus = Preconditions.checkNotNull(eventBus);
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

    @Subscribe
    private final boolean handleEventInternal(final LedgerConnectedEvent ledgerConnectedEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerConnectedEvent);
        final boolean result = this.handleEvent(ledgerConnectedEvent);
        logger.debug("Ledger '{}': Handled LedgerEvent '{}'", this.ledger, ledgerConnectedEvent);
        return result;
    }

    @Subscribe
    private final boolean handleEventInternal(final LedgerDisonnectedEvent ledgerDisonnectedEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerDisonnectedEvent);
        final boolean result = this.handleEvent(ledgerDisonnectedEvent);
        logger.debug("Ledger[{}]: Handled LedgerEvent '{}'", this.ledger, ledgerDisonnectedEvent);
        return result;
    }

    @Subscribe
    private final boolean handleEventInternal(final LedgerTransferPreparedEvent ledgerTransferPreparedEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerTransferPreparedEvent);
        final boolean result = this.handleEvent(ledgerTransferPreparedEvent);
        logger.debug("Ledger[{}]: Handled LedgerEvent '{}'", this.ledger, ledgerTransferPreparedEvent);
        return result;
    }

    @Subscribe
    private final boolean handleEventInternal(final LedgerTransferExecutedEvent ledgerTransferExecutedEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerTransferExecutedEvent);
        final boolean result = this.handleEvent(ledgerTransferExecutedEvent);
        logger.debug("Ledger[{}]: Handled LedgerEvent '{}'", this.ledger, ledgerTransferExecutedEvent);
        return result;
    }

    @Subscribe
    private final boolean handleEventInternal(final LedgerDirectTransferEvent ledgerDirectTransferEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerDirectTransferEvent);
        final boolean result = this.handleEvent(ledgerDirectTransferEvent);
        logger.debug("Ledger[{}]: Handled LedgerEvent '{}'", this.ledger, ledgerDirectTransferEvent);
        return result;
    }

    @Subscribe
    private final boolean handleEventInternal(final LedgerTransferRejectedEvent ledgerTransferRejectedEvent) {
        logger.debug("Ledger[{}]: About to handle LedgerEvent '{}'", this.ledger, ledgerTransferRejectedEvent);
        final boolean result = this.handleEvent(ledgerTransferRejectedEvent);
        logger.debug("Ledger[{}]: Handled LedgerEvent '{}'", this.ledger, ledgerTransferRejectedEvent);
        return result;
    }

    @Subscribe
    protected boolean deadEvent(final DeadEvent deadEvent) {
        throw new RuntimeException("Unhandled Event: " + deadEvent);
    }
}
