# QuickBite Delivery 🍽️🚀

QuickBite Delivery is a restaurant delivery platform backend developed using Spring Boot. This project provides RESTful APIs for managing dishes, categories, setmeals and flavors within the restaurant's menu.

## Features ✨

- **Backstage Management**: Efficiently manage categories, and dishes, and setmeals with functionalities to add, update, delete, and list them. Additionally, oversee employee and order management to ensure smooth operations 🍲.
- **Order Management**: Provides backend support for frontend ordering functionality, including data retrieval and SQL updates for orders.
- **Caching**: Uses Redis to cache dish data for improved performance ⚡.
- **Transactional Operations**: Ensures data integrity with transactional methods 💼.

## Technologies 🛠️

- **Spring Boot**: For building the backend services.
- **MyBatis-Plus**: For enhanced MyBatis operations and pagination support.
- **Redis**: For caching dish data and improving query performance.
- **Spring Cache**: For efficient data retrieval and management within the application.
- **Lombok**: To reduce boilerplate code.

## Documentation

I documented the errors that I think are common in CSDN (https://blog.csdn.net/wenti_baobao/article/details/141617284) in Chinese. 
Hope you find it helpful!

## Configuration ⚙️

### Redis Configuration

Ensure that Redis is installed and running. The application uses Redis for caching data. You need to have Redis up and running for the caching functionalities to work. My redis is implemented through a Linux virtual machine.

### MyBatis-Plus Configuration

Ensure that MyBatis-Plus is properly configured in your project.

## Running the Application 🚀

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Yunshan-CAI/QuickBite_Delivery.git
   
2. **Navigate to the project directory:**
   ```bash
   cd <project-directory>
   
3. **Install dependencies(Maven):**
   ```bash
   mvn install
   
4. **Access the API:**
Once the application is running, you can access the API endpoints through the configured port (default is 8080).
Open your browser or API client and navigate to: http://localhost:8080.

5. **Additional Configuration:**
Redis: Ensure that Redis is installed and running if you are using caching. The application uses Redis for caching dish data.
Database: Make sure your database is set up and properly configured in application.yml.
If you need to adjust any configuration settings, edit the application.yml file accordingly.





