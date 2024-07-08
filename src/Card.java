import java.math.BigDecimal;

public class Card {
    private String cardNumber;
    private String pinCode;
    private BigDecimal balance;
    private boolean access;

    public Card(String cardNumber, String pinCode, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.access = true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isAccess() {
        return access;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getCardDataAsString() {
        return cardNumber + " " + pinCode + " " + balance.toString();
    }
}
