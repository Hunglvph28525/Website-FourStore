package com.poly.datn.service.impl;

import com.poly.datn.dto.UserDto;
import com.poly.datn.dto.UserRequest;
import com.poly.datn.entity.Address;
import com.poly.datn.entity.Product;
import com.poly.datn.entity.User;
import com.poly.datn.repository.AddressRepository;
import com.poly.datn.repository.RoleRepository;
import com.poly.datn.repository.UserRepository;
import com.poly.datn.request.UserSignUpRequest;
import com.poly.datn.request.forgot_passwort.UserForgotPasswordRequest;
import com.poly.datn.service.ImageService;
import com.poly.datn.service.UserService;
import com.poly.datn.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
//        user.setRoles(Collections.singletonList(roleRepository.getByName("ROLE_USER")));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUser(username).get();
    }


    @Override
    public User convert(UserSignUpRequest source) {
        return User.builder()
                .email(source.getEmail())
                .username(source.getUserName())
                .password(source.getPassword())
                .build();
    }

    @Override
    public Optional<User> changePassword(UserForgotPasswordRequest userForgotPasswordRequest) {

        Optional<User> userOptional = this.userRepository.getByUser(userForgotPasswordRequest.getUserName());

        userOptional.ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(userForgotPasswordRequest.getPassword()));
            userRepository.save(user);
            System.out.println("\n\n\t user user saved with password: " + userForgotPasswordRequest.getPassword() + "encoded: " + user.getPassword() + "\n\n\t");
        });

        return userOptional;
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
        return MessageUtil.builder().status(0).message("Thêm thành công !").type("bg-success").object(uer).build();


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