package com.poly.datn.repository;

import com.poly.datn.dto.*;
import com.poly.datn.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    @Query("select c from Invoice c where c.status in ('1','2','3','4','-1') order by c.createDate desc ")
    List<Invoice> getAll();

    @Query("select c from Invoice c where c.status like :status order by c.createDate desc ")
    List<Invoice> getStatus(String status);

    @Query("select new com.poly.datn.dto.InvoiceDto( c ) from Invoice c where c.status like :status")
    List<InvoiceDto> fillAll(String status);

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
    List<ListSPBanChay> getSanPhamBanChay();


    @Query(value = "select create_date, sum(invoiceDetails.quantity *invoiceDetails.price) 'price'\n" +
            "from invoiceDetails join invoices\n" +
            "on invoiceDetails.ivoice_id = invoices.code_bill\n" +
            "\n" +
            "group by create_date", nativeQuery = true)
    List<BieuDoCot> getDoanhThu();


    //doanh thu tháng này
    @Query(value = "select sum (quantity * price)\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "where month(create_date) = MONTH(GETDATE()) and invoices.status = '1' ", nativeQuery = true)
    Double getDoanhThuThangNay();

//doanh thu hôm nay
    @Query(value = "select sum (quantity * price)\n" +
            "from invoices\n" +
            "join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "where day(create_date) = day(GETDATE()) and  invoices.status = '1'", nativeQuery = true)
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
            "group by create_date", nativeQuery = true)
    List<BieuDoCot> getBieuDoCot();

    Long countAllByStatus(String status);


    //tìm kiếm doanh thu theo ngày
    @Query(value = "\n" +
            "select convert(date,create_date) 'createDate',sum(quantity * price) 'price'\n" +
            "            from invoices\n" +
            "            join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "            where create_date  BETWEEN :ngay1 and :ngay2\n and  invoices.status = '1'" +
            "\t\t\tgroup by create_date", nativeQuery = true)
    List<BieuDoCot> getTimKiemBieuDoCot(Date ngay1, Date ngay2);

    //tổng hóa đơn
    @Query(value = "select count(code_bill) from invoices", nativeQuery = true)
    Integer getTongHoaDon();

    //đơn hàng hoàn thành
    @Query(value = "select count(code_bill) from invoices\n" +
            "where status = '4'", nativeQuery = true)
    Integer getDonHangHoanThanh();

    //đơn hàng đang giao
    @Query(value = "select count(code_bill) from invoices\n" +
            "where status = '3'", nativeQuery = true)
    Integer getDonHangDangGiao();

    //đơn hàng đã hủy
    @Query(value = "select count(code_bill) from invoices\n" +
            "where status = '-1'", nativeQuery = true)
    Integer getDonHangDaHuy();


    //doanh thu năm nay
    @Query(value = " select sum (quantity * price)\n" +
            "                      from invoices\n" +
            "                      join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "                       where year(create_date) = year(GETDATE()) and Invoices.status = '1'" +
            "  ", nativeQuery = true)
    Double getDoanhThuNamNay();


    //tổng doanh thu
    @Query(value = " select sum (quantity * price)\n" +
            "                        from invoices\n" +
            "                      join invoicedetails on invoices.code_bill = invoicedetails.ivoice_id\n" +
            "\t\t\t\t\t  where Invoices.status = '1'", nativeQuery = true)
    Double getTongDoanhThu();

    //doanh thu 7 ngày trước
    @Query(value = "SELECT CONVERT(date, create_date) 'createDate',sum (quantity * price) 'price'\n" +
            "FROM InvoiceDetails join Invoices on InvoiceDetails.ivoice_id = Invoices.code_bill\n" +
            "WHERE create_date >= DATEADD(day, -7, GETDATE())\n" +
            "group by create_date", nativeQuery = true)
    List<BieuDoCot> getDoanhThu7NgayTruoc();

    //doanh thu 1 tháng trước
    @Query(value = "SELECT CONVERT(date,create_date) 'createDate',sum (quantity * price) 'price'\n" +
            "FROM InvoiceDetails join Invoices on InvoiceDetails.ivoice_id = Invoices.code_bill\n" +
            "WHERE create_date >= DATEADD(month, -1, GETDATE())\n" +
            "group by create_date", nativeQuery = true)
    List<BieuDoCot> getDoanhThu1ThangTruoc();

    //doanh thu 6 tháng trước
    @Query(value = "SELECT month(create_date) 'createDate',sum (quantity * price) 'price'\n" +
            "FROM InvoiceDetails join Invoices on InvoiceDetails.ivoice_id = Invoices.code_bill\n" +
            "WHERE create_date >= DATEADD(month, -6, GETDATE())\n" +
            "group by month(create_date)", nativeQuery = true)
    List<BieuDoThongKeThang> getDoanhThu6ThangTruoc();

    //sản phẩm bán chạy trong ngày gần nhât
    @Query(value = "select top(10) product_name as 'productName',url ,sum(invoiceDetails.quantity) as 'quantity', invoiceDetails.price ,sum(invoiceDetails.quantity *invoiceDetails.price) 'pricec'\n" +
            "            from\n" +
            "            products join productDetails on products.Id = productDetails.Id_product\n" +
            "            join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
            "            join images on products.Id = images.Id_product\n" +
            "\t\t\tjoin Invoices on Invoices.code_bill = InvoiceDetails.ivoice_id\n" +
            "\t\t\twhere DATEDIFF(day, invoices.create_date, GETDATE()) <= 1\n" +
            "            group by product_name, url,invoiceDetails.price \n" +
            "            order by quantity desc", nativeQuery = true)
    List<ListSPBanChay> getSPBanChayNgay();

    //sản phẩm bán chạy trong tuần gần nhất
    @Query(value = "select top(10) product_name as 'productName',url ,sum(invoiceDetails.quantity) as 'quantity', invoiceDetails.price ,sum(invoiceDetails.quantity *invoiceDetails.price) 'pricec'\n" +
            "            from\n" +
            "            products join productDetails on products.Id = productDetails.Id_product\n" +
            "            join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
            "            join images on products.Id = images.Id_product\n" +
            "\t\t\tjoin Invoices on Invoices.code_bill = InvoiceDetails.ivoice_id\n" +
            "\t\t\twhere DATEDIFF(week, invoices.create_date, GETDATE()) <= 1\n" +
            "            group by product_name, url,invoiceDetails.price \n" +
            "            order by quantity desc", nativeQuery = true)
    List<ListSPBanChay> getSPBanChayTuan();

    //sản phẩm bán chạy tháng
    @Query(value = "select top(10) product_name as 'productName',url ,sum(invoiceDetails.quantity) as 'quantity', invoiceDetails.price ,sum(invoiceDetails.quantity *invoiceDetails.price) 'pricec'\n" +
            "            from\n" +
            "            products join productDetails on products.Id = productDetails.Id_product\n" +
            "            join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
            "            join images on products.Id = images.Id_product\n" +
            "\t\t\tjoin Invoices on Invoices.code_bill = InvoiceDetails.ivoice_id\n" +
            "\t\t\twhere DATEDIFF(month, invoices.create_date, GETDATE()) <= 1\n" +
            "            group by product_name, url,invoiceDetails.price \n" +
            "            order by quantity desc\n" +
            "\t\t\t", nativeQuery = true)
    List<ListSPBanChay> getSPBanChayThang();

    //sản phẩm bán chạy năm
    @Query(value = "select top(10) product_name as 'productName',url ,sum(invoiceDetails.quantity) as 'quantity', invoiceDetails.price ,sum(invoiceDetails.quantity *invoiceDetails.price) 'pricec'\n" +
            "            from\n" +
            "            products join productDetails on products.Id = productDetails.Id_product\n" +
            "            join invoiceDetails on productDetails.Id = invoiceDetails.product_id\n" +
            "            join images on products.Id = images.Id_product\n" +
            "\t\t\tjoin Invoices on Invoices.code_bill = InvoiceDetails.ivoice_id\n" +
            "\t\t\twhere DATEDIFF(year, invoices.create_date, GETDATE()) <= 1\n" +
            "            group by product_name, url,invoiceDetails.price \n" +
            "            order by quantity desc", nativeQuery = true)
    List<ListSPBanChay> getSPBanChayNam();

    //tổng hóa đơn
    @Query(value = "select count(code_bill) from invoices ", nativeQuery = true)
    Integer getHoaDon();

    @Query("select c from Invoice c where c.user.id =:id order by c.createDate desc ")
    List<Invoice> getInvoiceByUser(Long id);
}
