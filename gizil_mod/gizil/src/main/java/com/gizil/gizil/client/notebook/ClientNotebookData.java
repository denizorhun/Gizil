package com.gizil.gizil.client.notebook;

import java.util.HashSet;
import java.util.Set;

public class ClientNotebookData {

    private static Set<String> knownPages = new HashSet<>();

    public static void setKnownPages(Set<String> pages) {
        knownPages = pages;
    }

    public static boolean knows(String pageId) {
        return knownPages.contains(pageId);
    }
}