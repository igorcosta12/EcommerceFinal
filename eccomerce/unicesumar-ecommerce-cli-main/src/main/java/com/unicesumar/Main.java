package com.unicesumar;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.Sale;
import com.unicesumar.entities.User;
import com.unicesumar.paymentMethods.PaymentMethod;
import com.unicesumar.paymentMethods.PaymentMethodFactory;
import com.unicesumar.paymentMethods.PaymentType;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.SaleRepository;
import com.unicesumar.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        String url = "jdbc:sqlite:database.sqlite";
        Connection conn = null;


        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("Conexão com o banco de dados estabelecida.");
            } else {
                System.out.println("Falha na conexão com o banco de dados.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return;
        }

        UserRepository userRepo = new UserRepository(conn);
        ProductRepository productRepo = new ProductRepository(conn);
        SaleRepository saleRepo = new SaleRepository(conn);

        System.out.print("Digite o Email do usuário: ");
        String email = scanner.nextLine();
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }
        System.out.println("Usuário encontrado: " + user.getName());

        System.out.print("Digite os UUIDs dos produtos (separados por vírgula): ");
        String[] idsTexto = scanner.nextLine().split(",");
        List<Product> produtos = new ArrayList<>();

        for (String idTxt : idsTexto) {
            try {
                UUID uuid = UUID.fromString(idTxt.trim());
                Product p = productRepo.findByUuid(uuid).orElse(null);
                if (p != null) {
                    produtos.add(p);
                } else {
                    System.out.println("Produto com UUID " + uuid + " não encontrado.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("UUID inválido: " + idTxt);
            }
        }

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto válido foi informado.");
            return;
        }

        System.out.println("Produtos encontrados:");
        for (Product p : produtos) {
            System.out.println("- " + p.getName() + " (R$ " + p.getPrice() + ")");
        }

        System.out.println("\nEscolha a forma de pagamento:");
        for (PaymentType type : PaymentType.values()) {
            System.out.println(type.ordinal() + 1 + " - " + type);
        }
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao < 1 || opcao > PaymentType.values().length) {
            System.out.println("Forma de pagamento inválida.");
            return;
        }

        PaymentType selectedType = PaymentType.values()[opcao - 1];
        PaymentMethod pagamento = PaymentMethodFactory.create(selectedType);

        System.out.println("Aguarde, efetuando pagamento...");
        String autenticacao = UUID.randomUUID().toString();
        pagamento.pay(0);
        System.out.println("Pagamento confirmado com sucesso via " + selectedType + ". Chave de Autenticação: " + autenticacao);

        double total = produtos.stream().mapToDouble(Product::getPrice).sum();
        System.out.println("\nResumo da venda:");
        System.out.println("Cliente: " + user.getName());
        System.out.println("Produtos:");
        produtos.forEach(p -> System.out.println("- " + p.getName()));
        System.out.printf("Valor total: R$ %.2f\n", total);
        System.out.println("Pagamento: " + selectedType);

        Sale venda = new Sale(user, produtos, selectedType.toString());
        saleRepo.save(venda);

        System.out.println("\nVenda registrada com sucesso!");

        // Fechar a conexão com o banco de dados ao final
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão com o banco: " + e.getMessage());
        }
    }
}
