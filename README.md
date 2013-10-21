Arquillian Graphene 2 [![Build Status](https://buildhive.cloudbees.com/job/arquillian/job/arquillian-graphene/badge/icon)](https://buildhive.cloudbees.com/job/arquillian/job/arquillian-graphene/)
=====================

> Robust Functional Tests leveraging WebDriver with flavour of sexy AJAX-ready API


Graphene 2 project is designed as set of extensions for [Selenium WebDriver](http://docs.seleniumhq.org/) project focused on rapid development and usability in Java environment.

* API is **straightforward** and enforces tester to write **Ajax-enabled** and **reusable tests**,
* encourages to write tests in a [appropriate level of abstraction](https://docs.jboss.org/author/display/ARQGRA2/Page+Abstractions) using [**Page Objects**](https://docs.jboss.org/author/display/ARQGRA2/Page+Objects) and [**Page Fragments**](https://docs.jboss.org/author/display/ARQGRA2/Page+Fragments),
* the code is robust with improved readability,
  * supports [**request guarding**](https://docs.jboss.org/author/display/ARQGRA2/Request+Guards) and request interception,
  * exposes fluent [**Waiting API**](https://docs.jboss.org/author/display/ARQGRA2/Graphene+Utility+Class#GrapheneUtilityClass-Waitings),
* helps to write **tests portable across browsers**,
* it enables **JQuery selectors as a location strategy** for advanced locators
* brings concepts for on-the-fly **injection of extensions to a page code** (enabling advanced testing features)

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

There is comprehensive guide [Functional Testing using Drone and Graphene](http://arquillian.org/guides/functional_testing_using_graphene/).

You can also use [Getting Started guide](https://docs.jboss.org/author/display/ARQGRA2/Getting+Started) in the [Documentation](https://docs.jboss.org/author/display/ARQGRA2/Home).


Contributing
============

Community contributions are essential for maintaining the vitality of the Arquillian project.

Contributing to the project helps to deliver functionality you need and allows you to share your code with other contributors and users.

We want to keep it as easy as possible to contribute your changes and we will work hard to deliver your contributions in an upcoming release.

Please refer to [How to Contribute](https://github.com/arquillian/arquillian-graphene/blob/master/CONTRIBUTING.md) to find related instructions.


Building the Project
====================

Prerequisities
--------------

* JDK 1.6+
* Maven 3.0.3+


How-to build project
--------------------

    mvn clean install

Running Integration Tests
-------------------------
    
    mvn clean install
    
    cd ftest/
    mvn clean verify -Dbrowser=firefox
    mvn clean verify -Dbrowser=chrome
    mvn clean verify -Dbrowser=phantomjs
