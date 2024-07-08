import java.math.BigDecimal;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        String filePath = "D:\\IDE\\IntelliJ IDEA Community Edition 2023.3.2\\Projects\\pizza-repository\\ATM\\ATM.txt";
        TextFileManager textFileManager = new TextFileManager(filePath);
        Validation validation = new Validation();
        ATM atm = new ATM(textFileManager);

        atm.loadCards();

        Scanner scanner = new Scanner(System.in);
        outerLoop:
        while (true) {
            System.out.println("Введите номер карты (или 'exit' для выхода):");
            String cardNumber = scanner.nextLine();

            if (cardNumber.equalsIgnoreCase("exit")) {
                System.out.println("Выход из системы.");
                break;
            }
            if (!validation.isValidCardNumber(cardNumber)) {
                System.out.println("Неверный формат номера карты.");
                continue;
            }
            if (!atm.getCards().containsKey(cardNumber)) {
                System.out.println("Карта не найдена.");
                continue;
            }
            while (true) {
                System.out.println("Введите PIN код (или 'back' для возврата на шаг назад, 'exit' для выхода):");
                String pinCode = scanner.nextLine();
                if (pinCode.equalsIgnoreCase("exit")) {
                    System.out.println("Выход из системы.");
                    break outerLoop;
                }
                if (pinCode.equalsIgnoreCase("back")) {
                    System.out.println("Назад.");
                    continue outerLoop;
                }
                if (!validation.isValidCodePin(pinCode)) {
                    System.out.println("Неверный формат PIN кода.");
                    continue;
                }
                if (atm.authorize(cardNumber, pinCode)) {
                    System.out.println("Авторизация успешна.");
                    while (true) {
                        System.out.println("Выберите операцию: 1 - Проверить баланс, 2 - Снять средства, 3 - Пополнить баланс, 4 - Выход, 'back' для возврата на шаг назад, 'exit' для выхода");
                        String choice = scanner.nextLine();

                        if (choice.equalsIgnoreCase("exit")) {
                            System.out.println("Выход из системы.");
                            break outerLoop;
                        }
                        if (choice.equalsIgnoreCase("back")) {
                            System.out.println("Назад.");
                            break;
                        }
                        switch (choice) {
                            case "1":
                                System.out.println("Баланс: " + atm.checkBalance(cardNumber));
                                break;
                            case "2":
                                System.out.println("Введите сумму для снятия (или 'back' для возврата на шаг назад, 'exit' для выхода):");
                                String withdrawInput = scanner.nextLine();
                                if (withdrawInput.equalsIgnoreCase("exit")) {
                                    System.out.println("Выход из системы.");
                                    break outerLoop;
                                }
                                if (withdrawInput.equalsIgnoreCase("back")) {
                                    System.out.println("Назад.");
                                    break;
                                }
                                try {
                                    BigDecimal amountWithdraw = new BigDecimal(withdrawInput);
                                    if (atm.withdraw(cardNumber, amountWithdraw)) {
                                        System.out.println("Средства успешно сняты.");
                                    } else {
                                        System.out.println("Ошибка при снятии средств.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Неверный формат суммы.");
                                }
                                break;
                            case "3":
                                System.out.println("Введите сумму для пополнения (или 'back' для возврата на шаг назад, 'exit' для выхода):");
                                String depositInput = scanner.nextLine();
                                if (depositInput.equalsIgnoreCase("exit")) {
                                    System.out.println("Выход из системы.");
                                    break outerLoop;
                                }
                                if (depositInput.equalsIgnoreCase("back")) {
                                    System.out.println("Назад.");
                                    break;
                                }
                                try {
                                    BigDecimal amountDeposit = new BigDecimal(depositInput);
                                    if (atm.deposit(cardNumber, amountDeposit)) {
                                        System.out.println("Средства успешно пополнены.");
                                    } else {
                                        System.out.println("Ошибка при пополнении средств.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Неверный формат суммы.");
                                }
                                break;
                            case "4":
                                System.out.println("Выход.");
                                break;
                            default:
                                System.out.println("Неверный выбор.");
                        }
                        if (choice.equals("4")) {
                            break;
                        }
                    }
                    break;
                } else {
                    if (atm.isCardBlocked(cardNumber)) {
                        break;
                    }
                    System.out.println("Ошибка авторизации. Попробуйте снова.");
                }
            }
        }
    }
}