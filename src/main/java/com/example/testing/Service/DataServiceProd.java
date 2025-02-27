package com.example.testing.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class DataServiceProd implements DataService{
    @Override
    public String getData() {
        return "get Data Prod";
    }
}
