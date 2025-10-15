package com.example.dscommerce.tests;

import com.example.dscommerce.entities.Role;
import com.example.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {
    public static User createClientUser() {
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888",  LocalDate.parse("2001-07-25"), "$2a$10$lRo76SOP3HtDgeKw65fs0.SqG7TreUeAWrStp.ZqDEpYocO2H/znG");
        user.addRole(new Role("ROLE_CLIENT", 1L));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(2L, "Alex Green", "alex@gmail.com", "977777777",  LocalDate.parse("1987-12-13"), "$2a$10$lRo76SOP3HtDgeKw65fs0.SqG7TreUeAWrStp.ZqDEpYocO2H/znG");
        user.addRole(new Role("ROLE_ADMIN", 2L));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username) {
        User user = new User(id, "Alex Green", username, "977777777",  LocalDate.parse("1987-12-13"), "$2a$10$lRo76SOP3HtDgeKw65fs0.SqG7TreUeAWrStp.ZqDEpYocO2H/znG");
        user.addRole(new Role("ROLE_ADMIN", 2L));
        return user;
    }

    public static User createCustomClientUser(Long id, String username) {
        User user = new User(id, "Maria Brown", username, "988888888",  LocalDate.parse("2001-07-25"), "$2a$10$lRo76SOP3HtDgeKw65fs0.SqG7TreUeAWrStp.ZqDEpYocO2H/znG");
        user.addRole(new Role("ROLE_CLIENT", 1L));
        return user;
    }
}
