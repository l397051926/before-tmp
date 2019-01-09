package com.gennlife.platform.service;

import com.gennlife.platform.model.User;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

@Service
public interface RwsServiceImpl {
    public String PreLiminary(JsonObject paramObj,User user);
}
