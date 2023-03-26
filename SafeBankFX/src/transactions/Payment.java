package transactions;

@FunctionalInterface
public interface Payment {
    void processPayment(double amount) throws Exception;
}
