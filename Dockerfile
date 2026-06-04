# ビルドステージ
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# 実行ステージ
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# 生成されたjarファイルをapp.jarという名前でコピー
COPY --from=build /workspace/app/target/*.jar app.jar
EXPOSE 8080
# jarを直接起動（メインクラスは自動で検出されます）
ENTRYPOINT ["java", "-jar", "app.jar"]