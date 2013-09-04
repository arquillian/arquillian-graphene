document.unstable = {};

document.unstable.counter = 0;

document.unstable.simple = function() {
    document.unstable.counter++;
    if (document.unstable.counter == 2) {
        throw "page refresh"
    }
    return document.unstable.counter;
};