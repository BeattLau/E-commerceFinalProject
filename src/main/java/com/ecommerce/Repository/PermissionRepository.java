package com.ecommerce.Repository;

import com.ecommerce.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    default Optional<Permission.RolePermission> findByRole(Permission.Role role) {
        Permission permission = new Permission();
        return permission.getRolePermissions(role).stream()
                .findFirst();
    }
}
