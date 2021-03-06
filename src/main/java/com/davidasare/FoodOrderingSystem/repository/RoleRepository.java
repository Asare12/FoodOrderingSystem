package com.davidasare.FoodOrderingSystem.repository;

import com.davidasare.FoodOrderingSystem.model.Role;
import com.davidasare.FoodOrderingSystem.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
