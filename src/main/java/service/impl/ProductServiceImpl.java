package service.impl;

import model.Product;
import org.springframework.stereotype.Service;
import service.IProductService;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements IProductService {

    private static ArrayList<Product> products;

    static {
        products = new ArrayList<>();
    }

    @Override
    public ArrayList<Product> getAll() {
        return products;
    }

    @Override
    public Product findById(int id) {
        for (Product product: products) {
            if (product.getId() == id){
                return product;
            }
        }
        return null;
    }

    @Override
    public void add(Product product) {
        products.add(product);
    }

    @Override
    public void update(Product product) {
        for (Product product1: products) {
            if (product1.getId() == product.getId()){
                products.remove(product1);
                products.add(product);
                break;
            }
        }
    }

    @Override
    public void deleteById(int id) {
        Product product = findById(id);
        products.remove(product);
    }

    @Override
    public ArrayList<Product> findByName(String name) {
        ArrayList<Product> products1 = new ArrayList<>();
        String regex = ".*" + name + ".*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (Product product : products) {
            matcher = pattern.matcher(product.getName());
            if (matcher.find()) {
                products1.add(product);
            }
        }
        return products1;
    }
}
