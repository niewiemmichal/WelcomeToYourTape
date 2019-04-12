package pl.niewiemmichal.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Startup;

@DataSourceDefinition(
        className = "com.mysql.cj.jdbc.MysqlDataSource",
        name = "java:comp/env/MySQLDataSource",
        url="jdbc:mysql://wtyt-database.ct8carsorhgh.eu-central-1.rds.amazonaws.com:3306/wtyt_database",
        user = "root",
        password = "password"
)
@Startup
public class DataSourceConfig {}
