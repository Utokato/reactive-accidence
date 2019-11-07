### 0. 安装MongoDB数据库
	官方下载指定zip包，解压到指定文件夹，然后以下面的命令启动MongoDB
	需要手动创建data\db文件夹，并在启动时指定该文件夹
	也可以使用bat脚本
	.\mongod.exe --dbpath D:\MongoDB\data\db
### 1.引入MongoDB数据库依赖
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
	</dependency>
### 2.开启数据库相关的注解
	@EnableReactiveMongoRepositories
### 3.定义对象
	@Document(collection = "user")
	// 这是使用了lombok的@Data注解，它会帮我们自动生成getter/setter/toString方法
	@Data
### 4.定义仓库
	@Repository
	public interface UserRepository extends ReactiveMongoRepository<User, String> {}
### 5.启动MongoDB
	D:\MongoDB\bin\mongod --dbpath D:\MongoDB\data\db --smallfiles
### 6.数据库的配置
	spring.data.mongodb.uri=mongodb://localhost:27017/webflux
### 7.CRUD 编程，详见代码