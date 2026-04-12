import java.util.Arrays;
import java.util.Scanner;

/**
 <h1>Многоконтурная подстановка</h1>
 */
public class MultiContoursSubstitution {
    public static final String START_COLOR = String.valueOf((char) 27);
    public static final String FINAL_COLOR = ((char) 27 + "[0m");
    public static final String RED = "[31m";


    static char[] alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя0123456789 ;:,.!?'-\"".toCharArray(); // алфавит от 'А' до 'я' + цифры + ;:,.!?'-"

    static char[]     alphabetOne1 = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu".toCharArray();
    static char[]     alphabetTwo1 = ")*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~".toCharArray();
    static char[]     alphabetThree1 = "\\|/;:,.!?9876543210'qwertyuiopasdfghjklzxcvbnmMNBVCXZLKJHGFDSAPOIUYTREWQЙЦУКЕНГШЩЗХЪФЫ".toCharArray();
    static char[][]   contourOne = {alphabetOne1, alphabetTwo1, alphabetThree1};

    static char[]     alphabetOne2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯа".toCharArray();
    static char[]     alphabetTwo2 = "яфйчыцсвумакипетрньогблшюдщжзэхъЯФЙЧЫЦСВУМАКИПЕТРНЬОГБЛШЮДЩЖЗЭХЪzaqxswcdevfrbgtnhymjuk".toCharArray();
    static char[][]   contourTwo = {alphabetOne2, alphabetTwo2};

    static char[]     alphabetOne3 = "0А1Б2В3Г4Д5Е6Ё7Ж8З9И*Й%К@Л&М\\Н|О/П^Р#С[ТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя".toCharArray();
    static char[]     alphabetTwo3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzЯабвгдеёжзийклмнопрстуфхцч_ш№щ?ъ!ы".toCharArray();
    static char[]     alphabetThree3 = "ЪХЗЩШГНЕКУЦЙЭЖДЛОРПАВЫФЮБЬТИМСЧЯъхзщшгнекуцйэждлорпа=в<ы>ф-ю+б)ь(*т^и%м$с@ч\"яplmkonjib".toCharArray();
    static char[][]   contourThree = {alphabetOne3, alphabetTwo3, alphabetThree3};

    static char[][][] contours = {contourOne, contourTwo, contourThree};


    public static int indexInAlphabet(char symbol, char[] alphabet) {
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == symbol) {
                return i;
            }
        }
        return -1;
    }


    public static String crypt(String massage, int key, int[] periods) {
        StringBuilder code = new StringBuilder();
        int indexContour = 0;
        int indexAlphabet = -1;
        for (int index = 0; index < massage.length(); index++) {
            indexAlphabet++;
            indexAlphabet %= contours[indexContour].length;
            int indexInLenContour = (index % (Arrays.stream(periods).sum()));
            int len = 0;
            for (int period : periods) { // меняем контур если прошёл период
                len += period;
                if (indexInLenContour == len || (indexInLenContour == 0 && index != 0)) {
                    indexContour++;
                    indexContour %= periods.length;
                    indexAlphabet = 0;
                    break;
                }
            }
            int indexLetter = indexInAlphabet(massage.charAt(index), alphabet); // индекс текущего символа в исходном алфавите
            if (indexLetter == -1) {
                throw new IllegalArgumentException(START_COLOR + RED + "В тексте использованы символы, которых нет в исходном алфавите: " + massage.charAt(index) + FINAL_COLOR);
            }
            indexLetter += key % alphabet.length;
            if (indexLetter < 0) {
                indexLetter += alphabet.length;
            } else if (indexLetter >= alphabet.length) {
                indexLetter -= alphabet.length;
            }
            code.append(contours[indexContour][indexAlphabet][indexLetter]);
        }
        return code.toString();
    }


    public static String decrypt(String code, int key, int[] periods) {
        StringBuilder massage = new StringBuilder();
        int indexContour = 0;
        int indexAlphabet = -1;
        for (int index = 0; index < code.length(); index++) {
            indexAlphabet++;
            indexAlphabet %= contours[indexContour].length;
            int indexInLenContour = (index % (Arrays.stream(periods).sum()));
            int len = 0;
            for (int period : periods) { // меняем контур если прошёл период
                len += period;
                if (indexInLenContour == len || (indexInLenContour == 0 && index != 0)) {
                    indexContour++;
                    indexContour %= periods.length;
                    indexAlphabet = 0;
                    break;
                }
            }
            int indexLetter = indexInAlphabet(code.charAt(index), contours[indexContour][indexAlphabet]); // индекс текущего символа в текущем алфавите
            if (indexLetter == -1) {
                throw new IllegalArgumentException(START_COLOR + RED + "В тексте использованы символы, которых нет в алфавите для шифрования: " + code.charAt(index) + FINAL_COLOR);
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
        for (int countContours = 0; countContours < contours.length; countContours++) {
            System.out.println((countContours + 1) + "-й контур");
            for (int countAlphabet = 0; countAlphabet < contours[countContours].length; countAlphabet++) {
                for (char symbol : contours[countContours][countAlphabet]) System.out.print(symbol);
                System.out.println();
            }
            System.out.println();
        }

        String enter = "";
        while (!enter.equals("end")) {
            System.out.println("1. Шифровать\n2. Расшифровать");
            enter = scanner.nextLine();
            int[] periods = new int[contours.length];
            for (int i = 0; i < contours.length; i++) {
                System.out.println("Введите период применения " + (1+i) + "-го контуров через пробел");
                int periodForContour = Integer.parseInt(scanner.nextLine());
                periods[i] = periodForContour;
            }

            switch (enter) {
                case "1" -> {
                    System.out.println("Введите ключ(целое число):");
                    int key = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(crypt(scanner.nextLine(), key, periods));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> {
                    System.out.println("Введите ключ(целое число):");
                    int key = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите текст:");
                    try {
                        System.out.println(decrypt(scanner.nextLine(), key, periods));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
