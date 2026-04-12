import java.util.Scanner;

/**
<h1>Одноалфавитная подстановка </h1>
 <p>Каждый символ сообщения заменяется на символ из этого алфавита символов, но находящийся под индексом,
 увеличенным на значение ключа, которое ввёл пользователь.</p>
 <h3>Пример</h3>
 cообщение: 'кот'</br>
 ключ: 2</br>
 Посимвольно:</br>
 'к' - индекс 44 в алфавите (индексация с нуля), индекс символа в криптограмме будет 44 + 2 = 46, под этим индексом символ 'м'</br>
 'о' - индекс 48, 47 + 2 = 50, индекс 49 у символа 'р'</br>
 'т' - индекс 52, 52 + 2 = 54, индекс 54 у символа 'ф'</br>
 Криптограмма: 'мрф'
 */
public class MonoAlphabeticSubstitution {
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


    public static String crypt(String massage, int key) {
        StringBuilder code = new StringBuilder();
        for (char letter : massage.toCharArray()) {
            int indexInAlphabet = indexInAlphabet(letter);
            if (indexInAlphabet == -1) {
                throw new IllegalArgumentException("В тексте использованы символы, которых нет в алфавите");
            }
            indexInAlphabet += key % alphabet.length;
            if (indexInAlphabet < 0) {
                indexInAlphabet += alphabet.length;
            } else if (indexInAlphabet > alphabet.length - 1) {
                indexInAlphabet -= alphabet.length;
            }
            code.append(alphabet[indexInAlphabet]);
        }
        return code.toString();
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String enter = "";
        for (char c : alphabet) System.out.print(c + " ");
        System.out.println("\nКоличество символов в алфавите: " + alphabet.length);
        while (!enter.equals("end")) {
            System.out.println("Введите ключ(целое число):");
            int key = Integer.parseInt(scanner.nextLine());
            System.out.println("1. Шифровать\n2. Расшифровать");
            enter = scanner.nextLine();
            switch (enter) {
                case "1" -> {
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(crypt(scanner.nextLine(), key));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(crypt(scanner.nextLine(), -key));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}

