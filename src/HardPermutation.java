import java.util.Arrays;
import java.util.Scanner;

/**
<h1>Перестановка, усложнённая по таблице </h1>
 */
public class HardPermutation {
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


    public static int countHolesUp (int x, int y, int[] keyHoles) { // по координатам поля в таблице определяет, сколько в её столбце над ней "дыр"
        int count = 0;
        for (int i = 0; i < keyHoles.length - 1; i += 2) {
            if (keyHoles[i] == x && keyHoles[i+1] < y) {
                count++;
            }
        }
        return count;
    }


    public static boolean isHole(int x, int y, int[] keyHoles) {
        for (int i = 0; i < keyHoles.length - 1; i += 2) {
            if (keyHoles[i] == x && keyHoles[i+1] == y) {
                return true;
            }
        }
        return false;
    }


    public static int lenInTable(int countLetter, int[] keyHoles, int lenLine) { // кол-во символов, которое бы заняла строка длины countLetter < кол-ва свободных ячеек в таблице, если поместить его в таблицу длиной lenLine с дырами keyHoles
        int[] indexOfHoles = new int[keyHoles.length / 2]; // массив из индексов неиспользуемых ячеек, если бы таблица была строкой
        int i = 0; // x или y в keyLine
        int j = 0;
        while (i < keyHoles.length) {
            indexOfHoles[j++] = keyHoles[i++] - 1 + (keyHoles[i++] - 1) * lenLine;
        }

        int lenInTable = 0;
        int letter = - 1;
        while (countLetter > 0) {
            letter++;
            if (getIndex(indexOfHoles, letter) != -1) { // если на этом месте дыра
                lenInTable++;
            } else {
                lenInTable++;
                countLetter--;
            }
        }
        return lenInTable;
    }


    public static String changeMassage(String massage, int[] keyHoles, int len, int high) {
        char symbol = '-';
        while (massage.contains(String.valueOf(symbol))) { // выбираем символ, не содержащийся в сообщении, чтобы вставить его вместо неиспользуемых ячейки
            symbol++;
        }

        int[] indexOfSymbol =  new int[keyHoles.length / 2]; // массив из индексов неиспользуемых ячеек, если бы таблица была строкой
        int i = 0;
        int j = 0;
        while (i < keyHoles.length) {
            indexOfSymbol[j++] = keyHoles[i++] - 1 + (keyHoles[i++] - 1) * len;
        }
        Arrays.sort(indexOfSymbol); // сортируем, чтобы правильно в нужном порядке добавлялся symbol

        StringBuilder newString = new StringBuilder();
        int table = massage.length() / (len * high - keyHoles.length / 2); // сколько таблиц целиком займёт сообщение
        for (int part = 0; part <= table; part++) {
            String str; // строка, которая будет в одной таблице
            if (part == table) {
                str = massage.substring((len * high - keyHoles.length / 2) * part);
                if (str.isEmpty()) {
                    break;
                }
            } else {
                str = massage.substring((len * high - keyHoles.length / 2) * part, (len * high - keyHoles.length / 2) * (part + 1)); // добавляем символы, которые поместятся в одну таблицу
            }

            for (int index : indexOfSymbol) {
                if (index > str.length()) break;
                str = str.substring(0, index) + symbol + str.substring(index);
            }
            newString.append(str);
        }
        newString.append(symbol);
        return newString.toString();
    }


    public static String crypt(String massage, int[] key, int keyLine, int[] keyHoles) {
        StringBuilder code = new StringBuilder();
        massage = changeMassage(massage, keyHoles, key.length, keyLine);
        char symbol = massage.charAt(massage.length() - 1);
        massage = massage.substring(0, massage.length() - 1);

        int countTable = (massage.length() / (key.length * keyLine));
        for (int startLine = 0; startLine <= countTable; startLine++) {
            for (int column = 1; column <= key.length; column++) {
                for (int line = startLine * keyLine; line < (startLine * keyLine) + keyLine; line++) {
                    if (code.toString().length() == massage.length()) {
                        return code.toString();
                    }
                    if (line * key.length + getIndex(key, column) >= massage.length()) {
                        break;
                    }
                    char letter = massage.charAt(line * key.length + getIndex(key, column));
                    if (letter == symbol) continue; // если 'дыра', то просто переходим к следующему символу
                    code.append(letter);
                }
            }
        }
        return code.toString();
    }


    public static String decrypt(String code, int[] key, int keyLine, int[] keyHoles) {
        StringBuilder massage = new StringBuilder();
        int[] lenColumn = new int[key.length]; // кол-во символов в каждом столбце по порядку в полностью заполненой таблице
        for (int column = 0; column < key.length; column++) {
            lenColumn[column] = keyLine;
            for (int x = 0; x < keyHoles.length; x += 2) {
                if (keyHoles[x] == column + 1) lenColumn[column] -= 1;
            }
        }

        int countTable = (code.length() / Arrays.stream(lenColumn).sum());
        for (int table = 0; table <= countTable; table++) {
            if (table == countTable) { // когда есть остаток, нужно изменить lenColumn
                int lenCode = code.length() - massage.length(); // кол-во букв, не заполненной до конца таблицы
                int lenInTable = lenInTable(lenCode, keyHoles, key.length); // сколько символов с учётом дыр займут оставшиеся буквы
                for (int column = 0; column < key.length; column++) {
                    int countLetterInColumn = lenInTable / key.length + ((lenInTable % key.length >= column+1) ? 1 : 0);
                    // теперь уменьшим значения в lenColumn, если в столбце есть дыры, выше или в данной строке
                    int delta1 = countHolesUp(column+1, countLetterInColumn, keyHoles);
                    int delta2 = (isHole(column+1, countLetterInColumn, keyHoles) ? 1 : 0);
                    countLetterInColumn -= (delta1 + delta2);
                    lenColumn[column] = countLetterInColumn;
                }
            }
            for (int line = 0; line < keyLine; line++) {
                for (int start : key) {
                    if (isHole(getIndex(key, start) + 1, line + 1, keyHoles)) continue; // если дыра, то пропускаем, чтобы не добавить не ту букву

                    int index = (key.length * keyLine - keyHoles.length / 2) * table; // определяем индекс, с которого начинается текущая таблица
                    for (int i = 1; i < start; i++) { // добавляем кол-во символов каждого столбца, символы которого записаны раньше в шифрованном сообщении
                        index += lenColumn[getIndex(key, i)];
                    }
                    index += line - countHolesUp(getIndex(key, start) + 1, line + 1, keyHoles);
                    massage.append(code.charAt(index));
                    if (massage.length() == code.length()) {
                        return massage.toString();
                    }
                }
            }
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
                System.out.println("Введите координаты неиспользуемых ячеек(1-е число - порядковый номер столбца(слева на право), 2-е - порядковый номер строки(сверху вниз))");
                enter = scanner.nextLine();
                count = (int) enter.chars().filter(ch -> ch == ' ').count() + 1; // кол-во введённых чисел
                // не может быть нечётное число, хотя бы одна "дыра" должна быть, хотя бы 2 обычные ячейки должны быть
                if ((count % 2 != 0) || (count/2 < 1) || (count/2 >= (key.length * keyLine - 1))) {
                    throw new IllegalArgumentException(START_COLOR + RED + "Невозможное число координат" + FINAL_COLOR);
                }

                int[] keyHoles = new int[count];
                i = -1;
                int aldX = -1;
                int aldY = -1;
                while (i + 1 < enter.split(" ").length) {
                    int x = Integer.parseInt(enter.split(" ")[++i]);
                    int y = Integer.parseInt(enter.split(" ")[++i]);
                    if (x < 1 || x > key.length ||
                        y < 1 || y > keyLine) {
                        throw new IllegalArgumentException(START_COLOR + RED + "Координата вне границы таблицы" + FINAL_COLOR);
                    }
                    if (x == aldX && y == aldY) throw new IllegalArgumentException(START_COLOR + RED + "Не должно быть 2-х ячеек с одинаковыми координатами" + FINAL_COLOR);
                    keyHoles[i - 1] = x;
                    keyHoles[i] = y;
                    aldX = x;
                    aldY = y;
                }

                System.out.println("1. Шифровать\n2. Расшифровать");
                enter = scanner.nextLine();
                switch (enter) {
                    case "1" -> {
                        System.out.println("Введите текст:");
                        System.out.println(crypt(scanner.nextLine(), key, keyLine, keyHoles));
                    }
                    case "2" -> {
                        System.out.println("Введите зашифрованное сообщение:");
                        System.out.println(decrypt(scanner.nextLine(), key, keyLine, keyHoles));
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
