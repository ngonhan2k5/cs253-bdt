# ✴Tip to install Cloudera and Google Cloud (A replacement of VM images)

🌟Install docker and cloudera quickstart on Ubuntu

This is work for ubuntu 16.04 and 18.04 LTS, if you didn't have ubuntu, try to get free account on G cloud and follow the 🌟Create instance on google cloud (Image Tuts) in Wiki
sudo -i
## if this is the first time run, we need an update apt package meta
apt update
## install docker
apt-get install docker.io
## get cloudera quickstart image
docker pull cloudera/quickstart:latest
## first time create and run docker instance
docker run --hostname=quickstart.cloudera --privileged=true -t -i -p 7180:7180 -p 80:80 -p 8888:8888 -p 7187:7187 cloudera/quickstart /usr/bin/docker-quickstart
## now you can use browser to access http://"External Ip" depending on where your Ubuntu node hosted
New: you can install docker-compose and use this docker-compose.yml.

Notes: If you want to get use of Cloudera Manager, you must run /home/cloudera/cloudera-manager after the docker first started and do a instance down and start again.


🌟Create instance on google cloud (Image Tuts)






​


Tips: You can paste command. Now you can install docker and cloudera follow 🌟Install docker and cloudera on Ubuntu in Wiki

🌟Set static IP for instance

  
When we stated up an instance it automatically allocate an dynamic IP, we can make it static

 
 

🌟Add firewall rules to open all related service

  
Forget about security, just open all to get it simple at first!

Or

 
Find the default rule for our created instance, do you remember "Allow HTTP trafic" of previous Tuts ? 
 

