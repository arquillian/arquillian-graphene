Welcome to Arquillian Graphene - Testing Ajax with flavour of sexy type-safe API
================================================================================

Graphene project is designed as enhancement of Selenium project focused on rapid development andusability in Java environment.

* API is straightforward and enforces tester to write Ajax-enabled tests,
* internals enables JQuery location strategy, which improves performance of the tests,
* brings concepts for on-the-fly injection of own selenium extensions and also extensions to page code (enabling advanced testing features)
* allows command interception and request interception,
* it brings type-safe equivalent of Selenium version 1.x, enabling auto-completion and rapid development style,
* integrates with Arquillian using Arquillian Drone extension.

With all the concepts above, Graphene brings new power to space of enterprise Java-based testing.

Project Info
------------

<table>
    <tr>
        <td>License:</td>
        <td>LGPL v2.1, ASL v2.0 (dual-licensed)</td>
    </tr>
    <tr>
        <td>Build:</td>
        <td>Maven</td>
    </tr>
    <tr>
        <td>Documentation:</td>
        <td>https://docs.jboss.org/author/display/ARQGRA</td>
    </tr>
    <tr>
        <td>Issue tracker:</td>
        <td>https://issues.jboss.org/browse/ARQGRA</td>
    </tr>
</table>


Getting Started
---------------

*** Adding Maven Dependency

To use Graphene, add following dependencies to your project.

It will allow you to use Graphene with JUnit integration, in Standalone mode (check [documentation](https://docs.jboss.org/author/display/ARQGRA/Getting+Started) for other modes):

    <!-- JUnit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.8.2</version>
        <scope>test</scope>
    </dependency>
     
    <!-- Arquillian JUnit Standalone -->
    <dependency>
        <groupId>org.jboss.arquillian.junit</groupId>
        <artifactId>arquillian-junit-standalone</artifactId>
        <version>1.0.0.Final</version>
        <scope>test</scope>
    </dependency>
     
    <!-- Graphene dependency chain - imports all other dependencies required -->
    <dependency>
        <groupId>org.jboss.arquillian.graphene</groupId>
        <artifactId>arquillian-graphene</artifactId>
        <version>1.0.0.Final</version>
        <type>pom</type>
        <scope>test</scope>
    </dependency>

*** Adding the Java test

To start with Graphene from beloved Java, you can write similar code:

    @RunWith(Arquillian.class)
    public class BasicTestCase {
    
        URL url = URLUtils.buildUrl("http://www.google.com/");
        
        @Drone
        GrapheneSelenium browser;
    
        @Test
        public void testOpeningHomePage() {
            browser.open(url);
        }
    }

For more knowledge about framework's strengths, let's look at [Common API](https://docs.jboss.org/author/display/ARQGRA/Common+API) documentation.


For Developers
==============

Prerequisities
--------------

* JDK 1.6+
* Maven 3.0.3+


How-to build project
--------------------

    mvn clean install

Running Integration Tests
-------------------------
    
    mvn verify -Pftest
