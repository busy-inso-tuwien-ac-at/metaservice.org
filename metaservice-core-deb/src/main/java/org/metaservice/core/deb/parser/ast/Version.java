package org.metaservice.core.deb.parser.ast;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 15.10.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class Version extends SuperNode {
    public String epoch;
    public String upstreamVersion;
    public String debversion;

    public String relation = "=";

    @NotNull
    @Override
    public String toString() {
        boolean needParanthesis = (relation != null);
        if(epoch!=null || upstreamVersion!= null ||debversion!= null)
            return (needParanthesis?"(":"") + ((relation!= null)? relation + " ":"")+ ((epoch != null)?epoch + ':':"") + upstreamVersion + ((debversion!=null)?'-'+ debversion :"") +((needParanthesis)?')':"");
        else
            return "";
    }


    @NotNull
    public String toFileNameString(){
        if(epoch!=null || upstreamVersion!= null ||debversion!= null)
            return  ((epoch != null)?epoch + '.':"") + upstreamVersion + ((debversion!=null)?'-'+ debversion :"");
        else
            return "";
    }
}
