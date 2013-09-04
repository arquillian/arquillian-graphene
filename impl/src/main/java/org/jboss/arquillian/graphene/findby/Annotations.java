/*
Copyright 2007-2009 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/**
 * <p>
 * Utility class originally copied from WebDriver. It main purpose is to retrieve correct
 * <code>By</code> instance according to the field on which <code>@FindBy</code> or <code>@FinfBys</code> annotation is.</p>
 *
 * <p>The differences are:
 * <ul>
 *  <li>this class supports also Graphene <code>@FindBy</code> with JQuery locators support</li>
 *  <li>it is able to return default locating strategy according to the <code>GrapheneConfiguration</code></li>
 * </ul>
 *  </p>
 *
 * <p>Altered by <a href="mailto:jhuska@redhat.com">Juraj Huska</a></p>.
 */
package org.jboss.arquillian.graphene.findby;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.graphene.spi.findby.ImplementsLocationStrategy;
import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ByChained;

public class Annotations {

    private final Field field;
    private final org.jboss.arquillian.graphene.enricher.findby.How defaultElementLocatingStrategy;

    public Annotations(Field field, org.jboss.arquillian.graphene.enricher.findby.How defaultElementLocatingStrategy) {
        this.field = field;
        this.defaultElementLocatingStrategy = defaultElementLocatingStrategy;
    }

    public boolean isLookupCached() {
        return (field.getAnnotation(CacheLookup.class) != null);
    }

    public By buildBy() {
        assertValidAnnotations();

        By by = null;

        by = checkAndProcessEmptyFindBy();

        FindBys grapheneFindBys = field.getAnnotation(FindBys.class);
        if (by == null && grapheneFindBys != null) {
            by = buildByFromGrapheneFindBys(grapheneFindBys);
        }

        org.openqa.selenium.support.FindBys webDriverFindBys =
                field.getAnnotation(org.openqa.selenium.support.FindBys.class);
        if (by == null && webDriverFindBys != null) {
            by = buildByFromWebDriverFindBys(webDriverFindBys);
        }

        FindBy findBy = field.getAnnotation(FindBy.class);
        if (by == null && findBy != null) {
            by = buildByFromFindBy(findBy);
        }

        org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy = field
                .getAnnotation(org.jboss.arquillian.graphene.enricher.findby.FindBy.class);
        if (by == null && grapheneFindBy != null) {
            by = buildByFromFindBy(grapheneFindBy);
        }

        for (Annotation annotation : field.getAnnotations()) {
            ImplementsLocationStrategy strategy = annotation.annotationType().getAnnotation(ImplementsLocationStrategy.class);
            if (strategy != null) {
                by = buildByFromLocationStrategy(strategy, annotation);
            }
        }

        if (by == null) {
            by = buildByFromDefault();
        }

        if (by == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }

        return by;
    }

    private By checkAndProcessEmptyFindBy() {
        By result = null;

        FindBy findBy = field.getAnnotation(FindBy.class);
        if (findBy != null) {
            int numberOfValues = assertValidFindBy(findBy);
            if (numberOfValues == 0) {
                result = buildByFromDefault();
            }
        }

        org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy = field
                .getAnnotation(org.jboss.arquillian.graphene.enricher.findby.FindBy.class);
        if (grapheneFindBy != null) {
            int numberOfValues = assertValidFindBy(grapheneFindBy);
            if (numberOfValues == 0) {
                result = buildByFromDefault();
            }
        }

        return result;
    }

    protected By buildByFromDefault() {
        String using = field.getName();
        return getByFromGrapheneHow(defaultElementLocatingStrategy, using);
    }

    protected By buildByFromLocationStrategy(ImplementsLocationStrategy strategy, Annotation annotation) {
        try {
            LocationStrategy transformer = strategy.value().newInstance();
            return transformer.fromAnnotation(annotation);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot use locationStrategy " + strategy + " on annotation " + annotation + " on field " + field + ": " + e.getMessage(), e);
        }
    }

    protected By buildByFromGrapheneFindBys(FindBys grapheneFindBys) {
        assertValidGrapheneFindBys(grapheneFindBys);

        org.jboss.arquillian.graphene.enricher.findby.FindBy[] findByArray = grapheneFindBys.value();
        By[] byArray = new By[findByArray.length];
        for (int i = 0; i < findByArray.length; i++) {
            byArray[i] = buildByFromFindBy(findByArray[i]);
        }

        return new ByChained(byArray);
    }

    protected By buildByFromWebDriverFindBys(org.openqa.selenium.support.FindBys webDriverFindBys) {
        assertValidWebDriverFindBys(webDriverFindBys);

        FindBy[] findByArray = webDriverFindBys.value();
        By[] byArray = new By[findByArray.length];
        for (int i = 0; i < findByArray.length; i++) {
            byArray[i] = buildByFromFindBy(findByArray[i]);
        }

        return new ByChained(byArray);
    }

    protected By buildByFromFindBy(FindBy findBy) {
        assertValidFindBy(findBy);

        By ans = buildByFromShortFindBy(findBy);
        if (ans == null) {
            ans = buildByFromLongFindBy(findBy);
        }

        return ans;
    }

    protected By buildByFromFindBy(org.jboss.arquillian.graphene.enricher.findby.FindBy findBy) {
        assertValidFindBy(findBy);

        By ans = buildByFromShortFindBy(findBy);
        if (ans == null) {
            ans = buildByFromLongFindBy(findBy);
        }

        return ans;
    }

    protected By buildByFromLongFindBy(FindBy findBy) {
        How how = findBy.how();
        String using = findBy.using();

        switch (how) {
            case CLASS_NAME:
                return By.className(using);

            case CSS:
                return By.cssSelector(using);

            case ID:
                return By.id(using);

            case ID_OR_NAME:
                return new ByIdOrName(using);

            case LINK_TEXT:
                return By.linkText(using);

            case NAME:
                return By.name(using);

            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(using);

            case TAG_NAME:
                return By.tagName(using);

            case XPATH:
                return By.xpath(using);

            default:
                // Note that this shouldn't happen (eg, the above matches all
                // possible values for the How enum)
                throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }
    }

    protected By buildByFromLongFindBy(org.jboss.arquillian.graphene.enricher.findby.FindBy findBy) {
        org.jboss.arquillian.graphene.enricher.findby.How how = findBy.how();
        String using = findBy.using();
        return getByFromGrapheneHow(how, using);
    }

    private By getByFromGrapheneHow(org.jboss.arquillian.graphene.enricher.findby.How how, String using) {
        switch (how) {
            case CLASS_NAME:
                return By.className(using);

            case CSS:
                return By.cssSelector(using);

            case ID:
                return By.id(using);

            case ID_OR_NAME:
                return new ByIdOrName(using);

            case LINK_TEXT:
                return By.linkText(using);

            case NAME:
                return By.name(using);

            case PARTIAL_LINK_TEXT:
                return By.partialLinkText(using);

            case TAG_NAME:
                return By.tagName(using);

            case XPATH:
                return By.xpath(using);

            case JQUERY:
                return new ByJQuery(using);

            default:
                // Note that this shouldn't happen (eg, the above matches all
                // possible values for the How enum)
                throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }
    }

    protected By buildByFromShortFindBy(FindBy findBy) {
        if (!"".equals(findBy.className()))
            return By.className(findBy.className());

        if (!"".equals(findBy.css()))
            return By.cssSelector(findBy.css());

        if (!"".equals(findBy.id()))
            return By.id(findBy.id());

        if (!"".equals(findBy.linkText()))
            return By.linkText(findBy.linkText());

        if (!"".equals(findBy.name()))
            return By.name(findBy.name());

        if (!"".equals(findBy.partialLinkText()))
            return By.partialLinkText(findBy.partialLinkText());

        if (!"".equals(findBy.tagName()))
            return By.tagName(findBy.tagName());

        if (!"".equals(findBy.xpath()))
            return By.xpath(findBy.xpath());

        // Fall through
        return null;
    }

    protected By buildByFromShortFindBy(org.jboss.arquillian.graphene.enricher.findby.FindBy findBy) {
        if (!"".equals(findBy.className()))
            return By.className(findBy.className());

        if (!"".equals(findBy.css()))
            return By.cssSelector(findBy.css());

        if (!"".equals(findBy.id()))
            return By.id(findBy.id());

        if (!"".equals(findBy.linkText()))
            return By.linkText(findBy.linkText());

        if (!"".equals(findBy.name()))
            return By.name(findBy.name());

        if (!"".equals(findBy.partialLinkText()))
            return By.partialLinkText(findBy.partialLinkText());

        if (!"".equals(findBy.tagName()))
            return By.tagName(findBy.tagName());

        if (!"".equals(findBy.xpath()))
            return By.xpath(findBy.xpath());

        if (!"".equals(findBy.jquery()))
            return new ByJQuery(findBy.jquery());

        // Fall through
        return null;
    }

    private void assertValidAnnotations() {
        FindBys grapheneFindBys = field.getAnnotation(FindBys.class);

        org.openqa.selenium.support.FindBys webDriverFindBys = field.getAnnotation(org.openqa.selenium.support.FindBys.class);

        FindBy webDriverFindBy = field.getAnnotation(FindBy.class);

        org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy = field
                .getAnnotation(org.jboss.arquillian.graphene.enricher.findby.FindBy.class);

        if ((grapheneFindBys != null || webDriverFindBys != null) && (webDriverFindBy != null || grapheneFindBy != null)) {
            throw new IllegalArgumentException("If you use a '@FindBys' annotation, "
                    + "you must not also use a '@FindBy' annotation");
        }
    }

    private void assertValidGrapheneFindBys(FindBys grapheneFindBys) {
        for (org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy : grapheneFindBys.value()) {
            assertValidFindBy(grapheneFindBy);
        }
    }

    private void assertValidWebDriverFindBys(org.openqa.selenium.support.FindBys webDriverFindBys) {
        for (FindBy webDriverFindBy : webDriverFindBys.value()) {
            assertValidFindBy(webDriverFindBy);
        }
    }

    private int assertValidFindBy(FindBy findBy) {
        if (findBy.how() != null) {
            if (findBy.using() == null) {
                throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
            }
        }

        Set<String> finders = new HashSet<String>();
        if (!"".equals(findBy.using()))
            finders.add("how: " + findBy.using());
        if (!"".equals(findBy.className()))
            finders.add("class name:" + findBy.className());
        if (!"".equals(findBy.css()))
            finders.add("css:" + findBy.css());
        if (!"".equals(findBy.id()))
            finders.add("id: " + findBy.id());
        if (!"".equals(findBy.linkText()))
            finders.add("link text: " + findBy.linkText());
        if (!"".equals(findBy.name()))
            finders.add("name: " + findBy.name());
        if (!"".equals(findBy.partialLinkText()))
            finders.add("partial link text: " + findBy.partialLinkText());
        if (!"".equals(findBy.tagName()))
            finders.add("tag name: " + findBy.tagName());
        if (!"".equals(findBy.xpath()))
            finders.add("xpath: " + findBy.xpath());

        // A zero count is okay: it means to look by name or id.
        if (finders.size() > 1) {
            throw new IllegalArgumentException(
                    String.format("You must specify at most one location strategy. Number found: %d (%s)", finders.size(),
                            finders.toString()));
        }

        return finders.size();
    }

    private int assertValidFindBy(org.jboss.arquillian.graphene.enricher.findby.FindBy findBy) {
        if (findBy.how() != null) {
            if (findBy.using() == null) {
                throw new IllegalArgumentException("If you set the 'how' property, you must also set 'using'");
            }
        }

        Set<String> finders = new HashSet<String>();
        if (!"".equals(findBy.using()))
            finders.add("how: " + findBy.using());
        if (!"".equals(findBy.className()))
            finders.add("class name:" + findBy.className());
        if (!"".equals(findBy.css()))
            finders.add("css:" + findBy.css());
        if (!"".equals(findBy.id()))
            finders.add("id: " + findBy.id());
        if (!"".equals(findBy.linkText()))
            finders.add("link text: " + findBy.linkText());
        if (!"".equals(findBy.name()))
            finders.add("name: " + findBy.name());
        if (!"".equals(findBy.partialLinkText()))
            finders.add("partial link text: " + findBy.partialLinkText());
        if (!"".equals(findBy.tagName()))
            finders.add("tag name: " + findBy.tagName());
        if (!"".equals(findBy.xpath()))
            finders.add("xpath: " + findBy.xpath());
        if (!"".equals(findBy.jquery()))
            finders.add("xpath: " + findBy.jquery());

        // A zero count is okay: it means to look by name or id.
        if (finders.size() > 1) {
            throw new IllegalArgumentException(
                    String.format("You must specify at most one location strategy. Number found: %d (%s)", finders.size(),
                            finders.toString()));
        }

        return finders.size();
    }
}
