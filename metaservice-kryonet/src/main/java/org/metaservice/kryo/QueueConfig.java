package org.metaservice.kryo;

import org.bson.types.ObjectId;
import org.metaservice.kryo.beans.RegisterClientMessage;

/**
 * Created by ilo on 09.06.2014.
 */
public class QueueConfig {
    private ObjectId _id;
    private ObjectId last;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    private String name;
    private RegisterClientMessage.Type type;

    @Override
    public String toString() {
        return "QueueConfig{" +
                "_id=" + _id +
                ", last=" + last +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public ObjectId getLast() {
        return last;
    }

    public void setLast(ObjectId last) {
        this.last = last;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegisterClientMessage.Type getType() {
        return type;
    }

    public void setType(RegisterClientMessage.Type type) {
        this.type = type;
    }
}
