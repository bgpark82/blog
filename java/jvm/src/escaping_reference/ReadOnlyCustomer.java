package escaping_reference;

public interface ReadOnlyCustomer {
    String getName();

    @Override
    String toString();
}
