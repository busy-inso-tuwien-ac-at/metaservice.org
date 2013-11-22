package org.metaservice.api.provider;

import java.util.Date;

public interface ProviderContext {
    public Class<? extends Provider> getProviderClass();
    public String getProviderVersion();

    public Date getRunStartDate();
    public Date getRunEndDate();

    public Date getArchiveDate();
    public String getArchive();
    public String getArchivePath();

    public Date getLastRunDate();
    public Date getLastChangeDate();
}
