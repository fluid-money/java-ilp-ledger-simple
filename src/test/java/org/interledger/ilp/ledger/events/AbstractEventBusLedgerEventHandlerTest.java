package org.interledger.ilp.ledger.events;

import com.google.common.eventbus.DeadEvent;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link AbstractEventBusLedgerEventHandler}.  Merely tests that the abstract super-class is wired
 * properly to the Guava Event Bus.
 */
public class AbstractEventBusLedgerEventHandlerTest {

    @Mock
    private Ledger ledgerMock;

    private boolean ledgerConnectedEventHandled = false;
    private boolean ledgerDisonnectedEventHandled = false;
    private boolean ledgerTransferPreparedEventHandled = false;
    private boolean ledgerTransferExecutedEventHandled = false;
    private boolean ledgerTransferRejectedEventHandled = false;
    private boolean ledgerDirectTransferEventHandled = false;
    private boolean deadEventHandled = false;
    private AbstractEventBusLedgerEventHandler handler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.handler = new AbstractEventBusLedgerEventHandler(ledgerMock) {
            @Override
            protected boolean handleEvent(LedgerConnectedEvent ledgerConnectedEvent) {
                ledgerConnectedEventHandled = true;
                return true;
            }

            @Override
            protected boolean handleEvent(LedgerDisonnectedEvent ledgerDisonnectedEvent) {
                ledgerDisonnectedEventHandled = true;
                return true;
            }

            @Override
            protected boolean handleEvent(
                    LedgerTransferPreparedEvent ledgerTransferPreparedEvent
            ) {
                ledgerTransferPreparedEventHandled = true;
                return true;
            }

            @Override
            protected boolean handleEvent(
                    LedgerTransferExecutedEvent ledgerTransferExecutedEvent
            ) {
                ledgerTransferExecutedEventHandled = true;
                return true;
            }

            @Override
            protected boolean handleEvent(LedgerDirectTransferEvent ledgerDirectTransferEvent) {
                ledgerDirectTransferEventHandled = true;
                return true;
            }

            @Override
            protected boolean handleEvent(
                    LedgerTransferRejectedEvent ledgerTransferRejectedEvent
            ) {
                ledgerTransferRejectedEventHandled = true;
                return true;
            }

            @Override
            protected boolean deadEvent(final DeadEvent deadEvent) {
                deadEventHandled = true;
                return true;
            }
        };
    }

    /**
     * Assert that each event-bus handler method is called in response to a {@link LedgerEvent} of the proper type.  If
     * anything is mis-wired, then this test will fail.
     */
    @Test
    public void onLedgerEvents() {
        handler.onLedgerEvent(mock(LedgerConnectedEvent.class));
        handler.onLedgerEvent(mock(LedgerDisonnectedEvent.class));
        handler.onLedgerEvent(mock(LedgerTransferPreparedEvent.class));
        handler.onLedgerEvent(mock(LedgerTransferExecutedEvent.class));
        handler.onLedgerEvent(mock(LedgerTransferRejectedEvent.class));
        handler.onLedgerEvent(mock(LedgerDirectTransferEvent.class));

        assertThat(ledgerConnectedEventHandled, is(true));
        assertThat(ledgerDisonnectedEventHandled, is(true));
        assertThat(ledgerTransferPreparedEventHandled, is(true));
        assertThat(ledgerTransferExecutedEventHandled, is(true));
        assertThat(ledgerTransferRejectedEventHandled, is(true));
        assertThat(ledgerDirectTransferEventHandled, is(true));
        assertThat(deadEventHandled, is(false));
    }

    /**
     * Test that the dead-event handler is wired properly.
     */
    @Test
    public void testDeadEvent() {
        // Raw LedgerEvent is currently un-handled.
        handler.onLedgerEvent(mock(LedgerEvent.class));
        assertThat(deadEventHandled, is(true));
    }

}