//package com.example.petProject.config;
//
//import com.example.petProject.controller.BookController;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.lang.reflect.Method;
//
//@Component
//public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        Class<BookController> bookControllerClass = BookController.class;
//        Method method;
//        try {
//            // Get the method object using reflection
//            method = bookControllerClass.getMethod("bookLists");
//            // Check if the method has the PreAuthorize annotation
//            if (method.isAnnotationPresent(PreAuthorize.class)) {
//                // Get the PreAuthorize annotation
//                PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
//                // Get the value of the annotation
//                String value = annotation.value();
//                // Extract the authority value from the annotation value
//                String authority = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));
////                return authority;
//                System.out.println("LOOKING IN THE VALUE OF AUTHORITY: " + authority);
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
////        return null;
//
//    }
//}
//
//
//
//
//
