sed -i 's/${env.username}/'$username'/g' .travis.settings.xml
sed -i 's/${env.password}/'$password'/g' .travis.settings.xml
cp .travis.settings.xml $HOME/.m2/settings.xml
mvn deploy