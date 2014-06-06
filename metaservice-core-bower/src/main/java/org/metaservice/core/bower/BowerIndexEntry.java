package org.metaservice.core.bower;

import java.net.URL;
import java.util.Date;

/**
 * Created by ilo on 01.06.2014.
 */
public class BowerIndexEntry  {
    private String name;
    private String owner;
    private URL website;
    private String forks;
    private String stars;
    private Date updated;
    private Date created;

    @Override
    public String toString() {
        return "BowerIndexEntry{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", website=" + website +
                ", forks='" + forks + '\'' +
                ", stars='" + stars + '\'' +
                ", updated=" + updated +
                ", created=" + created +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }

    public String getForks() {
        return forks;
    }

    public void setForks(String forks) {
        this.forks = forks;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
