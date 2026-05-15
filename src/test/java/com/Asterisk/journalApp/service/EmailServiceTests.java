package com.Asterisk.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void  testSendMail(){
        emailService.sendEmail("siddarthsid957@gmail.com",
                "Testing java mail sender",
                "Hi, how are you");
    }


}
