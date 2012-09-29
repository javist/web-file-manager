package com.braintest.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.braintest.exception.UnpackerNotFoundException;
import com.braintest.service.UnpackManager;
import com.braintest.service.UnpackService;

/**
 * UnpackManagerImplTest
 *
 * @author den, @date 29.09.2012 18:28:59
 */
public class UnpackManagerImplTest {

    private UnpackManager target;

    @Before
    public void setup() {
        target = new UnpackManagerImpl();

        Map<String, UnpackService> unpackers = new HashMap<String, UnpackService>();
        unpackers.put("zip", new ZipUnpackService());
        unpackers.put("rar", null);
        ((UnpackManagerImpl) target).setUnpackers(unpackers);
    }

    @Test
    public void zipEndOfPathTest() {
        // Fixture
        String path = "\\root\\opt\\temp\\test.zip";

        // Test
        boolean result = target.hasArchiveInPath(path);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void rarMiddleOfPathTest() {
        // Fixture
        String path = "\\root\\opt\\temp\\test.rar\\rest\\etc";

        // Test
        boolean result = target.hasArchiveInPath(path);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void archiveNotInPathTest() {
        // Fixture
        String path = "\\root\\opt\\temp\\test\\rest\\etc";

        // Test
        boolean result = target.hasArchiveInPath(path);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void isArchiveTest() {
        // Fixture
        String filename = "file.rar";

        // Test
        boolean result = target.isArchive(filename);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isNotArchiveTest() {
        // Fixture
        String filename = "unzip.exe";

        // Test
        boolean result = target.isArchive(filename);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void unpackTest() throws IOException {
        // Fixture
        String path = "\\root\\opt\\temp\\test.zip";

        target = new UnpackManagerImpl();

        Map<String, UnpackService> unpackers = new HashMap<String, UnpackService>();
        ZipUnpackService zipMock = Mockito.mock(ZipUnpackService.class);
        unpackers.put("zip", zipMock);
        ((UnpackManagerImpl) target).setUnpackers(unpackers);

        String unpackDir = UnpackService.TEMP_DIR + "\\test.zip";
        Mockito.when(zipMock.unpack("\\root\\opt\\temp\\test.zip")).thenReturn(unpackDir);

        // Test
        String resultDir = target.unpack(path);

        // Assert
        Assert.assertEquals(unpackDir, resultDir);
    }

    @Test
    public void notFoundUnpackTest() throws IOException {
        // Fixture
        UnpackerNotFoundException exception = null;
        String path = "\\root\\opt\\temp\\test.rar\\rest\\etc";

        target = new UnpackManagerImpl();

        Map<String, UnpackService> unpackers = new HashMap<String, UnpackService>();
        ZipUnpackService zipMock = Mockito.mock(ZipUnpackService.class);
        unpackers.put("zip", zipMock);
        unpackers.put("rar", null);
        ((UnpackManagerImpl) target).setUnpackers(unpackers);

        // Test
        try {
            target.unpack(path);
        } catch (UnpackerNotFoundException e) {
            exception = e;
        }

        // Assert
        Assert.assertNotNull(exception);
    }
}
