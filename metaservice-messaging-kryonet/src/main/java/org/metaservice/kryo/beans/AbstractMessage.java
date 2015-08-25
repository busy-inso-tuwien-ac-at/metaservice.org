package org.metaservice.kryo.beans;

import org.bson.types.ObjectId;

/**
 * Created by ilo on 07.06.2014.
 */
public class AbstractMessage {

    ObjectId _id;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
