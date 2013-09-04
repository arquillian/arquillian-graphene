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
module("RequestGuard");

(function() {

    var inj = Graphene.xhrInterception;
    var guard = Graphene.Page.RequestGuard;
    var originalXhr = window.XMLHttpRequest;
    var originalSetTimeout = window.setTimeout;

    QUnit.testStart = QUnit.testDone = function() {
        window.XMLHttpRequest = originalXhr;
        window.setTimeout = originalSetTimeout;
        inj.uninstall();
    };

    function replaceXHRPrototype(methods) {
        var instances = [];
        var Replacement = function() {
            instances.push(this);
        };
        Replacement.prototype = methods;
        window.XMLHttpRequest = Replacement;
        return instances;
    }
    
    // TESTS
    test("initial state", function() {
        // when
        inj.install();
        guard.install();
        
        // then
        equal(guard.getRequestType(), "HTTP");
        equal(guard.getRequestState(), "DONE");
    });
    
    test("readyState changes", function() {
        
        // having
        var instances = replaceXHRPrototype();
        
        // when
        inj.install();
        guard.install();

        var xhr = new window.XMLHttpRequest();
        xhr.onreadystatechange = function() {
            statechanged = true;
        }
        var instance = instances[0];
        ok(instance);

        guard.clearRequestDone();
        equal(guard.getRequestType(), "NONE");

        for (var rs in [1,2,3]) {
            instance.readyState = rs;
            instance.onreadystatechange();
            equal(guard.getRequestType(), "XHR");
            equal(guard.getRequestState(), "IN_PROGRESS");
        }
        
        instance.readyState = 4;
        instance.onreadystatechange();
        
        equal(guard.getRequestType(), "XHR");
        equal(guard.getRequestState(), "DONE");
        
        guard.clearRequestDone();
        equal(guard.getRequestType(), "NONE");
    });
    
    test("request in progress", function() {
        
        // having
        var interactive = false;
        var instances = replaceXHRPrototype({
            open: function() {
                interactive = true;
            }
        });
        
        // when
        inj.install();
        guard.install();

        var xhr = new window.XMLHttpRequest();
        var instance = instances[0];
        ok(instance);
        
        guard.clearRequestDone();
        equal(guard.getRequestType(), "NONE");
        equal(guard.getRequestState(), "NONE");
        
        xhr.open("url");
        xhr.send("content")
        equal(guard.getRequestType(), "XHR");
        equal(guard.getRequestState(), "IN_PROGRESS");
    });
    
    test("setTimeout in onreadystatechange callback", function() {
        
        expect(5);

        // having
        var instances = replaceXHRPrototype();
        window.setTimeout = function(callback, interval) {
            equal(guard.getRequestType(), "NONE");
            callback();
            equal(guard.getRequestType(), "XHR");
            window.setTimeout = originalSetTimeout;
        };
        
        // when
        inj.install();
        guard.install();
        
        guard.clearRequestDone();
        equal(guard.getRequestType(), "NONE");

        var xhr = new window.XMLHttpRequest();
        xhr.onreadystatechange = function() {
            setTimeout(function() {}, 50);
        }
        var instance = instances[0];
        ok(instance);
        
        instance.readyState = 4;
        instance.onreadystatechange();
        
        equal(guard.getRequestType(), "XHR");
    });
    
    asyncTest("multiple nested setTimeout calls in onreadystatechange callback", function() {
        
        expect(10);

        // having
        var instances = replaceXHRPrototype();
        var calls = 0;

        var testingSetTimeout = function(callback, timeout) {
            originalSetTimeout(function() {
                equal(guard.getRequestType(), "NONE");
                callback();
                calls += 1;
                if (calls == 3) {
                    equal(guard.getRequestType(), "XHR");
                    cleanup();
                } else {
                    equal(guard.getRequestType(), "NONE");
                }
            }, timeout);
        };
        
        window.setTimeout = testingSetTimeout;
        
        var cleaning = false
        var cleanup = function() {
            originalSetTimeout(function() {
                if (!cleaning) {
                    cleaning = true;
                    equal(window.setTimeout, testingSetTimeout, "the setTimeout should be same as on the start of the onreadystatechange");
                    window.setTimeout = originalSetTimeout;
                    start();
                }
            }, 20);
        }
        
        // when
        inj.install();
        guard.install();
        
        guard.clearRequestDone();
        equal(guard.getRequestType(), "NONE");

        var xhr = new window.XMLHttpRequest();
        xhr.onreadystatechange = function() {
            setTimeout(function timeout1() {
                equal(guard.getRequestType(), "NONE");
            }, 20);
            setTimeout(function timeout2() {
                setTimeout(function timeout3() {
                    cleanup();
                }, 30);
                equal(guard.getRequestType(), "NONE");
            }, 30);
        }
        var instance = instances[0];
        
        instance.readyState = 4;
        instance.onreadystatechange();
    });
})();