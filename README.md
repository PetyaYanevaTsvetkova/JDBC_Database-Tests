## Project's Title:
	JDBC: Database Tests

## Project Description:
		Prerequisites:
	customers, addresses, orders and products tables with example data

		Theory:
	Decorator Pattern (Optional)

		Practical tasks:
	Create test data before test execution 
	Description: Before the tests are executed I want to drop all the data in customers,
	addresses, orders and products table and create X new records in each one. 
	Relations should exists - customers should have an address and between 0 and X (random) orders, 
	which should have between 1 and X products.

	Create tests for Customer data
	Description: Create customers tests that validate the data in customers table.
 	Verify:
	that there are no customers without address
	that there are no customers with the same phone or email
	create a new user, save it and verify that it was saved successfully 
	create a test that verifies that you cannot create and save a user without the mandatory fields set for the table
	Verify that customers table is not empty before each test and fail the test if it is.

	Create tests for Addresses data
	Description: Create addresses tests that validate the data in addresses table. 
	Get X random customers and verify that their addresses have all mandatory fields filled.
	create a test that verifies that you cannot create and save an address without the mandatory fields set for the table
	Verify that addresses table is not empty before each test and fail the test if it is.

	Create tests for Orders data
	Description: Create orders tests that validate the data in orders table. 
	Get X random customers and verify that their orders (if any) have all mandatory fields filled.
	get X random orders and verify that they have a customer
	get X random orders and verify that all their products exists in the products table
	create a new customer order and verify that it was saved successfully 
	create a test that verifies that you cannot create and save an order without the mandatory fields set for the table
	Verify that orders table is not empty before each test and fail the test if it is.

	Create tests for Products data
	Description: Create products tests that validate the data in products table. 
	Get X random orders and verify that their products (if any) have all mandatory fields filled
	create a new product and verify that it was saved successfully and there are no orders for it (as it was just created)
	create a test that verifies that you cannot create and save a product without the mandatory fields set for the table
	Verify that products table is not empty before each test and fail the test if it is.

	Acceptance criteria:
	As a QA Automation trainee, I want to gain knowledge of:
	how to write tests for relational data 
	how to use and implement decorator pattern (optional)
	As a QA Automation trainee, I want to be able to:
	run the tests through maven commandline
	to run different test suites


## Table of Contents:
	README.md file
	estafet.training.JDBCDatabaseTests
	.gitignore file
	DBscript.sql

## How to Install and Run the Project:
	SQL: Local database environment setup
	 
## How to Use the Project:
	IDE need to execute the java project

## Useful links:
	https://www.geeksforgeeks.org/postgresql-date-data-type/#:~:text=PostgreSQL%20uses%20the%20yyyy%2Dmm,CURRENT_DATE%20after%20the%20DEFAULT%20keyword

## Add a License
	no needed


