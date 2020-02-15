package com.company.steam_api.editor;

import com.company.utils.Utils;

import java.util.Map;

public class KDRationEditor extends AbstractEditor {

    public KDRationEditor() {
    }

    public KDRationEditor(AbstractEditor next) {
        super(next);
    }

    @Override
    public StringBuilder edit(Map<String, Long> stats, StringBuilder builder) {
        Long totalKills = stats.get("total_kills");
        Long totalDeaths = stats.get("total_deaths");

        double ratio = Utils.roundValue(totalKills.doubleValue() / totalDeaths);

        builder.append("K/D ratio: ").append(ratio).append("\n");

        if (next != null) {
            return next.edit(stats, builder);
        }
        return builder;
    }
}
