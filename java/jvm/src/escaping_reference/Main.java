package escaping_reference;

import escaping_reference.customers.Customer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        CustomerRecords records = new CustomerRecords();
        records.setRecords(new Customer("John"));
        records.setRecords(new Customer("Simon"));

//        records.getCustomers().clear();
//        records.find("John").setName("Jane");
        ReadOnlyCustomer john = records.find("John");
        Customer j = (Customer) john;
        j.setName("Jane");

        for (ReadOnlyCustomer next : records) {
            System.out.println(next);
        }
//        for(Customer next: records.getRecords().values()) {
//            System.out.println(next);
//        }

        System.out.println(records.find("John"));
    }



    public static class CustomerRecords implements Iterable<ReadOnlyCustomer> {
        private Map<String, ReadOnlyCustomer> records;

        @Override
        public Iterator<ReadOnlyCustomer> iterator() {
            return records.values().iterator();
        }

//        public Map<String, Customer> getRecords() {
//            return records;
//        }

        // 2. 새로운 객체를 반환
//        public Map<String ,Customer> getCustomers () {
//            return new HashMap<>(this.records);
//        }

        // 3. immutable 객체를 반환
        public Map<String , ReadOnlyCustomer> getCustomers () {
            return Collections.unmodifiableMap(records);
        }

//        public Customer find(String name) {
//            return new Customer(records.get(name));
//        }

        public ReadOnlyCustomer find(String name) {
            return new Customer(records.get(name));
        }

        public void setRecords(ReadOnlyCustomer customer) {
            this.records.put(customer.getName(), customer);
        }

        public CustomerRecords() {
            this.records = new HashMap<>();
        }
    }
}
