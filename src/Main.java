import java.util.*;
import java.util.function.BinaryOperator;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final Map<String, ComplexMatrix> memory = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.out.println("=== Калькулятор матриц комплексных чисел ===");
        while (true) {
            printMenu();
            String choice = readLine("> ");
            try {
                switch (choice) {
                    case "1" -> createMatrix();
                    case "2" -> showAll();
                    case "3" -> binaryOperation("A + B", ComplexMatrix::plus);
                    case "4" -> binaryOperation("A * B", ComplexMatrix::multiply);
                    case "5" -> binaryOperation("A / B (= A * B^-1)", ComplexMatrix::divide);
                    case "6" -> transposeOperation();
                    case "7" -> determinantOperation();
                    case "0" -> {
                        System.out.println("Bye bye bye!)");
                        return;
                    }
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("""

            1 - Создать матрицу
            2 - Показать все матрицы
            3 - Сложить (A + B)
            4 - Умножить (A * B)
            5 - Разделить (A / B)
            6 - Транспонировать
            7 - Определитель
            0 - Выход""");
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) System.exit(0);
        return in.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                int v = Integer.parseInt(readLine(prompt));
                if (v > 0) return v;
                System.out.println("Введите положительное число.");
            } catch (NumberFormatException e) {
                System.out.println("Это не целое число, попробуйте ещё раз.");
            }
        }
    }

    private static void createMatrix() {
        String name = readLine("Имя матрицы (например, A): ").toUpperCase();
        if (name.isEmpty()) { System.out.println("Имя не может быть пустым."); return; }

        int rows = readInt("Число строк: ");
        int cols = readInt("Число столбцов: ");
        System.out.println("Вводите строки: " + cols
                + " чисел через пробел, формат: 3, -2.5, i, 2i, 1+2i, 1.5-3i");

        Complex[][] data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            while (true) {
                String[] tokens = readLine("Строка " + (i + 1) + ": ").split("\\s+");
                if (tokens.length != cols) {
                    System.out.println("Нужно ровно " + cols + " чисел, введено " + tokens.length + ".");
                    continue;
                }
                try {
                    for (int j = 0; j < cols; j++) data[i][j] = Complex.parse(tokens[j]);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Не удалось разобрать число: " + e.getMessage());
                }
            }
        }
        memory.put(name, new ComplexMatrix(data));
        System.out.println("Матрица " + name + " сохранена.");
    }



    private static ComplexMatrix ask(String prompt) {
        String name = readLine(prompt).toUpperCase();
        ComplexMatrix m = memory.get(name);
        if (m == null) throw new IllegalArgumentException("Матрица '" + name + "' не найдена");
        return m;
    }



    private static void showAll() {
        if (memory.isEmpty()) { System.out.println("Матриц пока нет."); return; }
        memory.forEach((name, m) ->
                System.out.println(name + " (" + m.getRows() + "x" + m.getCols() + "):\n" + m));
    }





    private static void binaryOperation(String title, BinaryOperator<ComplexMatrix> op) {
        System.out.println("Операция: " + title);
        ComplexMatrix a = ask("Имя первой матрицы: ");
        ComplexMatrix b = ask("Имя второй матрицы: ");
        ComplexMatrix result = op.apply(a, b);
        memory.put("R", result);
        System.out.println("Результат (сохранён как R):\n" + result);
    }



    private static void transposeOperation() {
        ComplexMatrix result = ask("Имя матрицы: ").transpose();
        memory.put("R", result);
        System.out.println("Результат (сохранён как R):\n" + result);
    }



    private static void determinantOperation() {
        Complex det = ask("Имя матрицы: ").determinant();
        System.out.println("Определитель = " + det);
    }


}
