package memory_leak;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CustomerManager {

    private List<Customer> customers = new ArrayList<>();
    private int nextAvailableId = 0;
//    private int lastProcessedId = -1;

    public void addCustomer(Customer customer) {
        synchronized (this) {
            customer.setId(nextAvailableId);
            synchronized (customers) {
                customers.add(customer);
            }
            nextAvailableId++;
        }
    }

    // Out of memory 나는 코드
//    public Optional<Customer> getNextCustomer() {
//        if(lastProcessedId + 1 > nextAvailableId) {
//            lastProcessedId++;
//            return Optional.of(customers.get(lastProcessedId));
//        }
//        return Optional.empty();
//    }

    public Optional<Customer> getNextCustomer() {
        synchronized (customers) {
            if(customers.size() > 0) {
                return Optional.of(customers.remove(0));
            }
        }return Optional.empty();
    }

    public void howManyCustomers() {
        int size = 0;
        size = customers.size();
        System.out.println("" + new Date() + " Customers in Queue : " + size + " of " +nextAvailableId);
    }
}
