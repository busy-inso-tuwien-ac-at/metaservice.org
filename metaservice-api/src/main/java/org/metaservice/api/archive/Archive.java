package org.metaservice.api.archive;

import java.util.List;

public interface Archive {
    String getContent(String time, String path);
    List<String> getTimes();
}
