package memory_leak;

import java.util.UUID;

public class GenerateCustomerTask implements Runnable{

    private CustomerManager cm;

    public GenerateCustomerTask(CustomerManager cm) {
        this.cm = cm;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {

            }
            String name = UUID.randomUUID().toString();
            Customer c = new Customer(name);
            cm.addCustomer(c);
        }
    }
}
