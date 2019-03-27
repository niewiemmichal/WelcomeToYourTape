package pl.niewiemmichal.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Startup;

@DataSourceDefinition(
        className = "com.mysql.cj.jdbc.MysqlDataSource",
        name = "java:comp/env/MySQLDataSource",
        url="jdbc:mysql://database:3306/database",
        user = "root",
        password = "password"
)
@Startup
public class DataSourceConfig {}
