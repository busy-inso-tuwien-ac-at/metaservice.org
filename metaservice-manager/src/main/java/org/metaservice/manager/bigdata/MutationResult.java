package org.metaservice.manager.bigdata;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by ilo on 21.02.14.
 */
@XmlRootElement(name = "data")
public class MutationResult {

  private Integer  modified;
    private Integer rangeCount;
    private Integer milliseconds;

    @XmlAttribute
    public Integer getRangeCount() {
        return rangeCount;
    }

    public void setRangeCount(Integer rangeCount) {
        this.rangeCount = rangeCount;
    }

    @XmlAttribute
    public Integer getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Integer milliseconds) {
        this.milliseconds = milliseconds;
    }

    @XmlAttribute
    public Integer getModified() {
        return modified;
    }

    public void setModified(Integer modified) {
        this.modified = modified;
    }


}
