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
        equal(guard.getRequestDone(), "HTTP");
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
        equal(guard.getRequestDone(), "NONE");

        for (var rs in [1,2,3]) {
            instance.readyState = rs;
            instance.onreadystatechange();
            equal(guard.getRequestDone(), "NONE");
        }
        
        instance.readyState = 4;
        instance.onreadystatechange();
        
        equal(guard.getRequestDone(), "XHR");
        
        guard.clearRequestDone();
        equal(guard.getRequestDone(), "NONE");
    });
    
    test("setTimeout in onreadystatechange callback", function() {
        
        expect(5);

        // having
        var instances = replaceXHRPrototype();
        window.setTimeout = function(callback, interval) {
            equal(guard.getRequestDone(), "NONE");
            callback();
            equal(guard.getRequestDone(), "XHR");
            window.setTimeout = originalSetTimeout;
        };
        
        // when
        inj.install();
        guard.install();
        
        guard.clearRequestDone();
        equal(guard.getRequestDone(), "NONE");

        var xhr = new window.XMLHttpRequest();
        xhr.onreadystatechange = function() {
            setTimeout(function() {}, 50);
        }
        var instance = instances[0];
        ok(instance);
        
        instance.readyState = 4;
        instance.onreadystatechange();
        
        equal(guard.getRequestDone(), "XHR");
    });
    
    asyncTest("multiple nested setTimeout calls in onreadystatechange callback", function() {
        
        expect(10);

        // having
        var instances = replaceXHRPrototype();
        var calls = 0;

        var testingSetTimeout = function(callback, timeout) {
            originalSetTimeout(function() {
                equal(guard.getRequestDone(), "NONE");
                callback();
                calls += 1;
                if (calls == 3) {
                    equal(guard.getRequestDone(), "XHR");
                    cleanup();
                } else {
                    equal(guard.getRequestDone(), "NONE");
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
        equal(guard.getRequestDone(), "NONE");

        var xhr = new window.XMLHttpRequest();
        xhr.onreadystatechange = function() {
            setTimeout(function timeout1() {
                equal(guard.getRequestDone(), "NONE");
            }, 20);
            setTimeout(function timeout2() {
                setTimeout(function timeout3() {
                    cleanup();
                }, 30);
                equal(guard.getRequestDone(), "NONE");
            }, 30);
        }
        var instance = instances[0];
        
        instance.readyState = 4;
        instance.onreadystatechange();
    });
})();