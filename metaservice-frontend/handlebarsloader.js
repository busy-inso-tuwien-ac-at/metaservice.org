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

    function addToMap(result,subject,relation,value){
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
        return result;
    }


    var result = {};
    $.each(data.results.bindings,function(index,d){
	if(d.subject && d.relation && d.value)
        result = addToMap(result,d.subject.value, DEB[d.relation.value], d.value.value);
    });

    recursiveLink(result,result);
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