package explore_memory;

public class Main {

    public static void calculate(Integer calcValue) {
        calcValue = calcValue * 100;
    }

    public static void renameCustomer(Customer cust) {
        cust.setName("Sally");
    }

    public static void main(String[] args) {
        final int localValue = 5;
        calculate(localValue);
        System.out.println(localValue);

        final Customer customer = new Customer("Kassie");
        renameCustomer(customer);
        System.out.println(customer.getName());
    }

    static class Customer {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Customer(String name) {
            this.name = name;
        }
    }
}
