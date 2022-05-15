package com.example.f_vacc;

import java.io.IOException;
import java.util.List;

public interface DatabaseAccessCallback {
    void QueryResponse(List<String[]> data);
}