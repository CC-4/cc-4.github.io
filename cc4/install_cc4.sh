sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer -y
sudo apt-get install oracle-java8-set-default
sudo apt-get install bison build-essential csh libxaw7-dev git -y
sudo mkdir /usr/class
sudo chown $USER /usr/class
cd /usr/class
git clone https://github.com/CC-4/compiladores2016.git cs143
ln -s /usr/class/cs143/cool ~/cool
echo export\ PATH=/usr/class/cs143/cool/bin:\$PATH >> ~/.bashrc 
source ~/.bashrc 