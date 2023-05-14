package com.example.house.batch.service;

import org.springframework.stereotype.Service;

@Service
public class PrintNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
