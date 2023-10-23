package com.poly.datn.controller.admin;


import com.poly.datn.dto.ProductDto;
import com.poly.datn.entity.Product;
import com.poly.datn.service.CategoryService;
import com.poly.datn.service.ColorService;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.ProductDetailService;
import com.poly.datn.service.ProductService;
import com.poly.datn.service.SizeService;
import com.poly.datn.service.TypeProductService;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductDetailService productDetailService;
    @Autowired
    private ColorService colorService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private TypeProductService typeProductService;

    @GetMapping("/product")
    public String hienThi(Model model) {
        model.addAttribute("list", productService.getAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/product/product";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {
        model.addAttribute("object", new ProductDto());
        model.addAttribute("categorys", categoryService.fillAll());
        model.addAttribute("sizes", sizeService.getAll());
        model.addAttribute("colors", colorService.getAll());
        model.addAttribute("types", typeProductService.getAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/product/new-product";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute ProductDto dto) {
        Product product = productService.add(dto);
        return "redirect:/admin/product/"+product.getId();
    }

    @GetMapping("/product/{id}")
    public String detailProduct(Model model, @PathVariable("id") Long id) {
        model.addAttribute("list", productDetailService.getDetail(id));
        model.addAttribute("user", UserUltil.getUser());
        return "admin/product/product-detail";
    }

    @PostMapping("/product/{sp}/update/{id}")
    public String updateQuantity(@RequestParam("quantity") Integer quantity, @PathVariable("id") Long id, @PathVariable("sp") Long sp) {
        productDetailService.update(id,quantity);
        return "redirect:/admin/product/"+sp;
    }
    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,Model model){
        model.addAttribute("object",productService.getProductById(id));
        model.addAttribute("categorys", categoryService.fillAll());
        model.addAttribute("types", typeProductService.getAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/product/update-product";
    }
    @GetMapping("/product/delete/image/{id}/{sp}")
    public String deleteImage(@PathVariable("id") Long id, @PathVariable("sp") Long sp){
        imageService.deleteImage(id);
        return "redirect:/admin/product/edit/"+ sp;
    }

    @PostMapping("/product/edit/image/{sp}")
    public String addImage(@PathVariable("sp") Long sp, @RequestParam("files") MultipartFile[] files){
        imageService.add(sp, files);
        return "redirect:/admin/product/edit/"+ sp;
    }
    @PostMapping("/product/edit/save/{sp}")
    public String updateProduct(@ModelAttribute("object") ProductDto dto, @PathVariable("sp") Long sp){
        productService.updateProduct(dto,sp);
        return "redirect:/admin/product";
    }

}
