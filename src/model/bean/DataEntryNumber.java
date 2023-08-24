/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.regex.Pattern;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author diego
 */
public class DataEntryNumber {

    public static boolean isCpfCnpj(String cpfCnpj, String typeToCheck) {
        if (cpfCnpj.contains(".")) {
            cpfCnpj = cpfCnpj.replaceAll("\\.", "");
        }

        if (cpfCnpj.contains("-")) {
            cpfCnpj = cpfCnpj.replaceAll("-", "");
        }

        if (cpfCnpj.contains("/")) {
            cpfCnpj = cpfCnpj.replaceAll("/", "");
        }

        /*
         * if(cpfCnpj.length() == 11) {
         * return isCPF(cpfCnpj);
         * } else {
         * return isCNPJ(cpfCnpj);
         * }
         */
        if (typeToCheck.equals("cpf")) {
            return isCPF(cpfCnpj);
        } else {
            return isCNPJ(cpfCnpj);
        }
    }

    public static boolean isCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000")
                || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return (false);
        }

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }
            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String formatCpfCnpj(String cpfCnpj) {
        if (cpfCnpj.contains(".")) {
            cpfCnpj = cpfCnpj.replaceAll(""
                    + "\\.", "");
        }

        if (cpfCnpj.contains("-")) {
            cpfCnpj = cpfCnpj.replaceAll("-", "");
        }

        if (cpfCnpj.contains("/")) {
            cpfCnpj = cpfCnpj.replaceAll("/", "");
        }

        return cpfCnpj;
    }

    public static String printCPF(String cpf) {
        return (cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9, 11));
    }

    public static boolean isCNPJ(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
                || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333")
                || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555")
                || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777")
                || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999")
                || (CNPJ.length() != 14)) {
            return (false);
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig13 = '0';
            } else {
                dig13 = (char) ((11 - r) + 48);
            }

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            if ((r == 0) || (r == 1)) {
                dig14 = '0';
            } else {
                dig14 = (char) ((11 - r) + 48);
            }

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) {
                return (true);
            } else {
                return (false);
            }
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    public static String printCNPJ(String CNPJ) {
        // máscara do CNPJ: 99.999.999.9999-99
        try {
            return (CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "."
                    + CNPJ.substring(5, 8) + "/" + CNPJ.substring(8, 12) + "-"
                    + CNPJ.substring(12, 14));
        } catch (Exception ex) {
            return "";
        }
    }

    public static String printCpfCnpj(String cpfCpnj) {
        if (cpfCpnj == null || cpfCpnj.isEmpty())
            return "";

        if (cpfCpnj.length() == 11) {
            return printCPF(cpfCpnj);
        } else {
            return printCNPJ(cpfCpnj);
        }
    }

    public static String getPriceFromDouble(double price, int decimalPlaces) {
        BigDecimal b = new BigDecimal(price).setScale(decimalPlaces,
                RoundingMode.HALF_UP);

        String decimals = "";
        for (int i = 0; i < decimalPlaces; i++) {
            decimals += "0";
        }

        DecimalFormat df = new DecimalFormat("###,###." + decimals);
        // return (b.toString().contains(".") ? b.toString().replaceAll("\\.", ",") :
        // b.toString());
        if (price > 1D) {
            return df.format(b.doubleValue());
        } else {
            return (b.toString().contains(".") ? b.toString().replaceAll("\\.", ",") : b.toString());
        }
    }

    public static Double getPriceFromString(String price, int decimalPlaces) {
        if (price.isEmpty()) {
            return 0D;
        }

        if (price.contains(",")) {
            price = price.replaceAll("\\.", "");
            price = price.replaceAll(",", ".");
        }

        BigDecimal b = new BigDecimal(price).setScale(decimalPlaces,
                RoundingMode.HALF_UP);

        return b.doubleValue();
    }

    /**
     * Formata o número de telefone (##)#####-####
     *
     * @param number Número de telefone
     * @return
     */
    public static String formatPhone(String number) {

        if (number == null) {
            return "";
        }

        if (number.isEmpty()) {
            return "";
        }

        if (number.length() < 10 || number.length() > 11) {
            return number;
        }

        number = number.replaceAll("\\D", "");
        String mask = "";

        if (number.length() > 10) {
            mask = "(##) #####-####";
        } else {
            mask = "(##) ####-####";
        }
        try {
            MaskFormatter numberFormater = new MaskFormatter(mask);
            JFormattedTextField n = new JFormattedTextField(numberFormater);
            n.setText(number);
            return n.getText();
        } catch (java.text.ParseException e) {
            return number;
        }

    }

    /**
     * Remove tudo o que não é número
     *
     * @param toRemoveCharacters String para ser removida os caracteres
     * @return String somente com números
     */
    public static String getNumbersFromString(String toRemoveCharacters) {
        return toRemoveCharacters == null ? "" : toRemoveCharacters.replaceAll("\\D", "");
    }

    public static Integer verifyIntegerInput(String number) throws Exception {
        if (!Pattern.matches("^[0-9]*\\d*$", number)) {
            throw new Exception("Digite apenas números!");
        } else {
            return Integer.valueOf(number);
        }
    }

    public static Double verifyDoubleInput(String stringToVerify) throws Exception {
        if (!Pattern.matches("^[.,0-9]*\\d*$", stringToVerify)) {
            throw new Exception("Digite apenas números!");
        } else {
            return DataEntryNumber.getPriceFromString(stringToVerify, 2);
        }
    }

    public static Double setDoubleDecimalPlaces(double value, int decimalPlaces) {
        return value == 0 ? 0D
                : new BigDecimal(value).setScale(decimalPlaces,
                        RoundingMode.HALF_UP).doubleValue();
    }
}
