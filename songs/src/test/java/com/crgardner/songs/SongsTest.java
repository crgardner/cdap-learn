package com.crgardner.songs;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import co.cask.cdap.test.ApplicationManager;
import co.cask.cdap.test.FlowManager;
import co.cask.cdap.test.ProcedureManager;
import co.cask.cdap.test.RuntimeMetrics;
import co.cask.cdap.test.RuntimeStats;
import co.cask.cdap.test.StreamWriter;
import co.cask.cdap.test.TestBase;

import com.crgardner.songs.Songs;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class SongsTest extends TestBase {
    private static final Type stringMapType = new TypeToken<Map<String, String>>() {
        private static final long serialVersionUID = 1L; }.getType();
        
    @Test
    public void runsSongsFlow() throws Exception {
        ApplicationManager appManager = deployApplication(Songs.class);

        FlowManager flowManager = appManager.startFlow("SongsFlow");
        
        StreamWriter streamWriter = appManager.getStreamWriter("songTitles");
        streamSongsUsing(streamWriter);
        
        waitForFlowToComplete(flowManager);
        
        Map<String, String> statisticsReport = retrieveStatisticsReport(appManager);
        assertIsComplete(statisticsReport);
        
        appManager.stopAll();
    }

    private void streamSongsUsing(StreamWriter streamWriter) throws IOException {
        streamWriter.send("Love Me Do");
        streamWriter.send("Wicked Game");
        streamWriter.send("Only the Lonely");
        streamWriter.send("Love Hurts");
        streamWriter.send("Crazy Little Thing Called Love");
        streamWriter.send("I Love To Love (But My Baby Loves To Dance)");
        streamWriter.send("It's Love-Love-Love");
        streamWriter.send("Dirty Deeds Done Dirt Cheap");
        streamWriter.send("I Wouldn't Want to Be Like You");
    }
    
    private void waitForFlowToComplete(FlowManager flowManager) throws Exception {
        try {
            RuntimeMetrics metrics = RuntimeStats.getFlowletMetrics("Songs", "SongsFlow", "saver");
            metrics.waitForProcessed(5, 60, TimeUnit.SECONDS);
        } finally {
            flowManager.stop();
        }
    }

    private Map<String, String> retrieveStatisticsReport(ApplicationManager appManager) throws IOException {
        ProcedureManager procedureManager = appManager.startProcedure("SongStatisticsReporter");
        String response = procedureManager.getClient().query("reportStatistics", new HashMap<String, String>());
        return new Gson().<Map<String, String>>fromJson(response, stringMapType);
    }
    
    private void assertIsComplete(Map<String, String> statisticsReport) {
        assertThat(statisticsReport.get("loveSongs"), is("5"));
        assertThat(statisticsReport.get("lotsOfLoveSongs"), is("2"));
        assertThat(statisticsReport.get("minisculeLoveSongs"), is("3"));
    }
}

