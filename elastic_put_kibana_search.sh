#!/bin/sh

curl -i -v --data @- \
-H "Content-Type: application/json" \
-X PUT http://192.168.99.100:9200/.kibana/search/last-2-hours << EOF
{
    "title": "last-2-hours",
	"description": "",
	"hits": 0,
	"columns": [
	  "_source"
	],
	"sort": [
	  "@timestamp",
	  "desc"
	],
	"version": 1,
	"kibanaSavedObjectMeta": {
	  "searchSourceJSON": "{\"index\":\"logstash-*\",\"query\":{\"query_string\":{\"query\":\"@timestamp:[now-2h TO now]\",\"analyze_wildcard\":true}},\"filter\":[],\"highlight\":{\"pre_tags\":[\"@kibana-highlighted-field@\"],\"post_tags\":[\"@/kibana-highlighted-field@\"],\"fields\":{\"*\":{}},\"require_field_match\":false,\"fragment_size\":2147483647}}"
	}
}
EOF