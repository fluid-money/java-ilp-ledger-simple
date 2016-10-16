package org.interledger.ilp.ledger.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.LedgerTransferRejectedReason;
import org.interledger.ilp.core.events.LedgerConnectedEvent;
import org.interledger.ilp.core.events.LedgerDirectTransferEvent;
import org.interledger.ilp.core.events.LedgerDisonnectedEvent;
import org.interledger.ilp.core.events.LedgerEvent;
import org.interledger.ilp.core.events.LedgerTransferExecutedEvent;
import org.interledger.ilp.core.events.LedgerTransferPreparedEvent;
import org.interledger.ilp.core.events.LedgerTransferRejectedEvent;
import org.interledger.ilp.ledger.events.AbstractEventBusLedgerEventHandler;
import org.interledger.ilp.ledger.events.AbstractLedgerEventHandler;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link AbstractEventBusLedgerEventHandler}.  Merely tests that the abstract super-class is wired
 * properly to the Guava Event Bus.
 */
public class AbstractLedgerEventHandlerTest {

    private boolean ledgerConnectedEventHandled = false;
    private boolean ledgerDisonnectedEventHandled = false;
    private boolean ledgerTransferPreparedEventHandled = false;
    private boolean ledgerTransferExecutedEventHandled = false;
    private boolean ledgerTransferRejectedEventHandled = false;
    private boolean ledgerDirectTransferEventHandled = false;

    /**
     * An instance of {@link AbstractLedgerEventHandler} for testing purposes to check if each handler method was called
     * properly.
     */
    private AbstractLedgerEventHandler handler = new AbstractLedgerEventHandler() {
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
    };

    /**
     * Assert that each handler method is called in response to a {@link LedgerEvent} of the proper type.  If
     * anything is mis-wired, then this test will fail.
     */
    @Test
    public void onLedgerEvents() {
        final InterledgerPacketHeader interledgerPacketHeaderMock = mock(InterledgerPacketHeader.class);
        when(interledgerPacketHeaderMock.isOptimisticModeHeader()).thenReturn(true);

        handler.onLedgerEvent(new LedgerConnectedEvent(mock(Ledger.class)));
        handler.onLedgerEvent(new LedgerDisonnectedEvent(mock(Ledger.class)));
        handler.onLedgerEvent(
                new LedgerTransferPreparedEvent(mock(Ledger.class), interledgerPacketHeaderMock, "", "", ""));
        handler.onLedgerEvent(
                new LedgerTransferExecutedEvent(mock(Ledger.class), interledgerPacketHeaderMock, "", "", ""));
        handler.onLedgerEvent(
                new LedgerTransferRejectedEvent(mock(Ledger.class), interledgerPacketHeaderMock, "", "", "",
                                                LedgerTransferRejectedReason.REJECTED_BY_RECEIVER
                ));
        handler.onLedgerEvent(
                new LedgerDirectTransferEvent(mock(Ledger.class), interledgerPacketHeaderMock, "", "", ""));

        assertThat(ledgerConnectedEventHandled, is(true));
        assertThat(ledgerDisonnectedEventHandled, is(true));
        assertThat(ledgerTransferPreparedEventHandled, is(true));
        assertThat(ledgerTransferExecutedEventHandled, is(true));
        assertThat(ledgerTransferRejectedEventHandled, is(true));
        assertThat(ledgerDirectTransferEventHandled, is(true));
    }

    /**
     * Test that the dead-event handler is wired properly.
     */
    @Test(expected = RuntimeException.class)
    public void testDeadEvent() {
        // Raw LedgerEvent is currently un-handled.
        try {
            handler.onLedgerEvent(mock(LedgerEvent.class));
        } catch (RuntimeException e) {
            assertThat(e.getMessage().startsWith("Unhandled Event: "), is(true));
            throw e;
        }
    }
}