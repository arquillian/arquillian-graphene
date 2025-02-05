package org.arquillian.graphene.rusheye.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.testng.Arquillian;
import org.testng.annotations.BeforeSuite;

@RunAsClient
public class RichFaceBaseTest extends Arquillian{
    
    private final static String SNAPSHOT_PATH = "src/test/resources/snapshot/";
    private final static String RESULT_PATH = "src/test/resources/result/";
    
    @BeforeSuite
    public void cleanUpBaselines() throws IOException {
        this.cleanDir(SNAPSHOT_PATH);
        this.cleanDir(RESULT_PATH);
    }

    protected boolean isFilePresent(String path) {
        return new File(SNAPSHOT_PATH + path).exists();
    }
    
    protected void cleanDir(String path) throws IOException{
        Files.list(Paths.get(path))
            .map(Path::toFile)
            .forEach(File::delete);
    }
}
