package simpleexample;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleFunctions {
    /**
     * Saluda a una persona concatenando su nombre al prefijo "Hola, ".
     * @param name Nombre de la persona.
     * @return Cadena formateada con el saludo.
     */
    public String helloHuman(String name) {
        return "Hola, " + name + "!";
    }

    /**
     * Verifica si un número entero es par.
     * @param number Número a evaluar.
     * @return true si es par, false si es impar.
     */
    public boolean checkEvenNumber(int number) {
        return number % 2 == 0;
    }

    /**
     * Determina si un año es bisiesto según el calendario gregoriano.
     * @param year Año a verificar.
     * @return true si cumple las condiciones de año bisiesto, false en caso contrario o si es negativo.
     */
    public boolean isLeapYear(int year) {
        if (year <= 0) return false;
        if (year % 400 == 0) return true;
        if (year % 100 == 0) return false;
        return year % 4 == 0;
    }

    /**
     * Invierte una cadena de texto y la convierte completamente a mayúsculas.
     * @param input Cadena original.
     * @return Cadena invertida en mayúsculas, null si el input es null, o vacío si el input es vacío.
     */
    public String reverseAndUpper(String input){
        if (input == null) return null;
        if (input.isEmpty()) return "";
        return new StringBuilder(input)
                .reverse()
                .toString()
                .toUpperCase();
    }

    /**
     * Calcula qué porcentaje representa una parte sobre un total.
     * @param part Valor parcial.
     * @param total Valor base (total).
     * @return Resultado del cálculo porcentual.
     * @throws ArithmeticException Si el total es cero.
     */
    public double calculatePercentage(double part, double total) {
        if (total == 0){
            throw new ArithmeticException("Total cannot be zero");
        }
        return (part / total) * 100;
    }

    /**
     * Realiza operaciones aritméticas básicas entre dos números.
     * @param a Primer operando.
     * @param operation Símbolo de la operación ("*", "+", "-", "%", "/").
     * @param b Segundo operando.
     * @return Resultado de la operación seleccionada.
     * @throws ArithmeticException Si se intenta dividir o calcular módulo con denominador cero.
     * @throws IllegalArgumentException Si la operación no está soportada.
     */
    public double calculator(double a, String operation, double b) {
        return switch (operation) {
            case "*" -> a * b;
            case "+" -> a + b;
            case "-" -> a - b;
            case "%" -> {
                if (b == 0) throw new ArithmeticException("Denominator cannot be Zero");
                yield a % b;
            }
            case "/" -> {
                if (b == 0) throw new ArithmeticException("Denominator cannot be Zero");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Operation " + operation + " not supported");
        };
    }

    /**
     * Encuentra el valor numérico más alto dentro de un arreglo de enteros.
     * @param numbers Arreglo de entrada.
     * @return El valor máximo encontrado.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public int findMax(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        int max = numbers[0];
        for (int num : numbers) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    /**
     * Valida si una cadena cumple con un formato básico de correo electrónico mediante Regex.
     * @param email Cadena a validar.
     * @return true si el formato es válido, false si es nulo o inválido.
     */
    public boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    /**
     * Calcula el precio final aplicando descuentos progresivos basados en la cantidad adquirida.
     * @param price Precio unitario del producto.
     * @param quantity Cantidad comprada.
     * @return Precio total tras aplicar el descuento correspondiente (5%, 10% o 20%).
     */
    public double applyDiscount(double price, int quantity) {
        if (price < 0 || quantity < 0) return 0;

        double factor = 1.0;
        if (quantity >= 100) {
            factor = 0.80;
        } else if (quantity >= 50) {
            factor = 0.90;
        } else if (quantity >= 10){
            factor = 0.95;
        }
        return price * quantity * factor;
    }

    /**
     * Filtra una lista de nombres devolviendo solo aquellos que comienzan con una letra específica.
     * @param names Lista de nombres a filtrar.
     * @param letter Carácter inicial deseado.
     * @return Lista con los nombres que coinciden (ignora mayúsculas/minúsculas y omite nulos/vacíos).
     */
    public List<String> filterByLetter(List<String> names, char letter) {
        if (names == null) return List.of();
        char lowerLetter = Character.toLowerCase(letter);

        return names.stream()
                .filter(n -> n != null && !n.isEmpty())
                .filter(n -> n.toLowerCase().startsWith(String.valueOf(lowerLetter)))
                .collect(Collectors.toList());
    }
}
