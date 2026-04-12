import java.util.Scanner;

/**
 <h1>Многоалфавитная подстановка </h1>
 <p>Принцип как в одноалфавитной подстановке, но 1-й символ кодируется через alphabetOne, 2-й через alphabetTwo, 3-й через alphabetThree, и так по кругу</p>
 */
public class MultiAlphabeticSubstitution {
    public static final String START_COLOR = String.valueOf((char) 27);
    public static final String FINAL_COLOR = ((char) 27 + "[0m");
    public static final String RED = "[31m";
    // АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ;:,.!?'-"
    //  !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstu
    // )*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~
    // \|/;:,.!?9876543210'qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAPOIUYTREWQЙЦУКЕНГШЩЗХЪФЫ
    static char[]   alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ;:,.!?'-\"".toCharArray(); // алфавит от 'А' до 'я' + цифры + ;:,.!?'-"
    static char[]   alphabetOne = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu".toCharArray();
    static char[]   alphabetTwo = ")*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray();
    static char[]   alphabetThree = "\\|/;:,.!?9876543210'qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAPOIUYTREWQЙЦУКЕНГШЩЗХЪФЫ".toCharArray();
    static char[][] cryptAlphabets = {alphabetOne, alphabetTwo, alphabetThree};


    public static int indexInAlphabet(char symbol, char[] alphabet) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == symbol) {
                return i;
            }
        }
        return -1;
    }


    public static String crypt(String massage, int key) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < massage.length(); i++) {
            int indexLetter = indexInAlphabet(massage.charAt(i), alphabet); // индекс буквы в исходном алфавите
            if (indexLetter == -1) {
                throw new IllegalArgumentException(START_COLOR + RED + "В тексте использованы символы, которых нет в алфавите для ввода" + FINAL_COLOR);
            }
            indexLetter += key % alphabet.length;
            if (indexLetter < 0) {
                indexLetter += alphabet.length;
            } else if (indexLetter >= alphabet.length) {
                indexLetter -= alphabet.length;
            }
            code.append(cryptAlphabets[i % cryptAlphabets.length][indexLetter]);
        }
        return code.toString();
    }


    public static String decrypt(String code, int key) {
        StringBuilder massage = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            int indexLetter = indexInAlphabet(code.charAt(i), cryptAlphabets[i % cryptAlphabets.length]); // индекс буквы в алфавите для шифрования
            if (indexLetter == -1) {
                throw new IllegalArgumentException(START_COLOR + RED + "Такого зашифрованного сообщения быть не может" + FINAL_COLOR);
            }
            indexLetter -= key % alphabet.length;
            if (indexLetter < 0) {
                indexLetter += alphabet.length;
            } else if (indexLetter >= alphabet.length) {
                indexLetter -= alphabet.length;
            }
            massage.append(alphabet[indexLetter]);
        }
        return massage.toString();
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Алфавит для ввода:");
        for (char c : alphabet) System.out.print(c + " ");
        System.out.println("\nКоличество символов в алфавите: " + alphabet.length);
        for (int i = 0; i < cryptAlphabets.length; i++) {
            System.out.println((i + 1) + "-й алфавит для шифрования:");
            for (char c : cryptAlphabets[i]) System.out.print(c + " ");
            System.out.println();
        }

        String enter = "";
        while (!enter.equals("end")) {
            System.out.println("1. Шифровать\n2. Расшифровать");
            enter = scanner.nextLine();
            switch (enter) {
                case "1" -> {
                    System.out.println("Введите ключ(целое число):");
                    int key = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(crypt(scanner.nextLine(), key));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.println("Введите ключ(целое число):");
                    int key = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(decrypt(scanner.nextLine(), key));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
