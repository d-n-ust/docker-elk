@Grab(group='joda-time', module='joda-time', version='2.9.1')

import org.joda.time.DateTime
import org.joda.time.LocalDate

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
    rndDoubleInRange(interval[0] as Double, interval[1] as Double)
}

def rndIntInRange(min, max) {
    min + new Random().nextInt((max - min) + 1)
}
def rndDoubleInRange(min, max) {
    def x = (min*100 + new Random().nextInt(((max*100 - min*100) + 1) as Integer))
    def d = x / 100
    d
}

//-------------------------------------------------------------------

def file = new File("psi_gen.log")
def ln = System.getProperty('line.separator')
def count = 0
def clients = [1,2,3] as Set
def variables = ['V1','V2','V3'] as Set

def startDate = new DateTime(2015, 8, 1, 12, 0, 0, 0)
def endDate = new DateTime(2016, 1, 15, 12, 0, 0, 0)

for (LocalDate date = startDate.toLocalDate(); date.isBefore(endDate.toLocalDate()); date = date.plusDays(1)) {
    for (client in clients) {
        for (variable in variables) {
            def year  = String.format("%02d", date.year)
            def month = String.format("%02d", date.monthOfYear)
            def day   = String.format("%02d", date.dayOfMonth)

            def varvalue = rndWithProbability([(10) :[0.15, 0.5],
                                               (30) :[0.1,  0.15],
                                               (100):[0.001,0.1]])

            def content = """{"post_date":"${year}-${month}-${day}","client":${client},"var":"${variable}","value":${varvalue}}"""

            file << "$content$ln"

            count++
        }
    }
}
