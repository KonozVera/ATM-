import java.io.*;

public class TextFileManager {
    private String filePath;

    public TextFileManager(String filePath) {
        this.filePath = filePath;
    }

    public String readFromFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return content.toString();
    }

    public void writeToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public void updateCardData(Card updatedCard) {
        String fileContent = readFromFile();
        StringBuilder updatedContent = new StringBuilder();

        String[] lines = fileContent.split("\n");

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ");

            if (parts.length == 3 && parts[0].equals(updatedCard.getCardNumber())) {
                updatedContent.append(updatedCard.getCardNumber()).append(" ")
                        .append(updatedCard.getPinCode()).append(" ")
                        .append(updatedCard.getBalance()).append("\n");
            } else {
                updatedContent.append(line).append("\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
