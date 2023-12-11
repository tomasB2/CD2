package org.example.pos;

import org.json.JSONObject;

import java.util.Scanner;

public class Product {
    private String data;
    private int codigoProduto;
    private String nomeProduto;
    private int quant;
    private double precoUnitario;
    private double total;
    private double iva;

    public Product(String data, int codigoProduto, String nomeProduto, int quant, double precoUnitario, double iva) {
        this.data = data;
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.quant = quant;
        this.precoUnitario = precoUnitario;
        this.total = quant * precoUnitario;
        this.iva = iva;
    }

    public String getData() {
        return data;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuant() {
        return quant;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getTotal() {
        return total;
    }

    public double getIva() {
        return iva;
    }

    public static Product createProductFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the product data:");
        System.out.print("Data: ");
        String data = scanner.nextLine();

        System.out.print("CodigoProduto: ");
        int codigoProduto = scanner.nextInt();
        scanner.nextLine();

        System.out.print("NomeProduto: ");
        String nomeProduto = scanner.nextLine();

        System.out.print("Quant: ");
        int quant = scanner.nextInt();

        System.out.print("PreçoUnitário: ");
        double precoUnitario = scanner.nextDouble();

        System.out.print("Iva: ");
        double iva = scanner.nextDouble();

        return new Product(data, codigoProduto, nomeProduto, quant, precoUnitario, iva);
    }

    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("data", data);
        json.put("codigoProduto", codigoProduto);
        json.put("nomeProduto", nomeProduto);
        json.put("quant", quant);
        json.put("precoUnitario", precoUnitario);
        json.put("total", total);
        json.put("iva", iva);

        return json.toString();
    }

    public static Product fromJsonString(String jsonString) {
        JSONObject json = new JSONObject(jsonString);

        String data = json.getString("data");
        int codigoProduto = json.getInt("codigoProduto");
        String nomeProduto = json.getString("nomeProduto");
        int quant = json.getInt("quant");
        double precoUnitario = json.getDouble("precoUnitario");
        double iva = json.getDouble("iva");

        return new Product(data, codigoProduto, nomeProduto, quant, precoUnitario, iva);
    }
}
