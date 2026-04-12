import java.util.Scanner;

/**
 <h1>Гаммирование </h1>
 */
public class Gamming {
    public static final String START_COLOR = String.valueOf((char) 27);
    public static final String FINAL_COLOR = ((char) 27 + "[0m");
    public static final String RED = "[31m";
                           // АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz()*+,-./0123456789:;<=>?@ [\]!_'"{|}
    static char[] alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz()*+,-./0123456789:;<=>?@ [\\]!_'\"{|}".toCharArray();


    public static int getIndex(char symbol, char[] alphabet) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == symbol) {
                return i;
            }
        }
        return -1;
    }


    public static String crypt(String massage, String key) {
        StringBuilder code = new StringBuilder();
        for (int index = 0; index < massage.length(); index++) {
            int newIndex = getIndex(massage.charAt(index), alphabet) ^ getIndex(key.charAt(index % key.length()), alphabet);
            code.append(alphabet[newIndex]);
        }
        return code.toString();
    }
    

    public static void main(String[] args) {
        System.out.println("Алфавит для ввода: ");
        for (char symbol : alphabet) System.out.print(symbol);
        System.out.println("\nДлина алфавита: " + alphabet.length);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Введите ключ(набор символов алфавита)");
                String key = scanner.nextLine();
                if (key.isEmpty()) break;
                for (char symbol : key.toCharArray()) {
                    if (getIndex(symbol, alphabet) == -1) throw new IllegalArgumentException(START_COLOR + RED + "В ключе недопустимый символ: " + symbol + FINAL_COLOR);
                }
                System.out.println("Введите текст:");
                String text = scanner.nextLine();
                for (char symbol : text.toCharArray()) {
                    if (getIndex(symbol, alphabet) == -1) throw new IllegalArgumentException(START_COLOR + RED + "В тексте недопустимый символ: " + symbol + FINAL_COLOR);
                }
                System.out.println(crypt(text, key));
            } catch (IllegalArgumentException e ) {
                System.out.println(e.getMessage());
            }
        }

    }
}
