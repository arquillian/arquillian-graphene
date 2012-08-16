Document.helloworld = {};

Document.helloworld.install = function() {
    Document.helloworld.hellovar = "Hello World!";
}
Document.helloworld.hello = function() {
    return Document.helloworld.hellovar;
}