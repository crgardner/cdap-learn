package com.crgardner.songs;

import co.cask.cdap.api.flow.Flow;
import co.cask.cdap.api.flow.FlowSpecification;

public class SongsFlow implements Flow {

    public FlowSpecification configure() {
        return FlowSpecification.Builder.with()
                                        .setName("SongsFlow")
                                        .setDescription("Collects song titles")
                                        .withFlowlets()
                                        .add("saver", new SongTitleSaver())
                                        .connect().fromStream("songTitles")
                                        .to("saver")
                                        .build();
    }
}