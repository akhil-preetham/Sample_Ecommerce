package com.ecommerce.user.repository;

import com.ecommerce.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(String userId);

    void deleteByUserId(String userId);
}
