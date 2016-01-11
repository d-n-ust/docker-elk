#!/bin/sh

curl -i -v --data @- \
-H "Content-Type: application/json" \
-X PUT http://192.168.99.100:9200/.kibana/dashboard/last-3-hours-latency-dash << EOF
{
	"title": "last-3-hours-latency-dash",
	"hits": 0,
	"description": "",
	"panelsJSON": "[{\"id\":\"last-3-hours-latency\",\"type\":\"visualization\",\"panelIndex\":1,\"size_x\":4,\"size_y\":3,\"col\":1,\"row\":4}]",
	"optionsJSON": "{\"darkTheme\":false}",
	"uiStateJSON": "{}",
	"version": 1,
	"timeRestore": true,
	"timeTo": "now",
	"timeFrom": "now-12h",
	"kibanaSavedObjectMeta": {
	"searchSourceJSON": "{\"filter\":[{\"query\":{\"query_string\":{\"query\":\"*\",\"analyze_wildcard\":true}}}]}"
	}
}
EOF