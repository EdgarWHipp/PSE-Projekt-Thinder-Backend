package com.pse.thinder.backend.services.swipestrategy;

import java.util.List;
import java.util.UUID;

public interface ThesisSelectI {

    public List<UUID> getThesisIdList(UUID userId);
}
