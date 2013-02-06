function invokeInterface (target, method, args) {
    var args = args || [];
    try {
        if (target) {
            /* property */
            if (!args.length && method.indexOf("get") == 0) {
                var property = method.substring(3, method.length);
                /* uncapitalize */
                property = property.charAt(0).toLowerCase() + property.slice(1);
                if (property && target[property]) {
                    return target[property];
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
        throw e;
    }
};