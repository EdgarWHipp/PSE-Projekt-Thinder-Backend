package com.pse.thinder.backend.databaseFeatures.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public enum Role {
    SUPERVISOR ("supervisor"), STUDENT ("student");

    public String role;

    Role(String role) {
        this.role = role;
    }

    public String getString() {
        return role;
    }
}
