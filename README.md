# Config-Server
A Config Server is part of the Spring Cloud Config project. It is used to centralize the external configuration for applications across all environments.
                                          (OR)
A server that stores all your app’s config (like properties or YAML files) in one place (e.g., Git), and lets your apps fetch it dynamically.

🔧 Why Use a Config Server?
	1.Centralized Configuration Management
		Manage all your microservices' configuration from a single location.
	2.Dynamic Updates
		With Spring Cloud Bus, you can update configurations on the fly without restarting services.
	3.Environment-Specific Configuration
		Different config files for dev, test, prod, etc.
	4.Security
		You can lock down access to configs, and even encrypt sensitive properties.

🛠️ How It Works
	1.Spring Cloud Config Server connects to a remote Git repository (or other sources like file system, Vault).
	2.It exposes endpoints (usually /application-name/profile) that client apps can use to fetch their configuration.
	3.Spring Boot apps (clients) include the Spring Cloud Config Client dependency and use annotations to pull config at startup.

The @EnableConfigServer annotation is used in Spring Cloud Config to enable a Config Server, which is part of the Spring Cloud ecosystem.

🔧 Purpose of @EnableConfigServer
	-It enables a Spring Boot application to act as a Config Server, which serves external configuration properties to other applications in a distributed system.
	-The Config Server retrieves configurations from a remote repository (usually Git, but it can also use other backends like file systems, Vault, JDBC, etc.).
	-Client applications (using @EnableConfigClient or with Spring Cloud Config dependencies) can then fetch their configuration properties from the server at startup or refresh time.

You use @EnableConfigServer on the main class of a Spring Boot application when you want that application to act as a centralized config server for other microservices.
