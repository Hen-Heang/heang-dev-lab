package com.heang.springmybatistest.controller.store;

import com.heang.springmybatistest.service.CategoryService;
import com.heang.springmybatistest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Store Admin View Controller (스토어 어드민 뷰 컨트롤러)
 * Controller for rendering Thymeleaf pages (Thymeleaf 페이지 렌더링)
 *
 * Data is loaded via jQuery AJAX calls to REST API
 * Here we only render the initial page (데이터는 AJAX로 조회)
 */
@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreViewController {

    private final CategoryService categoryService;
    private final ProductService productService;

    /**
     * Category management page (카테고리 관리 페이지)
     * GET /store/category
     */
    @GetMapping("/category")
    public String categoryList(Model model) {
        model.addAttribute("pageTitle", "Category Management");
        return "category/list";
    }

    /**
     * Product management page (상품 관리 페이지)
     * GET /store/product
     */
    @GetMapping("/product")
    public String productList(Model model) {
        model.addAttribute("pageTitle", "Product Management");
        model.addAttribute("categories", categoryService.findAll());
        return "product/list";
    }

    /**
     * GET /store/category-tree
     * Shows categories with their products using <collection> JOIN (1:N JOIN 결과 페이지)
     *
     * ONE query → MyBatis groups flat rows into nested objects:
     *   List<CategoryWithProductsVO>, each with List<Product> inside
     */
    @GetMapping("/category-tree")
    public String categoryTree(Model model) {
        model.addAttribute("categories", categoryService.findAllWithProducts());
        return "store/category-tree";
    }
}
