package org.metaservice.api.postprocessor;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import sun.misc.MessageUtils;

import javax.management.remote.rmi._RMIConnectionImpl_Tie;
import java.util.ArrayList;
import java.util.List;

import static org.metaservice.api.postprocessor.SelectHeader.format;

/**
 * Created by ilo on 04.03.14.
 */
public interface SPARQLQueryBuilder {

    public SelectHeader select();
    public AskHeader ask();
    public ConstructHeader construct();
    public SPARQLQueryBuilder orderByAsc(Variable variable);
    public SPARQLQueryBuilder orderByDesc(Variable variable);
    public SPARQLQueryBuilder limit(int i);
    public String build();
    public WhereClause where();


}


class Default  implements SPARQLQueryBuilder{
    private QueryHeader queryHeader;
    private WhereClause whereClause;

    private List<String> order = new ArrayList<>();
    Integer limit= null;
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
    public SPARQLQueryBuilder orderByAsc(Variable variable) {
        order.add("ASC(" +format(variable) +")");
        return this;
    }

    @Override
    public SPARQLQueryBuilder orderByDesc(Variable variable) {
        order.add("DESC(" +format(variable) +")");
        return this;
    }

    @Override
    public SPARQLQueryBuilder limit(int i) {
        this.limit = i;
        return this;
    }

    @Override
    public String build() {
        StringBuilder result = new StringBuilder();
        result.append(queryHeader);
        result.append(" ");
        if(whereClause!=null)
            result.append(whereClause);
        result.append(" ");
        if(order.size() > 0){
            result.append(" ORDER BY ");
            result.append(StringUtils.join(order," "));
        }
        if(limit != null){
            result.append(" LIMIT ")
                    .append(limit);
        }
        return result.toString();
    }

    @Override
    public WhereClause where() {
        if(whereClause == null)
            whereClause =new WhereClause(this);
        return whereClause;
    }

    public static SPARQLQueryBuilder getInstance(){
        return new Default();
    }
}


interface QueryHeader{

}

class SelectHeader extends AbstractDelegate  implements QueryHeader{

    private String distinct = "";
    private List<String> variables = new ArrayList<>();


    public SelectHeader(SPARQLQueryBuilder aDefault) {
        super(aDefault);
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
    public SelectHeader count(@NotNull Variable variable,@Nullable Variable as){
        return aggregate("COUNT",variable,as);
    }
    @NotNull
    public SelectHeader count(@NotNull Variable variable){
        return count(variable,null);
    }
    @NotNull
    public SelectHeader max(@NotNull Variable variable,@Nullable Variable as){
        return aggregate("MAX",variable,as);
    }
    @NotNull
    public SelectHeader max(@NotNull Variable variable){
        return max(variable,null);
    }
    @NotNull
    public SelectHeader min(@NotNull Variable variable,@Nullable Variable as){
        return aggregate("MIN",variable,as);
    }
    @NotNull
    public SelectHeader min(@NotNull Variable variable){
        return min(variable,null);
    }

    @NotNull
    public SelectHeader aggregate(@NotNull String aggregate,@NotNull Variable variable,@Nullable Variable as){
        if(as == null){
            variables.add(aggregate+ "("+format(variable)+")");
        }else {
            variables.add("("+aggregate+"(" + format(variable) +") as "+ format(as)+")");
        }
        return this;
    }

    public SelectHeader var(Variable variable) {
        variables.add(format(variable));
        return this;
    }

    public SelectHeader var(Value value,Variable as) {
        variables.add("("+format(value)+" as " +format(as));
        return this;
    }

    public static String format(Value value) {
        if(value instanceof Variable){
            return "?"+value;
        }
        if(value instanceof URI){
            return "<" + value.stringValue() +">";
        }
        if(value instanceof Literal){
            return value.toString();
        }
        throw new IllegalArgumentException();
    }
}

class ConstructHeader extends AbstractDelegate implements QueryHeader{
    private String quadmode = " ";
    private List<String> patterns = new ArrayList<>();

    public ConstructHeader(SPARQLQueryBuilder aDefault) {
        super(aDefault);
    }

    public ConstructHeader triple(Value s,Value p ,Value o){
        patterns.add(format(s)+ " " + format(p) + " " + format(o) + ". ");
        return this;
    }

    public ConstructHeader quad(Value s,Value p,Value o, Value c) {
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

class WhereClause extends AbstractDelegate{
    StringBuilder builder = new StringBuilder();

    public WhereClause(SPARQLQueryBuilder aDefault) {
        super(aDefault);
    }
    public WhereClause filter(){
        builder.append("FILTER (");
        builder.append(")");
        return this;
    }

    public WhereClause filterExists(){
        builder.append("FILTER  EXISTS {");
        builder.append("}");
        return this;
    }
    public WhereClause filterNotExists(){
        builder.append("FILTER NOT EXISTS {");
        builder.append("}");
        return this;
    }

    public WhereClause pattern(Value s, Value  p, Value o) {
        builder.append(format(s))
                .append(" ")
                .append(format(p))
                .append(" ")
                .append(format(o));
        return this;
    }
    public WhereClause pattern(Value s, Value  p, Value o,Value c) {
        builder.append("GRAPH ")
                .append(format(c))
                .append("{")
                .append(format(s))
                .append(" ")
                .append(format(p))
                .append(" ")
                .append(format(o))
        .append("}");

        return this;
    }

    public String toString(){
        return  "{" + builder.toString() +"}";
    }
}

class AskHeader extends AbstractDelegate implements QueryHeader{

    public AskHeader(SPARQLQueryBuilder aDefault) {
        super(aDefault);
    }
}

abstract class AbstractDelegate implements SPARQLQueryBuilder{
    private final SPARQLQueryBuilder sparqlQueryBuilder;

    @Override
    public WhereClause where() {
        return sparqlQueryBuilder.where();
    }

    protected AbstractDelegate(SPARQLQueryBuilder sparqlQueryBuilder) {
        this.sparqlQueryBuilder = sparqlQueryBuilder;
    }

    @Override
    public SelectHeader select() {
        return sparqlQueryBuilder.select();
    }

    @Override
    public AskHeader ask() {
        return sparqlQueryBuilder.ask();
    }

    @Override
    public ConstructHeader construct() {
        return sparqlQueryBuilder.construct();
    }

    @Override
    public SPARQLQueryBuilder orderByAsc(Variable variable) {
        return sparqlQueryBuilder.orderByAsc(variable);
    }

    @Override
    public SPARQLQueryBuilder orderByDesc(Variable variable) {
        return sparqlQueryBuilder.orderByDesc(variable);
    }

    @Override
    public SPARQLQueryBuilder limit(int i) {
        return sparqlQueryBuilder.limit(i);
    }

    @Override
    public String build() {
        return sparqlQueryBuilder.build();
    }
}

class Variable implements Value{
    private final String name;

    Variable(String name) {
        this.name = name;
    }

    @Override
    public String stringValue() {
        return name;
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
