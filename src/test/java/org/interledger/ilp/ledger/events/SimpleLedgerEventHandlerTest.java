package org.interledger.ilp.ledger.events;

import com.google.common.eventbus.EventBus;
import org.interledger.ilp.core.LedgerTransfer;
import org.interledger.ilp.ledger.Currencies;
import org.interledger.ilp.ledger.account.LedgerAccount;
import org.interledger.ilp.ledger.events.SimpleEventBusLedgerEventHandler;
import org.interledger.ilp.ledger.impl.LedgerTransferBuilder;
import org.interledger.ilp.ledger.impl.SimpleLedger;
import org.interledger.ilp.ledger.impl.SimpleLedgerAccount;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryAmount;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Simple ledger tests
 *
 * @author mrmx
 */
public class SimpleLedgerEventHandlerTest {

    private EventBus eventBus;
    Currencies CURRENCY = Currencies.EURO;
    SimpleLedger simpleLedger;

    @Before
    public void setUp() {
        this.eventBus = new EventBus();
        simpleLedger = new SimpleLedger(CURRENCY, "test");
    }

    /**
     * Test of registerEventHandler method, of class SimpleLedger.
     */
    @Test
    public void testRegisterEventHandler() {
        System.out.println("registerEventHandler");

        final SimpleEventBusLedgerEventHandler handler = new SimpleEventBusLedgerEventHandler(this.eventBus);
        simpleLedger.registerEventHandler(handler);

        System.out.println("send");
        LedgerAccount alice = new SimpleLedgerAccount("alice", CURRENCY.code()).setBalance(100);
        LedgerAccount bob = new SimpleLedgerAccount("bob", CURRENCY.code()).setBalance(100);
        simpleLedger.getLedgerAccountManager().addAccount(alice);
        simpleLedger.getLedgerAccountManager().addAccount(bob);
        LedgerTransfer transfer = LedgerTransferBuilder.instance()
                .from(alice)
                .destination("bob@test")
                .amount(Money.of(10, CURRENCY.code()))
                .build();
        simpleLedger.send(transfer);
        alice = simpleLedger.getLedgerAccountManager().getAccountByName("alice");
        assertThat(alice.getBalance(), is((MonetaryAmount) Money.of(90, "EUR")));
    }

}
