package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import java.util.Map;

public interface AppUrlService {
    String buildResetUrl(String rawToken);
    String buildUrl(String path, Map<String, String> queryParams);
}

