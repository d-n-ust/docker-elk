#!/bin/sh

curl -i -v --data @- \
-H "Content-Type: application/json" \
-X PUT http://192.168.99.100:9200/.kibana/visualization/last-3-hours-latency << EOF
{
    "title": "last-3-hours-latency",
    "visState": "{\"type\":\"histogram\",\"params\":{\"shareYAxis\":true,\"addTooltip\":true,\"addLegend\":true,\"scale\":\"linear\",\"mode\":\"stacked\",\"times\":[],\"addTimeMarker\":false,\"defaultYExtents\":false,\"setYExtents\":false,\"yAxis\":{}},\"aggs\":[{\"id\":\"1\",\"type\":\"count\",\"schema\":\"metric\",\"params\":{}},{\"id\":\"2\",\"type\":\"date_histogram\",\"schema\":\"segment\",\"params\":{\"field\":\"@timestamp\",\"interval\":\"auto\",\"customInterval\":\"2h\",\"min_doc_count\":1,\"extended_bounds\":{}}},{\"id\":\"3\",\"type\":\"range\",\"schema\":\"group\",\"params\":{\"field\":\"latency\",\"ranges\":[{\"from\":0,\"to\":2000},{\"from\":2000,\"to\":4000},{\"from\":4000,\"to\":1000000}]}}],\"listeners\":{}}",
    "uiStateJSON": "{}",
    "description": "",
    "savedSearchId": "last-12-hours,-V2",
    "version": 1,
    "kibanaSavedObjectMeta": {
        "searchSourceJSON": "{\"filter\":[]}"
    }
}
EOF