package util;

import model.Buyer;
import model.Medicine;
import model.Sale;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    /**
     * Сохраняет список продаж в CSV-файл.
     */
    public static void saveSales(File file, List<Sale> sales) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Заголовок
            writer.write("ID;ID_покупателя;Фамилия;Имя;ID_препарата;Наименование;Категория;Цена;Требуется_рецепт;Количество;Дата;Рецепт_предъявлен;Сумма");
            writer.newLine();

            for (Sale s : sales) {
                Buyer b = s.getBuyer();
                Medicine m = s.getMedicine();
                String line = String.join(";",
                        String.valueOf(s.getId()),
                        String.valueOf(b.getId()),
                        b.getLastName(),
                        b.getFirstName(),
                        String.valueOf(m.getId()),
                        m.getName(),
                        m.getCategory(),
                        String.format("%.2f", m.getPrice()),
                        m.isNeedRecipe() ? "да" : "нет",
                        String.valueOf(s.getQuantity()),
                        s.getDate().toString(),
                        s.isRecipePresented() ? "да" : "нет",
                        String.format("%.2f", s.getSum())
                );
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Загружает список продаж из CSV-файла.
     */
    public static List<Sale> loadSales(File file) throws IOException {
        List<Sale> sales = new ArrayList<>();
        DataStore store = DataStore.getInstance();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    if (line.startsWith("ID;")) continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(";", -1);
                if (p.length < 13) continue;

                int buyerId = parseInt(p[1]);
                String buyerLastName = p[2];
                String buyerFirstName = p[3];

                int medId = parseInt(p[4]);
                String medName = p[5];
                String medCategory = p[6];
                double medPrice = parseDouble(p[7]);
                boolean medNeedRecipe = "да".equalsIgnoreCase(p[8]);

                int quantity = parseInt(p[9]);
                LocalDate date = LocalDate.parse(p[10]);
                boolean recipePresented = "да".equalsIgnoreCase(p[11]);

                Buyer buyer = store.getBuyers().stream()
                        .filter(b -> b.getId() == buyerId)
                        .findFirst().orElse(null);
                if (buyer == null) {
                    buyer = new Buyer(buyerId, buyerLastName, buyerFirstName, "", "00000000");
                    store.getBuyers().add(buyer);
                }

                Medicine med = store.getMedicines().stream()
                        .filter(m -> m.getId() == medId)
                        .findFirst().orElse(null);
                if (med == null) {
                    med = new Medicine(medId, medName, medCategory, medPrice, medNeedRecipe);
                    store.getMedicines().add(med);
                }

                Sale sale = new Sale(parseInt(p[0]), buyer, med, quantity, date, recipePresented);
                sales.add(sale);
            }
        }
        return sales;
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static double parseDouble(String s) {
        try { return Double.parseDouble(s.trim().replace(",", ".")); } catch (Exception e) { return 0; }
    }
}