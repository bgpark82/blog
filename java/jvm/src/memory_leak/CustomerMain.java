package memory_leak;

import java.util.ArrayList;
import java.util.List;

public class CustomerMain {

    public static void main(String[] args) {
        List<Customer> customers = new ArrayList<>();

        // 100명이 되면 10명을 제거할 예
        while(true) {
            Customer customer = new Customer("Matt");
            customers.add(customer);
            if(customers.size() > 10000) {
                for (int i = 0; i < 5000; i++) {
                    customers.remove(0);
                }
            }
        }
    }
}
