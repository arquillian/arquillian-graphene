package org.jboss.arquillian.graphene.page.extension;

import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractPageExtensionInstallatorProviderTestCase {

    @Mock
    private PageExtensionRegistry registry;

    @Before
    public void prepareRegistry() {
        when(registry.getExtension(WithCycleA.class.getName())).thenReturn(new WithCycleA());
        when(registry.getExtension(WithCycleB.class.getName())).thenReturn(new WithCycleB());
        when(registry.getExtension(WithoutCycleA.class.getName())).thenReturn(new WithoutCycleA());
        when(registry.getExtension(WithoutCycleB.class.getName())).thenReturn(new WithoutCycleB());
    }

    @Test(expected=IllegalStateException.class)
    public void testCycleInRequirements() {
        PageExtensionInstallatorProvider provider = new TestedPageExtensionInstallatorProvider(registry);
        provider.installator(WithCycleA.class.getName());
    }

    @Test
    public void testNoCycleInRequirements() {
        try {

        } catch(IllegalStateException e) {
            Assert.fail("False positive cycle has been detected. " + e.getMessage());
        }
    }

    private static class TestedPageExtensionInstallatorProvider extends AbstractPageExtensionInstallatorProvider {

        public TestedPageExtensionInstallatorProvider(PageExtensionRegistry registry) {
            super(registry);
        }

        @Override
        public PageExtensionInstallator createInstallator(PageExtension extension) {
            return null;
        }

    }

    private static class WithCycleA implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithCycleB.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithCycleB implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithCycleA.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithoutCycleA implements PageExtension {

         @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithoutCycleB.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithoutCycleB implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

}
