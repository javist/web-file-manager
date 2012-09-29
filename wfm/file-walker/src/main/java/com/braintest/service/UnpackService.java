package com.braintest.service;

import java.io.IOException;

/**
 * UnpackService
 *
 * @author den, @date 29.09.2012 0:55:12
 */
public interface UnpackService {

    final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    String unpack(String path) throws IOException;
}
