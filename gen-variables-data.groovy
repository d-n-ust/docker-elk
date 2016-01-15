@Grab(group='joda-time', module='joda-time', version='2.9.1')

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.Period

def rndWithProbability(Map pmap) {
    def rnd = new Random().nextInt(100)
    def interval
    def eset = pmap.entrySet()
    for (e in eset) {
        if (e.key > rnd) {
            interval = e.value
            break
        }
    }
    rndInRange(interval[0], interval[1])
}

def rndInRange(min, max) {
    if (min instanceof Integer) {
        (min + new Random().nextInt(((max - min) + 1) as Integer)) as Integer
    }
    else {
        def m  = min as Double
        def mx = max as Double
        (m*100 + new Random().nextInt(((mx*100 - m*100) + 1) as Integer)) / 100
    }
}

//-------------------------------------------------------------------

def file = new File("variables_gen.log")
def ln = System.getProperty('line.separator')
def count = 0
def clients = [1,2,3] as Set
def variables = ['V1','V2','V3'] as Set

def startDate = new DateTime(2015, 8, 1, 12, 0, 0, 0)
def endDate = new DateTime(2016, 1, 15, 12, 0, 0, 0)

for (LocalDate date = startDate.toLocalDate(); date.isBefore(endDate.toLocalDate()); date = date.plus(Period.days(1))) {
    for (client in clients) {
        for (variable in variables) {

            def year  = String.format("%02d", date.year)
            def month = String.format("%02d", date.monthOfYear)
            def day   = String.format("%02d", date.dayOfMonth)

            def v0 = rndWithProbability([(10) :[0.15, 0.5],
                                               (30) :[0.1,  0.15],
                                               (100):[0.001,0.1]])

            def v1 = rndWithProbability([(10) :[0,1],
                                         (20) :[1,2],
                                         (40) :[2,3],
                                         (98) :[3,4],
                                         (100) :[4,5]])
            def v2 = rndWithProbability([(30) :[0,1],
                                         (40) :[1,2],
                                         (45) :[2,3],
                                         (95) :[3,4],
                                         (100) :[4,5]])
            def v3 = rndWithProbability([(2) :[0,1],
                                         (30) :[1,2],
                                         (50) :[2,3],
                                         (90) :[3,4],
                                         (100) :[4,5]])
            def latency = rndWithProbability([(90) :[200,500],
                                         (95) :[500,1000],
                                         (98) :[1000,2000],
                                         (99) :[2000,5000],
                                         (100) :[5000,10000]])

            def content = """{"post_date":"${year}-${month}-${day}","client":${client},"latency":${latency},"results":{"V1":{"status":"OK","value":${v1}},"V2":{"status":"OK","value":${v2}},"V3":{"status":"OK","value":${v3}},"V0":{"status":"OK","value":${v0}}}}"""

            file << "$content$ln"

            count++
            println "$count -- $date"
        }
    }
}
