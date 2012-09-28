package com.braintest.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.braintest.exception.UnpackerNotFoundException;
import com.braintest.service.UnpackerManager;
import com.braintest.service.UnpackerService;

/**
 * ArchiveManagerImpl
 *
 * @author den, @date 29.09.2012 0:53:11
 */
public class UnpackerManagerImpl implements UnpackerManager {

    private final Set<String> unpackersType = new LinkedHashSet<String>();

    private final Map<String, UnpackerService> unpackers = new HashMap<String, UnpackerService>();

    {
        Map<String, UnpackerService> temp = new HashMap<String, UnpackerService>();
        temp.put("zip", new ZipUnpackerService());
        temp.put("rar", null);
        setUnpackers(temp);
    }

    @Override
    public boolean hasArchiveInPath(String path) {
        for (String type : unpackersType) {
            return path.indexOf(type) > -1;
        }
        return false;
    }

    @Override
    public boolean isArchive(String fileName) {
        String ext = FilenameUtils.getExtension(fileName);
        return unpackers.keySet().contains(ext);
    }

    @Override
    public String unpack(String path) throws UnpackerNotFoundException, IOException {

        StringBuilder archDir = new StringBuilder();
        String[] dirs = StringUtils.split(path, FileWalkerService.PATH_SEPARATOR);

        for (String dir : dirs) {

            archDir.append(FileWalkerService.PATH_SEPARATOR);
            archDir.append(dir);

            String ext = FilenameUtils.getExtension(dir);

            if (unpackers.containsKey(ext)) {
                UnpackerService unpacker = unpackers.get(ext);
                if (unpacker != null) {
                    String unpackDir = unpacker.unpack(archDir.toString());
                    archDir = new StringBuilder(unpackDir);
                } else {
                    throw new UnpackerNotFoundException("Unpacker for type " + ext + " not found");
                }
            }
        }
        return archDir.toString();
    }

    @Override
    public void setUnpackers(Map<String, UnpackerService> unpackers) {
        this.unpackers.putAll(unpackers);
        this.unpackersType.addAll(unpackers.keySet());
    }
}
