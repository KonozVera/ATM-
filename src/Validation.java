import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Validation {
    private static final String CARD_NUMBER_PATTERN = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";
    private static final String CODE_PIN_PATTERN = "\\d{4}";

    public static boolean isValidCardNumber(String cardNumber) {
        return Pattern.matches(CARD_NUMBER_PATTERN, cardNumber);
    }

    public static boolean isValidCodePin(String pinCode) {
        return Pattern.matches(CODE_PIN_PATTERN, pinCode);
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) >= 0 && amount.compareTo(new BigDecimal("1000000")) < 0;
    }
}
