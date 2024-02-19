package com.ecommerce.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    private Long id;

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


    // Define CRUD permissions
    private static final RolePermission ADMIN_PERMISSIONS = new RolePermission
            (Role.ADMIN, Set.of("CREATE", "READ", "UPDATE", "DELETE", "MANAGE_USERS", "MANAGE_PRODUCTS", "VIEW_REPORTS"));
    private static final RolePermission CUSTOMER_PERMISSIONS = new RolePermission
            (Role.CUSTOMER, Set.of("READ", "VIEW_PRODUCTS","ADD_PRODUCTS_TO_CART","REMOVE_PRODUCTS_FROM_CART", "VIEW_SHOPPING_CART", "PLACE_ORDER", "VIEW_ORDERS"));
    private static final RolePermission SELLER_PERMISSIONS = new RolePermission
            (Role.SELLER, Set.of("CREATE", "READ", "UPDATE", "DELETE", "MANAGE_PRODUCTS", "ADD_PRODUCTS", "UPDATE_PRODUCTS", "DELETE_PRODUCTS"));

    public static boolean hasPermission(Role role, String permission) {
        for (RolePermission rolePermission : getRolePermissions(Role.CUSTOMER)) {
            if (rolePermission.getRole() == role && rolePermission.getPermissions().contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public static Set<RolePermission> getRolePermissions(Role role) {
        return Set.of(ADMIN_PERMISSIONS, CUSTOMER_PERMISSIONS, SELLER_PERMISSIONS);
    }
}
