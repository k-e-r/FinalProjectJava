package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URISyntaxException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Controller
public class SampleController {
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  private SampleService sampleService;

  @ModelAttribute
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    LoginForm loginForm = new LoginForm();
    loginForm.setId("");
    loginForm.setName("");
    model.addAttribute("loginForm", loginForm);
    model.addAttribute("message", "Please enter the administrator information");

    return "login";
  }

  @ModelAttribute
  @RequestMapping(value = "/login_result", method = RequestMethod.POST)
  public String login_result(LoginForm loginForm, Model model) {
    List<Map<String, Object>> list;
    list = jdbcTemplate.queryForList("select * from \"user\"");

    for (int i = 1; i <= list.size(); i++) {
      if (("[" + loginForm.getId() + ", " + loginForm.getName() + "]").equals((list.get(i - 1).values().toString()))) {
        model.addAttribute("message", "Login was successful");
        break;
      } else {
        model.addAttribute("message", "Failed to login");
      }
    }

    return "login_result";
  }

  @ModelAttribute
  @RequestMapping(value = "/admin_menu", method = RequestMethod.GET)
  public String admin_menu(Model model) {
    model.addAttribute("message", "Please select a menu");
    return "admin_menu";
  }

  @ModelAttribute
  @RequestMapping(value = "/show_product", method = RequestMethod.GET)
  public String show_product(Model model) throws URISyntaxException {
    List<String> products = null;
    products = sampleService.selectAll();
    Map<Integer, Map<String, String>> outerMap = new HashMap<>();
    int j = 0;

    for (int i = 0; i < products.size(); i += 5) {
      Map<String, String> innerMap = new HashMap<>();
      innerMap.put("code", products.get(i));
      innerMap.put("name", products.get(i + 1));
      innerMap.put("description", products.get(i + 2));
      innerMap.put("price", products.get(i + 3));
      innerMap.put("evaluation", products.get(i + 4));
      outerMap.put(j, innerMap);
      j++;
    }

    model.addAttribute("outerMap", outerMap);
    model.addAttribute("products", products);
    model.addAttribute("message", "Displayed the product");

    return "show_product";
  }

  @ModelAttribute
  @RequestMapping(value = "/register_product", method = RequestMethod.GET)
  public String register_product(Model model) {
    ProductForm productForm = new ProductForm();
    model.addAttribute("productForm", productForm);
    model.addAttribute("message", "Please register the product");

    return "register_product";
  }

  @ModelAttribute
  @RequestMapping(value = "/update_product", method = RequestMethod.GET)
  public String update_product(Model model) {
    ProductForm productForm = new ProductForm();
    model.addAttribute("productForm", productForm);
    model.addAttribute("message", "Please update the product");

    return "update_product";
  }

  @ModelAttribute
  @RequestMapping(value = "/delete_product", method = RequestMethod.GET)
  public String delete_product(Model model) {
    ProductForm productForm = new ProductForm();
    model.addAttribute("productForm", productForm);
    model.addAttribute("message", "Please delete the product");

    return "delete_product";
  }

  @ModelAttribute
  @RequestMapping(value = "/after_delete_product", method = RequestMethod.POST)
  public String afeter_delete_product(ProductForm productForm, Model model) throws URISyntaxException {
    int flg = 0;
    String str = "";
    List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM product");
    for (int i = 0; i < list.size(); i++) {
      str = list.get(i).get("code").toString();
      if (str.equals(productForm.getCode())) {
        flg = 1;
      }
    }
    if (productForm.getCode().equals(""))
      flg = 2;

    switch (flg) {
    case 0:
      model.addAttribute("message", "The code is not exist, please retry");
      break;
    case 1:
      sampleService.delete(productForm);
      model.addAttribute("message", "Deleting is complete");
      break;
    default:
      model.addAttribute("message", "Please fill in a field");
      break;
    }

    model.addAttribute("code", productForm.getCode());
    return "after_delete_product";
  }

  @ModelAttribute
  @RequestMapping(value = "/show_register_result", method = RequestMethod.POST)
  public String show_register_result(ProductForm productForm, Model model) throws URISyntaxException {
    int flg = 0;
    String str;
    List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM product");
    for (int i = 0; i < list.size(); i++) {
      str = list.get(i).get("code").toString();
      if (str.equals(productForm.getCode())) {
        flg = 1;
      }
    }
    if (productForm.getCode().equals("") || productForm.getName().equals("") || productForm.getDescription().equals("")
        || productForm.getPrice().equals("") || productForm.getEvaluation().equals(""))
      flg = 2;

    switch (flg) {
    case 0:
      sampleService.insert(productForm);
      model.addAttribute("message", "Registration is complete");
      break;
    case 1:
      model.addAttribute("message", "The code is exist, please retry");
      break;
    default:
      model.addAttribute("message", "Please fill in all fields");
      break;
    }
    model.addAttribute("code", productForm.getCode());
    model.addAttribute("name", productForm.getName());
    model.addAttribute("description", productForm.getDescription());
    model.addAttribute("price", productForm.getPrice());
    model.addAttribute("evaluation", productForm.getEvaluation());

    return "show_result";
  }

  @ModelAttribute
  @RequestMapping(value = "/show_update_result", method = RequestMethod.POST)
  public String show_update_result(ProductForm productForm, Model model) throws URISyntaxException {
    int flg = 0;
    String str;
    List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM product");
    for (int i = 0; i < list.size(); i++) {
      str = list.get(i).get("code").toString();
      if (str.equals(productForm.getCode())) {
        flg = 1;
      }
    }
    if (productForm.getCode().equals("") || productForm.getName().equals("") || productForm.getDescription().equals("")
        || productForm.getPrice().equals("") || productForm.getEvaluation().equals(""))
      flg = 2;

    switch (flg) {
    case 0:
      model.addAttribute("message", "The product is not exist, please register first");
      break;
    case 1:
      sampleService.insert(productForm);
      model.addAttribute("message", "Updating is complete");
      break;
    default:
      model.addAttribute("message", "Please fill in all fields");
      break;
    }
    model.addAttribute("code", productForm.getCode());
    model.addAttribute("name", productForm.getName());
    model.addAttribute("description", productForm.getDescription());
    model.addAttribute("price", productForm.getPrice());
    model.addAttribute("evaluation", productForm.getEvaluation());

    return "show_result";
  }
}