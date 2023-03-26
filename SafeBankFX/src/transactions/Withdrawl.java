package transactions;

@FunctionalInterface
public interface Withdrawl {
    void processWithdrawl(double amount) throws Exception;
}
