package com.braintest.service.impl;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.braintest.model.NodeModel;
import com.braintest.service.UnpackManager;
import com.braintest.utils.ReflectionUtils;

/**
 * FileWalkerServiceTest
 *
 * @author den, @date 29.09.2012 18:58:13
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( FileWalkerService.class )
public class FileWalkerServiceTest {

    private FileWalkerService target;

    @Mock private UnpackManager unpackerManager;

    @Before
    public void setup() {
        target = new FileWalkerService();

        MockitoAnnotations.initMocks(this);
        ReflectionUtils.setField(target, "unpackerManager", unpackerManager);
    }

    @Test
    public void getSeparatorTest() {
        // Fixture
        String expectedSeparator = System.getProperty("file.separator");

        // Test
        String actualSeparator = target.getSeparator();

        // Assert
        Assert.assertEquals(expectedSeparator, actualSeparator);
    }

    @Test
    public void walkWithNoArchiveTest() throws Exception {
        // Fixture
        String path = "\\opt\\rest\\etc";

        File fileMock = Mockito.mock(File.class);
        File[] listMockFiles = mockListFiles();
        Mockito.when(fileMock.listFiles()).thenReturn(listMockFiles);

        PowerMockito.whenNew(File.class).withArguments(path).thenReturn(fileMock);

        // Test
        NodeModel[] actualNodes = target.walk(path);

        // Assert
        assertNodeModels(actualNodes);
    }

    @Test
    public void walkWithArchiveTest() throws Exception {
        // Fixture
        String path = "\\opt\\arch.zip\\rest\\etc";
        String unpackedPath = "c:\\Users\\someone\\temp\\arch.zip\\rest\\etc";

        Mockito.when(unpackerManager.hasArchiveInPath(path)).thenReturn(true);
        Mockito.when(unpackerManager.unpack(path)).thenReturn(unpackedPath);

        File fileMock = Mockito.mock(File.class);
        File[] listMockFiles = mockListFiles();
        Mockito.when(fileMock.listFiles()).thenReturn(listMockFiles);

        PowerMockito.whenNew(File.class).withArguments(unpackedPath).thenReturn(fileMock);

        // Test
        NodeModel[] actualNodes = target.walk(path);

        // Assert
        assertNodeModels(actualNodes);
    }

    private File[] mockListFiles() {
        File file1 = Mockito.mock(File.class);
        Mockito.when(file1.getName()).thenReturn("file1");
        Mockito.when(file1.isDirectory()).thenReturn(true);

        File file2 = Mockito.mock(File.class);
        Mockito.when(file2.getName()).thenReturn("file2");
        Mockito.when(file2.isDirectory()).thenReturn(false);

        File file3 = Mockito.mock(File.class);
        Mockito.when(file3.getName()).thenReturn("file3");
        Mockito.when(file3.isDirectory()).thenReturn(false);

        return new File[] {file1, file2, file3};
    }

    private void assertNodeModels(NodeModel[] actualNodes) {
        Assert.assertSame(3, actualNodes.length);

        Assert.assertEquals(actualNodes[0].getName(), "file1");
        Assert.assertEquals(actualNodes[0].isHasChild(), true);

        Assert.assertEquals(actualNodes[1].getName(), "file2");
        Assert.assertEquals(actualNodes[1].isHasChild(), false);

        Assert.assertEquals(actualNodes[2].getName(), "file3");
        Assert.assertEquals(actualNodes[2].isHasChild(), false);
    }
}
