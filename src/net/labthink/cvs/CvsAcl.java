/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.cvs;

import java.util.Vector;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author Mosliu
 */
public class CvsAcl {

    String username = "";
    boolean all = false;
    boolean deny = false;
    boolean read = false;
    boolean write = false;
    boolean tag = false;
    boolean create = false;
    boolean control = false;
    boolean error = false;

    CvsAcl() {
    }

    CvsAcl(String name ) {
        username = name;
    }
    CvsAcl(Vector v1) {
        username = (String) v1.get(0);
        all = (Boolean) v1.get(1);
        deny = (Boolean) v1.get(2);
        read = (Boolean) v1.get(3);
        write = (Boolean) v1.get(4);
        tag = (Boolean) v1.get(5);
        create = (Boolean) v1.get(6);
        control = (Boolean) v1.get(7);
        correct();
    }

    public Element getXmlElement() {
        correct();
        if (error == true) {
            return null;
        }
        Element acl = DocumentHelper.createElement("acl");
        if (username != null && (username.equals("") == false)) {
            acl.addAttribute("user", username);
        }
        if (all) {
            Element all = acl.addElement("all");
        }
        if (deny) {
            Element all = acl.addElement("all");
            all.addAttribute("deny", "1");
        }
        if (read) {
            Element el = acl.addElement("read");
        }
        if (write) {
            Element el = acl.addElement("write");
        }
        if (tag) {
            Element el = acl.addElement("tag");
        }
        if (create) {
            Element el = acl.addElement("create");
        }
        if (control) {
            Element el = acl.addElement("control");
        }
        return acl;
    }

    public void correct() {
        if (deny == true) {
            all = false;
            read = false;
            write = false;
            tag = false;
            create = false;
            control = false;
        }
        if (all == true) {
            read = false;
            write = false;
            tag = false;
            create = false;
            control = false;
        }
        if (all || deny || read || write || tag || create || control) {
            error = false;
        } else {
            error = true;
        }
    }

    public void set(String acl) {
        if (acl.equalsIgnoreCase("all")) {
            all = true;
        }
        if (acl.equalsIgnoreCase("deny")) {
            deny = true;
        }
        if (acl.equalsIgnoreCase("read")) {
            read = true;
        }
        if (acl.equalsIgnoreCase("write")) {
            write = true;
        }
        if (acl.equalsIgnoreCase("tag")) {
            tag = true;
        }
        if (acl.equalsIgnoreCase("create")) {
            create = true;
        }
        if (acl.equalsIgnoreCase("control")) {
            control = true;
        }
    }

    public Vector toVector() {
        Vector v = new Vector();
        v.add(username);
        v.add(all);
        v.add(deny);
        v.add(read);
        v.add(write);
        v.add(tag);
        v.add(create);
        v.add(control);
        return v;

    }
}
