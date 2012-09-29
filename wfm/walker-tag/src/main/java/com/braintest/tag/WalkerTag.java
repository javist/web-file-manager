package com.braintest.tag;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.braintest.api.WalkerService;
import com.braintest.model.NodeModel;
import com.braintest.service.impl.FileWalkerService;

public class WalkerTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private WalkerService walkerService = new FileWalkerService();

    public int doStartTag() {
        try {
            String cp = pageContext.getServletContext().getContextPath();
            pageContext.getOut().write("Hello world!  !!");

            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            String path = request.getParameter("path");
            final String parent;
            if (StringUtils.isBlank(path)) {
                path = parent = walkerService.getSeparator();
            } else {
                parent = path.substring(0, path.lastIndexOf("\\"));
            }

            pageContext.getOut().write("<br/>");

            if (StringUtils.isNotBlank(parent)) {
                pageContext.getOut().write("<a href=\"" + cp + "?path=" + parent + "\">");
            } else {
                pageContext.getOut().write("<a href=\"" + cp + "?path=" + walkerService.getSeparator() + "\">");
            }
            pageContext.getOut().write("..");
            pageContext.getOut().write("</a>");

            NodeModel[] nodes = walkerService.walk(path);
            for (NodeModel node : nodes) {
                final String fullpath;
                if (walkerService.getSeparator().equals(path)) {
                    fullpath = path + node.getName();
                } else {
                    fullpath = path + walkerService.getSeparator() + node.getName();
                }
                pageContext.getOut().write("<br/>");
                if (node.isHasChild()) {
                    pageContext.getOut().write("<a href=\"" + cp + "?path=" + fullpath + "\">");
                    pageContext.getOut().write(fullpath);
                    pageContext.getOut().write("</a>");
                } else {
                    pageContext.getOut().write("<span>");
                    pageContext.getOut().write(fullpath);
                    pageContext.getOut().write("</span>");
                }
            }


        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return SKIP_BODY;
    }

}
