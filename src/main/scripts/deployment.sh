cp .travis.settings.xml $HOME/.m2/settings.xml
sed -i 's/${env.prefix}/'$username'/g' $HOME/.m2/settings.xml
sed -i 's/${env.prefix}/'$password'/g' $HOME/.m2/settings.xml
mvn deploy