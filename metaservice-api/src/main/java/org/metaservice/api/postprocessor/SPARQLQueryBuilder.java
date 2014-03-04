package org.metaservice.api.postprocessor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 04.03.14.
 */
public interface SPARQLQueryBuilder {

    public SelectHeader select();
    public AskHeader ask();
    public ConstructHeader construct();
    public SPARQLQueryBuilder orderByAsc();
    public SPARQLQueryBuilder orderByDesc();
    public SPARQLQueryBuilder limit();
    public String build();

}


class Default  implements SPARQLQueryBuilder{
    private QueryHeader queryHeader;
    private WhereClause whereClause;

    public Default() {
        queryHeader = null;
        whereClause =null;
    }

    public SelectHeader select(){
        if(queryHeader != null && !(queryHeader instanceof SelectHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new SelectHeader(this);
        return (SelectHeader) queryHeader;
    }

    @Override
    public AskHeader ask() {
        if(queryHeader != null && !(queryHeader instanceof AskHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new AskHeader(this);
        return (AskHeader) queryHeader;
    }

    @Override
    public ConstructHeader construct() {
        if(queryHeader != null && !(queryHeader instanceof ConstructHeader)){
            throw new UnsupportedOperationException("no multiple headers");
        }
        if(queryHeader == null)
            queryHeader =new ConstructHeader(this);
        return (ConstructHeader) queryHeader;
    }

    @Override
    public String build() {
        return queryHeader.toString() +" " + whereClause.toString();
    }

    public static SPARQLQueryBuilder getInstance(){
        return new Default();
    }
}


interface QueryHeader{

}

class SelectHeader  implements QueryHeader{

    private String distinct = "";
    private List<String> variables = new ArrayList<>();

    public SelectHeader(Default aDefault) {

    }

    public  SelectHeader distinct(){
        distinct = "DISTINCT ";
        return this;
    }
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("SELECT ")
                .append(distinct);
        for(String variable : variables){
            stringBuilder.append(variable);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @NotNull
    public SelectHeader count(@NotNull String variable,@Nullable String as){
        return aggregate("COUNT",variable,as);
    }
    @NotNull
    public SelectHeader count(@NotNull String variable){
        return count(variable,null);
    }
    @NotNull
    public SelectHeader max(@NotNull String variable,@Nullable String as){
        return aggregate("MAX",variable,as);
    }
    @NotNull
    public SelectHeader max(@NotNull String variable){
        return max(variable,null);
    }
    @NotNull
    public SelectHeader min(@NotNull String variable,@Nullable String as){
        return aggregate("MIN",variable,as);
    }
    @NotNull
    public SelectHeader min(@NotNull String variable){
        return min(variable,null);
    }

    @NotNull
    public SelectHeader aggregate(@NotNull String aggregate,@NotNull String variable,@Nullable String as){
        if(as == null){
            variables.add(aggregate+ "(?"+variable+")");
        }else {
            variables.add("("+aggregate+"(?" + variable +") as "+ "?"+as+")");
        }
        return this;
    }
}

class ConstructHeader implements QueryHeader{
    private String quadmode = " ";
    private List<String> patterns = new ArrayList<>();


    public ConstructHeader(Default aDefault) {

    }

    public ConstructHeader triple(String s,String p ,String o){
        patterns.add("?" + s + " ?" + p + " ?" + o + ". ");
        return this;
    }

    public ConstructHeader quad(String s,String p,String o, String c) {
        quadmode =  "_QUADMODE_ ";
        patterns.add("GRAPH ?" + c + "{ ?" + s + " ?" + p + " ?" + o + " }. ");
        return this;
    }
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CONSTRUCT{");
        stringBuilder.append(quadmode);
        for(String pattern : patterns){
             stringBuilder.append(pattern);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

class WhereClause{

    public void filter();
    public void filterExists();
    public void filterNotExists();
}

class AskHeader implements QueryHeader{

    public AskHeader(Default aDefault) {

    }
}
