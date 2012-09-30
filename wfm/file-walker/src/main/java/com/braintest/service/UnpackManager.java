package com.braintest.service;

import java.io.IOException;

/**
 * Interface of manager service which check and unpack archives
 *
 * @author den, @date 29.09.2012 0:52:56
 */
public interface UnpackManager {

    /**
     * Check archives in a path
     *
     * @param path which can contains archives
     * @return true if has archive in path, false otherwise
     */
    boolean hasArchiveInPath(String path);

    /**
     * Check file name to archive
     *
     * @param fileName obejct
     * @return true if file name is archive, false otherwise
     */
    boolean isArchive(String fileName);

    /**
     * Unpack path
     *
     * @param path with archives
     * @return new unpacked path
     * @throws IOException if something wrong
     */
    String unpack(String path) throws IOException;
}
