import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ATM {
    private Map<String, Card> cards = new HashMap<>();
    private TextFileManager textFileManager;
    private Map<String, Integer> failedAttempts = new HashMap<>();
    private Map<String, Long> blockedCards = new HashMap<>();

    public ATM(TextFileManager textFileManager) {
        this.textFileManager = textFileManager;
    }

    public void loadCards() {
        String fileContent = textFileManager.readFromFile();
        String[] lines = fileContent.split("\n");

        String cardNumber = null;
        String pinCode = null;
        BigDecimal balance = null;

        for (String line : lines) {
            line = line.trim();

            if (line.contains(" ")) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    cardNumber = parts[0];
                    pinCode = parts[1];
                    try {
                        balance = new BigDecimal(parts[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            } else {
                if (Validation.isValidCardNumber(line)) {
                    cardNumber = line;
                } else if (Validation.isValidCodePin(line)) {
                    pinCode = line;
                } else {
                    try {
                        balance = new BigDecimal(line);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            if (cardNumber != null && pinCode != null && balance != null) {
                cards.put(cardNumber, new Card(cardNumber, pinCode, balance));
                cardNumber = null;
                pinCode = null;
                balance = null;
            }
        }
    }

    public boolean authorize(String cardNumber, String pin) {
        if (isCardBlocked(cardNumber)) {
            System.out.println("Карта заблокирована. Попробуйте позже.");
            return false;
        }

        Card card = cards.get(cardNumber);
        if (card != null && card.getPinCode().equals(pin) && card.isAccess()) {
            failedAttempts.put(cardNumber, 0);
            return true;
        } else {
            if (card != null) {
                int attempts = failedAttempts.getOrDefault(cardNumber, 0) + 1;
                failedAttempts.put(cardNumber, attempts);
                if (attempts >= 3) {
                    card.setAccess(false);
                    blockedCards.put(cardNumber, System.currentTimeMillis());
                    System.out.println("Карта заблокирована на 24 часа из-за 3 неудачных попыток ввода PIN-кода.");
                }
            }
            return false;
        }
    }

    public boolean isCardBlocked(String cardNumber) {
        if (blockedCards.containsKey(cardNumber)) {
            long blockTime = blockedCards.get(cardNumber);
            long currentTime = System.currentTimeMillis();
            if (currentTime - blockTime >= TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
                unblockCard(cardNumber);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void unblockCard(String cardNumber) {
        blockedCards.remove(cardNumber);
        failedAttempts.put(cardNumber, 0);
        cards.get(cardNumber).setAccess(true);
    }

    public BigDecimal checkBalance(String cardNumber) {
        Card card = cards.get(cardNumber);
        if (card != null) {
            return card.getBalance();
        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean withdraw(String cardNumber, BigDecimal amount) {
        Card card = cards.get(cardNumber);
        if (card != null && card.getBalance().compareTo(amount) >= 0) {
            card.setBalance(card.getBalance().subtract(amount));
            saveCardsToFile();
            return true;
        } else {
            return false;
        }
    }

    public boolean deposit(String cardNumber, BigDecimal amount) {
        if (Validation.isValidAmount(amount)) {
            Card card = cards.get(cardNumber);
            if (card != null) {
                card.setBalance(card.getBalance().add(amount));
                saveCardsToFile();
                return true;
            }
        }
        return false;
    }

    public Map<String, Card> getCards() {
        return cards;
    }

    public void saveCardsToFile() {
        StringBuilder content = new StringBuilder();
        for (Card card : cards.values()) {
            content.append(card.getCardNumber()).append(" ")
                    .append(card.getPinCode()).append(" ")
                    .append(card.getBalance()).append("\n");
        }
        textFileManager.writeToFile(content.toString().trim());
    }
}