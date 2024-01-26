package com.ecommerce.Entity;
import lombok.Getter;

import java.util.Set;

public class Permission {
    public enum Role {
        ADMIN, SELLER, CUSTOMER
    }
    @Getter
    public static class RolePermission {
        private final Role role;
        private final Set<String> permissions;

        public RolePermission(Role role, Set<String> permissions) {
            this.role = role;
            this.permissions = permissions;
        }
    }
    private static final RolePermission ADMIN_PERMISSIONS = new RolePermission
            (Role.ADMIN, Set.of("MANAGE_USERS", "MANAGE_PRODUCTS", "VIEW_REPORTS"));
    private static final RolePermission CUSTOMER_PERMISSIONS = new RolePermission
            (Role.CUSTOMER, Set.of("VIEW_PRODUCTS", "VIEW_SHOPPING_CART", "PLACE_ORDER", "VIEW_PROFILE"));
    private static final RolePermission SELLER_PERMISSIONS = new RolePermission
            (Role.SELLER, Set.of("MANAGE_PRODUCTS", "ADD_PRODUCTS", "UPDATE_PRODUCTS", "DELETE_PRODUCTS"));

    public static boolean hasPermission(Role role, String permission) {
        for (RolePermission rolePermission : getRolePermissions()) {
            if (rolePermission.getRole() == role && rolePermission.getPermissions().contains(permission)) {
                return true;
            }
        }
        return false;
    }
    private static Set<String> getPermissionsForRole(Role role) {
        for (RolePermission rolePermission : getRolePermissions()) {
            if (rolePermission.getRole() == role) {
                return rolePermission.getPermissions();
            }
        }
        return Set.of();
    }
    private static Set<RolePermission> getRolePermissions() {
        return Set.of(ADMIN_PERMISSIONS, CUSTOMER_PERMISSIONS, SELLER_PERMISSIONS);
    }
}