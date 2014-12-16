package com.crgardner.songs;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import co.cask.cdap.api.annotation.ProcessInput;
import co.cask.cdap.api.annotation.UseDataSet;
import co.cask.cdap.api.common.Bytes;
import co.cask.cdap.api.dataset.table.Increment;
import co.cask.cdap.api.dataset.table.Table;
import co.cask.cdap.api.flow.flowlet.AbstractFlowlet;
import co.cask.cdap.api.flow.flowlet.StreamEvent;
import co.cask.cdap.api.metrics.Metrics;

public class SongTitleAnalyzer extends AbstractFlowlet {
    
    @UseDataSet("songStatistics")
    private Table songStatistics;
    
    private Metrics flowletMetrics;

    @ProcessInput
    public void process(StreamEvent event) throws UnsupportedEncodingException {
        byte[] songTitle = Bytes.toBytes(event.getBody());

        if (songTitle == null) {
            return;
        }

        updateFlowletMetrics();
        captureStatisticsFor(Bytes.toString(songTitle));
    }

    private void updateFlowletMetrics() {
        flowletMetrics.count("allSongs", 1);
    }

    private void captureStatisticsFor(String songTitleValue) {
        int matchCount = StringUtils.countMatches(songTitleValue.toLowerCase(), "love");

        if (matchCount >= 1) {
            incrementMetric("love_songs");
        }

        if (matchCount > 1) {
            incrementMetric("lots_of_love");
        }

        if (matchCount == 1) {
            incrementMetric("miniscule_love");
        }
    }

    private void incrementMetric(String counterName) {
        songStatistics.increment(new Increment(counterName).add("value", 1L));
    }
}