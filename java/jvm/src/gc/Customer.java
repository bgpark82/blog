package gc;

public class Customer {

    private String name;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public Customer(Customer oldCustomer) {
        this.name = oldCustomer.getName();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void finalize() {
        System.out.println("This object is being g.c'd" + name);
        while(true) {

        }
    }
}
