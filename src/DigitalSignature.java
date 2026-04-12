import java.math.BigInteger;
import java.util.*;

/**
<h1>Цифровая подпись RSA</h1>
 */
public class DigitalSignature {
    public static int lenOfNumber(long n) {
        int len = 0;
        long number = 1;
        while (n / number >= 1) {
            number *= 10;
            len++;
        }
        return len;
    }


    public static long nod(long a, long b) {
        long finalNumber;
        if (2 * Math.min(a, b) <= Math.max(a, b)) {
            finalNumber = Math.min(a, b);
        } else {
            finalNumber = Math.min(a, b) / 2;
        }
        for (long i = finalNumber; i > 1; i--) {
            if (a % i == 0 && b % i == 0) return i;
        }
        return 1;
    }


    public static List<Long> decompose(long n) {
        long div = 2;
        long power = 0;
        List<Long> list = new ArrayList<>();
        while (n != 1) {
            while (n % div != 0) {
                div++;
            }
            while (n % div == 0) {
                n /= div;
                power++;
            }
            list.add(div);
            list.add(power);
            power = 0;
        }
        return list;
    }


    public static long getD(long number, long mod) {
        long remains = 1 - number;
        long multiplier = 1;
        while (true) {
            remains = Math.floorMod(remains, mod);
            if (remains % number == 0) {
                multiplier += remains/number;
                return multiplier;
            }
            multiplier += (remains / number) + 1;
            remains -= number * ((remains / number) + 1);
        }
    }


    public static long modInPower(long multi, long mod, long power) {
        List<Long> multipliers = new ArrayList<>();
        BigInteger bMulti;
        while (Math.pow(multi, power) > mod && power > 0) {
            if (multi > mod) {
                multi %= mod;
            }
            if (power % 2 == 0) {
                power /= 2;
                bMulti = BigInteger.valueOf(multi);
                bMulti = bMulti.modPow(BigInteger.valueOf(2), BigInteger.valueOf(mod));
                multi = Long.parseLong(String.valueOf(bMulti));
            } else {
                multipliers.add(multi);
                power -= 1;
            }
        }
        long result = (long) Math.pow(multi, power);

        for (long i : multipliers) {
            i = i % mod;
            long max = Long.MAX_VALUE / result;
            if ((i) >= max) {
                BigInteger bi = new BigInteger(String.valueOf(result));
                bi = bi.multiply(BigInteger.valueOf(i));
                bi = bi.mod(BigInteger.valueOf(mod));
                result = Long.parseLong(String.valueOf(bi));
            } else {
                result *= i;
                result %= mod;
            }
        }
        return result;
    }


    public static String crypt(String massage, long n, long e) {
        StringBuilder code = new StringBuilder();
        for (char letter : massage.toCharArray()) {
            long codeNumber = modInPower(letter, n, e);
            String str = "0".repeat(lenOfNumber(n) - lenOfNumber(codeNumber));
            str += String.valueOf(codeNumber);
            code.append(str);
            System.out.println("'" + letter + "'" + " = " + (int) letter + " -> " + str);
        }
        return code.toString();
    }


    public static String decrypt(String code, long n, long d) {
        StringBuilder massage = new StringBuilder();
        int len = lenOfNumber(n);
        for (int index = 0; index < code.length(); index += len) {
            long codeNumber = Long.parseLong(code.substring(index, index + len));
            long symbolNumber = modInPower(codeNumber, n, d);
            char symbol = (char) symbolNumber;
            System.out.println(codeNumber + " -> " + symbolNumber + " = " + "'" + symbol + "'");
            massage.append(symbol);
        }
        return massage.toString();
    }


    public static void main(String[] args) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите действие: \n1. Зашифровать\n2. Расшифровать\n3. Проверить подпись");
            String enter = scanner.nextLine();
            if (enter.isEmpty()) break;
            switch (enter) {
                case "1" -> {
                    System.out.print("Введите простое число p: ");
                    long p = Long.parseLong(scanner.nextLine());
                    if (decompose(p).size() != 2 || decompose(p).get(1) != 1) {
                        System.out.println("Это не простое число");
                        continue;
                    }
                    System.out.print("Введите простое число q: ");
                    long q = Long.parseLong(scanner.nextLine());
                    if (decompose(q).size() != 2 || decompose(q).get(1) != 1) {
                        System.out.println("Это не простое число");
                        continue;
                    }
                    if (Long.MAX_VALUE / Math.max(p, q) + ((Long.MAX_VALUE % Math.max(p, q) == 0) ? 1 : 0)  < Math.min(p, q)) {
                        System.out.println("Слишком большие значения p и q");
                        continue;
                    }
                    long n = q * p;
                    if (n < 1105 ) {
                        System.out.println("Некорректное значение n (слишком малое произведение 'q' и 'p')");
                        continue;
                    }
                    System.out.println("Ваш открытый ключ n = " + n);
                    long euler = (p-1)*(q-1);
                    System.out.println("φ(n) = " + euler);
                    System.out.print("Введите ключ e: (такое простое число, что НОД(e, φ(n)) = 1): ");
                    long e = Long.parseLong(scanner.nextLine());
                    if (e < 2 || nod(e, euler) != 1 || e >= euler || decompose(e).size() != 2 || decompose(e).get(1) != 1) {
                        System.out.println("Некорректное значение e");
                        continue;
                    }
                    long d = getD(e, euler);
                    System.out.println("Ваш закрытый ключ d = " + d);
                    System.out.println("Введите сообщение:");
                    String massage = scanner.nextLine();
                    System.out.println("Шифрование:");
                    System.out.println(crypt(massage, n, e));
                    System.out.println("Создание подписи:");
                    System.out.println("Ваша подпись:\n" + crypt(massage, n, d));
                }
                case "2" -> {
                    System.out.print("Введите ключ n: ");
                    long n = Long.parseLong(scanner.nextLine());
                    System.out.print("Введите ключ d: ");
                    long d = Long.parseLong(scanner.nextLine());
                    System.out.println("Введите криптограмму:");
                    String code = scanner.nextLine();
                    System.out.println(decrypt(code, n, d));
                }
                case "3" -> {
                    System.out.print("Введите ключ n: ");
                    long n = Long.parseLong(scanner.nextLine());
                    System.out.print("Введите ключ e: ");
                    long e = Long.parseLong(scanner.nextLine());
                    System.out.println("Введите сообщение:");
                    String message = scanner.nextLine();
                    System.out.println("Введите подпись:");
                    String signature = scanner.nextLine();
                    System.out.println(decrypt(signature, n, e).equals(message));
                }
            }
        }
    }
}
