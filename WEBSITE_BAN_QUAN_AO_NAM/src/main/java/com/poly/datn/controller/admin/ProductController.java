package com.poly.datn.controller.admin;


import com.poly.datn.dto.ProductRequest;
import com.poly.datn.entity.Brand;
import com.poly.datn.entity.Category;
import com.poly.datn.entity.Color;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.ProductDetail;
import com.poly.datn.entity.Size;
import com.poly.datn.entity.TypeProduct;
import com.poly.datn.service.BrandService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private BrandService brandService;


    @GetMapping("/product")
    public String hienThi(Model model, @RequestParam(value = "status", defaultValue = "0") String status,
                          @RequestParam(value = "category", defaultValue = "0") Long category,
                          @RequestParam(value = "type", defaultValue = "0") Long type,
                          @RequestParam(value = "brand", defaultValue = "0") Long brand) {
        model.addAttribute("products", productService.getAll(status, category, type, brand));
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("types", typeProductService.getAll());
        model.addAttribute("categorys", categoryService.getAll());
        model.addAttribute("user", UserUltil.getUser());
        return "admin/san-pham";
    }

    @PostMapping("/product/new")
    public String addProduct(@ModelAttribute("product") ProductRequest prductReq) {
        Product product = productService.add(prductReq);
        return "redirect:/admin/product/" + product.getId();
    }

    @GetMapping("/product/{id}")
    public String addDetail(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("listanh", imageService.getListanh(id));
        model.addAttribute("categorys", categoryService.getAll());
        model.addAttribute("sizes", sizeService.getAll());
        model.addAttribute("colors", colorService.getAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("types", typeProductService.getAll());

        ProductDetail[] details = productDetailService.getDetail(id).toArray(new ProductDetail[0]);
        model.addAttribute("productDetails", details);
        model.addAttribute("user", UserUltil.getUser());
        return "admin/update-sanpham";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categorys", categoryService.getAll());
        model.addAttribute("sizes", sizeService.getAll());
        model.addAttribute("colors", colorService.getAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("types", typeProductService.getAll());
        model.addAttribute("user", UserUltil.getUser());
        model.addAttribute("typeProduct", new TypeProduct());
        return "admin/add-sanpham";
    }

    @GetMapping("/product/{id}/deleteimg/{img}")
    public String deleteImage(@PathVariable("id") Long id, @PathVariable("img") Long img) {
        imageService.deleteImage(img);
        return "redirect:/admin/product/" + id;
    }

    @PostMapping("product/update/{id}")
    public String updateProduct(@ModelAttribute("product") ProductRequest request, @PathVariable("id") Long id) {
        productService.update(request, id);
        return "redirect:/admin/product";
    }

    @PostMapping("product/update-detail")
    public String updateProductdetail(@RequestParam("id") List<Long> ids,
                                      @RequestParam("quantity") List<Integer> quantitys,
                                      @RequestParam("price") List<BigDecimal> prices,
                                      @RequestParam("status") List<String> status) {

        productDetailService.update(ids, quantitys, prices, status);

        return "redirect:/admin/product";
    }

    @GetMapping("/product/tat/{id}")
    public String tatBan(@PathVariable("id") Long id) {
        productService.save(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/product/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Long kq = productDetailService.delete(id);
        return "redirect:/admin/product/"+kq;
    }

    @PostMapping("/product/add-color")
    public String addColor(@RequestParam("name") String name) {
        colorService.save(Color.builder().name(name).build());
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-size")
    public String addSize(@RequestParam("name") String name) {
        sizeService.save(Size.builder().name(name).build());
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-type")
    public String addType(@RequestParam("name") String name, @RequestParam("category") Category category) {
        typeProductService.save(TypeProduct.builder().category(category).name(name).build());
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-category")
    public String addCategory(@RequestParam("name") String name) {
        categoryService.save(Category.builder().name(name).build());
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-brand")
    public String addBrand(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) throws IOException {
        Map<?, ?> map = imageService.upload(file, "other");
        brandService.save(Brand.builder().name(name).logo(map.get("url").toString()).build());
        return "redirect:/admin/product/new";
    }
}
