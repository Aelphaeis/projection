cp .travis.settings.xml $HOME/.m2/settings.xml
sed -i 's/${env.username}/'$username'/g' $HOME/.m2/settings.xml
sed -i 's/${env.password}/'$password'/g' $HOME/.m2/settings.xml
mvn deploy