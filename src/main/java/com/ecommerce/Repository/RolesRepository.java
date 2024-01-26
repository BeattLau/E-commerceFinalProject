package com.ecommerce.Repository;
import com.ecommerce.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByRole_name(String role_name);
}