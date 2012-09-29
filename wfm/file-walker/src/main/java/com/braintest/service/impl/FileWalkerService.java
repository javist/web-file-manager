package com.braintest.service.impl;

import java.io.File;
import java.io.IOException;

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

    private UnpackManager unpackerManager = new UnpackManagerImpl();

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
    public NodeModel[] walk(final String path) {

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

    private NodeModel[] createNodes(File[] files) {
        NodeModel[] nodes = new NodeModel[files.length];
        for (int i = 0; i < files.length; i++) {
            nodes[i] = createNode(files[i]);
        }
        return nodes;
    }

    private NodeModel createNode(File file) {
        boolean hasChild = file.isDirectory() || unpackerManager.isArchive(file.getName());
        return new NodeModel(file.getName(), hasChild);
    }
}
