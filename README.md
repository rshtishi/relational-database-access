# relational-database-access

A quick review of the evolution history of accessing relational databases.

## Topics

Data access is a common requirement for most enterprise applications, which usually require accessing data stored in relational databases. In this example, we will look
at different approaches to accessing the relational database. Below are the topics we are going to cover in our examples:
- understanding the DAO (Data Access Object) pattern
- implementing DAO (Data Access Object) pattern with raw ```JDBC```
- implementing DAO (Data Access Object) pattern with ```JDBCTemplate```
- implementing DAO (Data Access Object) with JPA (Java Persistence API) 
- using Spring Data JPA to access the relational database

### Understanding the DAO (Data Access Object) pattern

Data Access Object (DAO) pattern separates data access logic from business logic and presentation logic. This pattern recommends that data access logic be encapsulated 
in independent modules called data access objects. By using this pattern we increase the module reusability, maintainability, and reduce the tight coupling between the 
data access layer and the business logic.

We start by creating the class that is going to represent the ```customer``` table in Java. Often these classes are referred to as the model, below is the 
implementation:

```
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	
	private int id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String zipCode;
	private String city;
	private String state;
	
}
```

We follow the DAO pattern by abstracting the data access operations in the DAO interface to allow different implementation. Below is the DAO interface:

```
public interface CustomerDAO {
	
	public static final String INSERT_SQL = "INSERT INTO CUSTOMER (ID, FIRST_NAME, LAST_NAME, PHONE, EMAIL, ZIP_CODE, CITY, STATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_SQL = "UPDATE CUSTOMER SET FIRST_NAME=?, LAST_NAME=?, PHONE=?, EMAIL=?, ZIP_CODE=?, CITY=?, STATE=? WHERE ID=?";
	public static final String SELECT_ALL_SQL = "SELECT * FROM CUSTOMER";
	public static final String SELECT_ONE_SQL = "SELECT * FROM  CUSTOMER WHERE ID=?";
	public static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE ID = ?";


	void insert(Customer customer);

	void insert(Iterable<Customer> customers);

	void update(Customer customer);

	void delete(Customer customer);

	Customer findByCustomerId(int customerId);

	List<Customer> findAll();
}
```

### Implementing DAO (Data Access Object) pattern with raw *JDBC*

Java Database Connectivity (JDBC) defines a set of standard APIs for us to access relational databases in a vendor-independent fashion. We use the preconfigured 
```javax.sql.DataSource``` object to obtain the database connection for executing SQL statements. The ```javax.sql.DataSource``` interface is a standard interface 
defined by the JDBC specification that factories Connection instances. In this example, we are using the ```HikariCP``` as the data source implementation. Below is
class implementing the DAO pattern:

```
@Repository
public class RawJdbcCustomerDAO implements CustomerDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void insert(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(INSERT_SQL)) {
			prepareInsertStatement(preparedStmt, customer);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void insert(Iterable<Customer> customers) {
		customers.forEach(this::insert);
	}

	@Override
	public void update(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(UPDATE_SQL)) {
			prepareUpdateStatement(preparedStmt, customer);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void delete(Customer customer) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(DELETE_SQL)) {
			preparedStmt.setInt(1, customer.getId());
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Customer findByCustomerId(int customerId) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(SELECT_ONE_SQL)) {
			preparedStmt.setInt(1, customerId);
			ResultSet rs = preparedStmt.executeQuery();
			rs.next();
			return toCustomer(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Customer> findAll() {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStmt = connection.prepareStatement(SELECT_ALL_SQL)) {
			List<Customer> customers = new ArrayList<>();
			ResultSet rs = preparedStmt.executeQuery();
			while (rs.next()) {
				customers.add(toCustomer(rs));
			}
			return customers;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void prepareInsertStatement(PreparedStatement preparedStmt, Customer customer) throws SQLException {
      ...
	}

	private void prepareUpdateStatement(PreparedStatement preparedStmt, Customer customer) throws SQLException {
     ...
	}

	private Customer toCustomer(ResultSet rs) throws SQLException {
     ...
	}
```

As you can see in the example above, using raw ```JDBC``` to implement DAO pattern is tedious and time-consuming. For each data access operation, there is a lot of 
boilerplate code, like:
- obtain database connection from the data source
- create a PreparedStatement object from the connection
- bind the parameters to the PreparedStatement object
- execute the PreparedStatement object
- handle SQLException
- clean up the statement object and connection


### Implementing DAO (Data Access Object) pattern with ```JDBCTemplate```
