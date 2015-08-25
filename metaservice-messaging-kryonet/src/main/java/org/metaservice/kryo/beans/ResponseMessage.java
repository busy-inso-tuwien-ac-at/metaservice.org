package org.metaservice.kryo.beans;

/**
 * Created by ilo on 07.06.2014.
 */
public class ResponseMessage extends AbstractMessage{
    private AbstractMessage aboutMessage;

    public void setAboutMessage(AbstractMessage abstractMessage) {
        this.aboutMessage = abstractMessage;
    }

    public enum Status{OK,FAILED}
    private Status status;
    private String errorMessage;

    public AbstractMessage getAboutMessage() {
        return aboutMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                " status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", aboutMessage=" + aboutMessage +
                '}';
    }
}
