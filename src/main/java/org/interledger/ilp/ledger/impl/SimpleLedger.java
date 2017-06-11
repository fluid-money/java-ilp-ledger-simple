package org.interledger.ilp.ledger.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.LedgerInfo;
import org.interledger.ilp.core.LedgerTransfer;
import org.interledger.ilp.core.LedgerTransferRejectedReason;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerEventHandler;
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

    private final LedgerAddressParser ledgerAddressParser;

    public SimpleLedger(Currencies currency, String name) {
        this(LedgerInfoFactory.from(currency), name);
    }

    public SimpleLedger(String currencyCode, String name) {
        this(LedgerInfoFactory.from(currencyCode), name);
    }

    public SimpleLedger(LedgerInfo info, String name) {
        this.info = info;
        this.name = name;
        this.ledgerAddressParser = new SimpleLedgerAddressParser();
    }


    public LedgerInfo getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    /**
     * Initiate an ILP transfer.
     */
    public void send(LedgerTransfer transfer) {
        Preconditions.checkNotNull(transfer);

        final LedgerAccountManager accountManager = LedgerAccountManagerFactory.getAccountManagerSingleton();
        final LedgerAccount from = accountManager.getAccountByName(transfer.getFromAccount());
        final LedgerAccount to = accountManager.getAccountByName(transfer.getToAccount());
        if (to.equals(from)) {
            throw new RuntimeException("Accounts are the same!");
        }

        final MonetaryAmount amount = MoneyUtils.toMonetaryAmount(transfer.getAmount(), info.getCurrencyCode());
        if (from.getBalance().isGreaterThanOrEqualTo(amount)) {
            from.debit(amount);
            to.credit(amount);
        } else {
            throw new InsufficientAmountException(amount.toString());
        }

        // For Local Transfer, the only event is a LedgerDirectTransferEvent.
        final LedgerDirectTransferEvent ledgerTransferExecutedEvent = new LedgerDirectTransferEvent(
                this, transfer.getHeader(), transfer.getFromAccount(), transfer.getToAccount(), transfer.getAmount()
        );
        this.notifyEventHandlers(ledgerTransferExecutedEvent);
    }

    /**
     * If the specified {@code destinationAddress} has a corresponding account on this ledger, then the account is
     * considered 'local', and this method will return {@code true}.  Otherwise, the account is considered 'non-local',
     * and this method will return {@code false}.
     *
     * @param destinationAddress A {@link String} representing an ILP address of the ultimate destination account that
     *                           funds will be transferred to as part of a {@link LedgerTransfer}.
     * @return
     */
    private boolean isLocalAccount(final String destinationAddress) {
        // TODO: Refactor SimpleLedgerAddressParser for DI and then thread-safety.
        final SimpleLedgerAddressParser parser = new SimpleLedgerAddressParser();
        parser.parse(destinationAddress);
        final String accountName = parser.getAccountName();
        final LedgerAccount account = this.getLedgerAccountManager().getAccountByName(accountName);
        return account != null;
    }

    /**
     * Completes the supplied {@link LedgerTransfer} locally without utilizing any conditions or connectors.
     *
     * @param transfer An instance of {@link LedgerTransfer} to complete locally.
     */
    private void sendLocally(final LedgerTransfer transfer) {

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
            handler.onLedgerEvent(ledgerEvent);
        }
    }

    // TODO: The Ledger interface should have the ability to unregister an event handler.  Or, alternatively, an abstract
    // class should be created that requires the event-handlers at construction time?

    @Override
    public LedgerAccountManager getLedgerAccountManager() {
        // FIXME: Remove getLedgerAccountManager here and in parent interface
        return LedgerAccountManagerFactory.getAccountManagerSingleton();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
