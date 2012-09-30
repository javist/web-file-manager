package com.braintest.service;

import java.io.IOException;

/**
 * Service interface which unpack the file path
 *
 * @author den, @date 29.09.2012 0:55:12
 */
public interface UnpackService {

    final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * Find archives in path and unpack it to OS temp dir
     *
     * @param path with archives
     * @return new unpacked path
     * @throws IOException
     */
    String unpack(String path) throws IOException;
}
