package com.company.steam_api.editor;

import java.util.Comparator;
import java.util.Map;

public class WeaponsEditor extends AbstractEditor {

    public WeaponsEditor() {
    }

    public WeaponsEditor(AbstractEditor next) {
        super(next);
    }

    @Override
    public StringBuilder edit(Map<String, Long> stats, StringBuilder builder) {
        builder.append("Total kills with:").append("\n");
        stats.entrySet().stream()
                .filter(entry -> entry.getKey().contains("total_kills_"))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> builder.append(entry.getKey().replace("total_kills_", "").toUpperCase())
                        .append(":\t\t")
                        .append(entry.getValue())
                        .append("\n"));

        if (next != null) {
            return next.edit(stats, builder);
        }
        return builder;
    }
}
