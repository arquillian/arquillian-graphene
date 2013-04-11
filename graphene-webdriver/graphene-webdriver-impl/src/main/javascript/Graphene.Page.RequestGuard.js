/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

    var requestDone = "HTTP";

    var originalTimeout;

    var latch = 0;

    var timeoutWrapper = function(originalCallback, timeout) {
        latch += 1;
        var callbackArguments = [];
        for (var i = 0; i < arguments.length; i++) {
            if (i >= 2) {
                callbackArguments.push(arguments[i]);
            }
        }

        originalTimeout(function() {
            try {
                if (typeof(originalCallback) == 'string') {
                    window.eval(originalCallback);
                } else {
                    originalCallback(callbackArguments);
                }
            } finally {
                latch -= 1;
                tryFinish();
            }
        }, timeout);
    };

    var tryFinish = function() {
        if (latch == 0) {
            window.setTimeout = originalTimeout;
            requestDone = "XHR";
        }
    };

    return {

    	getRequestDone : function() {
    		return requestDone;
    	},

    	clearRequestDone : function() {
    		var result = requestDone;
    		requestDone = "NONE";
    		return result;
    	},

        install: function() {
            window.Graphene.xhrInterception.onreadystatechange(
                function(context, args) {
                    if(this.readyState == 4) {
                        try {
                            latch = 0;
                            originalTimeout = window.setTimeout;
                            window.setTimeout = timeoutWrapper;
                            context.proceed(args);
                        } finally {
                            tryFinish();
                        }
                    } else {
                        context.proceed(args);
                    }
                }
            );
        }
    }

})();