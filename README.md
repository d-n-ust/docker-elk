# Docker ELK stack

Как поднять ELK локально, сгенерировать логи и посмотреть визуалиации в кибана:

1 mkdir elk_prototype

2 cd elk_prototype

3 git clone git@github.com:d-n-ust/docker-elk.git

4 cd docker-elk

5 создаем новую виртуалку (так удобнее работать, когда много проектов на docker):
docker-machine create --driver virtualbox elk

6 eval "$(docker-machine env elk)"

7 поднимаем ELK стек на виртуалке в докер контейнерах:
docker-compose up -d

8 сгенерировать логи:
nc $(docker-machine ip elk) 5000 < variables_gen.log
nc $(docker-machine ip elk) 6000 < psi_gen.log

9 открыть в браузере кибану: http://$(docker-machine ip elk):5601/

10 добавить индекс - паттерны в kibana (в качестве time-field name использовать post_date):
requests-index-*
psi-index-*

11 заимпортировать kibana-export.json

12 смотрим дашборды в кибане
     - psi
     - Variables last week distribution
