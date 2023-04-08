package transactions;

@FunctionalInterface
public interface Deposit {
   public boolean deposit(double amount) throws Exception;
}
