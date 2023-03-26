package banking;

public class BankingMain {
    public static void main(String[] args) {
        try {
            System.out.println("*** Welcome to Bank of Ameerpet ***");
            BankingServices.getAllServices();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
