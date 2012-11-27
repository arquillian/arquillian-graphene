/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
window.Graphene = window.Graphene || {};

/**
 * The XMLHttpRequest injection providing ability to intercept requests.
 */
window.Graphene.xhrInterception = (function() {

    /**
     * The backup of original XHR object after injection
     */
    var original;
    /**
     * Flags for different implementations of XHR
     */
    var isXHR = !!window.XMLHttpRequest;
    var isActiveX = !!window.ActiveXObject;
    /**
     * The hash of arrays of functions (with method names to intercept like keys)
     */
    var interceptors = {};

    /**
     * The prototype of injected XHR object.
     *
     * Delegates to intercepter chain.
     */
    var wrapperPrototype = {
        abort : function() {
            return invokeInterceptorChain(this, 'abort', arguments);
        },
        open : function() {
            return invokeInterceptorChain(this, 'open', arguments);
        },
        getAllResponseHeaders : function() {
            return invokeInterceptorChain(this, 'getAllResponseHeaders', arguments);
        },
        getResponseHeader : function() {
            return invokeInterceptorChain(this, 'getResponseHeader', arguments);
        },
        send : function() {
            return invokeInterceptorChain(this, 'send', arguments);
        },
        setRequestHeader : function() {
            return invokeInterceptorChain(this, 'setRequestHeader', arguments);
        },
        onreadystatechange : undefined
    };

    /**
     * Injects XHR wrapper for Firefox/Chromium/WebKit and similar browsers
     */
    var replaceXHR = function() {
        original = window.XMLHttpRequest;
        window.XMLHttpRequest = createReplacement();
    };

    /**
     * Reverts XHR wrapper for Firefox/Chromium/WebKit and similar browsers
     */
    var revertXHR = function() {
        window.XMLHttpRequest = original;
        original = undefined;
    };

    /**
     * Creates XHR wrapper for replacement of original XHR object
     */
    var createReplacement = function() {
        var Replacement = function() {
            this.xhr = new original();
            this.xhr.onreadystatechange = callback(this);
        };
        Replacement.prototype = wrapperPrototype;
        return Replacement;
    };

    /**
     * onreadystatechange callback which is registered on true XHR instance.
     *
     * Delegates to intercepter chain.
     */
    var callback = function(wrapper) {
        return function() {
            wrapper.readyState = this.readyState;
            if (wrapper.readyState == 4) {
                wrapper.responseText = this.responseText;
                wrapper.responseXML = this.responseXML;
                wrapper.status = this.status;
                wrapper.statusText = this.statusText;
            }
            invokeInterceptorChain(wrapper, 'onreadystatechange', [ wrapper ]);
        };
    };

    /**
     * Decides which injection is necessary for current browser
     */
    var replace = function() {
        if (isXHR) {
            replaceXHR();
        }
    };

    /**
     * Decides which injection is necessary for current browser
     */
    var revert = function() {
        if (isXHR) {
            revertXHR();
        }
    };

    /**
     * Registers intercepter in the chain of intercepters.
     */
    var registerInterceptor = function(methodName, interceptor) {
        interceptors[methodName] = interceptors[methodName] || [];
        interceptors[methodName].push(interceptor);
    };

    /**
     * Starts the execution of interceptor chain.
     *
     * The method calls or the interceptors in the chain and once all of them are fired, calls original implementation.
     *
     * @param wrapper
     *            XHR wrapper instance
     * @param methodName
     *            the name of the method invoked
     * @param arguments
     *            of the invocation
     * @param i
     *            (optional) the number of interceptor to invoke (if there is no such interceptor, function delegates to real
     *            method)
     */
    var invokeInterceptorChain = function(wrapper, methodName, args, i) {
        var i = i || 0;
        if (interceptors[methodName] && interceptors[methodName].length > i) {
            return invokeNextInterceptor(wrapper, methodName, args, i);
        } else {
            return invokeRealMethod(wrapper, methodName, args);
        }
    };

    /**
     * Invokes next intercepter in the chain
     */
    var invokeNextInterceptor = function(wrapper, methodName, args, i) {
        var context = {
            xhrOriginal : wrapper.xhr,
            xhrWrapper : wrapper,
            proceed : function() {
                return invokeInterceptorChain(wrapper, methodName, args, i + 1);
            }
        };
        var interceptor = interceptors[methodName][i];
        return interceptor.call(wrapper, context, args);
    };

    /**
     * Invokes original XHR implemention method.
     *
     * If onreadystatechange callback is processed, it is invoked on wrapper; otherwise method of the XHR instance is invoked.
     */
    var invokeRealMethod = function(wrapper, methodName, args) {
        var xhr = (methodName === 'onreadystatechange') ? wrapper : wrapper.xhr;
        if (xhr[methodName]) {
            return xhr[methodName].apply(xhr, args);
        }
    };

    /* PUBLIC METHODS */
    return {
        /**
         * Ensures the interceptor is installed properly
         */
        install : function() {
            if (!original) {
                replace();
            }
        },
        /**
         * Removes all registered interceptors.
         */
        uninstall : function() {
            interceptors = {};
            if (original) {
                revert();
            }
        },
        /**
         * Registers intercepter for abort method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onAbort : function(interceptor) {
            registerInterceptor('abort', interceptor);
        },
        /**
         * Registers intercepter for open method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onOpen : function(interceptor) {
            registerInterceptor('open', interceptor);
        },
        /**
         * Registers intercepter for getAllResponseHeaders method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onGetAllResponseHeaders : function(interceptor) {
            registerInterceptor('getAllResponseHeaders', interceptor);
        },
        /**
         * Registers intercepter for send method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onSend : function(interceptor) {
            registerInterceptor('send', interceptor);
        },
        /**
         * Registers intercepter for setRequestHeader method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onSetRequestHeader : function(interceptor) {
            registerInterceptor('setRequestHeader', interceptor);
        },
        /**
         * Registers intercepter for onreadystatechange callback method.
         *
         * Interceptor is function with two params: context and args.
         *
         * Sample: function(context, args) { context.proceed(args); }
         */
        onreadystatechange : function(interceptor) {
            registerInterceptor('onreadystatechange', interceptor);
        }
    }
})();