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
 * <code>By</code> instance according to the field on which <code>@Find</code> annotation is.</p> 
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
package org.jboss.arquillian.graphene.enricher.findby;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class Annotations {

    private Field field;

    public Annotations(Field field) {
        this.field = field;
    }

    public boolean isLookupCached() {
        return (field.getAnnotation(CacheLookup.class) != null);
    }

    public By buildBy() {
        //    assertValidAnnotations();

        By ans = null;

        ans = checkAndProcessEmptyFindBy();

        //    FindBys findBys = field.getAnnotation(FindBys.class);
        //    if (ans == null && findBys != null) {
        //      ans = buildByFromFindBys(findBys);
        //    }

        FindBy findBy = field.getAnnotation(FindBy.class);
        if (ans == null && findBy != null) {
            ans = buildByFromFindBy(findBy);
        }

        org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy = field
                .getAnnotation(org.jboss.arquillian.graphene.enricher.findby.FindBy.class);
        if (ans == null && grapheneFindBy != null) {
            ans = buildByFromFindBy(grapheneFindBy);
        }

        if (ans == null) {
            ans = buildByFromDefault();
        }

        if (ans == null) {
            throw new IllegalArgumentException("Cannot determine how to locate element " + field);
        }

        return ans;
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
        org.jboss.arquillian.graphene.enricher.findby.How how = org.jboss.arquillian.graphene.enricher.findby.How
                .valueOf(GrapheneConfigurationContext.getProxy().getDefaultElementLocatingStrategy().toUpperCase());

        String using = field.getName();
        return getByFromGrapheneHow(how, using);
    }

    //  protected By buildByFromFindBys(FindBys findBys) {
    //    assertValidFindBys(findBys);
    //
    //    FindBy[] findByArray = findBys.value();
    //    By[] byArray = new By[findByArray.length];
    //    for (int i = 0; i < findByArray.length; i++) {
    //      byArray[i] = buildByFromFindBy(findByArray[i]);
    //    }
    //
    //    return new ByChained(byArray);
    //  }

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
                return ByJQuery.jquerySelector(using);

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
            return ByJQuery.jquerySelector((findBy.jquery()));

        // Fall through
        return null;
    }

    //    private void assertValidAnnotations() {
    //        FindBys findBys = field.getAnnotation(FindBys.class);
    //        FindBy findBy = field.getAnnotation(FindBy.class);
    //        org.jboss.arquillian.graphene.enricher.findby.FindBy grapheneFindBy = field
    //                .getAnnotation(org.jboss.arquillian.graphene.enricher.findby.FindBy.class);
    //
    //        if (findBys != null && (findBy != null || grapheneFindBy != null)) {
    //            throw new IllegalArgumentException("If you use a '@FindBys' annotation, "
    //                    + "you must not also use a '@FindBy' annotation");
    //        }
    //    }

    //  private void assertValidFindBys(FindBys findBys) {
    //    for (FindBy findBy : findBys.value()) {
    //      assertValidFindBy(findBy);
    //    }
    //  }

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
