
variables = [
    'V1': [1,1,1,1,1,1,1,2,2,2,2,1,1,2,3,1,3,2,2,2,1,2,2,1,2,2,2,1,1,3,2],
    'V2': [1,3,4,2,3,1,1,1,2,4,1,1,1,1,2,4,4,4,2,1,4,1,1,1,2,2,2,1,1,1,1,1,4,1,1,1,1,2,2,2,3,3,3,3,3,3,3,3,2,2,1]
]

ref_populations = [
    'V1': [1: 0.56461098, 2: 0.40902148, 3: 0.02636754],
    'V2': [1: 0.40807946, 2: 0.13432536, 3: 0.26448934, 4: 0.19310585]
]

def get_populations(vars) {
    def counts = vars.inject([:]) { r, v -> r[v.key] = v.value.countBy{it}.sort() ; r }
    def sums = counts.inject([:]) { r, v -> r[v.key] = v.value.values().sum() ; r }
    counts.inject([:]) { r, v ->
        r[v.key] = v.value.inject([:]) { res, varNum ->
            res[varNum.key] = (varNum.value as Integer) / (sums[v.key] as Integer) ; res
        }
        r
    }
}

def index_value(actual, expected) {
    (actual - expected) * Math.log(actual / expected)
}

def stability_index(population, ref_population) {
    population.keySet().inject(0) { r, key ->
        r + index_value(population[key], ref_population[key])
    }
}

def populations = get_populations(variables)
println "populations: $populations"
println "ref_populations: $ref_populations"

def psi = populations.collect{ p -> stability_index(p.value, ref_populations[p.key]) }
println "psi: $psi"

psi