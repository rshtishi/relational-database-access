# relational-database-access

A quick review of the evolution history of accessing relational databases.

## Topics

Data access is a common requirement for most enterprise applications, which usually require accessing data stored in relational databases. In this example, we will look
at different approaches to accessing the relational database. Below are the topics we are going to cover in our examples:
- understanding the DAO (Data Access Object) pattern
- implementing DAO (Data Access Object) pattern with raw ```JDBC```
- implementing DAO (Data Access Object) pattern with ```JDBCTemplate```
- understaning the JPA (Java Persistence Api)
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


### Implementing DAO (Data Access Object) pattern with *JDBCTemplate*

By using ```JDBCTemplate``` we removed a lot of boilerplate code from the implementation of the DAO class. We removed the code needed for handling exception. In general, all exceptions thrown by the Spring JDBC framework are subclasses of ```org.springframework.dao.DataAccessException```, a type of RuntimeException that
we are not forced to catch. The ```JDBCTemplate``` takes care of resource acquisition, connection management, exception handling, and general error checking that is unrelated to what the code is meant to achieve. Below is the DAO implementation class with ```JDBCTemplate```:

```
@Repository
public class JdbcTemplateCustomerDAO implements CustomerDAO {

	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public void insert(Customer customer) {
		jdbc.update(INSERT_SQL, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getPhone(),
				customer.getEmail(), customer.getZipCode(), customer.getCity(), customer.getState());
	}

	@Override
	public void insert(Iterable<Customer> customers) {
		customers.forEach(this::insert);
	}

	@Override
	public void update(Customer customer) {
		jdbc.update(UPDATE_SQL, customer.getFirstName(), customer.getLastName(), customer.getPhone(),
				customer.getEmail(), customer.getZipCode(), customer.getCity(), customer.getState(),customer.getId());
	}

	@Override
	public void delete(Customer customer) {
		jdbc.update(DELETE_SQL, customer.getId());
	}

	@Override
	public Customer findByCustomerId(int customerId) {
		return jdbc.queryForObject(SELECT_ONE_SQL, this::mapRowToCustomer, customerId);
	}

	@Override
	public List<Customer> findAll() {
		return jdbc.query(SELECT_ALL_SQL, this::mapRowToCustomer);
	}

	private Customer mapRowToCustomer(ResultSet rs, int rownum) throws SQLException {
		...
	}

}
```

```JDBCTemplate``` doesn't help solve the problem of mapping database rows to objects. We have to write manually the code for translating the ```ResultSet``` to the domain model. When having a complex domain model the process of writing the code for each entity becomes tedious and error-prone.


### Understaning the JPA (Java Persistence Api)

JPA is the J2EE specification that each ORM (Objection-Relational-Mapping) provider must follow. Popular implementations of JPA specification are Hibernate, EclipseLink, and Apache OpenJPA. The JPA implementation is often called the persistence provider. We use the persistence provider(Hibernate, EclipseLink) to reduce the burden of writing codes for relational object management.

The mapping between POJOs and database tables is defined via persistence metadata. JPA metadata is typically defined via annotations in the POJO class. Alternatively, the metadata can be defined via XML or a combination of both. An XML configuration overwrites the annotations. Below is the POJO that we have mapped with database customer table:

```
@Table(name="customer")
@Entity(name="Customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaCustomerEntity  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

- ```@Entity``` annotation marks the classes the should be persisted in the database. The parameter passed to the Entity is the name that is going to be used to refer to this entity. 
- ```@Table``` annotation maps the POJO with the table in the database. The parameter passed to the ```@Table``` is the name of the table in the database.
- ```@Id``` annotation marks the id field as the primary key. All entity classes must define a primary key, must have a no-arg constructor.
- ```@GeneratedValue``` specifies the strategy for generating the primary key. In this example, we have chosen an IDENTITY strategy for primary key generation.

The entity manager ```javax.persistence.EntityManager``` provides the operations for accessing the database. The persistence context describes all entities of one entity manager.

 The ```EntityManagerFactory``` is responsible for creating the entity manager. We configure it by specifying the persistence unit in the persistence.xml file in the META-INF directory.
 
 
 ### Implementing DAO (Data Access Object) with JPA (Java Persistence API) 
 
 We have used te ```@PersistenceContext``` annotation to autowire the entity manager. The default persistence context type is ```PersistenceContextType.TRANSACTION```.
 The transaction persistence context is bound to the transaction. As soon as the transaction finishes, the entities present in the persistence context will be flushed into persistent storage. 
 Also, we use ```@Transactional``` annotation for removing boilerplate when managing transactions. Below is the DAO implementation with JPA:
 
 ```
 @Repository
public class JpaCustomerDAOImp implements JpaCustomerDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<JpaCustomerEntity> findAll() {
		TypedQuery<JpaCustomerEntity> query = entityManager.createQuery("Select c from Customer c",
				JpaCustomerEntity.class);
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public JpaCustomerEntity findById(int id) {
		return entityManager.find(JpaCustomerEntity.class, id);
	}

	@Transactional
	@Override
	public void save(JpaCustomerEntity customer) {
		entityManager.merge(customer);
	}
	
	@Transactional
	@Override
	public void saveAll(List<JpaCustomerEntity> customers) {
		customers.forEach(this::save);
	}

	@Transactional
	@Override
	public void delete(int id) {
		JpaCustomerEntity customer = entityManager.find(JpaCustomerEntity.class, id);
		entityManager.remove(customer);
	}

}
 ```
 Writing data access code with JPA is much better than with ```JDBCTemplate``` and raw ```JDBC```.  But even with the JPA writing DAO implementation can be tedious and repetitive. We often need access to EntityManager or EntityManagerFactory. And not mention the repetitive implementation of findAll, findById, and save for all different entities.
 
 ### Using Spring Data JPA to access the relational database

Spring Data JPA has brought the database access to the next level. Spring Data JPA allows you to focus on the parts that are important and not on the boilerplate needed to accomplish this. It also provides default implementations for the most commonly used data access methods (i.e., findAll, delete, save, etc.). 
To use Spring Data JPA, you have to extend one of its interfaces. These interfaces are detected, and a default implementation of that repository is generated at runtime. In most cases, it is enough to extend the ```CrudRepository<T, ID> ``` interface. If you need pagination you can extend ```PagingAndSortingRepository<T, ID>``` that has default implementation for pagination. Below is our repository for accessing the database:

```
@Repository
public interface SpringDataJpaCustomerDAO extends CrudRepository<JpaCustomerEntity, Integer>,
		QuerydslPredicateExecutor<JpaCustomerEntity>, SpringDataJpaCustomerDAOCustom {

	List<JpaCustomerEntity> findAll();

	List<JpaCustomerEntity> findAll(Predicate predicate);

	JpaCustomerEntity findById(int id);

	// derived queries

	List<JpaCustomerEntity> findByFirstName(String firstName);

	List<JpaCustomerEntity> findByEmailIsNotNull();

	// jpql query

	@Query("SELECT c FROM Customer c WHERE c.city=?1")
	List<JpaCustomerEntity> extractCustomersThatAreFrom(String city);

	// native query

	@Query(value = "SELECT * FROM Customer c WHERE c.state=?1", nativeQuery = true)
	List<JpaCustomerEntity> extractCustomerFromCountry(String country);
}
```

As mention above by extending the CrudRepository we by default get the implementation of commonly used method linke findAll, save, delete etc.. If we want to change
the signature of these methods we override the signature in our child interface. As we can see above we have changed the signature for the findAll method.
Another great feature of Spring Data JPA is query derivation.



