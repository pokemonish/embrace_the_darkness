package messagesystem;

import accountservice.AccountServiceTh;
import accountservice.MessageToAccountService;
import frontendservice.FrontEnd;
import frontendservice.MessageToFrontEnd;
import main.ThreadSettings;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by fatman on 24/12/15.
 */
public class MessageSystemTest {

    private static final Address ADDRESS = new Address();
    private static final Address ADDRESS_FRONT = new Address();
    private static final int MIN_TIMES = 30;
    private static final int MAX_TIMES = 100;
    private int counterAcc = 0;
    private int counterFront = 0;

    private final class TestMessage extends MessageToAccountService {

        private TestMessage(Address from, Address to) {
            super(from, to);
        }

        @Override
        protected void exec(AccountServiceTh accountServiceTh) {
            ++counterAcc;
        }
    }

    private final class TestMessageFront extends MessageToFrontEnd {

        private TestMessageFront(Address from, Address to) {
            super(from, to);
        }

        @Override
        protected void exec(FrontEnd frontEnd) {
            ++counterFront;
        }
    }

    private final class AccountServiceFake implements Runnable {
        private final MessageSystem messageSystemToAttack;
        private final AccountServiceTh accountServiceTh;
        private final int times;

        private AccountServiceFake(MessageSystem messageSystem, AccountServiceTh accountServiceTh, int times) {
            this.messageSystemToAttack = messageSystem;
            this.accountServiceTh = accountServiceTh;
            this.times = times;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; ++i) {
                messageSystemToAttack.sendMessage(new TestMessageFront(ADDRESS_FRONT, ADDRESS_FRONT));
            }
            while (true) {
                messageSystemToAttack.execForAbonent(accountServiceTh);
                try {
                    Thread.sleep(ThreadSettings.SERVICE_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    private final class FrontendFake implements Runnable {
        private final MessageSystem messageSystemToAttack;
        private final FrontEnd frontEnd;
        private final int times;

        private FrontendFake(MessageSystem messageSystem, FrontEnd frontEnd, int times) {
            this.messageSystemToAttack = messageSystem;
            this.frontEnd = frontEnd;
            this.times = times;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; ++i) {
                messageSystemToAttack.sendMessage(new TestMessage(ADDRESS, ADDRESS));
            }
            while (true) {
                messageSystemToAttack.execForAbonent(frontEnd);
                try {
                    Thread.sleep(ThreadSettings.SERVICE_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    private AccountServiceTh accountServiceThMock = mock(AccountServiceTh.class);
    private FrontEnd frontEndMock = mock(FrontEnd.class);
    private MessageSystem messageSystem = new MessageSystem();

    @Test
    public void messageSystemTest() throws InterruptedException {
        when(accountServiceThMock.getAddress()).thenReturn(ADDRESS);
        when(frontEndMock.getAddress()).thenReturn(ADDRESS_FRONT);
        messageSystem.addService(accountServiceThMock);
        messageSystem.addService(frontEndMock);

        int times = MIN_TIMES + new Random().nextInt(MAX_TIMES);

        Thread thread1 = new Thread(new AccountServiceFake(messageSystem, accountServiceThMock, times));
        Thread thread2 = new Thread(new FrontendFake(messageSystem, frontEndMock, times));

        thread1.start();
        thread2.start();

        Thread.sleep(2 * 1000);

        assertEquals(counterAcc, times);
        assertEquals(counterFront, times);
    }
}