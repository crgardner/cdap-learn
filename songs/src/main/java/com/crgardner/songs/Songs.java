package com.crgardner.songs;

import co.cask.cdap.api.app.AbstractApplication;
import co.cask.cdap.api.data.stream.Stream;
import co.cask.cdap.api.dataset.table.Table;

public class Songs extends AbstractApplication {

    @Override
    public void configure() {
        setName("Songs");
        setDescription("Searches song titles for the oft-used word \"love\"");
        addStream(new Stream("songTitles"));
        createDataset("songStatistics", Table.class);
        addFlow(new SongsFlow());
        addProcedure(new SongStatisticsReporter());
    }
}
