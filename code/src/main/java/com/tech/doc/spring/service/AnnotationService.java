package main.java.com.tech.doc.spring.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@ThreadNamePattern("test-thread")
@Service
public class AnnotationService {
    
    public void test() {
        System.out.println("test");
    }


    @Async
    @ThreadNamePattern("process-task-${id}")
    public void processTask(String id) {
        System.out.println("processTask");
    }
}
