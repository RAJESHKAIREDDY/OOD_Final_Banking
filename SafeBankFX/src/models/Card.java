package models;

public class Card {
    private int id;
    private CardProvider cardProvider;
    private CardAssociator cardAssociator;

    public int getId() {
        return id;
    }

    public CardProvider getCardProvider() {
        return cardProvider;
    }

    public void setCardProvider(CardProvider cardProvider) {
        this.cardProvider = cardProvider;
    }

    public CardAssociator getCardAssociator() {
        return cardAssociator;
    }

    public void setCardAssociator(CardAssociator cardAssociator) {
        this.cardAssociator = cardAssociator;
    }
}
