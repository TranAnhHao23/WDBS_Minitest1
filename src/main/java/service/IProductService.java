package service;

import model.Product;

import java.util.ArrayList;

public interface IProductService {

    ArrayList<Product> getAll();

    Product findById(int id);

    void add(Product product);

    void update(Product product);

    void deleteById(int id);

    ArrayList<Product> findByName(String name);


}
