package com.example.testing.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DataServiceDev implements DataService{

    @Override
    public String getData() {
        return "get Dev Data";
    }
}
