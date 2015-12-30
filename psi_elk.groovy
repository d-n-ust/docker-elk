@Grab(group='org.elasticsearch', module='elasticsearch', version='2.1.1')

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.aggregations.AggregationBuilders

//----------------------- get bucker counts from elastic -------------------------------

Settings settings = Settings.settingsBuilder()
        .put("cluster.name", "elasticsearch").build();

Client client = TransportClient.builder().settings(settings).build()
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.99.100"), 9300))

def agg_name = 'V2_range'

AggregationBuilder aggregation =
        AggregationBuilders
                .range(agg_name)
                .field("results.V2.value")
                .addRange(0, 1)
                .addRange(1, 2)
                .addRange(2, 3)
                .addRange(3, 4)
                .addRange(4, 5)
                .addRange(5, 6)
                .addRange(6, 7)
                .addRange(7, 8)
                .addRange(8, 9)

def aggs = client
        .prepareSearch('logstash-2015.12.29', 'logstash-2015.12.30')
        .addAggregation(aggregation)
        .execute()
        .actionGet()
        .aggregations
        .get(agg_name)
        .ranges.collectEntries{ [(it.from as Integer): it.docCount] }

println "from elastic: $aggs"
client.close()

//----------------------- calculate psi -------------------------------

ref_populations = [
        'V2': [0: 0.0, 1: 0.096461098, 2: 0.15902148, 3: 0.32636754, 4:0.23, 5: 0.07, 6:0, 7:0.06, 8:0]
]

def get_populations(counts) {
    def sums = counts.inject([:]) { r, v -> r[v.key] = v.value.values().sum() ; r }
    println "sums: $sums"
    counts.inject([:]) { r, v ->
        r[v.key] = v.value.inject([:]) { res, varNum ->
            res[varNum.key] = (varNum.value as Integer) / (sums[v.key] as Integer) ; res
        }
        r
    }
}

def index_value(actual, expected) {
    if (expected == 0) return 0
    (actual - expected) * Math.log(actual / expected)
}

def stability_index(population, ref_population) {
    population.keySet().inject(0) { r, key ->
        r + index_value(population[key], ref_population[key])
    }
}

def populations = get_populations(['V2':aggs])
println "populations: $populations"
println "ref_populations: $ref_populations"

def psi = populations.collect{ p -> stability_index(p.value, ref_populations[p.key]) }
println "psi: $psi"

psi