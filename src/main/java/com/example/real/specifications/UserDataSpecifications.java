package com.example.real.specifications;

import com.example.real.model.UserData;
import com.example.real.model.UserData_;
import org.springframework.data.jpa.domain.Specification;

public class UserDataSpecifications {
    public static Specification<UserData> findByEmail(String email) {
        return (root, query, cb) -> {
            return cb.equal(root.get(UserData_.email), email);

        };
    }
}


