package com.unicesumar.repository;

import com.unicesumar.entities.Product;
import com.unicesumar.entities.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.UUID;

public class SaleRepository {
    private final Connection connection;

    public SaleRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Sale sale) {
        try {
            // Gera um UUID para a venda
            String saleUuid = UUID.randomUUID().toString();
            sale.setUuid(saleUuid); // você pode precisar adicionar esse campo na entidade Sale

            String sqlSale = "INSERT INTO sale (uuid, user_uuid, payment_method, total) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sqlSale);
            stmt.setString(1, saleUuid);
            stmt.setString(2, sale.getUser().getUuid().toString());
            stmt.setString(3, sale.getPaymentMethod());
            stmt.setDouble(4, sale.getTotal());
            stmt.executeUpdate();

            // Inserir os itens da venda
            String sqlItem = "INSERT INTO sale_items (sale_id, product_uuid) VALUES (?, ?)";
            PreparedStatement stmtItem = connection.prepareStatement(sqlItem);

            for (Product product : sale.getProducts()) {
                stmtItem.setString(1, saleUuid);
                stmtItem.setString(2, product.getUuid().toString());
                stmtItem.executeUpdate();
            }

            stmt.close();
            stmtItem.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
