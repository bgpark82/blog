package memory_leak;

import java.util.Optional;
import java.util.UUID;

public class ProcessCustomerTask implements Runnable {

    private CustomerManager cm;

    public ProcessCustomerTask(CustomerManager cm) {
        this.cm = cm;
    }

    @Override
    public void run() {
        while(true) {
            Optional<Customer> customer = cm.getNextCustomer();
            if(!customer.isPresent()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {

            }
        }
    }
}
