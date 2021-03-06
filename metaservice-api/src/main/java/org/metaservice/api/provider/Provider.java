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

package org.metaservice.api.provider;

import org.jetbrains.annotations.NotNull;
import org.openrdf.repository.RepositoryConnection;

import java.util.HashMap;

public interface Provider<T>{
    public void provideModelFor(@NotNull T o,@NotNull RepositoryConnection resultConnection,@NotNull HashMap<String,String> properties) throws ProviderException;
}
