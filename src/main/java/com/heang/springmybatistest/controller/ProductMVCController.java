package com.heang.springmybatistest.controller;


import com.heang.springmybatistest.model.Product;
import com.heang.springmybatistest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor

public class ProductMVCController {


    private final ProductService productService;

    // Step 1: List all products
    // URL: GET /products/list
    @GetMapping("/list")
    public String list(Model model) {
        List<Product> products =
                productService.search(new
                        com.heang.springmybatistest.dto.ProductSearchRequest());
        model.addAttribute("products", products);
        return "products/list";
    }

//    Get Product by id or get product details,

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "products/detail";
    }

}
