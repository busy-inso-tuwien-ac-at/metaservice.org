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

function getTemplateAjax(path, callback, errorcallback) {
    var source;
    var template;

    $.ajax({
        url: path,
        success: function(data) {
            source    = data;
            template  = Handlebars.compile(source);

            //execute the callback if passed
            if (callback) callback(template);
        },
        error: function(data){
            if(errorcallback) errorcallback("failed to load " + template);
        }
    });
}

function convertToJson(data,root){
    function recursiveLink(result,curval){
        if($.isArray(curval) || $.isPlainObject(curval)){
            $.each(curval,function(index,d){
                if(index != 'resourceURI')
                    curval[index]= recursiveLink(result,d);
            });
        } else if(result[curval]){
            return result[curval];
        }
        return curval;
    }

    function addToMap(result,provenanceResult,graph,subject,relation,value){
        if(!result[subject]){
            result[subject] = {resourceURI:subject};
        }
        if(result[subject][relation]){
            if(!$.isArray(result[subject][relation])){
                var x = result[subject][relation];
                if(x != value){
                    result[subject][relation] = [];
                    result[subject][relation].push(x);
                    result[subject][relation].push(value);
                }
            } else if(-1 == $.inArray(value,result[subject][relation])){
                result[subject][relation].push(value);
            }
        }else{
            result[subject][relation] = value;
        }
        if(graph){
            var provenanceId = subject + "."+ relation + "[" + value + "]";
            if(!$.isArray(provenanceResult[provenanceId])){
                provenanceResult[provenanceId] = [];
            }
            provenanceResult[provenanceId].push(graph);
        }
        return result;
    }


    var result = {};
    var provenanceResult = {};
    $.each(data.results.bindings,function(index,d){
	if(d.subject && d.relation && d.value)
        result = addToMap(result,provenanceResult, (d.graph)?d.graph.value:null, d.subject.value, DEB[d.relation.value], d.value.value);
    });
    console.log(provenanceResult);

    recursiveLink(provenanceResult,result);

    recursiveLink(result,result);
    console.log(provenanceResult);
    return result[root];
}

Handlebars.registerHelper('loadScript',function(context,predicate){
    $.getScript(context);
});

Handlebars.registerHelper('script',function(context,predicate){
    console.log(context);
    console.log(context.fn(this));
    return new Handlebars.SafeString("<script>"+context.fn(this)+"</script>");
});

Handlebars.registerHelper('literal',function(context,predicate){
    if($.isArray(context)){
        //todo make language selection
        context = context[0];
    }
    if ($.isPlainObject(context)){
        if(context["@value"]){
            return context["@value"];
        }else{
            console.log('???');
        }
    }else{
        return context;
    }
});

Handlebars.registerHelper('provenance',function(context,predicate){
    var id = Handlebars.Utils.escapeExpression(context["ms:id"]);
    var object = Handlebars.Utils.escapeExpression(context[predicate]);
    return new Handlebars.SafeString("data-provenance=\"["+id+"]["+predicate+"]["+object+"]\"");
});


Handlebars.registerHelper('dependencyTag',function(context,predicate){
    var result ="";
    var depRelations = {
        "ms-swrel:antiDepends": "AntiDependency",
        "ms-swrel:dependsBuild": "Build",
        "ms-swrel:dependsTest": "Test",
        "ms-swrel:dependsInstallation": "Installation",
        "ms-swrel:dependsRuntime": "Runtime",
        "ms-swrel:dependsSoftware": "Software",
        "ms-swrel:optional": "Optional",
        "ms-swrel:pluginOf": "Plugin",
        "ms-swrel:requires": "Required",
        "ms-swrel:dependsCompiler": "Compiler",
        "ms-swrel:dependsInterpreter": "Interpreter",
        "ms-swrel:links": "Link",
        "ms-swrel:dependsStandalone": "Standalone",
        "ms-swrel:dependsMiddleware": "Middleware",
        "ms-swrel:dependsService": "Service"
    };
    var fun = function(a,x){
        result += "<li>";
        var obj;
        if(x["rdf:li"]){
            for(var i = 0 ; i < x["rdf:li"].length ;i++){
                obj = x["rdf:li"][i];
                //hack for sameas
                if(obj["owl:sameAs"]&& obj["ms:id"] && obj["ms:id"].indexOf("http://metaservice.org/d/") != 0){
                    obj = obj["owl:sameAs"];
                }
                if( i != 0) {
                    result += " <b>or</b> ";
                }
                var description;
                if(obj["doap:name"]){
                    description = obj["doap:name"];
                }
                else if(obj["dc:description"]){
                    description = obj["dc:description"];
                }else{
                    description = obj["ms:id"];
                }
                if( obj["ms-swrel:projectConstraint"]){
                    result += "<a href=\""+handlebarsURI(obj["ms-swrel:projectConstraint"])+"\">" + description + "</a>";
                }else{
                    result += "<a href=\""+handlebarsURI(obj)+"\">" + description + "</a>";
                }
            }
        }else {
            obj = x;
            //hack for sameas
            if(obj["owl:sameAs"]&& obj["ms:id"] && obj["ms:id"].indexOf("http://metaservice.org/d/") != 0){
                obj = obj["owl:sameAs"];
            }
            var description;
            if(obj["doap:name"]){
                description = obj["doap:name"];
            }
            else if(obj["dc:description"]){
                description = obj["dc:description"];
            }else{
                description = obj["ms:id"];
            }
            if( obj["ms-swrel:projectConstraint"]){
                result += "<a href=\""+handlebarsURI(obj["ms-swrel:projectConstraint"])+"\">" + description + "</a>";
            }else{
                result += "<a href=\""+handlebarsURI(obj)+"\">" + description + "</a>";
            }
        }
        $.each(depRelations,function(v,w){
            if(context[v]){
                if($.isArray(context[v])){
                    if(context[v].indexOf(x) != -1){
                        result += " <span class=\"badge\">" +w + "</span>";
                    }
                }else{
                    if(context[v] == x){
                        result += " <span class=\"badge\">" +w + "</span>";
                    }
                }
            }
        });
        result += "</li>";
    };
    if(context["ms-swrel:depends"]){
        $.each(context["ms-swrel:depends"],fun);
    }else if(context["ms-swrel:dependsSoftware"]){ //workaround temporary for presentation -> debProvider did not include ms-swrel ontology, didn't want to restart the process
        $.each(context["ms-swrel:dependsSoftware"],fun);
    }
    return new Handlebars.SafeString(result);
});

Handlebars.registerHelper('hostname',function(context,options){
   var result = document.createElement('a');
    if($.isPlainObject(context)){
        result.href= context["ms:id"];
    }else{
        result.href = context;
    }return result.hostname;
});

Handlebars.registerHelper('mboxToAddress',function(context,options){
    if(!context)
        return "";
    if($.isPlainObject(context)){
        context = context['ms:id'];
    }
    return context.replace(/^mailto:/,'');
});

function handlebarsURI (context,options){
    if($.isPlainObject(context)){
        return context['ms:id'];
    }
    return context;
}
Handlebars.registerHelper('uri',handlebarsURI);

Handlebars.registerHelper('text',function(context,options){
    if($.isPlainObject(context)){
        return context['@value'];
    }
    return context;
});
Handlebars.registerHelper('date',function(context,options){
    if($.isPlainObject(context)){
        return context['@value'];
    }
    return context;
});
Handlebars.registerHelper('eachArray', function(context, options) {
    if(!context)
    return "";
  var ret = "";
  if($.isArray(context)){
  	for(var i=0, j=context.length; i<j; i++) {
        options.data.index = i;
        options.data.isFirst = i === 0;
        options.data.isLast = i === context.length - 1;
        ret = ret + options.fn(context[i],options);
  	}
  }else{
      options.data.isFirst = true;
      ret = options.fn(context,options);
  }
  return ret;
});

Handlebars.registerHelper('eachNext', function(context, options) {
    if(!context)
        return "";

    //set prev
    if($.isArray(context)){
        for(i=0, j=context.length; i<j; i++) {
            if(context[i]['xhv:next'] && !context[i]['xhv:next']['xhv:prev']){
                context[i]['xhv:next']['xhv:prev'] = context[i];
            }
        }
    }else{
        var x = context;
        while(x['xhv:next']){
            if(!x['xhv:next']['xhv:prev']){
                x['xhv:next']['xhv:prev'] = x;
            }
        }
    }
    if($.isArray(context)){
        var first = [];
        for(i=0, j=context.length; i<j; i++) {
            if(!context[i]['xhv:prev']){
                first.push(context[i]);
            }
        }
         context = first;
    }else{
        context = [context];
    }
    var ret = "";

    for(i=0; i < context.length;i++){
        head = context[i];
        ret += options.fn(head);
        while(head['xhv:next']){
            head = head['xhv:next'];
            ret += options.fn(head);
        }
    }

    return ret;
});