package com.company.steam_api.editor;

import com.company.utils.Utils;

import java.util.Map;

public class HSPercentageEditor extends AbstractEditor {
    public HSPercentageEditor() {
    }

    public HSPercentageEditor(AbstractEditor next) {
        super(next);
    }

    @Override
    public StringBuilder edit(Map<String, Long> stats, StringBuilder builder) {
        Long totalKills = stats.get("total_kills");
        Long killsHeadshot = stats.remove("total_kills_headshot");

        double headShotPercentage = Utils.roundValue(killsHeadshot * 100. / totalKills);

        builder.append("Headshot percentage: ").append(headShotPercentage).append("\n\n");

        if (next != null) {
            return next.edit(stats, builder);
        }
        return builder;
    }
}
