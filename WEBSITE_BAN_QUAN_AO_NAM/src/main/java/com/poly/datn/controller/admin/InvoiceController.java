package com.poly.datn.controller.admin;

import com.poly.datn.entity.Invoice;
import com.poly.datn.entity.Transaction;
import com.poly.datn.service.InvoiceService;
import com.poly.datn.service.TransactionService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@SessionAttributes("user")
@Controller
@RequestMapping("/admin")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/invoice")
    public String invoice(Model model) {
        model.addAttribute("all",invoiceService.getAll());
        model.addAttribute("choxacnhan", invoiceService.getStatus("1"));
        model.addAttribute("chogiao", invoiceService.getStatus("2"));
        model.addAttribute("danggiao", invoiceService.getStatus("3"));
        model.addAttribute("hoanthanh", invoiceService.getStatus("4"));
        model.addAttribute("huy", invoiceService.getStatus("-1"));
        model.addAttribute("chothanhtoan", invoiceService.getStatus("0"));
        return "admin/hoa-don";
    }
    @GetMapping("/invoice/{ma}")
    public String detail(@PathVariable("ma") String ma, Model model){
        model.addAttribute("codeBill", ma);
        model.addAttribute("detail", invoiceService.detail(ma));
        model.addAttribute("transaction", transactionService.getAll(ma));
        return "admin/hoa-don-detail";
    }
    @GetMapping("/invoice/delete/{code}")
    public String delete(@PathVariable("code")String codeBill , RedirectAttributes attributes){
        MessageUtil messageUtil = invoiceService.delete(codeBill);
        attributes.addFlashAttribute("message",messageUtil);
        return "redirect:/admin/sale";
    }

    @ModelAttribute("user")
    public Object initUser() {
        return UserUltil.getUser();
    }
}
