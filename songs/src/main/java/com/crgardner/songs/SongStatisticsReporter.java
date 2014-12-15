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
        Builder<String, Object> songStatisticsReport = ImmutableMap.<String, Object> builder();

        report("love_songs", "loveSongs", songStatisticsReport);
        report("lots_of_love", "lotsOfLoveSongs", songStatisticsReport);
        report("miniscule_love", "minisculeLoveSongs", songStatisticsReport);

        responder.sendJson(new ProcedureResponse(SUCCESS),songStatisticsReport.build());
    }

    private void report(String songStatistic, String key, Builder<String, Object> songStatisticsReport) {
        Row row = songStatistics.get(Bytes.toBytes(songStatistic));
        songStatisticsReport.put(key, String.valueOf(row.getLong("value")));
    }
}