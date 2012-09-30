package com.braintest.tag;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.braintest.api.WalkerService;
import com.braintest.model.NodeModel;
import com.braintest.service.impl.FileWalkerService;

/**
 * JSTL Tag for render tree
 *
 * @author dkosinsky, @date 30.09.2012 21:31:26
 */
public class WalkerTag extends SimpleTagSupport {

    private static final String VIEW = "<div class=\"wfm\"><div class=\"wfm-parent\">%s</div>%s<ul class=\"wfm-tree\">%s</ul></div>";

    private static final String PARENT_NODE = "<div><a class=\"wfm-parent\" href=\"%s?path=%s\">..</a></div>";

    private static final String NODE = "<li><span class=\"wfm-file wfm-%s\">%s</span></li>";

    private static final String EXPANDABLE_NODE = "<li><a class=\"wfm-expand\" href=\"%s?path=%s\"><span class=\"%s\">%s</span></a></li>";

    private static final String ERROR_NODE = "<div><ul class=\"wfm-tree\"><li>%s</li></ul></div>";

    private final WalkerService walkerService;

    private String path;

    public WalkerTag() {
        walkerService = new FileWalkerService();
    }

    @Override
    public void doTag() throws JspException, IOException {
        super.doTag();

        final String separator = walkerService.getSeparator();
        final PageContext pageContext = (PageContext) getJspContext();
        final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        // Select path from http request or tag attribute or default separator
        final String curPath = selectPath(request);

        // Init parent and child path
        final String parentPath = FilenameUtils.getFullPathNoEndSeparator(curPath);
        final String childPath;
        if (curPath.endsWith(separator)) {
            childPath = curPath;
        } else {
            childPath = curPath + separator;
        }

        Collection<NodeModel> nodes;
        try {
            nodes = walkerService.walk(curPath);
        } catch (RuntimeException e) {
            String errorBody = String.format(ERROR_NODE, e.getMessage());
            pageContext.getOut().write(errorBody);
            return;
        }

        final JspFragment jspBody = getJspBody();
        if (jspBody != null) {
            // Render custom body html
            getJspContext().setAttribute("wfmParent", parentPath);
            getJspContext().setAttribute("wfmPath", childPath);
            getJspContext().setAttribute("wfmNodes", nodes);
            jspBody.invoke(null);
        } else {
            // Render default body html
            final String body = renderBody(curPath, parentPath, childPath, nodes, request);
            pageContext.getOut().write(body);
        }
    }

    /**
     * Get current path from request or attribute tag or default OS file separator
     *
     * @param request object
     * @return current path
     */
    private String selectPath(HttpServletRequest request) {
        String pathFromReq = request.getParameter("path");
        if (StringUtils.isBlank(pathFromReq) && StringUtils.isNotBlank(path)) {
            return path;
        } else if (StringUtils.isNotBlank(pathFromReq)) {
            return pathFromReq;
        } else {
            return walkerService.getSeparator();
        }
    }

    /**
     * Render Tag body
     *
     * @param curPath current path
     * @param parentPath parent of current path
     * @param childPath path for child nodes
     * @param nodes child nodes
     * @param request object
     * @return html body string
     */
    private String renderBody(String curPath, String parentPath, String childPath,
                              Collection<NodeModel> nodes, HttpServletRequest request) {

        final String contextPath = request.getRequestURI();
        final StringBuffer nodesView = new StringBuffer();

        final String parentLink;
        if (!childPath.equals(parentPath)) {
            parentLink = String.format(PARENT_NODE, contextPath, parentPath);
        } else {
            parentLink = "";
        }

        for (NodeModel node : nodes) {

            String nodeName = node.getName();
            String nodeType = node.getType();
            if (node.isHasChild()) {
                final String href = childPath + nodeName;
                nodesView.append(String.format(EXPANDABLE_NODE, contextPath, href, nodeType, nodeName));
            } else {
                nodesView.append(String.format(NODE, nodeType, nodeName));
            }
        }
        return String.format(VIEW, curPath, parentLink, nodesView.toString());
    }

    /**
     * Set default path from tag attribute
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }
}
