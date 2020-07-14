package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

interface Code {
    StringBuilder encrypt(StringBuilder sb, int key);
    StringBuilder decrypt(StringBuilder sb, int key);
}


class ShiftCode implements Code {

    @Override
    public StringBuilder encrypt(StringBuilder sb, int key) {
        int asciCode;
        char c;

        for (int i =0; i < sb.length(); i++) {
            if (sb.charAt(i) >= 97 && sb.charAt(i) <= 122) {
                asciCode = sb.charAt(i) + key;
                if (asciCode > 122) {
                    asciCode -= 26;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
            if (sb.charAt(i) >= 65 && sb.charAt(i) <= 90) {
                asciCode = sb.charAt(i) + key;
                if (asciCode > 90) {
                    asciCode -= 26;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
        }
        return sb;
    }

    @Override
    public StringBuilder decrypt(StringBuilder sb, int key) {
        int asciCode;
        char c;

        for (int i =0; i < sb.length(); i++) {
            if (sb.charAt(i) >= 97 && sb.charAt(i) <= 122) {
                asciCode = sb.charAt(i) - key;
                if (asciCode < 97) {
                    asciCode += 26;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
            if (sb.charAt(i) >= 65 && sb.charAt(i) <= 90) {
                asciCode = sb.charAt(i) - key;
                if (asciCode < 65) {
                    asciCode += 26;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
        }
        return sb;
    }
}


class UnicodeCode implements Code {

    @Override
    public StringBuilder encrypt(StringBuilder sb, int key) {
        int asciCode;
        char c;

        for (int i =0; i < sb.length(); i++) {
            if (sb.charAt(i) >= 32 && sb.charAt(i) <= 126) {
                asciCode = sb.charAt(i) + key;
                if (asciCode > 126) {
                    asciCode -= 93;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
        }
        return sb;
    }

    @Override
    public StringBuilder decrypt(StringBuilder sb, int key) {
        int asciCode;
        char c;

        for (int i =0; i < sb.length(); i++) {
            if (sb.charAt(i) >= 32 && sb.charAt(i) <= 126) {
                asciCode = sb.charAt(i) - key;
                if (asciCode < 32) {
                    asciCode += 93;
                }
                c = (char) asciCode;
                sb.setCharAt(i, c);
            }
        }
        return sb;
    }
}

class Crypting {
    private String algorithm;
    private Code code;
    private String mode;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        if ("unicode".equals(this.algorithm)) {
            this.code = new UnicodeCode();
        } else {
            this.code = new ShiftCode();
        }
    }

    public void code(StringBuilder sb, int key) {
        if (this.mode.equals("dec")) {
            this.code.decrypt(sb, key);
        } else {
            this.code.encrypt(sb, key);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = null;
        int key = 0;

        String mode = "";
        String keys;
        String alg = "";
        String text = "";
        String fileIn = "";
        String fileOut = "";
        boolean in = false;
        boolean out = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-mode")) {
                mode = args[i + 1];
                if (mode.length() == 0) {
                    mode = "enc";
                }
            }
            if (args[i].contains("-alg")) {
                alg = args[i + 1];
                if (alg.length() == 0) {
                    alg = "shift";
                }
            }
            if (args[i].contains("-key")) {
                keys = args[i + 1];
                if (keys.length() > 0) {
                    key = Integer.parseInt(keys);
                } else {
                    key = 0;
                }
            }
            if (args[i].contains("-data")) {
                text = args[i + 1];
            }

            if (args[i].contains("-in")) {
                fileIn = args[i + 1];
            }
            if (args[i].contains("-out")) {
                fileOut = args[i + 1];
                out = true;
            }
        }


        if (text.length() == 0 && fileIn.length() > 0) {
            in = true;
        }

        if (in) {
            try {
                File file1 = new File(fileIn);
                Scanner scanIn = new Scanner(file1);
                sb = new StringBuilder();
                while (scanIn.hasNext()) {
                    sb.append(scanIn.nextLine());
                }
                scanIn.close();
            } catch (Exception e) {
                System.out.println("Error" + e.getMessage());
            }
        } else {
            sb = new StringBuilder(text);
        }

        Crypting crypting = new Crypting();
        crypting.setMode(mode);
        crypting.setAlgorithm(alg);
        crypting.code(sb, key);

        if (out) {
            try {
                FileWriter writer = new FileWriter(fileOut);
                writer.write(String.valueOf(sb));
                writer.close();
            } catch (Exception ee) {
                System.out.println("Error" + ee.getMessage());
            }
        } else {
            System.out.println(sb);
        }
    }
}

