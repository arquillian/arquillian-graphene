document.helloworld = {};

document.helloworld.install = function() {
    document.helloworld.hellovar = "Hello World!";
};
document.helloworld.hello = function() {
    return document.helloworld.hellovar;
};