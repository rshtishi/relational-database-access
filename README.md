# relational-database-access

A quick review of the evolution history of accessing relational databases.

## Topics

Data access is a common requirement for most enterprise applications, which usually require accessing data stored in relational databases. In this example, we will look
at different approaches to accessing the relational database. Below are the topics we are going to cover in our examples:
- understanding the DAO (Data Access Object) pattern
- implementing DAO (Data Access Object) pattern with raw ```JDBC```
- implementing DAO (Data Access Object) pattern with ```JDBCTemplate```
- understaning the JPA (Java Persistence Api)
- Database table relationships translated to object associations with JPA
- implementing DAO (Data Access Object) with JPA (Java Persistence API) 
- using Spring Data JPA to access the relational database
- extending Spring Data JPA capabilities with QueryDSL

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
 
 ### Database table relationships translated to object associations with JPA
 
 We have three types of relationships between database tables. They are :
 - one-to-many (a row from the parent table is associated to multiple rows in the child table)
 - one-to-one (a row from the parent table is associated with only one row from the child table)
 - many-to-may (both tables are associated with multiple rows, requires a junction table that contains references from the parent tables)
 
 The following diagram depicts all database relatioships mention above:
 
 The foreign key is therefore the most important construct in building a table relationship, and, in a relation database, the foreign key is associated with the child-side only.
 
 When mapping a JPA entity the application developer can map entity relationships either in one direction or in a bidirectional way. This is another difference between the object-oriented entity model and relational database system. When using an ORM tool, the parent and the child-side can reference each other. JPA defines the following association mappings:
 
 - ```@ManyToOne``` represent the child side in one-to-many table relatioship
 - ```@OneToMany``` represent the parent side in one-to-many table relationship
 - ```@OneToOne```  represent both (parent side and child side) in one-to-one table relationship
 - ```@ManyToMany``` represent the many-to-many relationship
 
 ##### one-to-many table relationship
 
 We can mapped this relationship using the following JPA association:
 
 - ```@ManyToOne``` association it maps exactly to the one-to-many table relationship. The underlying foreign key is controlled by the child-side. The ```Review``` object has reference to the ```Product``` object and by default is fetched along with ```Product```. To change this we need to modify the change type to lazily loading. Below is the mapping that references the ```Product```:
 
 ```
 	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
 ```
 
 - Unidirectional ```@oneToMany``` association seems to be the most natural mapping of one-to-many table relationship but behind the scenes, an additional junction table is added that makes this association less efficient that ```@ManyToOne``` or bidirectional ```@OneToMany``` relationship. Since there is no ```@ManyToOne``` side to control this relationship, Hibernate uses a separate junction table to manage the association between a parent row and its child records. Below is the mapping that references the ```Review``` objects:
 
 ```
 	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Review> reviews= new ArrayList<>();
 ```
 
 - Bidirectional ```OneToMany``` association has a matching @ManyToOne child-side mapping that controls the underlying one-to-many table relationship. In this association, the foreign key is controlled by the child-side. For this reason, we need and ```mappedBy``` attribute in the parent-side for mirroring the @ManyToOne child-side mapping. Below is the mapping in parent-side:
 
 ```
 	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Review> reviews= new ArrayList<>();
 ```
 
 The following is the mapping in child-side:
 
 ```
 	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
 ```
 
 The following helper method in parent-side class help to keep the parent-side in sync with child-side:
 
 ```
 	public void addReview(Review review) {
		reviews.add(review);
		review.setProduct(this);
	}
	
	public void removeReview(Review review) {
		reviews.remove(review);
		review.setProduct(null);
	}
 ```
 
 - Unidirectional ```@OneToMany``` with ```@JoinColumn```, by mapping the unidirectional ```@OneToMany``` association with ```@JoinColumn``` mapping we place the control on the foreign key on the parent-side. We don't need a junction table in this case. It is more efficient than unidirectional ```@OneToMany``` association but still not near the efficiency of ```@ManyToOne``` and bidirectional ```@OneToMany``` association. In this association besides the regular insert statements, Hibernate issues three update statements for setting the foreign key column on the newly inserted child records. Below is the mapping for the unidirectional ```@OneToMany``` with ```@JoinColumn```.
 
 ```
  	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id")
	List<Review> reviews= new ArrayList<>();
 ```
 
  ##### one-to-one table relationship
  
  we can map this table relationship using the following JPA mappings:
  
  - Unidirectional ```@OneToOne``` 
  
  - Bidirectional ```@OneToOne```
  
  ##### many-to-many table relationship
  
 
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

As mention above by extending the ```CrudRepository```, we by default get the implementation of commonly used methods like findAll, save, delete, etc.. If we want to change
the signature of these methods we override the signature in our child interface. As we can see above we have changed the signature for the findAll method.

Another great feature of Spring Data JPA is query derivation. Spring Data JPA generates a query just by looking at the corresponding method name in our code.
We need to abide by the naming convention of Spring Data JPA if we want to use this feature.

Also, Spring Data JPA provides the ``` @Query``` annotation in Spring Data JPA to execute both JPQL and native SQL queries.

### Extending Spring Data JPA capabilities with QueryDSL

Querydsl is an extensive Java framework, which helps with creating and running type-safe queries in a domain specific language that is similar to SQL. Below are the dependencies needed to integrate QueryDSL:

```
		<dependency>
			<groupId>com.querydsl</groupId>
			<artifactId>querydsl-core</artifactId>
		</dependency>
		<dependency>
			<groupId>com.querydsl</groupId>
			<artifactId>querydsl-apt</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>com.querydsl</groupId>
			<artifactId>querydsl-jpa</artifactId>
		</dependency>
```

Also, we need to configure the annotation processing plugin like below:

```
			<plugin>
				<groupId>com.mysema.maven</groupId>
				<artifactId>apt-maven-plugin</artifactId>
				<version>1.1.3</version>
				<executions>
					<execution>
						<goals>
							<goal>process</goal>
						</goals>
						<configuration>
							<outputDirectory>target/generated-sources/java</outputDirectory>
							<processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

When we compile the project, QueryDSL will To generate Q-types for your entities.

To add support for our QueryDSL in our spring data repository we need to extends the ```QuerydslPredicateExecutor``` like below:

```
@Repository
public interface SpringDataJpaCustomerDAO extends CrudRepository<JpaCustomerEntity, Integer>,
		QuerydslPredicateExecutor<JpaCustomerEntity>, SpringDataJpaCustomerDAOCustom {
		...
}
```

Also, our spring data repository will extends a custom repository with custom method for querying entities. Below is the interface:

```
public interface SpringDataJpaCustomerDAOCustom {

	public List<JpaCustomerEntity> findCustomers(AddressFilter filter);

}
```

The implementation for this custom method should be provided by us. Below is the implementation:

```
public class SpringDataJpaCustomerDAOImpl implements SpringDataJpaCustomerDAOCustom {

	private static final Logger logger = LoggerFactory.getLogger(SpringDataJpaCustomerDAOImpl.class);

	@Autowired
	private SpringDataJpaCustomerDAO customerDAO;

	@Override
	public List<JpaCustomerEntity> findCustomers(AddressFilter filter) {

		BooleanBuilder builder = new BooleanBuilder();
		if (filter != null) {
			QJpaCustomerEntity qCustomer = QJpaCustomerEntity.jpaCustomerEntity;
			if (!StringUtils.isBlank(filter.getZipCode())) {
				builder.and(qCustomer.zipCode.equalsIgnoreCase(filter.getZipCode()));
			}
			if (!StringUtils.isBlank(filter.getCity())) {
				builder.and(qCustomer.city.equalsIgnoreCase(filter.getCity()));
			}
			if (!StringUtils.isBlank(filter.getState())) {
				builder.and(qCustomer.state.equalsIgnoreCase(filter.getState()));
			}
		}
		return customerDAO.findAll(builder);
	}

}
```

Spring will use the  ```SpringDataJpaCustomerDAOCustom``` interface to search for a class ```SpringDataJpaCustomerDAOImpl```  and integrate the manually implemented methods with any query methods included on the  ```SpringDataJpaCustomerDAO``` repository interface from extending the ```SpringDataJpaCustomerDAOCustom``` interface.

