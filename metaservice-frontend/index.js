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
    MS.namespaces =
        "PREFIX bds: <http://www.bigdata.com/rdf/search#> \n"
            +"PREFIX dc: <http://purl.org/dc/elements/1.1/> \n";
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
        +"           dc:title ?title ."
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
            for(var i = page -3 ; i < page+3;i++){
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

function handleResource(){
    var query = MS.namespaces
        +"Select  DISTINCT ?graph ?subject ?relation ?value {"
        +" {Select ?graph (?resource as ?subject) (?irelation as ?relation) (?ivalue as ?value)"
        +"   { GRAPH ?igraph {?resource ?irelation ?ivalue.} }} "
        +" UNION "
        +" {Select (?igraph2 AS ?graph) (?ivalue as ?subject) (?irelation2 as ?relation) (?ivalue2 as ?value) "
        +"   { GRAPH ?igraph {?resource ?irelation ?ivalue.} OPTIONAL { GRAPH ?igraph2 { ?ivalue ?irelation2 ?ivalue2 }}}}"
        +" }";
    query = query.split('?resource').join('<' + encodeURI(MS.resourceUrl) + '>');
    $.ajax({
        url: "/sparql",
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Accept","application/sparql-results+json");
        },
        dataType: "json",
        type: "post",
        data: {
            "query": query,
            "infer": "true"
        },
        success: function(data){
            if(data.results.bindings == 0){
                loadError('No Data Found','There was no response :-(');
                return;
            }
            var result = convertToJson(data,MS.resourceUrl);
            console.log(data);
            console.log(result);

            var templatePath = [];
            if($.isArray(result.type)){
                $.each(result.type,function(index,d){
                    if(d.view){
                        templatePath.push("/" + d.view);
                    }
                });
            }else if ($.isPlainObject(result.type)){
                if(result.type.view){
                    templatePath.push("/" + result.type.view);
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
            },function(){
                loadError('Template missing','Could not retrieve ' +templatePath[0] +'  :-(');
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

    if(MS.resourceUrl.contains("metaservice.org/d/search")){
        handleSearch();
    }else{
        handleResource();
    }
}
