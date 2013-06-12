Welcome to Arquillian Graphene 2
================================
<h3>Testing Ajax with a flavour of sexy WebDriver API</h3>


Graphene 2 project is designed as set of extensions for [Selenium WebDriver](http://docs.seleniumhq.org/) project focused on rapid development and usability in Java environment.

* API is **straightforward** and enforces tester to write **Ajax-enabled** and **reusable tests** and test abstractions,
* encourages to write tests in a [consistent level of abstraction](https://docs.jboss.org/author/display/ARQGRA2/Page+Abstractions) using [**Page Objects**](https://docs.jboss.org/author/display/ARQGRA2/Page+Objects) and [**Page Fragments**](https://docs.jboss.org/author/display/ARQGRA2/Page+Fragments),
* the code is robust with improved readability,
  * supports [**request guarding**](https://docs.jboss.org/author/display/ARQGRA2/Request+Guards) and interception,
  * improved [**Waiting API**](https://docs.jboss.org/author/display/ARQGRA2/Graphene+Utility+Class#GrapheneUtilityClass-Waitings),
* allows **WebDriver API interception**
* focuses on **portability across browsers**,
* it enables **JQuery selectors as a location strategy** which is familiar to web development community
* brings concepts for on-the-fly **injection of extensions to a page code** (enabling advanced testing features)
* integrates with **Arquillian Core** and **Arquillian Drone** extension

With all the concepts above, Graphene brings new power to space of Java-based testing.

Project Info
------------

<table>
    <tr>
        <td>License:</td>
        <td>LGPL v2.1 or ASL v2.0 (dual-licensed)</td>
    </tr>
    <tr>
        <td>Build:</td>
        <td>Maven</td>
    </tr>
    <tr>
        <td>Documentation:</td>
        <td><a href="https://docs.jboss.org/author/display/ARQGRA2">https://docs.jboss.org/author/display/ARQGRA2</a></td>
    </tr>
    <tr>
        <td>Issue tracker:</td>
        <td><a href="https://issues.jboss.org/browse/ARQGRA">https://issues.jboss.org/browse/ARQGRA</a></td>
    </tr>
</table>


Getting Started
---------------

### Adding Maven Dependency

To use Graphene, add following dependencies to your project.

It will allow you to use Graphene with *JUnit* integration, in *Standalone* mode (check [documentation](https://docs.jboss.org/author/display/ARQGRA2/Getting+Started) for other modes and framework integrations):

    <!-- JUnit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${version.junit}</version>
        <scope>test</scope>
    </dependency>
     
    <!-- Arquillian JUnit Standalone -->
    <dependency>
        <groupId>org.jboss.arquillian.junit</groupId>
        <artifactId>arquillian-junit-standalone</artifactId>
        <version>${version.arquillian.core}</version>
        <scope>test</scope>
    </dependency>
     
    <!-- Graphene dependency chain - imports all other dependencies required -->
    <dependency>
        <groupId>org.jboss.arquillian.graphene</groupId>
        <artifactId>graphene-webdriver</artifactId>
        <version>${version.arquillian.graphene}</version>
        <type>pom</type>
        <scope>test</scope>
    </dependency>

### Adding the Java test

To start with Graphene from beloved Java, you can write similar code:

    @RunWith(Arquillian.class)
    public class BasicTestCase {
    
        private URL url = URLUtils.buildUrl("http://www.google.com/");
        
        @Drone
        WebDriver browser;
    
        @Test
        public void testOpeningHomePage() {
            browser.get(url.toString());
        }
    }


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
