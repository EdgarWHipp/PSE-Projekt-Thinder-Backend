package com.pse.thinder.backend.databaseFeatures.account;

import java.io.Serializable;

public enum UserGroup implements Serializable{
	
    GROUP_STUDENT("STUDENT"), GROUP_SUPERVISOR("SUPERVISOR");
	
	private final String type;
	
	private UserGroup(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
