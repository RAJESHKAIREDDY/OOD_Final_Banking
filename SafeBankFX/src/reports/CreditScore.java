package reports;

public class CreditScore {
    private static int creditScore = 850;

    public static int getCreditScore() {
        return creditScore;
    }

    public static void setCreditScore(int creditScore) {
        if(creditScore < 600)
            CreditScore.creditScore = 600;
        else if(creditScore > 850)
            CreditScore.creditScore = 850;
        else
            CreditScore.creditScore = creditScore;
    }
}
