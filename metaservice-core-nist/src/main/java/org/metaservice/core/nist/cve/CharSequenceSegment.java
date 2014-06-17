package org.metaservice.core.nist.cve;

//todo this class is unsafe
public class CharSequenceSegment implements CharSequence {
    private final char[] source;

 private final int start;
    private final int end;
    public CharSequenceSegment(char[] source, int start, int end) {
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
        return source[start+index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharSequenceSegment(source,start+start,start+end);
    }
}
