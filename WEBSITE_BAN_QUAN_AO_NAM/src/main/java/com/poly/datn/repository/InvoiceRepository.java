package com.poly.datn.repository;

import com.poly.datn.dto.BieuDoCot;
import com.poly.datn.dto.ListThongK;
import com.poly.datn.dto.ThongKeDto;
import com.poly.datn.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("select c from Invoice c where c.status in ('1','2','3','4','-1') order by c.createDate desc ")
    List<Invoice> getAll();

    @Query("select c from Invoice c where c.status like :status order by c.createDate desc ")
    List<Invoice> getStatus(String status);

    @Query("select count(status) from Invoice where status = '0'")
    Integer getStatusChoThanhToan();

    @Query("select count(status) from Invoice where status = '1'")
    Integer getStatusChoXacNhan();

    @Query("select count(status) from Invoice where status = '2'")
    Integer getStatusChoGiaoHang();

    @Query("select count(status) from Invoice where status = '3'")
    Integer getStatusDangGiao();

    @Query("select count(status) from Invoice where status = '4'")
    Integer getStatusHoanThanh();

    @Query("select count(status) from Invoice where status = '-1'")
    Integer getStatusDaHuy();

//    @Query("select new com.poly.datn.dto.ThongKeDto(p.productName,i.url,v.quantity,v.price, sum(v.price * v.quantity))" +
//            "from Product p  join Image i on p .id = i.product.id join ProductDetail pd" +
//            " on pd.product.id = p .id join InvoiceDetail v  " +
//            "on v.invoiceId.productDetail.id = pd.id"
//
//
//            )


    @Query(value = "select top(10) product_name as 'productName',url ,sum(invoiceDetails.quantity) as 'quantity', invoiceDetails.price ,sum(invoiceDetails.quantity *invoiceDetails.price) 'pricec'\n" +
            "from\n" +
            "products join productDetails on products.Id = productDetails.Id_product\n" +
            "join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
            "join images on products.Id = images.Id_product\n" +
            "group by product_name, url,invoiceDetails.price \n" +
            "order by quantity desc", nativeQuery = true)
    List<ListThongK> getSanPhamBanChay();


//    @Query(value = "select top(10) product_name, invoiceDetails.price \n" +
//            "from\n" +
//            "products join productDetails on products.Id = productDetails.Id_product\n" +
//            "join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
//            "join images on products.Id = images.Id_product", nativeQuery = true)
//    List<ListThongK> getSanPhamBanChay();


    @Query(value = "select create_date, sum(invoiceDetails.quantity *invoiceDetails.price) 'price'\n" +
            "from invoiceDetails join invoices\n" +
            "on invoiceDetails.ivoice_id = invoices.code_bill\n" +
            "\n" +
            "group by create_date", nativeQuery = true)
    List<BieuDoCot> getDoanhThu();


    @Query(value = "select sum (quantity * price)\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "where month(create_date) = MONTH(GETDATE())", nativeQuery = true)
    Double getDoanhThuThangNay();


    @Query(value = "select sum (quantity * price)\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "where day(create_date) = day(GETDATE())", nativeQuery = true)
    Double getDoanhThuHomNay();

    @Query(value = "\n" +
            "select sum (quantity)\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "where month(create_date) = month(GETDATE())", nativeQuery = true)
    Integer getSoSanPhamDaBanDuocThangNay();

    @Query(value = "select create_date 'createDate', sum(quantity * price) 'price'\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "group by create_date",nativeQuery = true)
    List<BieuDoCot> getBieuDoCot();


}
