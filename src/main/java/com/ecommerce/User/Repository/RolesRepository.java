package com.ecommerce.User.Repository;
import com.ecommerce.User.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}