package org.interledger.ilp.ledger.impl;

import com.google.common.base.Preconditions;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.LedgerInfo;
import org.interledger.ilp.core.LedgerTransfer;
import org.interledger.ilp.core.LedgerTransferRejectedReason;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.exceptions.InsufficientAmountException;
import org.interledger.ilp.ledger.Currencies;
import org.interledger.ilp.ledger.LedgerAccountManagerFactory;
import org.interledger.ilp.ledger.LedgerInfoFactory;
import org.interledger.ilp.ledger.MoneyUtils;
import org.interledger.ilp.ledger.account.LedgerAccount;
import org.interledger.ilp.ledger.account.LedgerAccountManager;
import org.interledger.ilp.ledger.account.LedgerAccountManagerAware;

import javax.money.MonetaryAmount;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple in-memory ledger implementation
 *
 * @author mrmx
 */
public class SimpleLedger implements Ledger, LedgerAccountManagerAware {

    private List<LedgerEventHandler> ledgerEventHandlers = new LinkedList<LedgerEventHandler>();

    private LedgerInfo info;
    private String name;
//    private LedgerAccountManager accountManager;

    public SimpleLedger(Currencies currency, String name) {
        this(LedgerInfoFactory.from(currency), name);
    }

    public SimpleLedger(String currencyCode, String name) {
        this(LedgerInfoFactory.from(currencyCode), name);
    }

    public SimpleLedger(LedgerInfo info, String name) {
        this.info = info;
        this.name = name;
    }


    public LedgerInfo getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public void send(LedgerTransfer transfer) {
    	LedgerAccountManager accountManager = LedgerAccountManagerFactory.getAccountManagerSingleton();
        LedgerAccount from = accountManager.getAccountByName(transfer.getFromAccount());
        LedgerAccount to = accountManager.getAccountByName(transfer.getToAccount());
        if (to.equals(from)) {
            throw new RuntimeException("accounts are the same");
        }
        MonetaryAmount amount = MoneyUtils.toMonetaryAmount(transfer.getAmount(), info.getCurrencyCode());
        if (from.getBalance().isGreaterThanOrEqualTo(amount)) {
            from.debit(amount);
            to.credit(amount);
        } else {
            throw new InsufficientAmountException(amount.toString());
        }

        // Notify all Event Handlers...
        final LedgerTransferExecutedEvent ledgerTransferExecutedEvent = new LedgerTransferExecutedEvent(
                this, transfer.getHeader(), transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount()
        );
        this.notifyEventHandlers(ledgerTransferExecutedEvent);
    }

    public void rejectTransfer(LedgerTransfer transfer, LedgerTransferRejectedReason reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fulfillCondition(Fulfillment fulfillment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // TODO: consider returning a boolean to align with java.util.Collection - indicates if the handler already existed?
    @Override
    public void registerEventHandler(LedgerEventHandler<?> handler) {
        Preconditions.checkNotNull(handler);
        this.ledgerEventHandlers.add(handler);
    }

    // TODO: Consider modifying LedgerEventHandler#onLedgerEvent to return a boolean to indicate if an event was handled?
    private void notifyEventHandlers(final LedgerEvent ledgerEvent) {
        for (final LedgerEventHandler handler : this.ledgerEventHandlers) {
            //if (handler.isHandled(ledgerEvent)) {
            handler.onLedgerEvent(ledgerEvent);
            //}
        }
    }

    // TODO: The Ledger interface should have the ability to unregister an event handler.  Or, alternatively, an abstract
    // class should be created that requires the event-handlers at construction time?

    @Override
    public LedgerAccountManager getLedgerAccountManager() {
        // FIXME: Remove getLedgerAccountManager here and in parent interface
        return LedgerAccountManagerFactory.getAccountManagerSingleton();
    }
}
