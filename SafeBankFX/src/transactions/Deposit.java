package transactions;

@FunctionalInterface
public interface Deposit {
    void processDeposit(double amount) throws Exception;
}
