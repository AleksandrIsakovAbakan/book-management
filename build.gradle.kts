plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	//maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")

}


dependencies {

	implementation("org.springframework.boot:spring-boot-starter-web")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.0")


	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.0")
	//	exclude("io.lettuce.lettuce-core")
	//}
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-redis
	//implementation("org.springframework.boot:spring-boot-starter-redis:1.4.7.RELEASE")

	// https://mvnrepository.com/artifact/redis.clients/jedis
	//implementation("redis.clients:jedis:4.4.3")
	implementation("io.lettuce:lettuce-core")

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache
	implementation("org.springframework.boot:spring-boot-starter-cache")

	runtimeOnly("org.postgresql:postgresql")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//tasks.bootBuildImage {
//	builder.set("paketobuildpacks/builder-jammy-base:latest")
//}
