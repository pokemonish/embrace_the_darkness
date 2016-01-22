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

    private static final int MIN_SIZE = 500;
    private static final int MAX_SIZE = 5000;

    private final class AttackThread extends Thread {
        private int times;
        private Set<Address> addressSet;
        private AttackThread(int times, Set<Address> addressSet) {
            this.times = times;
            this.addressSet = addressSet;
        }

        @Override
        public void run() {
            for (int i = 0; i < times; ++i) {
                addressSet.add(addressService.getAccountServiceAddress());
            }
        }
    }

    private AddressService addressService = new AddressService();

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAccountServiceAddress() throws InterruptedException {
        Set<Address> addresses = Collections.synchronizedSet(new HashSet<>());

        startAttackThreads(addresses, addresses);

        assertEquals(MAX_SIZE, addresses.size());
    }

    private void startAttackThreads(Set<Address> addresses1, Set<Address> addresses2)
            throws InterruptedException {
        int times1 = new Random().nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE;
        int times2 = MAX_SIZE - times1;

        MessageSystem messageSystem = mock(MessageSystem.class, RETURNS_DEEP_STUBS);
        when(messageSystem.getAddressService()).thenReturn(mock(AddressService.class));

        for (int i = 0; i < MAX_SIZE; ++i) {
            addressService.registerAccountService(
                    new AccountServiceTh(messageSystem,
                            mock(DBService.class, RETURNS_DEEP_STUBS)));
        }

        Thread t1 = new AttackThread(times1, addresses1);
        Thread t2 = new AttackThread(times2, addresses2);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @Test
    public void testBalance() throws InterruptedException {
        Set<Address> addresses1 = Collections.synchronizedSet(new HashSet<>());
        Set<Address> addresses2 = Collections.synchronizedSet(new HashSet<>());

        startAttackThreads(addresses1, addresses2);

        assertFalse(addresses1.removeAll(addresses2));
    }
}
