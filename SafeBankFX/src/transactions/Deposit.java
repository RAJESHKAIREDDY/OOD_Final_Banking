package transactions;

@FunctionalInterface
public interface Deposit {
   public void deposit(double amount) throws Exception;
}
