package com.braintest.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.braintest.service.UnpackService;

/**
 * Implementation of ZIP unpacker
 *
 * @author den, @date 29.09.2012 1:05:17
 */
public class ZipUnpackService implements UnpackService {

    @Override
    public String unpack(String path) throws IOException {
        int BUFFER = 2048;
        File file = new File(path);

        ZipFile zip = new ZipFile(file);
        String newPath = TEMP_DIR + file.getName();

        new File(newPath).mkdir();
        Enumeration<?> zipFileEntries = zip.entries();

        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            File destinationParent = destFile.getParentFile();

            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                int currentByte;
                byte data[] = new byte[BUFFER];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(".zip")) {
                unpack(destFile.getAbsolutePath());
            }
        }
        return newPath;
    }

}
