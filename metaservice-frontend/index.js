function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}
function loadError(title,content){
    var modalBody =$('#loader').find('.modal-body');
    modalBody.empty();
    modalBody.append('<div class="alert alert-danger fade in"><strong>' + title+ '</strong><br>'+content+'</div>');
}

function initHandleBars() {
    Handlebars.registerPartial("copyInputField", $("#partial-copytextinputfield").html());

}

var MS;
function initMs() {
    MS = {};
    MS.context = {
        "admssw": "http://purl.org/adms/sw/",
        "bds": "http://www.bigdata.com/rdf/search#",
        "cc": "http://creativecommons.org/ns#",
        "dc": "http://purl.org/dc/elements/1.1/",
        "dcterms": "http://purl.org/dc/terms/",
        "doap": "http://usefulinc.com/ns/doap#",
        "foaf":"http://xmlns.com/foaf/0.1/",
        "ms": "http://metaservice.org/ns/metaservice#",
        "ms-deb": "http://metaservice.org/ns/metaservice-deb#",
        "owl": "http://www.w3.org/2002/07/owl#",
        "rad": "http://www.w3.org/ns/radion#",
        "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
        "rdfs":"http://www.w3.org/2000/01/rdf-schema#",
        "sf": "http://sourceforge.net/api/sfelements.rdf#",
        "skos":"http://www.w3.org/2004/02/skos/core#",
        "vcard":"http://www.w3.org/2006/vcard/ns#",
        "xhv":"http://www.w3.org/1999/xhtml/vocab#",
        "xsd": "http://www.w3.org/2001/XMLSchema#"
    };
    MS.namespaces = '';
    $.each(MS.context,function(prefix,uri){
        MS.namespaces += 'PREFIX ' + prefix + ': <' + uri +'>\n';
    });
    MS.resourceUrl = document.location.toString().replace(/\/$/,'');
}

function handleSearch(){
    var q = getURLParameter("q");
    var limit = parseInt(getURLParameter("limit"),10);
    if(!limit){
        limit = 10;
    }
    var offset = parseInt(getURLParameter("offset"),10);
    if(!offset){
        offset = 0;
    }
    var page = (offset/limit) +1;


    var query = MS.namespaces
        +"SELECT DISTINCT"
        +" ?subject ?title ?type ?relevance "
        +" WHERE {"
        +"  ?title bds:search '" + q + "' ;"
        +"         bds:relevance  ?relevance ."
        +"  ?subject a ?type; "
        +"           dc:title ?title "
        +"     FILTER ("
        +"         ?type != <http://www.w3.org/2000/01/rdf-schema#Resource> "
        +"     )"
        +"}"
        +" ORDER BY ?relevance ?title "
        +" LIMIT   " + limit
        +" OFFSET  " +offset;

    $.ajax({
        url: "/sparql",
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Accept","application/sparql-results+json");
        },
        dataType: "json",
        type: "post",
        data: {
            "query": query
        },
        success: function(data){
            if(data.results.bindings == 0){
                loadError('No Data Found','There was no response :-(');
                return;
            }
            console.log(data);
            var result = {
                result:[],
                pagination:{
                    next:{
                        url:"http://metaservice.org/d/search?q=" + q + "&limit=" + limit + "&offset="+ (offset + limit)
                    },
                    prev:{
                        url:"http://metaservice.org/d/search?q=" + q + "&limit=" + limit + "&offset="+ (offset - limit)},
                    pages:[
                    ]},
                query:q
            };
            $.each(data.results.bindings,function(index,d){
                if(d.subject && d.title )
                    result.result.push({
                        subject: d.subject.value,
                        title: d.title.value,
                        type: d.type.value
                    });
            });
            if(result.result.length < limit){
                //last page
                result.pagination.next = null;
            }
            if(page == 1){
                result.pagination.prev = null;
            }
            for(var i = page -3 ; i < page+4;i++){
                if(i>0){
                    result.pagination.pages.push({url:"http://metaservice.org/d/search?q=" + q + "&limit=" + limit + "&offset="+(offset + (i-page)*limit),title:i, selected: i == page});
                }
            }
            console.log(result);



            template  = Handlebars.compile($("#searchResults").html());
            $('#content').append(template(result));
            $('#loader').modal('hide');

        },
        error: function(){
            loadError('Service unreachable','Could not reach the service /sparql :-(');
        }
    });
}

function getProvenanceContent(element){
    return $(element).text();
}
function getProvenanceTitle(element){
    return $(element).text();
}

function jsonLdFollowIris(source,iri){
    if(source[iri]){
        var obj = source[iri];
        if($.isArray(obj) || $.isPlainObject(obj)){
            $.each(obj,function(dindex,d){
                if(dindex == '@type'){
                    if($.isArray(d) || $.isPlainObject(d)){
                        $.each(d,function(xindex,x){
                            d[xindex] = jsonLdFollowIris(source,x);
                        });
                    }else{
                        obj[dindex] = jsonLdFollowIris(source,d);
                    }
                }else if($.isArray(d) || $.isPlainObject(d)){
                    if(d['@id']){
                        var id = d['@id'];
                        //break loops
                        delete obj[dindex];
                        obj[dindex] = jsonLdFollowIris(source,id);
                    }else{
                        $.each(d,function(xindex,x){
                            if(!x){
                                console.log(iri);
                                console.log(dindex);
                                console.log(JSON.stringify(d));
                                console.log(xindex);
                            }
                            if(x && x['@id']){
                                var id = x['@id'];
                                //break loops
                                delete d[xindex];
                                d[xindex] = jsonLdFollowIris(source,id);
                            }
                        });
                    }
                }
            });
        }
        return obj;
    }
    return iri;
}


function jsonLdUnion(array){
    var result = {};
    $.each(array,function(graphIndex,graph){
        $.each(graph["@graph"],function(valueIndex,value){
            if(!result[value["@id"]]){
                console.log('FOOLONG');
                result[value["@id"]] = {'ms:id':value['@id']};
            }
            var object = result[value["@id"]];
            $.each(value,function(dataIndex,data){
                if(dataIndex.match(/^@/) && dataIndex != '@type' )
                {
                    console.log("return");
                    return;
                }
                var toProcess;
                if(data["@value"]){
                    toProcess = data["@value"];
                }else{
                    toProcess = data;
                }
                if(toProcess){
                    if(!object[dataIndex]){
                        object[dataIndex] = toProcess;
                    } else {
                        if($.isArray(object[dataIndex]) && $.isArray(toProcess)){
                            for(var i = 0; i < toProcess.length;i++){
                                if($.inArray(toProcess[i],object[dataIndex])==-1){
                                    object[dataIndex].push(toProcess[i]);
                                }
                            }
                        } else if ($.isArray(object[dataIndex])){
                            if($.inArray(toProcess,object[dataIndex])==-1){
                                object[dataIndex].push(toProcess);
                            }
                        } else if ($.isArray(toProcess)){
                            if($.inArray(object[dataIndex],toProcess)){
                                toProcess.push(object[dataIndex]);
                                object[dataIndex] = toProcess;
                            }
                        } else {
                            object[dataIndex] = [object[dataIndex],toProcess];
                        }
                    }
                }
            });
        });
    });
    console.log(result);
    return result;
}


function handleResource(){
    var query = MS.namespaces +
        "CONSTRUCT {_QUADMODE_ \n"+
        "    GRAPH ?graph {?resource ?relation ?value}.\n"+
        "    GRAPH ?ggraph {?graph ?grelation ?gvalue}.\n"+
        "    GRAPH ?vgraph { ?value ?vrelation ?vvalue}.\n"+
        "    GRAPH ?vggraph {?vgraph ?vgrelation ?vgvalue}.\n"+
        "    GRAPH ?vggraph {?vgraph <ex:type> 'value'}.\n" +
        "    GRAPH ?vggraph {?vgraph <ex:reason> ?value}.\n" +
        "}\n" +
        "WHERE{\n"+
        "    {\n" +
        "        SELECT DISTINCT ?graph ?relation ?value {\n"+
        "            GRAPH ?graph { ?resource ?relation   ?value}.\n"+
        "            OPTIONAL { GRAPH ?ggraph { ?graph ?grelation ?gvalue.}}\n"+
        "        }\n" +
        "    }.\n"+
        "    OPTIONAL {\n"+
        "        GRAPH ?vgraph { ?value ?vrelation ?vvalue}.\n"+
        "        OPTIONAL { GRAPH ?vggraph {?vgraph ?vgrelation ?vgvalue.}} \n"+
     //   "        FILTER(! strstarts(str(?value),'http://metaservice.org/ns/metaservice-deb#')) \n" + //todo remove when db cleaned
        "    }\n"+
        "}";
    query = query.split('?resource').join('<' + encodeURI(MS.resourceUrl) + '>');
    $.ajax({
        url: "/sparql",
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Accept","application/ld+json");
        },
       dataType: "json",
        type: "post",
        data: {
            "query": query,
            "infer": "true"
        },
        success: function(data){

            if(data.length == 0){
                loadError('No Data Found','Metaservice does not have any data for this uri :-(');
                return;
            }
            var result =jsonLdUnion(data);
            jsonld.compact(result,MS.context,function(err,compacted){
                result = compacted;
                result = jsonLdFollowIris(result,MS.resourceUrl);
                console.log(data);
                console.log(result);

                var templatePath = [];
                var type = result['@type'];
                console.log(type);
                if($.isArray(type)){
                    $.each(type,function(index,d){
                        if(d['ms:view']){
                            templatePath.push("/" + d['ms:view']);
                        }
                    });
                }else if ($.isPlainObject(type)){
                    if(type['ms:view']){
                        templatePath.push("/" + type['ms:view']);
                    }
                }
                console.log(templatePath);

                if(templatePath.length ==0){
                    loadError('Template missing','There is no view specified for this type of data  :-(');
                    return;
                }

                getTemplateAjax(templatePath[0], function(template) {

                    $('#content').append(template(result));
                    $('#loader').modal('hide');
                    $('.refresh').click(function(){
                        console.log('Sending Refresh request for ' + MS.resourceUrl );
                        $.ajax({
                            url:'/rest/refresh',
                            type: "POST",
                            dataType: "json",
                            contentType: "application/json; charset=utf-8",
                            data:JSON.stringify({"url": MS.resourceUrl}),
                            success:function(){console.log('success')},
                            error:function(){console.log ('error')}
                        });
                    });
                    $('*[data-provenance]').each(function(index,element){
                        $(element).popover({
                            html: true,
                            placement: 'auto right',
                            trigger: 'hover',
                            title : function(){
                                return getProvenanceTitle(element);
                            },
                            content: function(){
                                return getProvenanceContent(element);
                            }
                        })
                    });

                },function(){
                    loadError('Template missing','Could not retrieve ' +templatePath[0] +'  :-(');
                });
            });
        },
        error: function(){
            loadError('Service unreachable','Could not reach the service /sparql :-(');
        }
    });
}


function initSearchBar() {

}
function main(){
    initHandleBars();
    initMs();
    initSearchBar();

    if(MS.resourceUrl.indexOf("metaservice.org/d/search") != -1){
        handleSearch();
    }else{
        handleResource();
    }
}
