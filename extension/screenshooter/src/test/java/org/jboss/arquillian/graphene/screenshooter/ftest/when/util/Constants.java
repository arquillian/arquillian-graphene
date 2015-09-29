package org.jboss.arquillian.graphene.screenshooter.ftest.when.util;

import java.io.File;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Constants {

    public static final String TEST_METHOD_NAME = "testMethod";

    public static final String SAMPLE_HTML_PATH = ("file://" + System.getProperty("user.dir")
        + "/src/test/resources/org/jboss/arquillian/graphene/screenshooter/ftest/sample.html").replace("/",
        File.separator);

    public static final String SCREENSHOTS_DIRECTORY =
        (System.getProperty("user.dir") + "/target/screenshots/").replace("/", File.separator);

    public static final String PATH_TO_ARQ_XML =
        (System.getProperty("user.dir") + "/src/test/resources/when/arquillian.xml").replace("/", File.separator);
}
