package com.poly.datn.service.impl;

import com.poly.datn.dto.UserDto;
import com.poly.datn.dto.UserRequest;
import com.poly.datn.entity.Address;
import com.poly.datn.entity.User;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import com.poly.datn.util.UserUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAvatar("https://res.cloudinary.com/dg8hhxkah/image/upload/v1703776131/other/bixhah1m4u2nu0zqsq7m.jpg");
        user.setStatus("onKH");
        userRepository.save(user);
    }

    @Override
    public Optional<User> detail(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getByUserName(String userName) {
        return userRepository.getByUser(userName);
    }

    @Override
    public User addUserToBill(User user) {
        String matKhau = "abcd" + generateCodeBill(5);
        user.setPassword(passwordEncoder.encode(matKhau));
        String s1 = user.getEmail();
        String[] parts = s1.split("@");
        String part1 = parts[0];
        user.setUsername(part1);
        user.setStatus("onKH");
        user.setAvatar("https://res.cloudinary.com/dg8hhxkah/image/upload/v1698080470/avartar/vedidtoxs2wn2khvtekq.jpg");
        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("THÔNG TIN ĐĂNG NHẬP CỦA BẠN");
        mailMessage.setText("Tên đăng nhập:" + part1 + "\nMật khẩu đăng nhập:" + matKhau);
        javaMailSender.send(mailMessage);
        return userRepository.save(user);
    }
    /**
     * @param email
     * @return nếu mà ko tìm đc email thì ko tồn tại -> đăng ký
     * nếu mà email tồn tại -> lỗi
     */
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.getByEmail(email).isPresent();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUser(username).get();
    }

    @Override
    public User convert(UserSignUpRequest source) {
        return User.builder()
                .email(source.getEmail())
                .username(source.getUsername())
                .password(source.getPassword())
                .phoneNumber(source.getPhoneNumber())
                .name(source.getName())
                .build();
    }


    @Override
    public List<UserDto> getAll(String status) {
        if (status.equals("0")) {
            return userRepository.getAll();
        } else {
            return userRepository.tkStatus(status);
        }
    }

    @Override
    public User detailCustomer(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public MessageUtil add(UserRequest userRequest, MultipartFile file) {

//        if (userRequest.getName() == null || userRequest.getName().isEmpty()) {
//            throw new IllegalArgumentException("Name cannot be empty");
//        }

        Random random = new Random();
        int number;
        String mk = "";
        for (int i = 0; i < 5; i++) {
            number = random.nextInt(9);
            mk += number;
        }
        String matKhau = "abcd" + mk;
        User uer = userRequest.user();
        uer.setPassword(passwordEncoder.encode(matKhau));
        String s1 = uer.getEmail();
        String[] parts = s1.split("@");
        String part1 = parts[0];
        uer.setUsername(part1);
        uer.setStatus("onKH");
        uer.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(uer.getEmail());
        mailMessage.setSubject("THÔNG TIN ĐĂNG NHẬP CỦA BẠN");
        mailMessage.setText("Tên đăng nhập:" + part1 + "\nMật khẩu đăng nhập:" + matKhau);
        javaMailSender.send(mailMessage);
        Map<?, ?> uploadResult = null;
        try {
            uploadResult = imageService.upload(file, "avatar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String avatar = uploadResult.get("url").toString();
        uer.setAvatar(avatar);
        User u = userRepository.save(uer);
        Address address = userRequest.address();
        address.setUser(u);
        address.setStatus("on");
        Address a = addressRepository.save(address);
        return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").build();
//        return new MessageUtil(1, "Thêm thành công", "bg-success", uer);
//        return MessageUtil.builder().status(1).message("Thêm thành công !").type("bg-success").object(uer).build();


    }

    @Override
    public List<Address> findByIdDiaChi(Long cid) {
        return addressRepository.findByIdKhachHang(cid);
    }

    @Override
    public List<UserDto> getAllStaff() {
        return userRepository.getAllStaff();
    }

    @Override
    public MessageUtil addStaff(UserRequest userRequest, MultipartFile file) {

        Random random = new Random();
        int number;
        String mk = "";
        for (int i = 0; i < 5; i++) {
            number = random.nextInt(9);
            mk += number;
        }
        String matKhau = "abcd" + mk;
        User uer = userRequest.user();
        uer.setPassword(passwordEncoder.encode(matKhau));
        String s1 = uer.getEmail();
        String[] parts = s1.split("@");
        String part1 = parts[0];
        uer.setUsername(part1);
        uer.setStatus("onNV");
        uer.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_NV")));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(uer.getEmail());
        mailMessage.setSubject("THÔNG TIN ĐĂNG NHẬP CỦA BẠN");
        mailMessage.setText("Tên đăng nhập:" + part1 + "\nMật khẩu đăng nhập:" + matKhau);
        javaMailSender.send(mailMessage);
        Map<?, ?> uploadResult = null;
        try {
            uploadResult = imageService.upload(file, "avatar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String avatar = uploadResult.get("url").toString();
        uer.setAvatar(avatar);
        User u = userRepository.save(uer);
        Address address = userRequest.address();
        address.setUser(u);
        address.setStatus("on");
        Address a = addressRepository.save(address);
        return MessageUtil.builder().status(0).message("Thêm thành công !").type("bg-success").object(uer).build();
    }

    @Override
    public User detailStaff(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public Address detailAddress(Long id) {
        return addressRepository.getReferenceById(id);
    }

    @Override
    public void updateAddress(Long idKH, Long idAddress) {


    }

    @Override
    public MessageUtil doiThongTin(String pass) {
        User user = UserUltil.getUser();
        if (user == null) return MessageUtil.builder().status(0).message("Đã xảy ra lõi vui lòng đăng nhập lại !").type("bg-danger").build();
        user.setPassword(passwordEncoder.encode(pass));
        userRepository.save(user);
        return MessageUtil.builder().status(0).message("Đổi mật khẩu thông !").type("bg-success").build();
    }

    @Override
    public String dangKy(Model model, UserSignUpRequest userSignUpRequest, RedirectAttributes attributes) {
        if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
            model.addAttribute("emailExists", "email da ton tai");
            userSignUpRequest.setPassword("");
            userSignUpRequest.setConfirmPassword("");
            return "web/login";
        }if (userRepository.existsByPhoneNumber(userSignUpRequest.getPhoneNumber())) {
            model.addAttribute("sdtExists", "Số Điện thoại đã tồn tại");
            userSignUpRequest.setPassword("");
            userSignUpRequest.setConfirmPassword("");
            return "web/login";
        }
        if (userRepository.existsByUsername(userSignUpRequest.getUsername())) {
            model.addAttribute("usernameExists", "Username đã tồn tại");
            userSignUpRequest.setPassword("");
            userSignUpRequest.setConfirmPassword("");
            System.out.println("\n\n\n email da ton tai \n\n\n");
            return "web/login";
        }
        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword())) {
            model.addAttribute("error", "mat khau khong khop");
            return "web/login";
        }
        add(convert(userSignUpRequest));
        attributes.addFlashAttribute("thanhcong", "Tạo tài khaorn thành công ");
        return "redirect:/login";
    }

    @Override
    public String fogotPass(String email, RedirectAttributes attributes) {
        if (userRepository.existsByEmail(email)){
            User user = userRepository.getByEmail(email).get();
            String newPass = "123456abc";
            user.setPassword(passwordEncoder.encode(newPass));
            user = userRepository.save(user);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Mật Khảu Mới Của Bạn ");
            mailMessage.setText("Chào bạn :" + user.getUsername() + "\nMật khẩu mới của bạn là :" + newPass + "Hãy dùng nó để đăng nhập và đổi mật khẩu để đảm bảo an toàn");
            javaMailSender.send(mailMessage);
            attributes.addFlashAttribute("message",MessageUtil.builder().status(0).message("Vui Lòng kiểm tra email đẻ lấy thông tin đăng nhập").type("bg-success").build());
            return "redirect:/login";
        }
        attributes.addFlashAttribute("message",MessageUtil.builder().status(0).message("Thông tin không chính xác vui lòng thử lại").type("bg-danger").build());
        return "redirect:fogot-password";
    }

    private String generateCodeBill(int x) {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < x; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }


//    @Override
//    public List<UserDto> getAll(String status) {
//        if (status.equals("0")) {
//            return userRepository.getAll();
//        } else {
//            return userRepository.tkStatus(status);
//        }
//    }

}