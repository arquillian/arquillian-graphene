function invokeInterface (target, method, args) {
    var args = args || [];
    try {
        if (target) {
            /* property getter */
            if (!args.length && method.indexOf("get") == 0) {
                var property = method.substring(3, method.length);
                /* uncapitalize */
                property = property.charAt(0).toLowerCase() + property.slice(1);
                if (property && target[property]) {
                    return target[property];
                }
            }
            
            /* property setter */
            if (args.length === 1 && method.indexOf("set") == 0) {
                var property = method.substring(3, method.length);
                /* uncapitalize */
                property = property.charAt(0).toLowerCase() + property.slice(1);
                if (property && target[property] != undefined) {
                    target[property] = args[0];
                    return;
                }
            }

            /* method */
            if (typeof(target[method]) == "function") {
                return target[method].apply(target, args);
            }
        } else {
            throw "target object of invocation is not defined"
        }
    } catch (e) {
        console.log('exception thrown when executing method ' + method + ': ' + e);
        /*
         * here an exception should be thrown, but it can't be because of
         * 'ARQGRA-289: JavaScript interfaces fails on Chrome and PhantomJS'
         */
        return 'GRAPHENE ERROR: ' + e;
    }
};