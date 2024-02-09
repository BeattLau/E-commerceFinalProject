package com.ecommerce.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Collections;
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

    private static final RolePermission ADMIN_PERMISSIONS = new RolePermission
            (Role.ADMIN, Set.of("CREATE", "READ", "UPDATE", "DELETE", "MANAGE_USERS", "MANAGE_PRODUCTS", "VIEW_REPORTS"));
    private static final RolePermission CUSTOMER_PERMISSIONS = new RolePermission
            (Role.CUSTOMER, Set.of("READ", "VIEW_PRODUCTS","ADD_PRODUCTS_TO_CART","REMOVE_PRODUCTS_FROM_CART", "VIEW_SHOPPING_CART", "PLACE_ORDER", "VIEW_ORDERS"));
    private static final RolePermission SELLER_PERMISSIONS = new RolePermission
            (Role.SELLER, Set.of("CREATE", "READ", "UPDATE", "DELETE", "MANAGE_PRODUCTS", "ADD_PRODUCTS", "UPDATE_PRODUCTS", "DELETE_PRODUCTS"));

    public static boolean hasPermission(Role role, String permission) {
        for (RolePermission rolePermission : getRolePermissions(role)) {
            if (rolePermission.getPermissions().contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public static Set<RolePermission> getRolePermissions(Role role) {
        switch (role) {
            case ADMIN:
                return Set.of(ADMIN_PERMISSIONS);
            case CUSTOMER:
                return Set.of(CUSTOMER_PERMISSIONS);
            case SELLER:
                return Set.of(SELLER_PERMISSIONS);
            default:
                return Collections.emptySet();
        }
    }
}
