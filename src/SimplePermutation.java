import java.util.Arrays;
import java.util.Scanner;

/**
 <h1>Простая перестановка </h1>
 */
public class SimplePermutation {
    public static final String START_COLOR = String.valueOf((char) 27);
    public static final String FINAL_COLOR = ((char) 27 + "[0m");
    public static final String RED = "[31m";

    public static int getIndex(int[] mas, int number) {
        for(int i = 0; i < mas.length; i++) {
            if (mas[i] == number) {
                return i;
            }
        }
        return -1;
    }

    // 5 3 1 2 4
    public static String crypt(String massage, int[] key, int keyLine) {
        StringBuilder code = new StringBuilder();
        int countTable = (massage.length() / (key.length * keyLine));
        for (int startLine = 0; startLine <= countTable; startLine++) {
            for (int column = 1; column <= key.length; column++) {
                toColumn:
                for (int line = startLine * keyLine; line < (startLine * keyLine) + keyLine; line++) {
                    if (code.toString().length() == massage.length()) return code.toString();
                    if (line * key.length + getIndex(key, column) >= massage.length()) {
                        break toColumn;
                    }
                    code.append(massage.charAt(line * key.length + getIndex(key, column)));
                }
            }
        }
        return code.toString();
    }


    public static String decrypt(String code, int[] key, int keyLine) {
        StringBuilder massage = new StringBuilder();
        int countTable = (code.length() / (key.length * keyLine));
        for (int table = 0; table < countTable; table++) { // восстановим сообщению, полностью поместившееся в таблицу
            for (int step = 0; step < keyLine; step++) {
                for (int start : key) {
                   // System.out.println("table line start  "  + table + " " + step + " " + start);
                    massage.append(code.charAt(key.length * keyLine * table + (start - 1) * keyLine + step));
                    if (massage.length() == code.length()) return massage.toString();
                    System.out.println(massage);
                }
            }
        }
        code = code.substring(massage.length(), code.length()); // часть шифра, не полностью заполнившая совою таблицу
        System.out.print("осталось расшифровать: " + code + "\n");

        int[] lenOfColumn = new int[key.length]; // кол-во букв в столбцах по порядку столбцов(а не их номеров в key)
        for (int j = 0; j < lenOfColumn.length; j++) {
            lenOfColumn[j] = code.length() / key.length + ((code.length() % key.length >= j + 1) ? 1 : 0);
        }

        int indexInKey = -1; // номер текущего столбца(по порядку)
        for (int index = 0; index < code.length(); index++) {
            indexInKey++;
            if (indexInKey == key.length) indexInKey = 0;
            int countOfSlice = 0;
            for (int сolumn = 1; сolumn < key[indexInKey]; сolumn++) {
                countOfSlice += lenOfColumn[getIndex(key, сolumn)];
            }
            int start = countOfSlice + (index / key.length);
            massage.append(code.charAt(start));
            System.out.println(massage);
        }
        return massage.toString();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String enter;
                System.out.println("Введите ключ(набор натуральных чисел от 1 до n>1 с шагом 1 в случайном порядке через пробел");
                enter = scanner.nextLine();
                if (enter.equals("end")) break;
                int count = (int) enter.chars().filter(ch -> ch == ' ').count() + 1; // кол-во чисел в ключе
                if (count < 2) throw new IllegalArgumentException(START_COLOR + RED + "Некорректный ключ: меньше двух чисел" + FINAL_COLOR);
                int[] key = new int[count];
                int i = -1;
                for (String symbol : enter.split(" ")) {
                    key[++i] = Integer.parseInt(symbol);
                }
                int j = 1;
                int[] sortedKey = key.clone();
                Arrays.sort(sortedKey);
                for(int index : sortedKey) {
                    if (index != j++) throw new IllegalArgumentException(START_COLOR + RED + "Некорректный ключ: значения чисел не подходят" + FINAL_COLOR);
                }
                int keyLine = 0;
                while (keyLine <= 0) {
                    System.out.println("Введите число строк таблицы: ");
                    keyLine = Integer.parseInt(scanner.nextLine());
                }

                System.out.println("1. Шифровать\n2. Расшифровать");
                enter = scanner.nextLine();
                switch (enter) {
                    case "1" -> {
                        System.out.println("Введите текст:");
                        System.out.println(crypt(scanner.nextLine(), key, keyLine));
                    }
                    case "2" -> {
                        System.out.println("Введите зашифрованное сообщение:");
                        System.out.println(decrypt(scanner.nextLine(), key, keyLine));
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
