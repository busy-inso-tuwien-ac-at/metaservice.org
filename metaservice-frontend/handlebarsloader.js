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


Handlebars.registerHelper('eachArray', function(context, options) {
    if(!context)
    return "";
  var ret = "";
  if($.isArray(context)){
  	for(var i=0, j=context.length; i<j; i++) {
    		ret = ret + options.fn(context[i]);
  	}
  }else{
	ret = options.fn(context);
  }
  return ret;
});

Handlebars.registerHelper('eachNext', function(context, options) {
    console.log('TRYING');
    console.log(context);
    if(!context)
        return "";

    //set prev
    /*
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
    }*/
    if($.isArray(context)){
        var first = [];
        for(i=0, j=context.length; i<j; i++) {
            if(!context[i]['xhv:prev']){
                first.push(context[i]);
            }
        }
         context = first;
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