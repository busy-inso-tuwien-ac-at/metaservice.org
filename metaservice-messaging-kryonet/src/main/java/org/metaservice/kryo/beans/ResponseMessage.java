/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
