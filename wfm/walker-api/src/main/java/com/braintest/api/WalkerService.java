package com.braintest.api;

import java.util.Collection;

import com.braintest.model.NodeModel;

/**
 * Walker API
 *
 * @author dkosinsky, @date 29.09.2012 0:34:26
 */
public interface WalkerService {

    /**
     * Get default OS file separator
     *
     * @return file separator
     */
    String getSeparator();

    /**
     * Get child nodes for parent path
     *
     * @param path of parent
     * @return collection of child nodes
     */
    Collection<NodeModel> walk(String path);
}
