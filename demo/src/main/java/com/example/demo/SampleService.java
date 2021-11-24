package com.example.demo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
  public List<String> selectAll() throws URISyntaxException {
    List<String> entities = null;
    entities = new ArrayList<String>();
    ResultSet resultSet = null;
    Connection connection = null;
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    try {
      connection = DriverManager.getConnection(dbUrl, username, password); // localhost:3306を変更
      Statement statement = connection.createStatement();
      resultSet = statement.executeQuery("select * from product");
      while (resultSet.next()) {
        entities.add(resultSet.getString("code"));
        entities.add(resultSet.getString("name"));
        entities.add(resultSet.getString("description"));
        entities.add(resultSet.getString("price"));
        entities.add(resultSet.getString("evaluation"));
      }

      if (connection != null) {
        connection.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return entities;
  }

  public void insert(ProductForm productForm) throws URISyntaxException {
    Connection connection = null;
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    try {
      connection = DriverManager.getConnection(dbUrl, username, password);
      delete(productForm);
      PreparedStatement statement = connection
          .prepareStatement("INSERT INTO product VALUES (cast(? as int), ?, ?, ?, ?)");

      statement.setString(1, productForm.getCode());
      statement.setString(2, productForm.getName());
      statement.setString(3, productForm.getDescription());
      statement.setString(4, productForm.getPrice());
      statement.setString(5, productForm.getEvaluation());
      connection.setAutoCommit(true);
      statement.execute();

      if (connection != null) {
        connection.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void update(ProductForm productForm) throws URISyntaxException {
    Connection connection = null;
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    try {
      connection = DriverManager.getConnection(dbUrl, username, password);
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE product SET code=cast(? as int), name=?, description=?, price=?, evaluation=? WHERE code=?");

      statement.setString(1, productForm.getCode());
      statement.setString(2, productForm.getName());
      statement.setString(3, productForm.getDescription());
      statement.setString(4, productForm.getPrice());
      statement.setString(5, productForm.getEvaluation());
      statement.setString(6, productForm.getCode());

      connection.setAutoCommit(true);
      statement.execute();
      if (connection != null) {
        connection.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void delete(ProductForm productForm) throws URISyntaxException {
    Connection connection = null;
    URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    try {
      connection = DriverManager.getConnection(dbUrl, username, password);
      PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE code=cast(? as int)");

      statement.setString(1, productForm.getCode());

      connection.setAutoCommit(true);
      statement.execute();

      if (connection != null) {
        connection.close();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}