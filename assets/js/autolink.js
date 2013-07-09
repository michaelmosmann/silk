var typeURLs = [];
var methodURLs = [];
var constantURLs = [];
var githubRootURL = "https://github.com/jbee/silk/tree/master/";
var ambigous = "-AMBIGIOUS-";
var java = "_java_";
var javadocRootURL = "/assets/javadoc/0.6/";

function wrapCode() {
	var name = $(this).text();
	var jname = java+name;
	var type = typeURLs[jname];
		if (type) { 
    		return "<a href='"+type.url+"' class='javadoc "+type.type+"' target='_blank'></a>"; 
		}  
	if (name.match("^Test") && name.match("Binds$")) {
		return "<a href='"+githubRootURL+"src/test/se/jbee/inject/bind/"+name+".java' class='test source C' target='_blank'></a>"; 
	}
	var method = methodURLs[jname];
	if (method) {
		return "<a href='"+method.url+"' class='javadoc method' target='_blank'></a>";
	}
	var constant = constantURLs[jname];
	if (constant) {
		return "<a href='"+constant.url+"' class='javadoc constant' target='_blank'></a>";
	}
		return ""; // do not wrap
}
  
$(document).ready(function() {
		$.getJSON(javadocRootURL+'types.json', function(data) {
			$.each(data['types'], function(index, value) { 
			if (value != null) {
					typeURLs[java+value.name]={type: value.kind,url: javadocRootURL+value['package'].replace(/\./g, '/')+"/"+value.fullName+".html"};
			}
			});
			var methodNames = [];
			$.each(data['methods'], function(index, value) { 
				if (value != null) {
				var type = typeURLs[java+value.declaringClass];
				var name = java+value.name;
				if (methodNames[name]) {
					if (methodNames[name] != type) {
						methodNames[name] = ambigous;
					}
				} else { 
					methodNames[name] = type;
				}
				}
			}); 
			$.each(data['methods'], function(index, value) { 
				if (value != null) {
					var name = java+value.name;
				 	if (methodNames[name] != ambigous) {
					var type = typeURLs[java+value.declaringClass];
					if (type) {
							methodURLs[name]={ url: type.url +"#"+value.name+value.signature.replace(/<[^>]+[>]+/, '') };
					}
				 	}
				}
		});
			var constantNames = [];
			$.each(data['constants'], function(index, value) { 
				if (value != null) {
				var type = typeURLs[java+value.declaringClass];
				var name = java+value.name;
				if (constantNames[name]) {
					if (constantNames[name] != type) {
						constantNames[name] = ambigous;
					}
				} else { 
					constantNames[name] = type;
				}
				}
			}); 
			$.each(data['constants'], function(index, value) { 
				if (value != null) {
					var name = java+value.name;
				 	if(constantNames[name] != ambigous) {
					var type = typeURLs[java+value.declaringClass];
					if (type) {
						constantURLs[name]={ url: type.url +"#"+value.name };
					}
				 	}
				}
		});    			   			
    		$('code').wrap(wrapCode);
    		$('pre code.java span.n, pre code.java span.nc, pre code.java span.na').wrap(wrapCode);
		});
});
