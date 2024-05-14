package com.example.petProject.config;

import com.example.petProject.entity.*;
import com.example.petProject.entity.Module;
import com.example.petProject.enums.ModuleName;
import com.example.petProject.enums.UserType;
import com.example.petProject.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DBSeeder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final FeatureRepository featureRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DBSeeder(RoleRepository roleRepository, UserRepository userRepository, ModuleRepository moduleRepository,
                    FeatureRepository featureRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository)
            throws NoSuchMethodException {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.featureRepository = featureRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
        initData();
    }

    private void initData() throws NoSuchMethodException {
        logger.info("---------  Push data in DB Started  -----------");
        Role bookAdmin = null;
        Role bookStudent = null;

        if (moduleRepository.count() == 0) {
            logger.info("---------  Creating some module & features starts -----------");
            Module module = new Module();
            module.setModuleId(1L);
            module.setModuleName(ModuleName.BOOK.getName());
            module.setActive(Boolean.TRUE);

//            Module author = new Module();
//            author.setModuleId(2L);
//            author.setModuleName(ModuleName.AUTHOR.getName());
//            author.setActive(Boolean.TRUE);
//            author.setParentModule(module);
//
//            Module publisher = new Module();
//            publisher.setModuleId(3L);
//            publisher.setModuleName(ModuleName.PUBLISHER.getName());
//            publisher.setActive(Boolean.TRUE);
//            publisher.setParentModule(author);


            Feature feature = new Feature();
            feature.setFeatureId(1L);
            feature.setFeatureName("book-view");
            feature.setPrivilegeType("BOOK_VIEW");
            feature.setActive(Boolean.TRUE);
            feature.setUrl("/api/v1/book/{id}");
            feature.setModule(module);

            Feature feature2 = new Feature();
            feature2.setFeatureId(2L);
            feature2.setFeatureName("book-list-view");
            feature2.setPrivilegeType("BOOK_LIST_VIEW");
            feature2.setActive(Boolean.TRUE);
            feature2.setUrl("/api/v1/book/list");
            feature2.setModule(module);

            Feature feature3 = new Feature();
            feature3.setFeatureId(3L);
            feature3.setFeatureName("book-add");
            feature3.setPrivilegeType("BOOK_ADD");
            feature3.setActive(Boolean.TRUE);
            feature3.setUrl("/api/v1/book/save");
            feature3.setModule(module);

            Feature feature4 = new Feature();
            feature4.setFeatureId(4L);
            feature4.setPrivilegeType("BOOK_DELETE");
            feature4.setFeatureName("book-delete");
            feature4.setActive(Boolean.TRUE);
            feature4.setUrl("/api/v1/book/{id}");
            feature4.setModule(module);

            Feature feature5 = new Feature();
            feature5.setFeatureId(5L);
            feature5.setFeatureName("book-update");
            feature5.setPrivilegeType("BOOK_UPDATE");
            feature5.setActive(Boolean.TRUE);
            feature5.setUrl("/api/v1/book/update/{id}");
            feature5.setModule(module);



            Feature feature6 = new Feature();
            feature6.setFeatureId(6L);
            feature6.setFeatureName("author-update");
            feature6.setPrivilegeType("AUTHOR_UPDATE");
            feature6.setActive(Boolean.TRUE);
            feature6.setUrl("/api/v1/book/author/{id}");
            feature6.setModule(module);


            Feature feature7 = new Feature();
            feature7.setFeatureId(7L);
            feature7.setFeatureName("author-update");
            feature7.setPrivilegeType("AUTHOR_UPDATE");
            feature7.setActive(Boolean.TRUE);
            feature7.setUrl("/api/v1/book/author/{id}");
            feature7.setModule(module);



            module.getFeatures().add(feature);
            module.getFeatures().add(feature2);
            module.getFeatures().add(feature3);
            module.getFeatures().add(feature4);
            module.getFeatures().add(feature5);
            module.getFeatures().add(feature6);
            module.getFeatures().add(feature7);


            moduleRepository.save(module);
//            moduleRepository.save(author);
//            moduleRepository.save(publisher);
        }

        if (roleRepository.count() == 0) {
            logger.info("---------  Creating some role starts -----------");
            Role admin = new Role();
            admin.setRoleName(UserType.ADMIN.name());
            List<Feature> features = featureRepository.findByIds(Arrays.asList(2L,3L,4L));
//            admin.setFeatures(features);
            admin.setFeatures(features);
            bookAdmin = roleRepository.save(admin);

            Role user = new Role();
            user.setRoleName(UserType.USER.name());
            List<Feature> features2 = featureRepository.findByIds(Arrays.asList(1L,5L));
//            user.setFeatures(features2);
            user.setFeatures(features2);
            bookStudent = roleRepository.save(user);
        }
        if (userRepository.count() == 0) {
            logger.info("---------  Creating some user starts -----------");

            List<Feature> adminFeat = featureRepository.findByIds(Arrays.asList(2L,3L,4L)); // book list, delete book, save book
            List<Feature> stdFeat = featureRepository.findByIds(Arrays.asList(1L,5L));  // single book view, book update




            User admin = new User();
            admin.setUsername("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setEnabled(true);
            admin.setUserType(UserType.ADMIN);
            final List<Role> roles = new ArrayList<>();
            roles.add(bookAdmin);
            admin.setRoles(roles.stream().collect(Collectors.toSet()));
//            admin.setPermissions(new HashSet<>(adminFeat));
            userRepository.save(admin);


            User student = new User();
            student.setUsername("student@mail.com");
            student.setPassword(passwordEncoder.encode("5678"));
            student.setEnabled(true);
            student.setUserType(UserType.USER);
            final List<Role> roles2 = new ArrayList<>();
            roles2.add(bookStudent);
            student.setRoles(roles2.stream().collect(Collectors.toSet()));
//            student.setPermissions(new HashSet<>(stdFeat));
            userRepository.save(student);
        }

        if(bookRepository.count() == 0) {
            logger.info("---------  Creating some book starts -----------");
            List<Book> books = new ArrayList<>();

            Book book = new Book();
            book.setAuthor("author 1");
            book.setPublisher("publisher 1");
            book.setTitle("title 1");
            books.add(book);

            Book book2 = new Book();
            book2.setAuthor("author 2");
            book2.setPublisher("publisher 2");
            book2.setTitle("title 2");
            books.add(book2);

            Book book3 = new Book();
            book3.setAuthor("author 3");
            book3.setPublisher("publisher 3");
            book3.setTitle("title 3");
            books.add(book3);

            Book book4 = new Book();
            book4.setAuthor("author 4");
            book4.setPublisher("publisher 4");
            book4.setTitle("title 4");
            books.add(book4);

            bookRepository.saveAll(books);
        }
    }

}
