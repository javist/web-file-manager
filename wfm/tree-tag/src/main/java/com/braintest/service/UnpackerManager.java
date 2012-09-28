package com.braintest.service;

import java.io.IOException;
import java.util.Map;

/**
 * ArchiveManager
 *
 * @author den, @date 29.09.2012 0:52:56
 */
public interface UnpackerManager {

    boolean hasArchiveInPath(String path);

    boolean isArchive(String fileName);

    String unpack(String path) throws IOException;

    void setUnpackers(Map<String, UnpackerService> unpackers);
}
