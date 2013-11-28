/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

window.Graphene.Page = window.Graphene.Page || {};

window.Graphene.Page.RequestGuard = (function() {

    var requestType = "HTTP";
    var requestState = "DONE";
    
    var filters = [];
    
    function replaceTimeout(xhr) {
        xhr.originalTimeout = window.setTimeout;
        window.setTimeout = function(originalCallback, timeout) {
            if (timeout > window.Graphene.Page.RequestGuard.maximumCallbackTimeout) {
                 //provide 'apply' borrowed from Function for the case the host object does not have the own
                 return Function.prototype.apply.call(xhr.originalTimeout, window, arguments);
            } else {
                xhr.callbackCount += 1;
                
                var callbackArguments = [];
                for (var i = 0; i < arguments.length; i++) {
                    if (i >= 2) {
                        callbackArguments.push(arguments[i]);
                    }
                }
                //provide 'call' borrowed from Function for the case the host object does not have the own
                return Function.prototype.call.call(xhr.originalTimeout, window, function() {
                    try {
                        replaceTimeout(xhr);
                        if (typeof(originalCallback) === 'string') {
                            window.eval(originalCallback);
                        } else {
                            originalCallback(callbackArguments);
                        }
                    } finally {
                        revertTimeout(xhr);
                        xhr.callbackCount -= 1;
                        xhr.tryFinish();
                    }
                }, timeout);
            }
        };
    }
    
    function revertTimeout(xhr) {
        window.setTimeout = xhr.originalTimeout;
        xhr.originalTimeout = null;
    }
    
    function enhanceXhrObject(xhr) {
        xhr.guarded = true;
        xhr.callbackCount = 0;
        xhr.changeState = function(type, state) {
            if (this.guarded) {
                requestType = type;
                requestState = state;
            }
        };
        xhr.tryFinish = function() {
            if (this.callbackCount === 0) {
                this.changeState("XHR", "DONE");
            }
        };
        xhr.proceedWithCallbacks = function(context, args) {
            if (this.guarded) {
                replaceTimeout(this);
                try {
                    context.proceed(args);
                } finally {
                    revertTimeout(this);
                }
            } else {
                context.proceed(args);
            }
        };
        xhr.isGuarded = function() {
            for (var i = 0; i < filters.length; i++) {
                var filter = filters[i];
                try {
                    if (!eval(filter)) {
                        return false;
                    }
                } catch (e) {
                    console.log('failed to filter XHR request "' + filter + '": ' + e.message);
                }
            }
            return true;
        };
    }

    return {
        
        maximumCallbackTimeout : 50, 

        getRequestType : function() {
            return requestType;
        },

        getRequestState : function() {
            return requestState;
        },

        clearRequestDone : function() {
            var result = requestType;
            requestType = "NONE";
            requestState = "NONE";
            return result;
        },
        
        filter : function(evalDeclaration) {
            filters.push(evalDeclaration);
        },
        
        clearFilters : function() {
            filters = [];
        },

        install: function() {
            window.Graphene.xhrInterception.onConstruct(
                function(context) {
                    enhanceXhrObject(this);
                    return context.proceed();
                }
            );
            window.Graphene.xhrInterception.onOpen(
                function(context, args) {
                    this.filterContext = {
                        method: args[0],
                        url: args[1],
                        async: args[2]
                    };
                    context.proceed(args);
                }
            );
            window.Graphene.xhrInterception.onSend(
                function(context, args) {
                    this.filterContext.body = args[0];
                    this.guarded = this.isGuarded();
                    this.changeState("XHR", "IN_PROGRESS");

                    context.proceed(args);
                }
            );
            window.Graphene.xhrInterception.onreadystatechange(
                function(context, args) {
                    if(this.readyState === 4) {
                        try {
                            this.proceedWithCallbacks(context, args);
                        } finally {
                            this.tryFinish();
                        }
                    } else {
                        this.changeState("XHR", "IN_PROGRESS");
                        
                        this.proceedWithCallbacks(context, args);
                    }
                }
            );
        }
    };

})();