package controllers;

import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.IProductService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping("/home")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Value("${file-upload}")
    private String fileUpload;

    @Value("${view}")
    private String view;

    @GetMapping
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("index");
        ArrayList<Product> products = productService.getAll();
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("create");
        Product product = new Product();
        String path = "";
        modelAndView.addObject("file",view);
        modelAndView.addObject("path",path);
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping
    public ModelAndView save(@ModelAttribute Product product) {
        ModelAndView modelAndView = new ModelAndView("index");
        MultipartFile multipartFile = product.getImage();
        String fileName =multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        boolean check = false;
        for (Product prod: productService.getAll()) {
            if (prod.getId() == product.getId()){
                productService.update(product);
                check = true;
                break;
            }
        }
        if (!check){
            product.setId(productService.getAll().size()+1);
            productService.add(product);
        }
        System.out.println(product.getImage());
        System.out.println(product.getImage().getOriginalFilename());
        System.out.println(product.getImage().getOriginalFilename().equals(""));
        ArrayList<Product> products = productService.getAll();
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewDetail(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView("view-detail");
        Product product = productService.findById(id);
        modelAndView.addObject("product",product);
        modelAndView.addObject("file", view);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView("index");
        productService.deleteById(id);
        ArrayList<Product> products = productService.getAll();
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView("create");
        Product product = productService.findById(id);
        modelAndView.addObject("file",view);
        modelAndView.addObject("path", product.getImage().getOriginalFilename());
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView searchByName(@RequestParam(name = "search",required = false) String name){
        ModelAndView modelAndView = new ModelAndView("index");
        ArrayList<Product> products;
        if (name.equals("")){
            products = productService.getAll();
        } else {
            products = productService.findByName(name);
        }
        modelAndView.addObject("file", view);
        modelAndView.addObject("products",products);
        return modelAndView;
    }
}
