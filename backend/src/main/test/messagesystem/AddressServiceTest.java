package messagesystem;

import accountservice.AccountServiceTh;
import base.DBService;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by fatman on 24/12/15.
 */
public class AddressServiceTest {

    private static final int MAX_SIZE = 5000;
    private static final int MIN_SIZE = 500;

    Set<Address> addresses = Collections.synchronizedSet(new HashSet<>());

    private final class AttackThread extends Thread {
        private int times;
        private AttackThread(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; ++i) {
                addresses.add(addressService.getAccountServiceAddress());
            }
        }
    }

    private AddressService addressService = new AddressService();

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAccountServiceAddress() throws InterruptedException {
        int times1 = new Random().nextInt(MAX_SIZE) + MIN_SIZE;
        int times2 = MAX_SIZE - times1;

        MessageSystem messageSystem = mock(MessageSystem.class, RETURNS_DEEP_STUBS);
        when(messageSystem.getAddressService()).thenReturn(mock(AddressService.class));

        for (int i = 0; i < MAX_SIZE; ++i) {
            addressService.registerAccountService(new AccountServiceTh(messageSystem, mock(DBService.class, RETURNS_DEEP_STUBS)));
        }

        Thread t1 = new AttackThread(times1);
        Thread t2 = new AttackThread(times2);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(MAX_SIZE, addresses.size());
    }
}