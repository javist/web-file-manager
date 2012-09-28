package com.braintest.service;

import com.braintest.model.NodeModel;

/**
 * WalkerService
 *
 * @author den, @date 29.09.2012 0:34:26
 */
public interface WalkerService {

    /**
     * Get default separator in path
     *
     * @return separator as string
     */
    String getSeparator();

    /**
     * Get child nodes for parent path
     *
     * @param path of parent
     * @return collection of nodes
     */
    NodeModel[] walk(String path);
}
