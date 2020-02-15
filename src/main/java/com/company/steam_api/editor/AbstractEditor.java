package com.company.steam_api.editor;

import java.util.Map;

public abstract class AbstractEditor {
    protected AbstractEditor next;

    public AbstractEditor() {
    }

    public AbstractEditor(AbstractEditor next) {
        this.next = next;
    }

    public abstract StringBuilder edit(Map<String, Long> stats, StringBuilder builder);
}
