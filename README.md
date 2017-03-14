Arquillian Graphene 2 [![Build Status](https://travis-ci.org/arquillian/arquillian-graphene.svg?branch=master)](https://travis-ci.org/arquillian/arquillian-graphene)
=====================

> Robust Functional Tests leveraging WebDriver with flavour of sexy AJAX-ready API

Documentation
=============

Graphene 2 project is designed as set of extensions for [Selenium WebDriver](http://docs.seleniumhq.org/) project focused on rapid development and usability in Java environment.

Extensive documentation is available in the docs section.

* [Graphene Introduction]() walks you through the project information, elaborating what it is about and why use it. 
* If you want to quickly get started with sample tests and use-cases, have a look at [Getting Started Guide]() to see how to setup 
project and use it to write robust tests.
* Have a look at [Graphene Configuration]() for all your configuration needs and details of different properties available at your disposal.
* [Graphene Utility Class]() enlists the library of static methods that can be directly used from tests.
* For detailed reading of the different features available to you out of the box, don't forget to have a look at:
   * [Waiting API]()
   * [Request Guards]()
   * [Page Abstractions]()
   * [Location Strategies]()
   * [Dependency Injection]()
   * [Parallel Browsers]()
   * [JavaScript Interface]()
   * [Tips and Tricks]()
   * [Advanced Techniques]()    
   
Getting Started
---------------

Apart from the documentation available here, there is also a comprehensive guide 
[Functional Testing using Drone and Graphene](http://arquillian.org/guides/functional_testing_using_graphene/) to 
help you leverage the benefits of Graphene for writing robust functional tests.

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
