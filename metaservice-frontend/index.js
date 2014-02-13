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
    Handlebars.registerPartial("button_controls", $("#partial-button_controls").html());
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
    MS.raw = {};
    MS.provenanceCache = {};
    MS.resourceUrl = document.location.toString().replace(/\/$/,'').replace('#.*$','');
    $('#rdflink').attr('href',MS.resourceUrl);
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

    $.ajax({
        url: "http://metaservice.org/d/search",
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Accept","application/sparql-results+json");
        },
        dataType: "json",
        type: "get",
        data: {
            "q": q,
            "limit": limit,
            "offset": offset
        },
        success: function(data){
            if(data.results.bindings == 0){
                loadError('No Data Found','There was no response :-(');
                return;
            }
            MS.raw = data;
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

            var template  = Handlebars.compile($("#searchResults").html());
            $('#content').append(template(result));
            $('#loader').modal('hide');

        },
        error: function(){
            loadError('Service unreachable','Could not reach the service :-(');
        }
    });
}



function getProvenanceContent(element){
    if(MS.provenanceCache[element]){
       return MS.provenanceCache[element];
    }
    var container = $('<div></div>');

    function calculate(){
        var regex =  /^\[([^\]]*)\]\[([^\]]*)\]\[([^\]]*)\]/;
        var subject = element.replace(regex,'$1');
        var predicate = element.replace(regex,'$2');
        var object = element.replace(regex,'$3');
        var result = [];
        jsonld.compact(MS.raw,MS.context,function(err,compacted){
            console.log(compacted);

            for(graphId =0; graphId < compacted['@graph'].length; graphId++){
                var graph = compacted['@graph'][graphId];
                var graphelements = graph['@graph'];
                for(resourceID =0 ; resourceID < graphelements.length ; resourceID++){
                    var resource = graphelements[resourceID];
                    if(resource['@id'] == subject){
                        if(resource[predicate]) {
                            if(resource[predicate] == object || resource[predicate]['@id'] == object)
                            {
                                result.push(graph);
                            }
                        }
                    }
                }
            }

            var result2 = [];
            for(i = 0 ; i < result.length ; i++){
                graph = result[i];
                graphelements = graph['@graph'];
                for(resourceID =0 ; resourceID < graphelements.length ; resourceID++){
                    resource = graphelements[resourceID];
                    if(resource['@id'] == result[i]['@id']){
                        result2.push(resource);
                    }
                }
            }
            console.log(result2);
            resultcontainer = $('<div/>');
            $.each(result2,function(index,data){
                panel = $('<div class="panel panel-info" ></div>');
                panel.append('<div class="panel-heading"><h4 class="panel-title" >'+data['ms:generator']+'</h4></div>');
                table = $('<table class="table"></table>');
                table.append('<tr><th>Id</th><td>'+data['@id']+'</td></tr>');
                table.append('<tr><th>Source</th><td>'+data['ms:source']+data['ms:path']+'</td></tr>');
                table.append('<tr><th>Created</th><td>' +data['ms:creation_time']['@value']+'</td></tr>');
                panel.append(table);
                resultcontainer.append(panel);
            });
            container.empty();
            container.append(resultcontainer);
            MS.provenanceCache[element] = container;
        });
    }
    container.append('loading....');
    setTimeout(calculate(),0);
    return container;
}
function getProvenanceTitle(element){
    return 'Provenance Data';
}


// source http://stackoverflow.com/questions/1068834/object-comparison-in-javascript
function deepCompare () {
    var leftChain, rightChain;

    function compare2Objects (x, y) {
        var p;

        // remember that NaN === NaN returns false
        // and isNaN(undefined) returns true
        if (isNaN(x) && isNaN(y) && typeof x === 'number' && typeof y === 'number') {
            return true;
        }

        // Compare primitives and functions.
        // Check if both arguments link to the same object.
        // Especially useful on step when comparing prototypes
        if (x === y) {
            return true;
        }

        // Works in case when functions are created in constructor.
        // Comparing dates is a common scenario. Another built-ins?
        // We can even handle functions passed across iframes
        if ((typeof x === 'function' && typeof y === 'function') ||
            (x instanceof Date && y instanceof Date) ||
            (x instanceof RegExp && y instanceof RegExp) ||
            (x instanceof String && y instanceof String) ||
            (x instanceof Number && y instanceof Number)) {
            return x.toString() === y.toString();
        }

        // At last checking prototypes as good a we can
        if (!(x instanceof Object && y instanceof Object)) {
            return false;
        }

        if (x.isPrototypeOf(y) || y.isPrototypeOf(x)) {
            return false;
        }

        if (x.constructor !== y.constructor) {
            return false;
        }

        if (x.prototype !== y.prototype) {
            return false;
        }

        // check for infinitive linking loops
        if (leftChain.indexOf(x) > -1 || rightChain.indexOf(y) > -1) {
            return false;
        }

        // Quick checking of one object beeing a subset of another.
        // todo: cache the structure of arguments[0] for performance
        for (p in y) {
            if (y.hasOwnProperty(p) !== x.hasOwnProperty(p)) {
                return false;
            }
            else if (typeof y[p] !== typeof x[p]) {
                return false;
            }
        }

        for (p in x) {
            if (y.hasOwnProperty(p) !== x.hasOwnProperty(p)) {
                return false;
            }
            else if (typeof y[p] !== typeof x[p]) {
                return false;
            }

            switch (typeof (x[p])) {
                case 'object':
                case 'function':

                    leftChain.push(x);
                    rightChain.push(y);

                    if (!compare2Objects (x[p], y[p])) {
                        return false;
                    }

                    leftChain.pop();
                    rightChain.pop();
                    break;

                default:
                    if (x[p] !== y[p]) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    if (arguments.length < 1) {
        return true; //Die silently? Don't know how to handle such case, please help...
        // throw "Need two or more arguments to compare";
    }

    for (var i = 1, l = arguments.length; i < l; i++) {

        leftChain = []; //todo: this can be cached
        rightChain = [];

        if (!compare2Objects(arguments[0], arguments[i])) {
            return false;
        }
    }

    return true;
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
                            if(x && x['@id']){
                                var id = x['@id'];
                                //break loops
                            //    delete d[xindex];
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
                result[value["@id"]] = {'ms:id':value['@id']};
            }
            var object = result[value["@id"]];
            $.each(value,function(dataIndex,data){
                if(dataIndex.match(/^@/) && dataIndex != '@type' )
                {
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
                            for(i = 0; i < toProcess.length;i++){
                                found = false;
                                for(j = 0; j < object[dataIndex].length;j++){
                                    if(deepCompare(toProcess[i],object[dataIndex][j]))
                                    found = true;
                                }
                                if(!found){
                                    object[dataIndex].push(toProcess[i]);
                                }
                            }
                        } else if ($.isArray(object[dataIndex])){
                            found = false;
                                for(j = 0; j < object[dataIndex].length;j++){
                                    if(deepCompare(toProcess,object[dataIndex][j]))
                                        found = true;
                                }
                            if(!found){
                                object[dataIndex].push(toProcess);
                            }
                        } else if ($.isArray(toProcess)){
                            found = false;
                            for(i = 0; i < toProcess.length;i++){
                                 if(deepCompare(toProcess[i],object[dataIndex]))
                                    found = true;
                            }
                            if(!found){
                                toProcess.push(object[dataIndex]);
                            }
                            object[dataIndex] = toProcess;
                        } else {
                            if(!deepCompare(toProcess,object[dataIndex])){
                                object[dataIndex] = [object[dataIndex],toProcess];
                            }
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
    $.ajax({
        url: MS.resourceUrl,
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Accept","application/ld+json");
        },
        success: function(data){
            if(data.length == 0){
                loadError('No Data Found','Metaservice does not have any data for this uri :-(');
                return;
            }
            MS.raw = data;
            var result =jsonLdUnion(data);
            jsonld.compact(result,MS.context,function(err,compacted){
                result = compacted;
                result = jsonLdFollowIris(result,MS.resourceUrl);
                console.log(result);

                var templatePath = [];
                var type = result['@type'];
                //console.log(type);
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
                   /* $('*[data-provenance]').each(function(index,element){
                       annotated = $(element);
                       annotated.find('')
                    });*/
                    var window = $('#provenancewindow');
                    $('*[data-provenance]').hover(function(event){
                        if(window.is(":visible")
                        ){
                            hovered = $(event.target);
                            if(!hovered.attr('data-provenance')){
                                hovered = hovered.parent('*[data-provenance]');
                            }
                            if(hovered && hovered.attr('data-provenance')){
                                var contentpanel = window.find('.panel-content');
                                contentpanel.empty();
                                contentpanel.append(getProvenanceContent(hovered.attr('data-provenance')));
                            }

                        }
                    },function(){});
                    $('.show-provenance').click(function(){
                        if( window.is(':visible') ) {
                            window.hide();
                        } else{
                            window.show();
                        }

                    });
                },function(){
                    loadError('Template missing','Could not retrieve ' +templatePath[0] +'  :-(');
                });
            });
        },
        error: function(){
            loadError('Service unreachable','Could not reach the service :-(');
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
