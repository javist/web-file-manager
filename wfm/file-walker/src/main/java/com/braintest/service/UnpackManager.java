package com.braintest.service;

import java.io.IOException;

/**
 * ArchiveManager
 *
 * @author den, @date 29.09.2012 0:52:56
 */
public interface UnpackManager {

    boolean hasArchiveInPath(String path);

    boolean isArchive(String fileName);

    String unpack(String path) throws IOException;
}
