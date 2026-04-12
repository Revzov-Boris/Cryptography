import java.util.Scanner;

/**
<h1>Многоалфавитная подстановка по таблице Виженера </h1>
 */
public class VisionTableSubstitution {
    public static final String START_COLOR = String.valueOf((char) 27);
    public static final String FINAL_COLOR = ((char) 27 + "[0m");
    public static final String RED = "[31m";
    // АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ;:,.!?'-"
    static char[] alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ;:,.!?'-\"".toCharArray();


    public static int indexInAlphabet(char symbol) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == symbol) {
                return i;
            }
        }
        return -1;
    }


    public static String crypt(String massage, String key, int verb) {
        StringBuilder code = new StringBuilder();
        int indexInKey = -1; // индекс текущего символа ключа
        for (char letter : massage.toCharArray()) {
            if (++indexInKey == key.length()) indexInKey = 0;
            int indexLetter = indexInAlphabet(letter);
            if (indexLetter == -1) {
                throw new IllegalArgumentException(START_COLOR + RED + "В тексте использованы недопустимые символы" + FINAL_COLOR);
            }
            int indexInAlphabet = indexInAlphabet(key.charAt(indexInKey)); // индекс текущего символа ключа в алфавите
            int newIndex = (indexLetter + indexInAlphabet * verb); // если verb = 1, то шифрация, если verb = -1, то дешифрация
            if (newIndex >= alphabet.length) {
                newIndex -= alphabet.length;
            } else if (newIndex < 0) {
                newIndex += alphabet.length;
            }
            code.append(alphabet[newIndex]);
        }
        return code.toString();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Поглядите:");
        for (int start = 0; start < alphabet.length; start++) {
            for (int i = 0; i < alphabet.length; i++) {
                int j = i;
                if (j + start >= alphabet.length) j -= alphabet.length;
                System.out.print(alphabet[j + start] + " ");
            }
            System.out.println();
        }

        String enter = "";
        while (!enter.equals("end")) {
            System.out.println("1. Шифровать\n2. Расшифровать");
            enter = scanner.nextLine();
            switch (enter) {
                case "1" -> {
                    try {
                        System.out.println("Введите ключ(строка из символов алфавита):");
                        String key = scanner.nextLine();
                        for (char symbol : key.toCharArray()) {
                            int indexInAlphabet = indexInAlphabet(symbol);
                            if (indexInAlphabet == -1) {
                                throw new IllegalArgumentException(START_COLOR + RED + "Недопустимый ключ" + FINAL_COLOR);
                            }
                        }

                        System.out.println("Введите текст:");
                        System.out.println(crypt(scanner.nextLine(), key, 1));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> {
                    try {
                        System.out.println("Введите ключ(строка из символов алфавита):");
                        String key = scanner.nextLine();
                        for (char symbol : key.toCharArray()) {
                            int indexInAlphabet = indexInAlphabet(symbol);
                            if (indexInAlphabet == -1) {
                                throw new IllegalArgumentException(START_COLOR + RED + "Недопустимый ключ" + FINAL_COLOR);
                            }
                        }
                        System.out.println("Введите текст:");
                        System.out.println(crypt(scanner.nextLine(), key, -1));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
