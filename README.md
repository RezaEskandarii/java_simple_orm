### this project is simple java orm for create and manage java objects in database by reflection and jdbc tecnology

--The project configuration file is located in the directory src / main / resources / application.properties
+ In this file you can perform the following configurations
	- com.settings.db_user=your database username
	- com.setting.db_pass= your database password
	- com.setting.db_url=jdbc:mysql://localhost:3306/your_database_name_in_mysql
	- com.setting.db_driver=com.mysql.jdbc.Driver
	- com.setting.show_sql=true
	- com.setting.ddl=update 
### If com.setting.dll is set to create, each time the object is saved in session, the entire table will be erased and rebuilt.
If we put it equal to the update, the table will not be deleted and only new objects will be saved.
Set the com.setting.dll equal to create the first instance of the Session class save method so that the entity equivalent will create a table in the database.
