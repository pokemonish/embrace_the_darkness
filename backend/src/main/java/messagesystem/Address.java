package messagesystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author e.shubin
 */
public final class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final int id;

    public Address(){
        id = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return id == address.id;

    }

    @Override

    public int hashCode() {
        return id;
    }
}
