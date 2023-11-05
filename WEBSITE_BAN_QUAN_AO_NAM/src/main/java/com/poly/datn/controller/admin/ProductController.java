package com.poly.datn.controller.admin;


import com.poly.datn.dto.ProductRequest;
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
import com.poly.datn.util.MessageUtil;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@SessionAttributes("user")
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
                          @RequestParam(value = "category", required = false) Long category,
                          @RequestParam(value = "type", required = false) Long type,
                          @RequestParam(value = "brand", required = false) Long brand) {
        model.addAttribute("products", productService.getAll(status, category, type, brand));
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("types", typeProductService.getAll());
        model.addAttribute("categorys", categoryService.getAll());
        return "admin/san-pham";
    }

    @PostMapping("/product/new")
    public String addProduct(@ModelAttribute("product") ProductRequest prductReq, RedirectAttributes attributes) {
        MessageUtil message = productService.add(prductReq);
        attributes.addFlashAttribute("message", message);
        if (message.getStatus() == 1){
            Product product = (Product) message.getObject();
            return "redirect:/admin/product/" + product.getId();
        }
        else {
            attributes.addFlashAttribute("product", message.getObject());
            return "redirect:/admin/product/new";
        }

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
        return "admin/update-sanpham";
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {
        model.addAttribute("categorys", categoryService.getAll());
        model.addAttribute("sizes", sizeService.getAll());
        model.addAttribute("colors", colorService.getAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("types", typeProductService.getAll());

        model.addAttribute("typeProduct", new TypeProduct());
        return "admin/add-sanpham";
    }

    @GetMapping("/product/{id}/deleteimg/{img}")
    public String deleteImage(@PathVariable("id") Long id, @PathVariable("img") Long img) {
        imageService.deleteImage(img);
        return "redirect:/admin/product/" + id;
    }

    @PostMapping("product/update/{id}")
    public String updateProduct(@ModelAttribute("product") ProductRequest request, @PathVariable("id") Long id, RedirectAttributes attributes) {
        MessageUtil message = productService.update(request, id);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product";
    }

    @PostMapping("product/update-detail")
    public String updateProductdetail(@RequestParam("id") List<Long> ids,
                                      @RequestParam("quantity") List<Integer> quantitys,
                                      @RequestParam("price") List<BigDecimal> prices,
                                      RedirectAttributes attributes) {
        MessageUtil message = productDetailService.update(ids, quantitys, prices);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product";
    }

    @GetMapping("/product/tat/{id}")
    public String tatBan(@PathVariable("id") Long id, RedirectAttributes attributes) {
        MessageUtil message = productService.save(id);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product";
    }

    @GetMapping("/product/{id}/delete/{sp}")
    public String delete(@PathVariable("id") Long id, @PathVariable("sp") Long sp,RedirectAttributes attributes) {
        MessageUtil message = productDetailService.delete(sp);
        attributes.addFlashAttribute("message",message);
        return "redirect:/admin/product/" + id;
    }

    @PostMapping("/product/add-color")
    public String addColor(@RequestParam("name") String name, RedirectAttributes attributes) {
        MessageUtil message = colorService.save(Color.builder().name(name).build());
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-size")
    public String addSize(@RequestParam("name") String name, RedirectAttributes attributes) {
        MessageUtil message = sizeService.save(Size.builder().name(name).build());
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-type")
    public String addType(@RequestParam("name") String name, @RequestParam("category") Category category, RedirectAttributes attributes) {
        MessageUtil message = typeProductService.save(TypeProduct.builder().category(category).name(name).build());
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-category")
    public String addCategory(@RequestParam("name") String name, RedirectAttributes attributes) {
        MessageUtil message = categoryService.save(Category.builder().name(name).build());
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product/new";
    }

    @PostMapping("/product/add-brand")
    public String addBrand(@RequestParam("name") String name, @RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {
        MessageUtil message = brandService.save(name, file);
        attributes.addFlashAttribute("message", message);
        return "redirect:/admin/product/new";
    }

    @ModelAttribute("message")
    public MessageUtil initMessage() {
        return new MessageUtil();
    }
    @ModelAttribute("user")
    public Object initUser(){
        return UserUltil.getUser();
    }
    @ModelAttribute("product")
    public Object initNewProduct(){
        return new ProductRequest();
    }
}
