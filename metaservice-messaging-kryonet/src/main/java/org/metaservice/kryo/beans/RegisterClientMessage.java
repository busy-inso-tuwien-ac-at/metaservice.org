package org.metaservice.kryo.beans;

/**
 * Created by ilo on 07.06.2014.
 */
public class RegisterClientMessage  extends AbstractMessage{
    public static enum Type{
        PROVIDER_REFRESH,PROVIDER_CREATE,POSTPROCESS
    }
    private Type type;
    private String name;
    private int messageCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String toString() {
        return "RegisterClientMessage{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", messageCount=" + messageCount +
                '}';
    }
}
