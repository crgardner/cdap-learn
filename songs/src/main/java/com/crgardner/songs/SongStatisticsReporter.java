package com.crgardner.songs;

import static co.cask.cdap.api.procedure.ProcedureResponse.Code.SUCCESS;
import co.cask.cdap.api.annotation.Handle;
import co.cask.cdap.api.annotation.UseDataSet;
import co.cask.cdap.api.common.Bytes;
import co.cask.cdap.api.dataset.table.Row;
import co.cask.cdap.api.dataset.table.Table;
import co.cask.cdap.api.procedure.AbstractProcedure;
import co.cask.cdap.api.procedure.ProcedureRequest;
import co.cask.cdap.api.procedure.ProcedureResponder;
import co.cask.cdap.api.procedure.ProcedureResponse;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class SongStatisticsReporter extends AbstractProcedure {

    @UseDataSet("songStatistics")
    private Table songStatistics;

    @Handle("reportStatistics")
    public void report(ProcedureRequest request, ProcedureResponder responder) throws Exception {
        Builder<String, String> songStatisticsReport = ImmutableMap.builder();

        doReport("love_songs", "loveSongs", songStatisticsReport);
        doReport("lots_of_love", "lotsOfLove", songStatisticsReport);
        doReport("miniscule_love", "minisculeLove", songStatisticsReport);

        responder.sendJson(new ProcedureResponse(SUCCESS),songStatisticsReport.build());
    }

    private void doReport(String songStatistic, String key, Builder<String, String> songStatisticsReport) {
        Row row = songStatistics.get(Bytes.toBytes(songStatistic));
        songStatisticsReport.put(key, String.valueOf(row.getLong("value")));
    }
}