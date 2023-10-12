package com.poly.datn.controller.admin;

import com.poly.datn.entity.*;
import com.poly.datn.service.ColorService;
import com.poly.datn.service.ProductDetailService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.SizeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/chi-tiet-san-pham")
public class ProductDetailController {

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    ColorService colorService;

    @Autowired
    SizeService service;

    @Autowired
    ProductService productService;

    private List<Color> listMS = new ArrayList<>();
    private List<Size> listS = new ArrayList<>();

    @GetMapping("/hien-thi")
    public String hienThi(@RequestParam(defaultValue = "0", name = "page") Integer pageNum, Model model) {
        Page<ProductDetail> page = productDetailService.phanTrang(pageNum, 1);
        model.addAttribute("list", page);
        model.addAttribute("att", new ProductDetail());
        return "productdetail/index";
    }


    @GetMapping("view-add")
    public String viewAdd(Model model) {
        model.addAttribute("listMauSac", colorService.getAll());
        model.addAttribute("listSize",service.getAll());
        model.addAttribute("listSanPham",productService.getAll());
        model.addAttribute("att", new ProductDetail());
        model.addAttribute("ms",new Color());
        model.addAttribute("s",new Size());
        model.addAttribute("sp",new Product());
        return "productdetail/add";
    }

    @PostMapping("add")
    public String add(@Valid @ModelAttribute("att") ProductDetail product,
                      BindingResult result,
                      RedirectAttributes redirectAttributes,
                      Model model, HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("listMauSac", colorService.getAll());
            model.addAttribute("listSize",service.getAll());
            model.addAttribute("listSanPham",productService.getAll());
            model.addAttribute("att", new ProductDetail());
            model.addAttribute("ms",new Color());
            model.addAttribute("s",new Size());
            model.addAttribute("sp",new Product());
            return "productdetail/add";
        }

        product.setColor(product.getColor());
        product.setSize(product.getSize());
        product.setProduct(product.getProduct());
        product.setQuantity(product.getQuantity());

        product.setStatus("1");
        productDetailService.add(product);
        return "redirect:/chi-tiet-san-pham/hien-thi";
    }

    @GetMapping("view-update/{ma}")
    public String viewUpdate(@PathVariable  Long ma, Model model) {
        ProductDetail productDetail = productDetailService.detail(ma);
        List<Color> colors = colorService.getAll();
        List<Size> sizes = service.getAll();
        model.addAttribute("listPr", productDetail);
        model.addAttribute("cl", colors);
        model.addAttribute("s", sizes);
        return "productdetail/update";
    }





}