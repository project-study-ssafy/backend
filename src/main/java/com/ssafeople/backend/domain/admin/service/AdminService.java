package com.ssafeople.backend.domain.admin.service;

import com.ssafeople.backend.domain.user.domain.User;

public interface AdminService {

    User login(String email, String password);
}
