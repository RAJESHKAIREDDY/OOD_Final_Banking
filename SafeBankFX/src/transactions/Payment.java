package transactions;

@FunctionalInterface
public interface Payment {
    public boolean payment(double amount) throws Exception;
}
