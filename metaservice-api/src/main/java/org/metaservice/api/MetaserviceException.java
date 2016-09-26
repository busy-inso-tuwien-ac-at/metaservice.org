/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.api;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 21.11.13
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class MetaserviceException extends Exception {
    public MetaserviceException() {
    }

    public MetaserviceException(String message) {
        super(message);
    }

    public MetaserviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaserviceException(Throwable cause) {
        super(cause);
    }

    public MetaserviceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
