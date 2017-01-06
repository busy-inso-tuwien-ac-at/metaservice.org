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

package org.metaservice.core.nvd.cve;

//todo this class is unsafe
public class CharSequenceSegment implements CharSequence {
    private final CharSequence source;

 private final int start;
    private final int end;
    public CharSequenceSegment(CharSequence source, int start, int end) {
        this.source = source;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end-start;
    }

    @Override
    public char charAt(int index) {
        return source.charAt(start+index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharSequenceSegment(source,start+start,start+end);
    }

    @Override
    public String toString() {
        return source.subSequence(start,end).toString();
    }
}
