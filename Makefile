install:
	mvn clean install -DskipTests

package:
	mvn clean package -DskipTests

upload:
	./upload.sh

pu: package upload

clean:
	rm -rf target
