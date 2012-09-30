package com.braintest.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.braintest.model.NodeModel;
import com.braintest.service.UnpackManager;
import com.braintest.api.WalkerService;

/**
 * Implementation of WalkerService API for file system
 *
 * @author den, @date 29.09.2012 0:38:11
 */
public class FileWalkerService implements WalkerService {

    static final String PATH_SEPARATOR = System.getProperty("file.separator");

    // TODO: if possible, to extract it in the config (spring)
    private UnpackManager unpackerManager = new UnpackManagerImpl();

    private static class FileNodeComparator implements Comparator<NodeModel> {

        @Override
        public int compare(NodeModel o1, NodeModel o2) {
            if (o1.isHasChild() && !o2.isHasChild()) {
                return -1;
            } else if (!o1.isHasChild() && o2.isHasChild()) {
                return 1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }

    }

    private static final FileNodeComparator nodeComparator = new FileNodeComparator();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSeparator() {
        return PATH_SEPARATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<NodeModel> walk(final String path) {

        final File file;

        if (unpackerManager.hasArchiveInPath(path)) {
            try {
                file = new File(unpackerManager.unpack(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            file = new File(path);
        }

        return createNodes(file.listFiles());
    }

    private Collection<NodeModel> createNodes(File[] files) {
        List<NodeModel> nodes = new LinkedList<NodeModel>();
        for (File file : files) {
            nodes.add(createNode(file));
        }
        Collections.sort(nodes, nodeComparator);
        return nodes;
    }

    private NodeModel createNode(File file) {
        String fileName = file.getName();
        String fileExt = StringUtils.lowerCase(FilenameUtils.getExtension(fileName));
        if (unpackerManager.isArchive(fileName)) {
            return new NodeModel(fileName, fileExt, true);
        } else if (file.isDirectory()) {
            return new NodeModel(fileName, "folder", true);
        } else {
            return new NodeModel(fileName, fileExt, false);
        }
    }
}
