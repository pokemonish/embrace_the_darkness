package messagesystem;

import accountservice.AccountServiceTh;
import frontendservice.FrontEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author e.shubin
 */
public class AddressService {
    private Address frontEnd;
    private List<Address> accountServiceList = new ArrayList<>();

    private AtomicInteger accountServiceCounter = new AtomicInteger();

    public void registerFrontEnd(FrontEnd frontEndserv) {
        this.frontEnd = frontEndserv.getAddress();
    }

    public void registerAccountService(AccountServiceTh accountService) {
        accountServiceList.add(accountService.getAddress());
    }

    public Address getFrontEndAddress() {
        return frontEnd;
    }

    public synchronized Address getAccountServiceAddress() {
        int index = accountServiceCounter.getAndIncrement() % accountServiceList.size();
        return accountServiceList.get(index);
    }
}
