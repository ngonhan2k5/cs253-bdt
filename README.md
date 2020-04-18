# ✴Tip to install Cloudera and Google Cloud (A replacement of VM images)

### 🌟Install docker and cloudera quickstart on Ubuntu <a name="headin"></a>

This is work for ubuntu 16.04 and 18.04 LTS, if you didn't have ubuntu, try to get free account on G cloud and follow the [🌟Create instance on google cloud (Image Tuts)](#headin2)

sudo -i
### if this is the first time run, we need an update apt package meta
apt update
### install docker and docker-composer
apt-get install docker.io
apt-get install docker-compose
### buid and start docker
./start
### now you can use browser to access http://"External Ip" depending on where your Ubuntu node hosted

## Notes: 
* If you want to get use of Cloudera Manager, you must run /home/cloudera/cloudera-manager after the docker first started and do a instance down and start again.
* If you want to update spark2 java8 and kafka pls download and put /kafka /spark /jdk1.8.0_202 in /ext/lib (see volumes in docker-compose.yml) and you may need to change /cloudera/profile file to match these foldername


## 🌟Create instance on google cloud (Image Tuts) <a name="headin2"></a>

![Image](docs/img/step1.PNG?raw=true)
![Image](docs/img/step2.PNG?raw=true)
![Image](docs/img/step4.PNG?raw=true)
![Image](docs/img/step5.PNG?raw=true)
![Image](docs/img/step6.PNG?raw=true)

Tips: You can paste command. Now you can install docker and cloudera follow [🌟Install docker and cloudera on Ubuntu](#headin)

## 🌟Set static IP for instance

When we stated up an instance it automatically allocate an dynamic IP, we can make it static
![Image](docs/img/step2.1.PNG?raw=true)
![Image](docs/img/step2.2.PNG?raw=true)
![Image](docs/img/step2.3.PNG?raw=true)
  

## 🌟Add firewall rules to open all related service

Forget about security, just open all to get it simple at first!
![Image](docs/img/step3.1.PNG?raw=true)
Or
![Image](docs/img/step3.2.PNG?raw=true)
 
Find the default rule for our created instance, do you remember "Allow HTTP trafic" of previous Tuts ? 
![Image](docs/img/step3.3.PNG?raw=true)
![Image](docs/img/step3.4.PNG?raw=true)
![Image](docs/img/step3.5.PNG?raw=true)

Notes: to avoid datamining trojan aim to docker manager port you should open only needed ports, do not open 2375 and 2376

