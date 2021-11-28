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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource(value = { "classpath:application.properties" })
public class SampleService {
  @Autowired
  Environment environment;

  public List<String> selectAll() {
    List<String> entities = null;
    entities = new ArrayList<String>();
    ResultSet resultSet = null;
    Connection connection = null;

    String username = environment.getProperty("spring.datasource.username");
    String password = environment.getProperty("spring.datasource.password");
    String dbUrl = environment.getProperty("spring.datasource.url");

    try {
      connection = DriverManager.getConnection(dbUrl, username, password); // localhost:3306を変更
      Statement statement = connection.createStatement();
      resultSet = statement.executeQuery("select * from product order by code asc");
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

  public void insert(ProductForm productForm) {
    Connection connection = null;

    String username = environment.getProperty("spring.datasource.username");
    String password = environment.getProperty("spring.datasource.password");
    String dbUrl = environment.getProperty("spring.datasource.url");

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

  public void update(ProductForm productForm) {
    Connection connection = null;

    String username = environment.getProperty("spring.datasource.username");
    String password = environment.getProperty("spring.datasource.password");
    String dbUrl = environment.getProperty("spring.datasource.url");

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

  public void delete(ProductForm productForm) {
    Connection connection = null;

    String username = environment.getProperty("spring.datasource.username");
    String password = environment.getProperty("spring.datasource.password");
    String dbUrl = environment.getProperty("spring.datasource.url");

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